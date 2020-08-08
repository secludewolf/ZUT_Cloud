package com.ztu.cloud.cloud.common.dto.user.repository;

import com.ztu.cloud.cloud.common.validation.Name;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 创建文件夹
 * @date 2020/06/26-08:28
 **/
@Data
public class CreateFolder {
	@NotBlank(message = "仓库ID不能为空")
	private String repositoryId;
	@NotBlank(message = "名称不能为空")
	@Name(type = "file")
	private String name;
	@NotBlank(message = "路径不能为空")
	private String path;
}
