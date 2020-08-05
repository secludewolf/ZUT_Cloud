package com.ztu.cloud.cloud.common.dto.user.repository;

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
	private String oldName;
	@NotBlank(message = "新名称不能为空")
	private String newName;
	@NotBlank(message = "路径不能为空")
	private String path;


	public RenameFolder(String repositoryId, String oldName, String newName, String path) {
		this.repositoryId = repositoryId;
		this.oldName = oldName;
		this.newName = newName;
		this.path = path;
	}
}

