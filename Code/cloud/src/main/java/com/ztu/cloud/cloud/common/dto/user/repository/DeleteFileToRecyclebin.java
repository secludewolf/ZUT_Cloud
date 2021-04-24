package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 删除文件至回收站
 * @date 2020/06/26-12:47
 **/
@Data
public class DeleteFileToRecyclebin {
    @NotBlank(message = "仓库ID不能为空")
    private String repositoryId;
    @NotNull(message = "文件类型标记不能为空")
    private Boolean isFile;
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "路径不能为空")
    private String path;
}
