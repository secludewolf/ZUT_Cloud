package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Jager
 * @description 账号登陆信息
 * @date 2020/06/23-18:01
 **/
@Data
public class LoginAccount {
    @NotBlank(message = " 账号不能为空")
    @Size(min = 4, max = 16, message = "账号长度必须在4~16位之间")
    private String account;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在6~16位之间")
    private String password;
    private boolean rememberMe = false;

    public LoginAccount(String account, String password, boolean rememberMe) {
        this.account = account;
        this.password = password;
        this.rememberMe = rememberMe;
    }
}
