package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

/**
 * @author Jager
 * @description 账号登陆信息
 * @date 2020/06/23-18:01
 **/
@Data
public class LoginAccount {
	private String account;
	private String password;
	private boolean rememberMe;

	public LoginAccount(String account, String password, boolean rememberMe) {
		this.account = account;
		this.password = password;
		this.rememberMe = rememberMe;
	}
}
