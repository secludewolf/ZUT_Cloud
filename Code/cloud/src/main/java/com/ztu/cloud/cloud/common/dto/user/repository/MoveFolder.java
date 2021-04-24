package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 移动文件
 * @date 2020/06/26-10:07
 **/
@Data
public class MoveFolder {
    @NotBlank(message = "仓库ID不能为空")
    private String repositoryId;
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "原始路径不能为空")
    private String oldPath;
    @NotBlank(message = "新路径不能为空")
    private String newPath;
}
