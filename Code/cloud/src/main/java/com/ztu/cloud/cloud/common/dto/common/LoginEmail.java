package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

/**
 * @author Jager
 * @description 邮箱登录数据
 * @date 2020/06/23-17:30
 **/
@Data
public class LoginEmail {
	private String email;
	private String password;
	private boolean rememberMe;

	public LoginEmail(String email, String password, boolean rememberMe) {
		this.email = email;
		this.password = password;
		this.rememberMe = rememberMe;
	}
}
