package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

/**
 * @author Jager
 * @description 移动文件
 * @date 2020/06/26-10:07
 **/
@Data
public class RenameFile {
	private String repositoryId;
	private String oldName;
	private String newName;
	private String path;

	public RenameFile(String repositoryId, String oldName, String newName, String path) {
		this.repositoryId = repositoryId;
		this.oldName = oldName;
		this.newName = newName;
		this.path = path;
	}
}

