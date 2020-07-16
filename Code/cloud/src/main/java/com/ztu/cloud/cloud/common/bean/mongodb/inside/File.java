package com.ztu.cloud.cloud.common.bean.mongodb.inside;

import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description 文件信息
 * @date 2020/06/22-10:22
 **/
@Data
public class File implements Cloneable {
    private String id;
    private long userFileId;
    private List<String> shareIdList;
    private String name;
    private String path;
    private String type;
    private long size;
    private int status;
    private long createTime;
    private long changeTime;

    public File() {
    }

    public File(String id, long userFileId, String name, String path, String type, long size) {
        this.id = id;
        this.userFileId = userFileId;
        this.shareIdList = null;
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = size;
        this.status = 1;
        this.createTime = System.currentTimeMillis();
        this.changeTime = System.currentTimeMillis();
    }

    @Override
    public File clone() {
        try {
            File file = (File) super.clone();
            file.setShareIdList(null);
            return file;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
