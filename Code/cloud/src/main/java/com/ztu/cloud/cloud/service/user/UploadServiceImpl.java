package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mysql.File;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.bean.redis.TempFile;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.dao.redis.TempFileDao;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.user.Upload;
import com.ztu.cloud.cloud.util.Md5Util;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.StoreUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Jager
 * @description 上传业务接口实现
 * @date 2020/06/27-09:34
 **/
@Component
public class UploadServiceImpl implements UploadService {
	FileMapper fileDao;
	UserMapper userDao;
	StoreUtil storeUtil;
	TempFileDao tempFileDao;

	public UploadServiceImpl(FileMapper fileDao, UserMapper userDao, StoreUtil storeUtil, TempFileDao tempFileDao) {
		this.fileDao = fileDao;
		this.userDao = userDao;
		this.storeUtil = storeUtil;
		this.tempFileDao = tempFileDao;
	}

	/**
	 * 上传文件
	 *
	 * @param token    用户Token
	 * @param block    文件块
	 * @param fileName 文件名
	 * @param blockMd5 文件块MD5
	 * @param fileMd5  文件MD5
	 * @param index    文件块号
	 * @param length   文件总块数
	 * @return 已接收的文件块号
	 */
	@Override
	public ResultResponseEntity upload(String token, String fileName, MultipartFile block, String blockMd5, String fileMd5, Integer index, Integer length) {
		if (block.getSize() > 5 * 1024 * 1024) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		if (blockMd5 == null || "".equals(blockMd5) || fileMd5 == null || "".equals(fileMd5) || fileName == null || "".equals(fileName) || index == null || length == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		User user = this.userDao.getUserById(id);
		if (user == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (user.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		blockMd5 = blockMd5.toLowerCase();
		fileMd5 = fileMd5.toLowerCase();
		if (this.fileDao.getFileById(fileMd5) != null) {
			return ResultConstant.SUCCESS;
		}
		try {
			if (index == -1) {
				if (!Md5Util.getMd5(block.getBytes()).equals(fileMd5)) {
					return ResultConstant.FILE_DAMAGE;
				}
				String path = this.storeUtil.getUsablePath() + fileMd5;
				boolean flag = this.storeUtil.storeFile(path, block.getInputStream());
				if (!flag) {
					return ResultConstant.SERVER_ERROR;
				}
				String extension = getExtension(fileName, block);
				File file = new File(fileMd5, fileName, path, block.getSize(), extension);
				if (this.fileDao.insertFile(file) != 1) {
					return ResultConstant.SERVER_ERROR;
				}
				return ResultConstant.SUCCESS;
			} else if (index > -1) {
				System.out.println(Md5Util.getMd5(block.getBytes()));
				if (!Md5Util.getMd5(block.getBytes()).equals(blockMd5)) {
					return ResultConstant.FILE_DAMAGE;
				}
				TempFile tempFile = this.tempFileDao.get(fileMd5);
				if (tempFile == null) {
					String extension = getExtension(fileName, block);
					tempFile = new TempFile(fileMd5, fileName, extension, length);
					if (this.tempFileDao.insert(tempFile) != 1) {
						return ResultConstant.SERVER_ERROR;
					}
				}
				System.out.println("length:"+length);
				System.out.println("index:"+index);
				System.out.println(tempFile.getSaves());
				if (!tempFile.getSaves().contains(index)) {
					System.out.println("插入:" + index);
					this.storeUtil.storeTempFile(fileMd5 + "_" + index, block.getInputStream());
					tempFile.getSaves().add(index);
					this.tempFileDao.update(tempFile);
				}
				if (tempFile.getSaves().size() == length) {
					String[] names = new String[length];
					for (int i = 0; i < length; i++) {
						names[i] = fileMd5 + "_" + i;
					}
					if (this.storeUtil.checkTempFile(names)) {
						long size = this.storeUtil.mergeTempFile(names, fileMd5);
						if (size != -1) {
							String path = this.storeUtil.getUsablePath() + fileMd5;
							if (this.storeUtil.uploadTempFile(path, fileMd5)) {
								this.storeUtil.deleteTempFile(fileMd5);
								this.tempFileDao.delete(tempFile.getFileId());
								File file = new File(tempFile.getFileId(), tempFile.getName(), path, size, tempFile.getType());
								this.fileDao.insertFile(file);
								return ResultConstant.SUCCESS;
							}
						}
					}
					this.tempFileDao.delete(tempFile.getFileId());
					this.storeUtil.deleteTempFile(fileMd5);
					this.storeUtil.deleteTempFiles(names);
					return ResultUtil.createResult(0, "合并失败,重新上传", new Upload());
				}
				return ResultUtil.createResult(0, "上传成功", new Upload(tempFile.getSaves()));
			} else {
				return ResultConstant.REQUEST_PARAMETER_ERROR;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
	}

	private String getExtension(String fileName, MultipartFile block) {
		MimeTypes mimeType = MimeTypes.getDefaultMimeTypes();
		String extension;
		if (fileName.contains(".")) {
			String[] split = fileName.split("\\.");
			extension = split[split.length - 1];
		} else {
			try {
				extension = mimeType.forName(block.getContentType()).getExtension();
			} catch (MimeTypeException e) {
				e.printStackTrace();
				extension = "unknown";
			}
		}
		return extension;
	}
}
