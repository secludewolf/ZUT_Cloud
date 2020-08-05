package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description CreateInform
 * @date 2020/06/28-18:07
 **/
@Data
public class CreateInform {
    @NotBlank(message = "标题不能为空")
    private String header;
    @NotBlank(message = "内容不能为空")
    private String content;
    @NotNull(message = "有效期不能为空")
    private Long validTime;

    public CreateInform(String header, String content, Long validTime) {
        this.header = header;
        this.content = content;
        this.validTime = validTime;
    }
}
