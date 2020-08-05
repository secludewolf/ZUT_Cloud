package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Jager
 * @description 修改用户信息请求数据
 * @date 2020/06/23-20:02
 **/
@Data
public class ChangeAdminInfo {
	@NotNull(message = "用户ID不能为空")
	private int id;
	@NotBlank(message = "昵称不能为空")
	@Size(min = 2, max = 16, message = "昵称长度必须在2~16位之间")
	private String name;
	@NotBlank(message = "账号不能为空")
	@Size(min = 4, max = 16, message = "账号长度必须在4~16位之间")
	private String account;
	@Email(message = "邮箱格式错误")
	private String email;
	@Size(min = 11, max = 11, message = "手机长度必须为11位")
	private String phone;
}
