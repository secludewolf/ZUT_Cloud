package com.ztu.cloud.cloud.common.dto.user.download;

import lombok.Data;

import java.io.InputStream;

/**
 * @author Jager
 * @description 下载数据
 * @date 2020/06/27-10:14
 **/
@Data
public class Download {
    private String fileName;
    private InputStream inputStream;

    public Download(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }
}
