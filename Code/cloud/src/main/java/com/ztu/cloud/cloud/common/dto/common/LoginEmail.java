package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Jager
 * @description 邮箱登录数据
 * @date 2020/06/23-17:30
 **/
@Data
public class LoginEmail {
	@NotBlank(message = "邮箱不能为空")
	@Email(message = "邮箱格式错误")
	private String email;
	@NotBlank(message = "密码不能为空")
	@Size(min = 6, max = 16, message = "密码长度必须在6~16位之间")
	private String password;
	private boolean rememberMe = false;

	public LoginEmail() {
	}

	public LoginEmail(String email, String password, boolean rememberMe) {
		this.email = email;
		this.password = password;
		this.rememberMe = rememberMe;
	}
}
