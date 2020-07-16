package com.ztu.cloud.cloud.common.dto.user.share;

import lombok.Data;

/**
 * @author Jager
 * @description 创建分享
 * @date 2020/06/28-10:13
 **/
@Data
public class CreateShare {
    private String repositoryId;
    private String name;
    private String path;
    private String password;
    private long validTime;

    public CreateShare(String repositoryId, String name, String path, long validTime) {
        this.repositoryId = repositoryId;
        this.name = name;
        this.path = path;
        this.validTime = validTime;
    }
}
