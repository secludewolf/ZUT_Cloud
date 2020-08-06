package com.ztu.cloud.cloud.common.dto.user.user;

import com.ztu.cloud.cloud.common.validation.Name;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Jager
 * @description 邮箱注册数据
 * @date 2020/06/23-19:02
 **/
@Data
public class RegisterAccount {
	@NotBlank(message = "昵称不能为空")
	@Size(min = 2, max = 16, message = "昵称长度必须在2~16位之间")
	@Name(type = "user")
	private String name;
	@NotBlank(message = "账号不能为空")
	@Size(min = 4, max = 16, message = "账号长度必须在4~16位之间")
	private String account;
	@NotBlank(message = "密码不能为空")
	@Size(min = 6, max = 16, message = "密码长度必须在6~16位之间")
	private String password;

	public RegisterAccount() {
	}

	public RegisterAccount(String name, String account, String password) {
		this.name = name;
		this.account = account;
		this.password = password;
	}
}
