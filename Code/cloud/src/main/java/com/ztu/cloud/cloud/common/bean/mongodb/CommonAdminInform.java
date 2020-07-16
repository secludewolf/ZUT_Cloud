package com.ztu.cloud.cloud.common.bean.mongodb;

import lombok.Data;
import org.bson.types.ObjectId;

/**
 * @author Jager
 * @description 公共管理员通知文档
 * @date 2020/06/22-10:18
 **/
@Data
public class CommonAdminInform {
    private ObjectId id;
    private String header;
    private String content;
    private long createTime;
    private long validTime;
    private int status;
    private int adminId;

    public CommonAdminInform() {
    }

    public CommonAdminInform(String header, String content, long createTime, long validTime, int status, int adminId) {
        this.header = header;
        this.content = content;
        this.createTime = createTime;
        this.validTime = validTime;
        this.status = status;
        this.adminId = adminId;
    }

    public CommonAdminInform(String header, String content, long validTime, int adminId) {
        this.header = header;
        this.content = content;
        this.createTime = System.currentTimeMillis();
        this.validTime = validTime;
        this.status = 1;
        this.adminId = adminId;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }
}
