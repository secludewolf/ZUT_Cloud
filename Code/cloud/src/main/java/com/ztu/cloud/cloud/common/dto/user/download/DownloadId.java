package com.ztu.cloud.cloud.common.dto.user.download;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;

import lombok.Data;

/**
 * @author Jager
 * @description 获取下载ID
 * @date 2020/06/27-10:14
 **/
@Data
public class DownloadId {
    @NotBlank(message = "仓库ID不能为空")
    private String repositoryId;
    @NotBlank(message = "分享ID不能为空")
    private String shareId;
    @NotNull(message = "用户文件关系ID不能为空")
    private Long userFileId;
    @NotBlank(message = "文件名称不能为空")
    private String fileName;
    private Folder folder;
    //TODO 暂时未检测
}