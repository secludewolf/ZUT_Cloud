package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.File;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.admin.FileInfo;
import com.ztu.cloud.cloud.common.vo.admin.FileList;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.StoreUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * @author Jager
 * @description 文件控制接口实现
 * @date 2020/06/28-08:45
 **/
@Component
public class FileServiceImpl implements FileService {
	AdminMapper adminDao;
	FileMapper fileDao;
	StoreUtil storeUtil;

	public FileServiceImpl(AdminMapper adminDao, FileMapper fileDao, StoreUtil storeUtil) {
		this.adminDao = adminDao;
		this.fileDao = fileDao;
		this.storeUtil = storeUtil;
	}

	/**
	 * 下载文件
	 *
	 * @param token  管理员Token
	 * @param fileId 文件id
	 * @return 文件数据
	 */
	@Override
	public Download download(String token, String fileId) {
		if (!TokenUtil.isAdmin(token)) {
			return null;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return null;
		}
		if (admin.getId() != id) {
			return null;
		}
		if (admin.getStatus() != 1) {
			return null;
		}
		//TODO 权限验证
		if (admin.getLevel() <= 0) {
			return null;
		}
		File fileInfo = this.fileDao.getFileById(fileId);
		if (fileInfo == null) {
			return null;
		}
		InputStream file = this.storeUtil.getFile(fileInfo.getPath());
		return new Download(fileInfo.getName(), file);
	}

	/**
	 * 获取文件列表
	 *
	 * @param token      管理员Token
	 * @param pageNumber 第几页
	 * @return 文件列表
	 */
	@Override
	public ResultResponseEntity getFileList(String token, int pageNumber) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//TODO 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		List<File> fileList = this.fileDao.getFile((pageNumber - 1) * 20, pageNumber * 20);
		return ResultUtil.createResult("", new FileList(fileList));
	}

	/**
	 * 获取文件信息
	 *
	 * @param token  管理员Token
	 * @param fileId 文件ID
	 * @return 文件信息
	 */
	@Override
	public ResultResponseEntity getFileInfo(String token, String fileId) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//TODO 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		File file = this.fileDao.getFileById(fileId);
		if (file == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		return ResultUtil.createResult("", new FileInfo(file));
	}

	/**
	 * 删除文件
	 *
	 * @param token  管理员Token
	 * @param fileId 文件ID
	 * @return 是否成功
	 */
	@Override
	public ResultResponseEntity deleteFile(String token, String fileId) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//TODO 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		File file = this.fileDao.getFileById(fileId);
		if (file == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		file.setStatus(-1);
		this.fileDao.updateFile(file);
		return ResultUtil.createResult(1, "删除成功");
	}
}
