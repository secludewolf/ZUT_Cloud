package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import com.ztu.cloud.cloud.common.bean.mysql.File;
import com.ztu.cloud.cloud.common.bean.mysql.Share;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import com.ztu.cloud.cloud.common.bean.redis.Download;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.ShareRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserFileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.dao.redis.DownLoadDao;
import com.ztu.cloud.cloud.common.dto.user.download.DownloadId;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.util.RequestUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.StoreUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author Jager
 * @description 下载业务实现
 * @date 2020/06/27-09:34
 **/
@Component
public class DownLoadServiceImpl implements DownloadService {
    UserMapper userDao;
    ShareMapper shareDao;
    FileMapper fileDao;
    UserFileMapper userFileDao;
    ShareRepositoryDao shareRepositoryDao;
    StoreUtil storeUtil;
    DownLoadDao downloadDao;

    public DownLoadServiceImpl(UserMapper userDao, ShareMapper shareDao, FileMapper fileDao, UserFileMapper userFileDao, ShareRepositoryDao shareRepositoryDao, StoreUtil storeUtil, DownLoadDao downloadDao) {
        this.userDao = userDao;
        this.shareDao = shareDao;
        this.fileDao = fileDao;
        this.userFileDao = userFileDao;
        this.shareRepositoryDao = shareRepositoryDao;
        this.storeUtil = storeUtil;
        this.downloadDao = downloadDao;
    }

    /**
     * 获取下载ID
     *
     * @param token 用户Token
     * @param data  请求参数
     *              shareId 分享ID
     *              repositoryId 仓库ID
     *              fileId 文件Id
     *              fileName 文件名
     *              folder 文件夹
     * @return 下载ID
     */
    @Override
    public ResultResponseEntity getDownloadId(String token, String data) {
        //TODO 创建缓存文件并使用缓存文件下载
        //TODO 判断文件状态,敏感文件下载重定向
        //TODO 防止重复创建链接
        //TODO 更新下载次数

        DownloadId downloadId = RequestUtil.getDownloadId(data);
        if (downloadId == null) {
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
        if (downloadId.getFolder() != null) {
            //TODO 带结构多文件下载
            return ResultConstant.SERVER_ERROR;
        }
        if (downloadId.getRepositoryId() != null && downloadId.getUserFileId() != null) {
            UserFile userFile = this.userFileDao.getUserFile(downloadId.getUserFileId());
            if (userFile == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (userFile.getUserId() == id) {
                Download download = new Download();
                String uuid = getUUID();
                download.setId(uuid);
                download.setFileId(userFile.getFileId());
                download.setName(downloadId.getFileName());
                this.downloadDao.insert(download);
                return ResultUtil.createResult("成功", new com.ztu.cloud.cloud.common.vo.user.DownloadId(uuid));
            }
        }
        if (downloadId.getShareId() != null && downloadId.getUserFileId() != null) {
            Share share = this.shareDao.getShareById(downloadId.getShareId());
            if (share == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (share.getStatus() != 1) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (share.getValidTime() < System.currentTimeMillis()) {
                this.shareDao.updateShareStatus(share.getId(), -1);
            }
            ShareRepository shareRepository = this.shareRepositoryDao.getById(downloadId.getShareId());
            if (shareRepository == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (!shareRepository.getUserFileIdList().contains(downloadId.getUserFileId())) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            UserFile userFile = this.userFileDao.getUserFile(downloadId.getUserFileId());
            if (userFile == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (userFile.getUserId() == id) {
                Download download = new Download();
                String uuid = getUUID();
                download.setId(uuid);
                download.setFileId(userFile.getFileId());
                download.setName(downloadId.getFileName());
                download.setShareId(downloadId.getShareId());
                this.downloadDao.insert(download);
                return ResultUtil.createResult("成功", new com.ztu.cloud.cloud.common.vo.user.DownloadId(uuid));
            }
        }
        return ResultConstant.REQUEST_PARAMETER_ERROR;
    }

    /**
     * 下载文件
     *
     * @param downloadId 下载ID
     * @return 文件数据流
     */
    @Override
    public com.ztu.cloud.cloud.common.dto.user.download.Download download(String downloadId) {
        Download download = this.downloadDao.get(downloadId);
        if (download == null) {
            return null;
        }
        //TODO 缓存下载
        //TODO 检测文件状态重定向下载位置
        File fileInfo = this.fileDao.getFileById(download.getFileId());
        if (fileInfo == null) {
            return null;
        }
        InputStream file = this.storeUtil.getFile(fileInfo.getPath());
        return new com.ztu.cloud.cloud.common.dto.user.download.Download(download.getName(), file);
    }

    private static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replace("-", "");
    }
}
