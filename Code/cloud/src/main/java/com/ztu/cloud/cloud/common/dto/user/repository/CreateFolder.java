package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

/**
 * @author Jager
 * @description 创建文件夹
 * @date 2020/06/26-08:28
 **/
@Data
public class CreateFolder {
	private String repositoryId;
	private String name;
	private String path;

	public CreateFolder() {
	}

	public CreateFolder(String repositoryId, String name, String path) {
		this.repositoryId = repositoryId;
		this.name = name;
		this.path = path;
	}
}
