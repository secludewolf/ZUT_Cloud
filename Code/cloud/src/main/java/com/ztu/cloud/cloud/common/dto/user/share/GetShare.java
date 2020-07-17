package com.ztu.cloud.cloud.common.dto.user.share;

import lombok.Data;

/**
 * @author Jager
 * @description 获取分享信息
 * @date 2020/06/28-11:04
 **/
@Data
public class GetShare {
	private String shareId;
	private String password;

	public GetShare(String shareId) {
		this.shareId = shareId;
	}
}
