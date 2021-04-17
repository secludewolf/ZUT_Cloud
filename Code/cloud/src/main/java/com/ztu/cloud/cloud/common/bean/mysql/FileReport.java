package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

/**
 * @author Jager
 * @description 文件举报表
 * @date 2021/04/17-15:28
 **/
@Data
public class FileReport {
    int id;
    int userId;
    String fileId;
    String type;
    String content;

    public FileReport() {
    }

    public FileReport(int id, int userId, String fileId, String type, String content) {
        this.id = id;
        this.userId = userId;
        this.fileId = fileId;
        this.type = type;
        this.content = content;
    }

    public FileReport(com.ztu.cloud.cloud.common.dto.user.repository.FileReport fileReport, int userId) {
        this.userId = userId;
        this.fileId = fileReport.getFileId();
        this.type = fileReport.getType();
        this.content = fileReport.getContent();
    }
}
