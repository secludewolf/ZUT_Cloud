package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

/**
 * @author Jager
 * @description 修改用户密码请求数据
 * @date 2020/06/23-20:03
 **/
@Data
public class ChangePassword {
    private int id;
    private String oldPassword;
    private String newPassword;

    public ChangePassword() {
    }

    public ChangePassword(int id, String oldPassword, String newPassword) {
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
