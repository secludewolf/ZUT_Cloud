package com.ztu.cloud.cloud.common.dto.user.preview;

import lombok.Data;

import java.io.File;

/**
 * @author Jager
 * @description 预览视频数据
 * @date 2021/04/19-16:01
 **/
@Data
public class PreviewVideo {
    private int code;
    private String message;
    private File file;
}