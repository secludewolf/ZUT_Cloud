package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

/**
 * @author Jager
 * @description 文件举报表
 * @date 2021/04/17-15:28
 **/
@Data
public class ShareReport {
    int id;
    int userId;
    String shareId;
    String type;
    String content;

    public ShareReport() {
    }

    public ShareReport(int id, int userId, String shareId, String type, String content) {
        this.id = id;
        this.userId = userId;
        this.shareId = shareId;
        this.type = type;
        this.content = content;
    }

    public ShareReport(com.ztu.cloud.cloud.common.dto.user.share.ShareReport shareReport, int userId) {
        this.userId = userId;
        this.shareId = shareReport.getShareId();
        this.type = shareReport.getType();
        this.content = shareReport.getContent();
    }


}
