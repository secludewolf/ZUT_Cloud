package com.ztu.cloud.cloud.common.dto.user.repository;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 清空回收站
 * @date 2020/06/26-12:51
 **/
@Data
public class CleanRecyclebin {
	@NotBlank(message = "仓库ID不能为空")
	private String repositoryId;
}
