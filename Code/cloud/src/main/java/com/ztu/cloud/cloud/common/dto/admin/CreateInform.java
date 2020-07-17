package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

/**
 * @author Jager
 * @description CreateInform
 * @date 2020/06/28-18:07
 **/
@Data
public class CreateInform {
	private String header;
	private String content;
	private Long validTime;

	public CreateInform(String header, String content, Long validTime) {
		this.header = header;
		this.content = content;
		this.validTime = validTime;
	}
}
