package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

/**
 * @author Jager
 * @description 恢复文件
 * @date 2020/06/26-12:49
 **/
@Data
public class RestoreFile {
    private String repositoryId;
    private boolean isFile;
    private String recycleId;


    public RestoreFile() {
    }

    public RestoreFile(String repositoryId, boolean isFile, String recycleId) {
        this.repositoryId = repositoryId;
        this.isFile = isFile;
        this.recycleId = recycleId;
    }
}