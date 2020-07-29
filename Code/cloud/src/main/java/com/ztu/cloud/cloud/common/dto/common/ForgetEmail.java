package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

/**
 * @author Jager
 * @description 忘记密码邮箱信息
 * @date 2020/06/23-18:45
 **/
@Data
public class ForgetEmail {
	private String email;

	public ForgetEmail(String email) {
		this.email = email;
	}
}
