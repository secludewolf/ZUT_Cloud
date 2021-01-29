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
     * @param pageNumber 页数
     * @param pageSize   分页大小
     * @param sortKey    排序主键
     * @param sortType   排序类型
     * @param status     状态
     * @param startTime  起始时间
     * @param endTime    终止时间
     * @param type       文件类型
     * @param size       文件大小
     * @param name       文件名称
     * @return 文件列表
     */
    ResultResponseEntity getFileList(String token, Integer pageNumber, Integer pageSize, String sortKey, String sortType, Integer status, Long startTime, Long endTime, String type, String size, String name);

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
