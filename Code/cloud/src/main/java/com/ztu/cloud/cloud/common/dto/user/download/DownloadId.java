package com.ztu.cloud.cloud.common.dto.user.download;

import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 获取下载ID
 * @date 2020/06/27-10:14
 **/
@Data
public class DownloadId {
    private String repositoryId;
    private String shareId;
    @NotNull(message = "用户文件关系ID不能为空")
    private Long userFileId;
    @NotBlank(message = "文件名称不能为空")
    private String fileName;
    private Folder folder;
    //TODO 暂时未检测
}