package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

/**
 * @author Jager
 * @description 删除文件至回收站
 * @date 2020/06/26-12:47
 **/
@Data
public class DeleteFileToRecyclebin {
	private String repositoryId;
	private boolean isFile;
	private String name;
	private String path;

	public DeleteFileToRecyclebin(String repositoryId, boolean isFile, String name, String path) {
		this.repositoryId = repositoryId;
		this.isFile = isFile;
		this.name = name;
		this.path = path;
	}
}
