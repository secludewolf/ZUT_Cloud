package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 恢复文件
 * @date 2020/06/26-12:49
 **/
@Data
public class RestoreFile {
	@NotBlank(message = "仓库ID不能为空")
	private String repositoryId;
	@NotNull(message = "文件类型不能为空")
	private boolean isFile;
	@NotBlank(message = "回收ID不能为空")
	private String recycleId;

	public RestoreFile(String repositoryId, boolean isFile, String recycleId) {
		this.repositoryId = repositoryId;
		this.isFile = isFile;
		this.recycleId = recycleId;
	}
}
