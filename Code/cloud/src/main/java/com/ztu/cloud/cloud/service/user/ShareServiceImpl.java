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
import com.ztu.cloud.cloud.util.CommonUtil;
import com.ztu.cloud.cloud.util.RepositoryUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

    public ShareServiceImpl(UserMapper userDao, UserFileMapper userFileDao, ShareMapper shareDao,
        UserRepositoryDao userRepositoryDao, ShareRepositoryDao shareRepositoryDao) {
        this.userDao = userDao;
        this.userFileDao = userFileDao;
        this.shareDao = shareDao;
        this.userRepositoryDao = userRepositoryDao;
        this.shareRepositoryDao = shareRepositoryDao;
    }

    /**
     * 创建分享
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID name 分享名称 path 分享位置 password 密码
     * @return 分享信息
     */
    @Override
    public ResultResponseEntity createShare(String token, CreateShare parameter) {
        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        Folder source = RepositoryUtil.getFolder(repository, parameter.getPath());
        if (source == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        Folder folder = source.clone();
        String shareId = CommonUtil.getUuid();
        ShareRepository shareRepository = new ShareRepository(shareId, userId, folder);
        LinkedList<File> files = new LinkedList<>();
        LinkedList<Folder> folders = new LinkedList<>();
        RepositoryUtil.getFileList(folder, files);
        getFolderList(folder, folders);
        for (File item : files) {
            shareRepository.getFileIdList().add(item.getId());
            shareRepository.getUserFileIdList().add(item.getUserFileId());
            item.setPath(item.getPath().replaceFirst(source.getPath(), ""));
            item.setShareIdList(new LinkedList<>());
        }
        for (Folder item : folders) {
            item.setPath(item.getPath().replaceFirst(source.getPath(), ""));
            item.setDepth(item.getDepth() - folder.getDepth());
        }
        folder.setPath("");
        folder.setDepth(0);
        files.clear();
        RepositoryUtil.getFileList(source, files);
        LinkedList<String> shareIdList = new LinkedList<>();
        shareIdList.add(shareId);
        for (File item : files) {
            if (item.getShareIdList() == null) {
                item.setShareIdList(shareIdList);
            } else {
                item.getShareIdList().add(shareId);
            }
        }
        this.shareRepositoryDao.insert(shareRepository);
        Share share = new Share(shareId, userId, shareRepository.getId(), parameter.getName(), parameter.getPassword(),
            parameter.getValidTime());
        this.shareDao.insertShare(share);
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        return ResultUtil.createResult("成功", new ShareInfo(share));
    }

    /**
     * 获取分享列表
     *
     * @param token
     *            用户Token
     * @return 分享列表
     */
    @Override
    public ResultResponseEntity getShareList(String token) {
        int userId = TokenUtil.getId(token);
        List<Share> shareList = this.shareDao.getShareByUserId(userId);
        return ResultUtil.createResult("成功", new ShareList(shareList));
    }

    /**
     * 删除信息
     *
     * @param token
     *            用户Token
     * @param shareId
     *            分享ID
     * @return 删除结果
     */
    @Override
    public ResultResponseEntity deleteShare(String token, String shareId) {
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
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 shareId 分享ID password 分享密码
     * @return 分享信息
     */
    @Override
    public ResultResponseEntity getShare(String token, GetShare parameter) {
        Share shareInfo = this.shareDao.getShareById(parameter.getShareId());
        if (shareInfo == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        if (shareInfo.getStatus() != 1) {
            return ResultConstant.SHARE_INVALID;
        }
        if (shareInfo.getPassword() != null && shareInfo.getPassword().length() != 0 && parameter.getPassword() == null
            && !shareInfo.getPassword().equals(parameter.getPassword())) {
            return ResultConstant.PASSWORD_INVALID;
        }
        ShareRepository shareRepository = this.shareRepositoryDao.getById(shareInfo.getRepoId());
        return ResultUtil.createResult("成功",
            new com.ztu.cloud.cloud.common.vo.user.ShareRepository(shareInfo, shareRepository));
    }

    /**
     * 保存分享
     *
     * @param token
     *            用户信Token
     * @param parameter
     *            请求参数 shareId 分享ID password 分享密码 path 保存位置
     * @return 是否保存成功
     */
    @Override
    public ResultResponseEntity saveShare(String token, SaveShare parameter) {
        int userId = TokenUtil.getId(token);
        Share shareInfo = this.shareDao.getShareById(parameter.getShareId());
        if (shareInfo == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        if (shareInfo.getPassword() != null && parameter.getPassword() == null) {
            return ResultConstant.PASSWORD_INVALID;
        }
        if (shareInfo.getPassword() != null && !shareInfo.getPassword().equals(parameter.getPassword())) {
            return ResultConstant.PASSWORD_INVALID;
        }
        UserRepository userRepository = this.userRepositoryDao.getByUserId(userId);
        if (userRepository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        Folder folder = RepositoryUtil.getFolder(userRepository, parameter.getPath());
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
        RepositoryUtil.getFileList(source, files);
        getFolderList(source, folders);
        long size = 0;
        for (File item : files) {
            size += item.getSize();
            item.setPath(folder.getPath() + "/" + folder.getName() + item.getPath());
            UserFile userFile = new UserFile(userId, item.getId(), item.getPath());
            this.userFileDao.insertUserFile(userFile);
            item.setUserFileId(userFile.getId());
        }
        for (Folder item : folders) {
            item.setPath(folder.getPath() + "/" + folder.getName() + item.getPath());
            item.setDepth(item.getDepth() + folder.getDepth() + 1);
        }
        source.setDepth(folder.getDepth() + 1);
        source.setPath(folder.getPath() + "/" + folder.getName());
        if ((userRepository.getUseSize() + size) > userRepository.getRepoSize()) {
            return ResultConstant.REPOSITORY_FULL;
        }
        folder.getFolders().put(source.getName(), source);
        this.userRepositoryDao.updateUseSizeById(userRepository.getId(), userRepository.getUseSize() + size);
        this.userRepositoryDao.updateFolderById(userRepository.getId(), userRepository.getFolder());
        if (shareRepository.getSaveUserIdList() == null) {
            shareRepository.setSaveUserIdList(new HashMap<>(1));
            shareRepository.getSaveUserIdList().put(userId, new LinkedList<>());
        }
        if (shareRepository.getSaveUserIdList() != null && shareRepository.getSaveUserIdList().get(userId) == null) {
            shareRepository.getSaveUserIdList().put(userId, new LinkedList<>());
        }
        assert shareRepository.getSaveUserIdList() != null;
        shareRepository.getSaveUserIdList().get(userId).add(System.currentTimeMillis());
        this.shareRepositoryDao.updateSaveUserIdMapById(shareRepository.getId(), shareRepository.getSaveUserIdList());
        return ResultUtil.createResult(1, "成功");
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
}
