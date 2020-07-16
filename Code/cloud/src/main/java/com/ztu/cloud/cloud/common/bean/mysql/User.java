package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

/**
 * @author Jager
 * @description 用户信息表
 * @date 2020/06/21-11:49
 **/
@Data
public class User {
    private int id;
    private String repoId;
    private String account;
    private String password;
    private String email;
    private String phone;
    private String name;
    private int status;
    private int level;
    private long createTime;
    private long changeTime;

    public User() {
    }

    public User(String repoId, String account, String password, String email, String phone, String name, int status, int level, long createTime, long changeTime) {
        this.repoId = repoId;
        this.account = account;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.status = status;
        this.level = level;
        this.createTime = createTime;
        this.changeTime = changeTime;
    }

    public User(String account, String password, String email, String phone, String name) {
        this.account = account;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.repoId = "";
        this.status = 1;
        this.level = 1;
        this.createTime = System.currentTimeMillis();
        this.changeTime = System.currentTimeMillis();
    }

    public User(String account, String password, String email, String name) {
        this.account = account;
        this.password = password;
        this.email = email;
        this.name = name;
        this.repoId = "";
        this.status = 1;
        this.level = 1;
        this.createTime = System.currentTimeMillis();
        this.changeTime = System.currentTimeMillis();
    }

    public User(String account, String password, String name) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.repoId = "";
        this.status = 1;
        this.level = 1;
        this.createTime = System.currentTimeMillis();
        this.changeTime = System.currentTimeMillis();
    }
}
