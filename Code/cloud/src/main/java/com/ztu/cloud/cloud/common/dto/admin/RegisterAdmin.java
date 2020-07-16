package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

/**
 * @author Jager
 * @description 创建管理员账户数据
 * @date 2020/06/24-11:14
 **/
@Data
public class RegisterAdmin {
    private String name;
    private String account;
    private String email;
    private String phone;
    private String password;
    private String key;

    public RegisterAdmin() {
    }

    public RegisterAdmin(String name, String account, String email, String phone, String password, String key) {
        this.name = name;
        this.account = account;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.key = key;
    }
}
