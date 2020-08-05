package com.ztu.cloud.cloud.common.dto.admin;

import com.ztu.cloud.cloud.common.validation.ValidTime;
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
    @ValidTime(min = 86400000)
    private Long validTime;
}
