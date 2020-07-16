package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

/**
 * @author Jager
 * @description 创建管理员
 * @date 2020/06/24-14:54
 **/
@Data
public class CreateAdmin {
    private String name;
    private String account;
    private String email;
    private String phone;
    private String password;
    private String key;

    public CreateAdmin() {
    }

    public CreateAdmin(String name, String account, String email, String phone, String password, String key) {
        this.name = name;
        this.account = account;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.key = key;
    }
}
