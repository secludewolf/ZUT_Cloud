package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.File;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mysql.Share;
import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.ShareRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserFileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.dto.user.share.CreateShare;
import com.ztu.cloud.cloud.common.dto.user.share.GetShare;
import com.ztu.cloud.cloud.common.dto.user.share.SaveShare;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.user.ShareInfo;
import com.ztu.cloud.cloud.common.vo.user.ShareList;
import com.ztu.cloud.cloud.util.RequestUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Jager
 * @description 分享业务实现
 * @date 2020/06/28-09:51
 **/
@Component
public class ShareServiceImpl implements ShareService {
	UserMapper userDao;
	UserFileMapper userFileDao;
	ShareMapper shareDao;
	UserRepositoryDao userRepositoryDao;
	ShareRepositoryDao shareRepositoryDao;

	public ShareServiceImpl(UserMapper userDao, UserFileMapper userFileDao, ShareMapper shareDao, UserRepositoryDao userRepositoryDao, ShareRepositoryDao shareRepositoryDao) {
		this.userDao = userDao;
		this.userFileDao = userFileDao;
		this.shareDao = shareDao;
		this.userRepositoryDao = userRepositoryDao;
		this.shareRepositoryDao = shareRepositoryDao;
	}

	/**
	 * 创建分享
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              name 分享名称
	 *              path 分享位置
	 *              password 密码
	 * @return 分享信息
	 */
	@Override
	public ResultResponseEntity createShare(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CreateShare createShare = RequestUtil.getCreateShare(data);
		if (createShare == null) {
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
		if (!repository.getId().equals(createShare.getRepositoryId())) {
			return ResultConstant.NO_ACCESS;
		}
		Folder source = getFolder(repository, createShare.getPath());
		if (source == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		Folder folder = source.clone();
		String shareId = getUUID();
		ShareRepository shareRepository = new ShareRepository(shareId, userId, folder);
		LinkedList<File> files = new LinkedList<>();
		LinkedList<Folder> folders = new LinkedList<>();
		getFileList(folder, files);
		getFolderList(folder, folders);
		for (File temp : files) {
			shareRepository.getFileIdList().add(temp.getId());
			shareRepository.getUserFileIdList().add(temp.getUserFileId());
			temp.setPath(temp.getPath().replaceFirst(source.getPath(), ""));
			temp.setShareIdList(null);
		}
		for (Folder temp : folders) {
			temp.setPath(temp.getPath().replaceFirst(source.getPath(), ""));
			temp.setDepth(temp.getDepth() - folder.getDepth());
		}
		folder.setPath("");
		folder.setDepth(0);
		files.clear();
		getFileList(source, files);
		LinkedList<String> shareIdList = new LinkedList<>();
		shareIdList.add(shareId);
		for (File temp : files) {
			if (temp.getShareIdList() == null) {
				temp.setShareIdList(shareIdList);
			} else {
				temp.getShareIdList().add(shareId);
			}
		}
		this.shareRepositoryDao.insert(shareRepository);
		Share share = new Share(shareId, userId, shareRepository.getId(), createShare.getName(), createShare.getPassword(), createShare.getValidTime());
		this.shareDao.insertShare(share);
		this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
		return ResultUtil.createResult("成功", new ShareInfo(share));
	}

	/**
	 * 获取分享列表
	 *
	 * @param token 用户Token
	 * @return 分享列表
	 */
	@Override
	public ResultResponseEntity getShareList(String token) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int userId = TokenUtil.getId(token);
		List<Share> shareList = this.shareDao.getShareByUserId(userId);
		return ResultUtil.createResult("成功", new ShareList(shareList));
	}

	/**
	 * 删除信息
	 *
	 * @param token   用户Token
	 * @param shareId 分享ID
	 * @return 删除结果
	 */
	@Override
	public ResultResponseEntity deleteShare(String token, String shareId) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int userId = TokenUtil.getId(token);
		Share share = this.shareDao.getShareById(shareId);
		if (share == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		if (share.getUserId() != userId) {
			return ResultConstant.NO_ACCESS;
		}
		this.shareDao.updateShareStatus(shareId, -1);
		return ResultUtil.createResult(1, "删除成功");
	}

	/**
	 * 获取分享信息
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              shareId 分享ID
	 *              password 分享密码
	 * @return 分享信息
	 */
	@Override
	public ResultResponseEntity getShare(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		GetShare getShare = RequestUtil.getShare(data);
		if (getShare == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		Share shareInfo = this.shareDao.getShareById(getShare.getShareId());
		if (shareInfo == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		if (shareInfo.getStatus() != 1) {
			return ResultConstant.SHARE_INVALID;
		}
		if (shareInfo.getPassword() != null && getShare.getPassword() == null) {
			return ResultConstant.PASSWORD_INVALID;
		}
		if (shareInfo.getPassword() != null && !shareInfo.getPassword().equals(getShare.getPassword())) {
			return ResultConstant.PASSWORD_INVALID;
		}
		ShareRepository shareRepository = this.shareRepositoryDao.getById(shareInfo.getRepoId());
		return ResultUtil.createResult("成功", new com.ztu.cloud.cloud.common.vo.user.ShareRepository(shareInfo, shareRepository));
	}

	/**
	 * 保存分享
	 *
	 * @param token 用户信Token
	 * @param data  请求参数
	 *              shareId 分享ID
	 *              password 分享密码
	 *              path 保存位置
	 * @return 是否保存成功
	 */
	@Override
	public ResultResponseEntity saveShare(String token, String data) {
		if (!TokenUtil.isUser(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		SaveShare saveShare = RequestUtil.getSaveShare(data);
		if (saveShare == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int userId = TokenUtil.getId(token);
		Share shareInfo = this.shareDao.getShareById(saveShare.getShareId());
		if (shareInfo == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		if (shareInfo.getPassword() != null && saveShare.getPassword() == null) {
			return ResultConstant.PASSWORD_INVALID;
		}
		if (shareInfo.getPassword() != null && !shareInfo.getPassword().equals(saveShare.getPassword())) {
			return ResultConstant.PASSWORD_INVALID;
		}
		UserRepository userRepository = this.userRepositoryDao.getByUserId(userId);
		if (userRepository.getStatus() != 1) {
			return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
		}
		Folder folder = getFolder(userRepository, saveShare.getPath());
		if (folder == null) {
			return ResultConstant.FOLDER_NOT_EXISTED;
		}
		ShareRepository shareRepository = this.shareRepositoryDao.getById(shareInfo.getRepoId());
		Folder source = shareRepository.getFolder();
		if (folder.getFolders().get(source.getName()) != null) {
			return ResultConstant.FILE_EXISTED;
		}
		LinkedList<File> files = new LinkedList<>();
		LinkedList<Folder> folders = new LinkedList<>();
		getFileList(source, files);
		getFolderList(source, folders);
		long size = 0;
		for (File temp : files) {
			size += temp.getSize();
			temp.setPath(folder.getPath() + "/" + folder.getName() + temp.getPath());
			UserFile userFile = new UserFile(userId, temp.getId(), temp.getPath());
			this.userFileDao.insertUserFile(userFile);
			temp.setUserFileId(userFile.getId());
		}
		for (Folder temp : folders) {
			temp.setPath(folder.getPath() + "/" + folder.getName() + temp.getPath());
			temp.setDepth(temp.getDepth() + folder.getDepth() + 1);
		}
		source.setDepth(folder.getDepth() + 1);
		source.setPath(folder.getPath() + "/" + folder.getName());
		if ((userRepository.getUseSize() + size) > userRepository.getRepoSize()) {
			return ResultConstant.REPOSITORY_FULL;
		}
		folder.getFolders().put(source.getName(), source);
		this.userRepositoryDao.updateFolderById(userRepository.getId(), userRepository.getFolder());
		if (shareRepository.getSaveUserIdList() == null) {
			shareRepository.setSaveUserIdList(new HashMap<>());
			shareRepository.getSaveUserIdList().put(userId, new LinkedList<>());
		}
		if (shareRepository.getSaveUserIdList() != null && shareRepository.getSaveUserIdList().get(userId) == null) {
			shareRepository.getSaveUserIdList().put(userId, new LinkedList<>());
		}
		shareRepository.getSaveUserIdList().get(userId).add(System.currentTimeMillis());
		this.shareRepositoryDao.updateSaveUserIdMapById(shareRepository.getId(), shareRepository.getSaveUserIdList());
		return ResultUtil.createResult(1, "成功");
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
