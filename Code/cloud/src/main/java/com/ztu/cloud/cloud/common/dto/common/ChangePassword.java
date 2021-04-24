package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Jager
 * @description 修改用户密码请求数据
 * @date 2020/06/23-20:03
 **/
@Data
public class ChangePassword {
    @NotNull(message = "用户ID不能为空")
    private int id;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在6~16位之间")
    private String oldPassword;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在6~16位之间")
    private String newPassword;
}
