package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Jager
 * @description 修改用户信息请求数据
 * @date 2020/06/23-20:02
 **/
@Data
public class ChangeAdminInfoManage {
    @NotNull(message = "管理员ID不能为空")
    private Integer id;
    @NotBlank(message = "账号不能为空")
    @Size(min = 4, max = 16, message = "账号长度必须在4~16位之间")
    private String account;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在6~16之间")
    private String password;
    @Email(message = "邮箱格式错误")
    private String email;
    @Size(min = 11, max = 11, message = "手机号程度必须为11位")
    private String phone;
    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 16, message = "昵称程度必须在2~16位之间")
    private String name;
    @NotNull(message = "状态不能为空")
    private Integer status;
    @NotNull(message = "等级不能为空")
    private Integer level;

    public ChangeAdminInfoManage(int id, String account, String password, String email, String phone, String name,
        int status, int level) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.status = status;
        this.level = level;
    }
}