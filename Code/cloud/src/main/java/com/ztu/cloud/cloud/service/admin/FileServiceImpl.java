package com.ztu.cloud.cloud.service.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.File;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.admin.FileInfo;
import com.ztu.cloud.cloud.common.vo.common.PageVo;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.StoreUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.LinkedList;
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
		// 权限验证
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
	 * @param pageNumber 页数
	 * @param pageSize   分页大小
	 * @param sortKey    排序主键
	 * @param sortType   排序类型
	 * @return 文件列表
	 */
	@Override
	public ResultResponseEntity getFileList(String token, Integer pageNumber, Integer pageSize, String sortKey, String sortType) {
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
		// 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		String orderBy = sortKey + " " + sortType;
		PageHelper.startPage(pageNumber, pageSize, orderBy);
		List<File> result;
		try {
			result = this.fileDao.getFiles();
		} catch (Exception e) {
			e.printStackTrace();
			result = new LinkedList<>();
		}
		PageVo<File> pageVo = new PageVo<>(new PageInfo<>(result), sortKey, sortType);
		return ResultUtil.createResult("查询成功", pageVo);
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
		// 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		File file = this.fileDao.getFileById(fileId);
		if (file == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		return ResultUtil.createResult("查询成功", new FileInfo(file));
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
		// 权限验证
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
