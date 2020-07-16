package com.ztu.cloud.cloud.common.dto.user.share;

import lombok.Data;

/**
 * @author Jager
 * @description 保存分享
 * @date 2020/06/28-11:30
 **/
@Data
public class SaveShare {
    private String shareId;
    private String path;
    private String password;

    public SaveShare(String shareId, String path) {
        this.shareId = shareId;
        this.path = path;
    }
}
