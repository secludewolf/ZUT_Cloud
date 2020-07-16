package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

/**
 * @author Jager
 * @description 用户分享表
 * @date 2020/06/21-12:45
 **/
@Data
public class UserShare {
    private int id;
    private int userId;
    private String shareId;

    public UserShare() {
    }

    public UserShare(int userId, String shareId) {
        this.userId = userId;
        this.shareId = shareId;
    }
}
