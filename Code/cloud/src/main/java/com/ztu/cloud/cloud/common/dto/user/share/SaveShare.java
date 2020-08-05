package com.ztu.cloud.cloud.common.dto.user.share;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 保存分享
 * @date 2020/06/28-11:30
 **/
@Data
public class SaveShare {
	@NotBlank(message = "分享ID不能为欧空")
	private String shareId;
	@NotBlank(message = "路径不能为空")
	private String path;
	private String password;

	public SaveShare(String shareId, String path) {
		this.shareId = shareId;
		this.path = path;
	}
}
