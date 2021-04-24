package com.ztu.cloud.cloud.common.dto.user.share;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 举报分享
 * @date 2020/06/26-10:07
 **/
@Data
public class ShareReport {
    @NotBlank(message = "分享ID不能为空")
    private String shareId;
    @NotBlank(message = "举报类型不能为空")
    private String type;
    private String content;
}
