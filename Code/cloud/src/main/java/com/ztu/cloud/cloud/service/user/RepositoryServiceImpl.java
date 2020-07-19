package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.File;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserFileMapper;
import com.ztu.cloud.cloud.common.dto.user.repository.*;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.user.RepositoryInfo;
import com.ztu.cloud.cloud.util.ForbiddenUtil;
import com.ztu.cloud.cloud.util.RequestUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

/**
 * @author Jager
 * @description 仓库操作业务层实现
 * @date 2020/06/25-08:48
 **/
@Component
public class RepositoryServiceImpl implements RepositoryService {
	UserRepositoryDao userRepositoryDao;
	UserFileMapper userFileDao;
	FileMapper fileDao;
	ShareMapper shareDao;

	public RepositoryServiceImpl(UserRepositoryDao userRepositoryDao, UserFileMapper userFileDao, FileMapper fileDao, ShareMapper shareDao) {
		this.userRepositoryDao = userRepositoryDao;
		this.userFileDao = userFileDao;
		this.fileDao = fileDao;
		this.shareDao = shareDao;
	}

	/**
	 * 获取仓库信息
	 *
	 * @param token        用户Token
	 * @param repositoryId 仓库ID
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity getRepository(String token, String repositoryId) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(id);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(repositoryId)) {
			return ResultConstant.NO_ACCESS;
		}
		return ResultUtil.createResult("查询成功", new RepositoryInfo(repository));
	}

	/**
	 * 创建文件
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              fileId 文件ID
	 *              name 文件名
	 *              path 保存路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity createFile(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CreateFile createFile = RequestUtil.getCreateFile(data);
		if (createFile == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(createFile.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		if (!ForbiddenUtil.isFileNameValid(createFile.getName())) {
			return ResultConstant.NAME_INVALID;
		}
		String fileId = createFile.getFileId();
		com.ztu.cloud.cloud.common.bean.mysql.File file = this.fileDao.getFileById(fileId);
		if (file == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		if ((repository.getUseSize() + file.getSize()) > repository.getRepoSize()) {
			return ResultConstant.REPOSITORY_FULL;
		}
		Folder folder = getFolder(repository, createFile.getPath());
		if (folder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (folder.getFiles() == null) {
			folder.setFiles(new HashMap<>(1));
		}
		if (folder.getFiles().get(createFile.getName()) != null) {
			return ResultConstant.FILE_EXISTED;
		}
		UserFile userFile = new UserFile(userId, fileId, createFile.getPath());
		this.userFileDao.insertUserFile(userFile);
		this.fileDao.fileQuoteNumberAdd(file.getId(), 1);
		String[] temp = createFile.getName().split("\\.");
		String type = temp[temp.length - 1];
		folder.getFiles().put(createFile.getName(), new com.ztu.cloud.cloud.common.bean.mongodb.inside.File(createFile.getFileId(), userFile.getId(), createFile.getName(), createFile.getPath(), type, file.getSize()));
		repository.setUseSize(repository.getUseSize() + file.getSize());
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		this.userRepositoryDao.updateUseSizeById(repository.getId(), repository.getUseSize());
		return ResultUtil.createResult("创建成功", new RepositoryInfo(repository));
	}

	/**
	 * 创建文件夹
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              name 文件名
	 *              path 保存路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity createFolder(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CreateFolder createFolder = RequestUtil.getCreateFolder(data);
		if (createFolder == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(createFolder.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		if (!ForbiddenUtil.isFileNameValid(createFolder.getName())) {
			return ResultConstant.NAME_INVALID;
		}
		Folder folder = getFolder(repository, createFolder.getPath());
		if (folder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (folder.getFolders() == null) {
			folder.setFolders(new HashMap<>(1));
		}
		if (folder.getFolders().get(createFolder.getName()) != null) {
			return ResultConstant.FOLDER_EXISTED;
		}
		folder.getFolders().put(createFolder.getName(), new Folder(createFolder.getName(), createFolder.getPath(), folder.getDepth() + 1));
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		return ResultUtil.createResult("创建成功", new RepositoryInfo(repository));
	}

	/**
	 * 移动文件
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              name 文件名
	 *              oldPath 原路径
	 *              newPath 新路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity moveFile(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		MoveFile moveFile = RequestUtil.getMoveFile(data);
		if (moveFile == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(moveFile.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		Folder oldFolder = getFolder(repository, moveFile.getOldPath());
		if (oldFolder == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		File file = oldFolder.getFiles().get(moveFile.getName());
		if (file == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		Folder newFolder = getFolder(repository, moveFile.getNewPath());
		if (newFolder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (newFolder.getFiles() == null) {
			newFolder.setFiles(new HashMap<>(1));
		}
		if (newFolder.getFiles().get(moveFile.getName()) != null) {
			return ResultConstant.FILE_EXISTED;
		}
		file.setPath(moveFile.getNewPath());
		file.setChangeTime(System.currentTimeMillis());
		this.userFileDao.updateUserFilePath(file.getUserFileId(), moveFile.getNewPath());
		oldFolder.getFiles().remove(file.getName());
		newFolder.getFiles().put(file.getName(), file);
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		return ResultUtil.createResult("创建成功", new RepositoryInfo(repository));
	}

	/**
	 * 移动文件夹
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              name 文件名
	 *              oldPath 原路径
	 *              newPath 新路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity moveFolder(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		MoveFolder moveFolder = RequestUtil.getMoveFolder(data);
		if (moveFolder == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(moveFolder.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		if ((moveFolder.getOldPath() + "/" + moveFolder.getName()).equals(moveFolder.getNewPath().substring(0, (moveFolder.getOldPath() + "/" + moveFolder.getName()).length()))) {
			return ResultConstant.PATH_INVALID;
		}
		Folder oldFolder = getFolder(repository, moveFolder.getOldPath());
		if (oldFolder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (oldFolder.getFolders() == null) {
			oldFolder.setFolders(new HashMap<>(1));
		}
		Folder folder = oldFolder.getFolders().get(moveFolder.getName());
		if (folder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		Folder newFolder = getFolder(repository, moveFolder.getNewPath());
		if (newFolder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (newFolder.getFolders() == null) {
			newFolder.setFolders(new HashMap<>());
		}
		if (newFolder.getFolders().get(folder.getName()) != null) {
			return ResultConstant.FOLDER_EXISTED;
		}
		LinkedList<File> files = new LinkedList<>();
		LinkedList<Folder> folders = new LinkedList<>();
		getFileList(folder, files);
		getFolderList(folder, folders);
		int depthDiffer = oldFolder.getDepth() - newFolder.getDepth();
		for (File file : files) {
			file.setPath(file.getPath().replaceFirst(moveFolder.getOldPath(), moveFolder.getNewPath()));
		}
		for (Folder temp : folders) {
			temp.setPath(temp.getPath().replaceFirst(moveFolder.getOldPath(), moveFolder.getNewPath()));
			temp.setDepth(temp.getDepth() - depthDiffer);
		}
		folder.setPath(folder.getPath().replaceFirst(moveFolder.getOldPath(), moveFolder.getNewPath()));
		folder.setChangeTime(System.currentTimeMillis());
		oldFolder.getFolders().remove(folder.getName());
		newFolder.getFolders().put(moveFolder.getName(), folder);
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		return ResultUtil.createResult("移动成功", new RepositoryInfo(repository));
	}

	/**
	 * 复制文件
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              name 文件名
	 *              oldPath 原路径
	 *              newPath 新路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity copyFile(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CopyFile copyFile = RequestUtil.getCopyFile(data);
		if (copyFile == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(copyFile.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		Folder oldFolder = getFolder(repository, copyFile.getOldPath());
		if (oldFolder == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		File file = oldFolder.getFiles().get(copyFile.getName());
		if (file == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		if ((repository.getUseSize() + file.getSize()) > repository.getRepoSize()) {
			return ResultConstant.REPOSITORY_FULL;
		}
		Folder newFolder = getFolder(repository, copyFile.getNewPath());
		if (newFolder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (newFolder.getFiles() == null) {
			newFolder.setFiles(new HashMap<>(1));
		}
		if (newFolder.getFiles().get(copyFile.getName()) != null) {
			return ResultConstant.FILE_EXISTED;
		}
		File newFile = file.clone();
		if (newFile == null) {
			return ResultConstant.SERVER_ERROR;
		}
		UserFile userFile = new UserFile(userId, newFile.getId(), copyFile.getNewPath());
		this.userFileDao.insertUserFile(userFile);
		this.fileDao.fileQuoteNumberAdd(file.getId(), 1);
		newFile.setPath(copyFile.getNewPath());
		newFile.setUserFileId(userFile.getId());
		newFile.setChangeTime(System.currentTimeMillis());
		newFolder.getFiles().put(newFile.getName(), newFile);
		repository.setUseSize(repository.getUseSize() + file.getSize());
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		return ResultUtil.createResult("复制成功", new RepositoryInfo(repository));
	}

	/**
	 * 复制文件夹
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              name 文件名
	 *              oldPath 原路径
	 *              newPath 新路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity copyFolder(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CopyFolder copyFolder = RequestUtil.getCopyFolder(data);
		if (copyFolder == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(copyFolder.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		if ((copyFolder.getOldPath() + "/" + copyFolder.getName()).equals(copyFolder.getNewPath().substring(0, (copyFolder.getOldPath() + "/" + copyFolder.getName()).length()))) {
			return ResultConstant.PATH_INVALID;
		}
		Folder oldParent = getFolder(repository, copyFolder.getOldPath());
		if (oldParent == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (oldParent.getFolders() == null) {
			oldParent.setFolders(new HashMap<>(1));
		}
		Folder folder = oldParent.getFolders().get(copyFolder.getName());
		if (folder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		Folder newParent = getFolder(repository, copyFolder.getNewPath());
		if (newParent == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (newParent.getFolders() == null) {
			newParent.setFolders(new HashMap<>());
		}
		if (newParent.getFolders().get(folder.getName()) != null) {
			return ResultConstant.FOLDER_EXISTED;
		}
		LinkedList<File> files = new LinkedList<>();
		LinkedList<Folder> folders = new LinkedList<>();
		Folder newFolder = folder.clone();
		getFileList(newFolder, files);
		getFolderList(newFolder, folders);
		long size = 0;
		int depthDiffer = oldParent.getDepth() - newParent.getDepth();
		for (File file : files) {
			size += file.getSize();
			file.setPath(file.getPath().replaceFirst(copyFolder.getOldPath(), copyFolder.getNewPath()));
			UserFile userFile = new UserFile(userId, file.getId(), copyFolder.getNewPath());
			this.userFileDao.insertUserFile(userFile);
			file.setUserFileId(userFile.getId());
			this.fileDao.fileQuoteNumberAdd(file.getId(), 1);
		}
		for (Folder temp : folders) {
			temp.setPath(temp.getPath().replaceFirst(copyFolder.getOldPath(), copyFolder.getNewPath()));
			temp.setDepth(temp.getDepth() - depthDiffer);
		}
		if ((repository.getUseSize() + size) > repository.getRepoSize()) {
			return ResultConstant.REPOSITORY_FULL;
		}
		newFolder.setPath(folder.getPath().replaceFirst(copyFolder.getOldPath(), copyFolder.getNewPath()));
		newFolder.setChangeTime(System.currentTimeMillis());
		newParent.getFolders().put(copyFolder.getName(), newFolder);
		repository.setUseSize(repository.getUseSize() + size);
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		return ResultUtil.createResult("复制成功", new RepositoryInfo(repository));
	}

	/**
	 * 文件重命名
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              oldName 原名称
	 *              newName 新名称
	 *              path 路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity renameFile(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		RenameFile renameFile = RequestUtil.getRenameFile(data);
		if (renameFile == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(renameFile.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		if (!ForbiddenUtil.isFileNameValid(renameFile.getNewName())) {
			return ResultConstant.NAME_INVALID;
		}
		Folder folder = getFolder(repository, renameFile.getPath());
		if (folder == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		if (folder.getFiles() == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		if (folder.getFiles().get(renameFile.getNewName()) != null) {
			return ResultConstant.FILE_EXISTED;
		}
		File file = folder.getFiles().get(renameFile.getOldName());
		if (file == null) {
			return ResultConstant.FILE_NOT_EXISTED;
		}
		file.setName(renameFile.getNewName());
		file.setChangeTime(System.currentTimeMillis());
		folder.getFiles().remove(renameFile.getOldName());
		folder.getFiles().put(renameFile.getNewName(), file);
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		return ResultUtil.createResult("修改成功", new RepositoryInfo(repository));
	}

	/**
	 * 文件夹重命名
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              oldName 原名称
	 *              newName 新名称
	 *              path 路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity renameFolder(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		RenameFolder renameFolder = RequestUtil.getRenameFolder(data);
		if (renameFolder == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(renameFolder.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		if (!ForbiddenUtil.isFileNameValid(renameFolder.getNewName())) {
			return ResultConstant.NAME_INVALID;
		}
		Folder parent = getFolder(repository, renameFolder.getPath());
		if (parent == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (parent.getFolders() == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		if (parent.getFolders().get(renameFolder.getNewName()) != null) {
			return ResultConstant.FOLDER_EXISTED;
		}
		Folder folder = parent.getFolders().get(renameFolder.getOldName());
		if (folder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		LinkedList<File> files = new LinkedList<>();
		LinkedList<Folder> folders = new LinkedList<>();
		getFileList(folder, files);
		getFolderList(folder, folders);
		for (File file : files) {
			file.setPath(file.getPath().replaceFirst(folder.getPath() + "/" + renameFolder.getOldName(), folder.getPath() + "/" + renameFolder.getNewName()));
		}
		for (Folder temp : folders) {
			temp.setPath(temp.getPath().replaceFirst(folder.getPath() + "/" + renameFolder.getOldName(), folder.getPath() + "/" + renameFolder.getNewName()));
		}
		folder.setName(renameFolder.getNewName());
		folder.setChangeTime(System.currentTimeMillis());
		parent.getFolders().remove(renameFolder.getOldName());
		parent.getFolders().put(renameFolder.getNewName(), folder);
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		return ResultUtil.createResult("修改成功", new RepositoryInfo(repository));
	}

	/**
	 * 删除文件
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              isFile 是否是文件
	 *              name 文件名
	 *              path 保存路径
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity deleteFromRepository(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		DeleteFileToRecyclebin deleteFileToRecyclebin = RequestUtil.getDeleteFileToRecyclebin(data);
		if (deleteFileToRecyclebin == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(deleteFileToRecyclebin.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		Folder parent = getFolder(repository, deleteFileToRecyclebin.getPath());
		if (parent == null) {
			return ResultConstant.FOLDER_EXISTED;
		}
		if (deleteFileToRecyclebin.isFile()) {
			if (parent.getFiles() == null) {
				return ResultConstant.FILE_NOT_EXISTED;
			}
			File file = parent.getFiles().get(deleteFileToRecyclebin.getName());
			if (file == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			String uuid = getUUID();
			if (repository.getRecycleBin().getFiles() == null) {
				repository.getRecycleBin().setFiles(new HashMap<>(1));
			}
			repository.getRecycleBin().getFiles().put(uuid, file);
			parent.getFiles().remove(file.getName());
		} else {
			if (parent.getFolders() == null) {
				return ResultConstant.FILE_NOT_EXISTED;
			}
			Folder folder = parent.getFolders().get(deleteFileToRecyclebin.getName());
			if (folder == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			String uuid = getUUID();
			if (repository.getRecycleBin().getFolders() == null) {
				repository.getRecycleBin().setFolders(new HashMap<>(1));
			}
			repository.getRecycleBin().getFolders().put(uuid, folder);
			parent.getFolders().remove(folder.getName());
		}
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
		return ResultUtil.createResult("删除成功", new RepositoryInfo(repository));
	}

	/**
	 * 恢复文件
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              isFile 是否是文件
	 *              recycleId 回收站ID
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity restoreFromRecycleBin(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		RestoreFile restoreFile = RequestUtil.getDeleteRestoreFile(data);
		if (restoreFile == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(restoreFile.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		if (restoreFile.isFile()) {
			if (repository.getRecycleBin().getFiles() == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			File file = repository.getRecycleBin().getFiles().get(restoreFile.getRecycleId());
			if (file == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			Folder folder = getFolder(repository, file.getPath());
			if (folder == null) {
				return ResultConstant.FOLDER_NOT_EXISTED;
			}
			if (folder.getFiles() == null) {
				folder.setFiles(new HashMap<>(1));
			}
			if (folder.getFiles().get(file.getName()) != null) {
				return ResultConstant.FILE_EXISTED;
			}
			folder.getFiles().put(file.getName(), file);
			repository.getRecycleBin().getFiles().remove(restoreFile.getRecycleId());
		} else {
			if (repository.getRecycleBin().getFolders() == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			Folder folder = repository.getRecycleBin().getFolders().get(restoreFile.getRecycleId());
			if (folder == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			Folder parent = getFolder(repository, folder.getPath());
			if (parent == null) {
				return ResultConstant.FOLDER_NOT_EXISTED;
			}
			if (parent.getFolders() == null) {
				parent.setFolders(new HashMap<>(1));
			}
			if (parent.getFolders().get(folder.getName()) != null) {
				return ResultConstant.FOLDER_EXISTED;
			}
			parent.getFolders().put(folder.getName(), folder);
			repository.getRecycleBin().getFolders().remove(restoreFile.getRecycleId());
		}
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
		return ResultUtil.createResult("恢复成功", new RepositoryInfo(repository));
	}

	/**
	 * 删除文件
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              isFile 是否是文件
	 *              recycleId 回收站ID
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity deleteFromRecycleBin(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		DeleteFileFromRecyclebin deleteFileFromRecyclebin = RequestUtil.getDeleteFileFromRecyclebin(data);
		if (deleteFileFromRecyclebin == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(deleteFileFromRecyclebin.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		if (deleteFileFromRecyclebin.isFile()) {
			if (repository.getRecycleBin().getFiles() == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			File file = repository.getRecycleBin().getFiles().get(deleteFileFromRecyclebin.getRecycleId());
			if (file == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			if (file.getShareIdList() != null) {
				for (String shareId : file.getShareIdList()) {
					this.shareDao.updateShareStatus(shareId, -1);
				}
			}
			repository.setUseSize(repository.getUseSize() - file.getSize());
			this.userFileDao.deleteUserFile(file.getUserFileId());
			this.fileDao.fileQuoteNumberSub(file.getId(), 1);
			repository.getRecycleBin().getFiles().remove(deleteFileFromRecyclebin.getRecycleId());
		} else {
			if (repository.getRecycleBin().getFolders() == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			Folder folder = repository.getRecycleBin().getFolders().get(deleteFileFromRecyclebin.getRecycleId());
			if (folder == null) {
				return ResultConstant.TARGET_NOT_EXISTED;
			}
			LinkedList<File> files = new LinkedList<>();
			getFileList(folder, files);
			long size = 0;
			for (File file : files) {
				size += file.getSize();
				if (file.getShareIdList() != null) {
					for (String shareId : file.getShareIdList()) {
						this.shareDao.updateShareStatus(shareId, -1);
					}
				}
				this.userFileDao.deleteUserFile(file.getUserFileId());
				this.fileDao.fileQuoteNumberSub(file.getId(), 1);
			}
			repository.setUseSize(repository.getUseSize() - size);
			repository.getRecycleBin().getFolders().remove(deleteFileFromRecyclebin.getRecycleId());
		}
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
		return ResultUtil.createResult("删除成功", new RepositoryInfo(repository));
	}

	/**
	 * 删除文件
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 * @return 仓库信息
	 */
	@Override
	public ResultResponseEntity cleanRecycleBin(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CleanRecyclebin cleanRecyclebin = RequestUtil.getCleanRecyclebin(data);
		if (cleanRecyclebin == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		UserRepository repository = this.userRepositoryDao.getByUserId(userId);
		if (repository == null) {
			return ResultConstant.REPOSITORY_NOT_FOUND;
		}
		if (repository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		if (!repository.getId().equals(cleanRecyclebin.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		long size = 0;
		if (repository.getRecycleBin().getFiles() != null) {
			Collection<File> values = repository.getRecycleBin().getFiles().values();
			for (File file : values) {
				size += file.getSize();
				if (file.getShareIdList() != null) {
					for (String shareId : file.getShareIdList()) {
						this.shareDao.updateShareStatus(shareId, -1);
					}
				}
				this.userFileDao.deleteUserFile(file.getUserFileId());
				this.fileDao.fileQuoteNumberSub(file.getId(), 1);
			}
			repository.getRecycleBin().getFiles().clear();
		}
		if (repository.getRecycleBin().getFolders() != null) {
			Collection<Folder> values = repository.getRecycleBin().getFolders().values();
			for (Folder folder : values) {
				LinkedList<File> files = new LinkedList<>();
				getFileList(folder, files);
				for (File file : files) {
					size += file.getSize();
					if (file.getShareIdList() != null) {
						for (String shareId : file.getShareIdList()) {
							this.shareDao.updateShareStatus(shareId, -1);
						}
					}
					this.userFileDao.deleteUserFile(file.getUserFileId());
					this.fileDao.fileQuoteNumberSub(file.getId(), 1);
				}
			}
			repository.getRecycleBin().getFolders().clear();
		}
		repository.setUseSize(repository.getUseSize() - size);
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
		return ResultUtil.createResult("删除成功", new RepositoryInfo(repository));
	}

	private Folder getFolder(UserRepository repository, String path) {
		String[] list = path.split("/");
		if (!"root".equals(list[1])) {
			return null;
		}
		Folder folder = repository.getFolder();
		for (int i = 2; folder != null && i < list.length; i++) {
			folder = folder.getFolders().get(list[i]);
		}
		return folder;
	}

	private void getFileList(Folder folder, Collection<File> files) {
		if (folder.getFiles() != null) {
			Collection<File> values = folder.getFiles().values();
			files.addAll(values);
		}
		if (folder.getFolders() != null) {
			Collection<Folder> values = folder.getFolders().values();
			for (Folder temp : values) {
				getFileList(temp, files);
			}
		}
	}

	private void getFolderList(Folder folder, Collection<Folder> folders) {
		if (folder.getFolders() != null) {
			Collection<Folder> values = folder.getFolders().values();
			folders.addAll(values);
			for (Folder temp : values) {
				getFolderList(temp, folders);
			}
		}
	}

	private static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		return str.replace("-", "");
	}
}
