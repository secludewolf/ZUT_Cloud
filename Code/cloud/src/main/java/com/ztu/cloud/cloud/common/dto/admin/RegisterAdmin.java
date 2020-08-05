package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 创建管理员账户数据
 * @date 2020/06/24-11:14
 **/
@Data
public class RegisterAdmin {
	@NotBlank(message = "昵称不能为空")
	private String name;
	@NotBlank(message = "账号不能为空")
	private String account;
	@NotBlank(message = "邮箱不能为空")
	@Email(message = "邮箱格式错误")
	private String email;
	@NotBlank(message = "手机不能为空")
	private String phone;
	@NotBlank(message = "密码不能为空")
	private String password;
	@NotBlank(message = "权限验证码不能为空")
	private String key;
}
