package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 文件控制业务接口
 * @date 2020/06/28-08:44
 **/
public interface FileService {
    /**
     * 下载文件
     *
     * @param token  管理员Token
     * @param fileId 文件id
     * @return 文件数据
     */
    Download download(String token, String fileId);

    /**
     * 获取文件列表
     *
     * @param token      管理员Token
     * @param pageNumber 第几页
     * @return 文件列表
     */
    ResultResponseEntity getFileList(String token, int pageNumber);

    /**
     * 获取文件信息
     *
     * @param token  管理员Token
     * @param fileId 文件ID
     * @return 文件信息
     */
    ResultResponseEntity getFileInfo(String token, String fileId);

    /**
     * 删除文件
     *
     * @param token  管理员Token
     * @param fileId 文件ID
     * @return 是否成功
     */
    ResultResponseEntity deleteFile(String token, String fileId);
}
