package com.ztu.cloud.cloud.common.dto.condition;

import lombok.Data;

/**
 * @author Jager
 * @description 用户实体查询类
 * @date 2021/01/28-11:04
 **/
@Data
public class User {
    private Integer id;
    private String repoId;
    private String account;
    private String password;
    private String email;
    private String phone;
    private String name;
    private Integer status;
    private Integer level;
    private Long createTime;
    private Long changeTime;
    private Long startTime;
    private Long endTime;

    public User() {
    }

    public User(Integer id, String repoId, String account, String password, String email, String phone, String name, Integer status, Integer level, Long createTime, Long changeTime, Long startTime, Long endTime) {
        this.id = id;
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
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public User(String account, String email, String phone, Integer status, Long startTime, Long endTime) {
        this.account = account;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
