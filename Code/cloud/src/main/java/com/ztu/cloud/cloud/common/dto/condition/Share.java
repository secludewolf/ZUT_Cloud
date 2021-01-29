package com.ztu.cloud.cloud.common.dto.condition;

import lombok.Data;

/**
 * @author Jager
 * @description Share实体查询类
 * @date 2021/01/29-18:56
 **/
@Data
public class Share {
    private String id;
    private Integer userId;
    private String repoId;
    private String name;
    private String password;
    private Integer status;
    private Long createTime;
    private Long validTime;
    private Long startTime;
    private Long endTime;

    public Share() {
    }

    public Share(Integer userId, String name, Integer status, Long startTime, Long endTime) {
        this.userId = userId;
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Share(String id, Integer userId, String repoId, String name, String password, Integer status, Long createTime, Long validTime, Long startTime, Long endTime) {
        this.id = id;
        this.userId = userId;
        this.repoId = repoId;
        this.name = name;
        this.password = password;
        this.status = status;
        this.createTime = createTime;
        this.validTime = validTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
