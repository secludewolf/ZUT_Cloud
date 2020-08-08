package com.ztu.cloud.cloud.common.dto.user.repository;

import com.ztu.cloud.cloud.common.validation.Name;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 创建文件
 * @date 2020/06/26-08:27
 **/
@Data
public class CreateFile {
    @NotBlank(message = "仓库ID不能为空")
    private String repositoryId;
    @NotBlank(message = "文件ID不能为空")
    private String fileId;
    @NotBlank(message = "文件名不能为空")
    @Name(type = "file")
    private String name;
    @NotBlank(message = "路径不能为空")
    private String path;
}
