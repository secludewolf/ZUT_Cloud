package com.ztu.cloud.cloud.common.dto.user.user;

import lombok.Data;

/**
 * @author Jager
 * @description 账号注册数据
 * @date 2020/06/23-19:02
 **/
@Data
public class RegisterEmail {
    private String name;
    private String account;
    private String email;
    private String password;

    public RegisterEmail() {
    }

    public RegisterEmail(String name, String account, String email, String password) {
        this.name = name;
        this.account = account;
        this.email = email;
        this.password = password;
    }
}
