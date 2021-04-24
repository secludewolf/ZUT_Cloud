package com.ztu.cloud.cloud.common.vo.admin;

import lombok.Data;

/**
 * @author Jager
 * @description 管理员信息
 * @date 2020/06/24-10:59
 **/
@Data
public class Admin {
    private int id;
    private String repoId;
    private String account;
    private String email;
    private String phone;
    private String name;
    private String password;
    private int status;
    private int level;
    private long createTime;
    private long changeTime;

    public Admin() {
    }

    public Admin(com.ztu.cloud.cloud.common.bean.mysql.Admin admin) {
        this.id = admin.getId();
        this.account = admin.getAccount();
        this.email = admin.getEmail();
        this.phone = admin.getPhone();
        this.name = admin.getName();
        this.password = admin.getPassword();
        this.status = admin.getStatus();
        this.level = admin.getLevel();
        this.createTime = admin.getCreateTime();
        this.changeTime = admin.getChangeTime();
    }
}
