package com.ztu.cloud.cloud.common.dto.condition;

import lombok.Data;

/**
 * @author Jager
 * @description 文件实体查找类
 * @date 2021/01/29-13:48
 **/
@Data
public class File {
    private String id;
    private String name;
    private String path;
    private Long size;
    private Integer status;
    private String type;
    private Long quoteNumber;
    private Long createTime;
    private Long changeTime;
    private Long startSize;
    private Long endSize;
    private Long startTime;
    private Long endTime;

    public File() {
    }

    public File(String id, String name, String path, Long size, Integer status, String type, Long quoteNumber, Long createTime, Long changeTime, Long startTime, Long endTime) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.size = size;
        this.status = status;
        this.type = type;
        this.quoteNumber = quoteNumber;
        this.createTime = createTime;
        this.changeTime = changeTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public File(String name, Integer status, Long startTime, Long endTime) {
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
