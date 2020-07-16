package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

/**
 * @author Jager
 * @description 清空回收站
 * @date 2020/06/26-12:51
 **/
@Data
public class CleanRecyclebin {
    private String repositoryId;

    public CleanRecyclebin() {
    }

    public CleanRecyclebin(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}
