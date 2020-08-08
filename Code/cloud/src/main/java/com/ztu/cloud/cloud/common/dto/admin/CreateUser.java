package com.ztu.cloud.cloud.common.dto.admin;

import com.ztu.cloud.cloud.common.validation.Name;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Jager
 * @description 创建用户
 * @date 2020/06/24-14:54
 **/
@Data
public class CreateUser {
    @NotBlank(message = "账号不能为空")
    @Size(min = 4, max = 16, message = "账号长度必须在4~16位之间")
    private String account;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在6~16位之间")
    private String password;
    @Email(message = "邮箱格式错误")
    private String email;
    @Size(min = 11,max = 11,message = " 手机号长度必须为11位")
    private String phone;
    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 16, message = "昵称长度必须在2~16位之间")
    @Name(type = "user")
    private String name;
}
