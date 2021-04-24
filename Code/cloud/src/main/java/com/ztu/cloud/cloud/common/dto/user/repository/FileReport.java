package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 举报文件
 * @date 2020/06/26-10:07
 **/
@Data
public class FileReport {
    @NotBlank(message = "文件ID不能为空")
    private String fileId;
    @NotBlank(message = "举报类型不能为空")
    private String type;
    private String content;
}
