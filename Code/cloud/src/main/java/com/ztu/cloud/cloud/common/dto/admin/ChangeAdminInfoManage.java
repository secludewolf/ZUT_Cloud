package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

/**
 * @author Jager
 * @description 修改用户信息请求数据
 * @date 2020/06/23-20:02
 **/
@Data
public class ChangeAdminInfoManage {
    private int id;
    private String account;
    private String password;
    private String email;
    private String phone;
    private String name;
    private int status;
    private int level;

    public ChangeAdminInfoManage() {
    }

    public ChangeAdminInfoManage(int id, String account, String password, String email, String phone, String name, int status, int level) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.status = status;
        this.level = level;
    }
}
