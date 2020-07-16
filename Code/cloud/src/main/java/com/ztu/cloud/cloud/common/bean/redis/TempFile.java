package com.ztu.cloud.cloud.common.bean.redis;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jager
 * @description 临时文件
 * @date 2020/06/27-11:16
 **/
@Data
public class TempFile {
    private String fileId;
    private String name;
    private String type;
    private Integer length;
    private Set<Integer> saves;

    public TempFile(String fileId, String name, String type) {
        this.fileId = fileId;
        this.name = name;
        this.type = type;
        this.length = 0;
        this.saves = new HashSet<>();
    }

    public TempFile(String fileId, String name, String type, Integer length) {
        this.fileId = fileId;
        this.name = name;
        this.type = type;
        this.length = length;
        this.saves = new HashSet<>();
    }
}
