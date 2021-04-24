package com.ztu.cloud.cloud.common.dto.user.repository;

import com.ztu.cloud.cloud.common.validation.Name;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 移动文件
 * @date 2020/06/26-10:07
 **/
@Data
public class RenameFolder {
    @NotBlank(message = "仓库ID不能为空")
    private String repositoryId;
    @NotBlank(message = "原始名称不能为空")
    @Name(type = "file")
    private String oldName;
    @NotBlank(message = "新名称不能为空")
    @Name(type = "file")
    private String newName;
    @NotBlank(message = "路径不能为空")
    private String path;
}

