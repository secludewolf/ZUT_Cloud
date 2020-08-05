package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.dto.user.download.DownloadId;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 下载业务接口
 * @date 2020/06/27-09:34
 **/
public interface DownloadService {
    /**
     * 获取下载ID
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 shareId 分享ID repositoryId 仓库ID fileId 文件Id fileName 文件名 folder 文件夹
     * @return 下载ID
     */
    ResultResponseEntity getDownloadId(String token, DownloadId parameter);

    /**
     * 下载文件
     *
     * @param downloadId
     *            下载ID
     * @return 文件数据流
     */
    Download download(String downloadId);
}
