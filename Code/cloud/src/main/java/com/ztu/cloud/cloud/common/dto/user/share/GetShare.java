package com.ztu.cloud.cloud.common.dto.user.share;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 获取分享信息
 * @date 2020/06/28-11:04
 **/
@Data
public class GetShare {
	@NotBlank(message = "分享ID不能为空")
	private String shareId;
	private String password;

	public GetShare(String shareId) {
		this.shareId = shareId;
	}
}
