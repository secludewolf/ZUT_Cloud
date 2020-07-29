package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

/**
 * @author Jager
 * @description 移动文件
 * @date 2020/06/26-10:07
 **/
@Data
public class MoveFile {
	private String repositoryId;
	private String name;
	private String oldPath;
	private String newPath;

	public MoveFile(String repositoryId, String name, String oldPath, String newPath) {
		this.repositoryId = repositoryId;
		this.name = name;
		this.oldPath = oldPath;
		this.newPath = newPath;
	}
}
