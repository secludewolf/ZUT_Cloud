package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

/**
 * @author Jager
 * @description 创建文件
 * @date 2020/06/26-08:27
 **/
@Data
public class CreateFile {
	private String repositoryId;
	private String fileId;
	private String name;
	private String path;

	public CreateFile(String repositoryId, String fileId, String name, String path) {
		this.repositoryId = repositoryId;
		this.fileId = fileId;
		this.name = name;
		this.path = path;
	}
}
