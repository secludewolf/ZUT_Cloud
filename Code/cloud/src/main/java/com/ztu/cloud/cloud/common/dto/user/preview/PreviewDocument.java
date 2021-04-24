package com.ztu.cloud.cloud.common.dto.user.preview;

import lombok.Data;
import org.jodconverter.document.DocumentFormat;

import java.io.InputStream;

/**
 * @author Jager
 * @description 预览文件数据
 * @date 2021/04/19-16:01
 **/
@Data
public class PreviewDocument {
    DocumentFormat type;
    private int code;
    private String message;
    private InputStream inputStream;
}
