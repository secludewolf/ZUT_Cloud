package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

/**
 * @author Jager
 * @description 创建用户
 * @date 2020/06/24-14:54
 **/
@Data
public class CreateUser {
	private String account;
	private String password;
	private String email;
	private String phone;
	private String name;

	public CreateUser() {
	}

	public CreateUser(String account, String password, String name) {
		this.account = account;
		this.password = password;
		this.name = name;
	}
}
