package com.ztu.cloud.cloud.service.common;

import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import com.ztu.cloud.cloud.common.bean.mysql.*;
import com.ztu.cloud.cloud.common.bean.redis.Download;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.ShareRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.*;
import com.ztu.cloud.cloud.common.dao.redis.DownLoadDao;
import com.ztu.cloud.cloud.common.dto.user.download.DownloadId;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
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
    AdminMapper adminMapper;
    ShareMapper shareDao;
    FileMapper fileDao;
    UserFileMapper userFileDao;
    ShareRepositoryDao shareRepositoryDao;
    StoreUtil storeUtil;
    DownLoadDao downloadDao;

    public DownLoadServiceImpl(UserMapper userDao, AdminMapper adminMapper, ShareMapper shareDao, FileMapper fileDao, UserFileMapper userFileDao, ShareRepositoryDao shareRepositoryDao, StoreUtil storeUtil, DownLoadDao downloadDao) {
        this.userDao = userDao;
        this.adminMapper = adminMapper;
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
     * @param token     用户Token
     * @param parameter 请求参数 shareId 分享ID repositoryId 仓库ID fileId 文件Id fileName 文件名 folder 文件夹
     * @return 下载ID
     */
    @Override
    public ResultResponseEntity getDownloadId(String token, DownloadId parameter) {
        // TODO 创建缓存文件并使用缓存文件下载
        // TODO 判断文件状态,敏感文件下载重定向
        // TODO 防止重复创建链接
        // TODO 更新下载次数

        int id = TokenUtil.getId(token);
        User user = this.userDao.getUserById(id);
        if (user == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (user.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        if (parameter.getFolder() != null) {
            // TODO 带结构多文件下载
            return ResultConstant.SERVER_ERROR;
        }
        // 仓库下载
        if (parameter.getRepositoryId() != null && parameter.getUserFileId() != null) {
            UserFile userFile = this.userFileDao.getUserFile(parameter.getUserFileId());
            if (userFile == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (userFile.getUserId() == id) {
                File file = this.fileDao.getFileById(userFile.getFileId());
                if (file == null) {
                    return ResultConstant.FILE_NOT_EXISTED;
                }
                if (file.getStatus() != 1) {
                    return ResultConstant.FILE_INVALID;
                }
                Download download = new Download();
                String uuid = getUUID();
                download.setId(uuid);
                download.setFileId(userFile.getFileId());
                download.setName(parameter.getFileName());
                this.downloadDao.insert(download);
                return ResultUtil.createResult("成功", new com.ztu.cloud.cloud.common.vo.user.DownloadId(uuid));
            }
        }
        // 分享下载
        // TODO 密码验证
        // TODO 漏洞:shareId不存在也能成功获取下载链接
        if (parameter.getShareId() != null && parameter.getUserFileId() != null) {
            Share share = this.shareDao.getShareById(parameter.getShareId());
            if (share == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (share.getStatus() != 1) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (share.getValidTime() < System.currentTimeMillis()) {
                this.shareDao.updateShareStatus(share.getId(), -1);
            }
            ShareRepository shareRepository = this.shareRepositoryDao.getByShareId(parameter.getShareId());
            if (shareRepository == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (!shareRepository.getUserFileIdList().contains(parameter.getUserFileId())) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            UserFile userFile = this.userFileDao.getUserFile(parameter.getUserFileId());
            if (userFile == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            if (userFile.getUserId() == id) {
                File file = this.fileDao.getFileById(userFile.getFileId());
                if (file == null) {
                    return ResultConstant.FILE_NOT_EXISTED;
                }
                if (file.getStatus() != 1) {
                    return ResultConstant.FILE_INVALID;
                }
                Download download = new Download();
                String uuid = getUUID();
                download.setId(uuid);
                download.setFileId(userFile.getFileId());
                download.setName(parameter.getFileName());
                download.setShareId(parameter.getShareId());
                this.downloadDao.insert(download);
                return ResultUtil.createResult("成功", new com.ztu.cloud.cloud.common.vo.user.DownloadId(uuid));
            }
        }
        return ResultConstant.REQUEST_PARAMETER_ERROR;
    }

    /**
     * 获取下载ID
     *
     * @param token  管理员Token
     * @param fileId 文件ID
     * @return 下载ID
     */
    @Override
    public ResultResponseEntity getAdminDownloadId(String token, String fileId) {
        // TODO 创建缓存文件并使用缓存文件下载
        // TODO 防止重复创建链接
        // TODO 更新下载次数
        int id = TokenUtil.getId(token);
        Admin admin = this.adminMapper.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }

        File fileById = this.fileDao.getFileById(fileId);
        if (fileById == null) {
            return ResultConstant.REQUEST_PARAMETER_ERROR;
        }
        // 仓库下载
        Download download = new Download();
        String uuid = getUUID();
        download.setId(uuid);
        download.setFileId(fileId);
        download.setName(fileById.getName());
        this.downloadDao.insert(download);
        return ResultUtil.createResult("成功", new com.ztu.cloud.cloud.common.vo.user.DownloadId(uuid));
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
        // TODO 缓存下载
        // TODO 检测文件状态重定向下载位置
        File fileInfo = this.fileDao.getFileById(download.getFileId());
        if (fileInfo == null) {
            return null;
        }
        InputStream file = this.storeUtil.getFileInputStream(fileInfo.getPath());
        //TODO inputStream可能为空
        return new com.ztu.cloud.cloud.common.dto.user.download.Download(download.getName(), file);
    }

    private static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replace("-", "");
    }
}
