package com.ztu.cloud.cloud.common.dto.user.user;

import lombok.Data;

/**
 * @author Jager
 * @description 邮箱注册数据
 * @date 2020/06/23-19:02
 **/
@Data
public class RegisterAccount {
    private String name;
    private String account;
    private String password;

    public RegisterAccount() {
    }

    public RegisterAccount(String name, String account, String password) {
        this.name = name;
        this.account = account;
        this.password = password;
    }
}
