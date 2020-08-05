package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 删除管理员数据
 * @date 2020/06/24-15:25
 **/
@Data
public class DeleteAdmin {
    @NotNull(message = "管理员ID不能为空")
    private Integer id;
}
