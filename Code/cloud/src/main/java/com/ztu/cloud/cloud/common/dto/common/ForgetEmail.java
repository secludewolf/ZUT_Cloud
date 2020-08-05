package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 忘记密码邮箱信息
 * @date 2020/06/23-18:45
 **/
@Data
public class ForgetEmail {
	@NotBlank(message = "邮箱不能为空")
	@Email(message = "邮箱格式错误")
	private String email;
}
