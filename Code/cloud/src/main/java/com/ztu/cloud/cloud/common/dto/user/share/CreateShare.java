package com.ztu.cloud.cloud.common.dto.user.share;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 创建分享
 * @date 2020/06/28-10:13
 **/
@Data
public class CreateShare {
	@NotBlank(message = "仓库ID不能为空")
	private String repositoryId;
	@NotBlank(message = "名称不能为空")
	private String name;
	@NotBlank(message = "路径不能为空")
	private String path;
	private String password;
	@NotNull(message = "有效期不能为空")
	private Long validTime;

	public CreateShare(String repositoryId, String name, String path, Long validTime) {
		this.repositoryId = repositoryId;
		this.name = name;
		this.path = path;
		this.validTime = validTime;
	}
}
