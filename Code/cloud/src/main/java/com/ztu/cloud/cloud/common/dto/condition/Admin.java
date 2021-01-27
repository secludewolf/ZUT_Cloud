package com.ztu.cloud.cloud.common.dto.condition;

import lombok.Data;

/**
 * @author Jager
 * @description 管理员实体查询类
 * @date 2021/01/27-18:28
 **/
@Data
public class Admin {
    private Integer id;
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

    public Admin() {
    }

    public Admin(Integer id, String account, String password, String email, String phone, String name, Integer status, Integer level, Long createTime, Long changeTime, Long startTime, Long endTime) {
        this.id = id;
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

    public Admin(String account, String email, String phone, Integer status, Long startTime, Long endTime) {
        this.account = account;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
