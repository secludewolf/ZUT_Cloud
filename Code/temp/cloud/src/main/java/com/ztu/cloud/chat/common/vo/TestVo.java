package com.ztu.cloud.chat.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jager
 * @description 测试Vo
 * @date 2020/8/12-16:23
 **/
@Data
@ApiModel(description = "测试消息内容")
public class TestVo {
	@ApiModelProperty(value = "用户id")
	private Integer userId;
	@ApiModelProperty(value = "用户名")
	private String username;
	@ApiModelProperty(value = "用户头像")
	private String avatar;
	@ApiModelProperty(value = "消息")
	private String message;
	@ApiModelProperty(value = "在线人数")
	private Integer count;
}
