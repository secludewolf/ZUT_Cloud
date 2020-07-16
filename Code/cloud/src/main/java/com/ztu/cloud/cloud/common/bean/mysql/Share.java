package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

/**
 * @author Jager
 * @description 分享信息表
 * @date 2020/06/21-12:46
 **/
@Data
public class Share {
    private String id;
    private int userId;
    private String repoId;
    private String name;
    private String password;
    private int status;
    private long createTime;
    private long validTime;

    public Share() {
    }

    public Share(String id, int userId, String repoId, String name, String password, int status, long createTime, long validTime) {
        this.id = id;
        this.userId = userId;
        this.repoId = repoId;
        this.name = name;
        this.password = password;
        this.status = status;
        this.createTime = createTime;
        this.validTime = validTime;
    }

    public Share(String id, int userId, String repoId, String name, String password, long validTime) {
        this.id = id;
        this.userId = userId;
        this.repoId = repoId;
        this.name = name;
        this.password = password;
        this.status = 1;
        this.createTime = System.currentTimeMillis();
        this.validTime = validTime;
    }
}
