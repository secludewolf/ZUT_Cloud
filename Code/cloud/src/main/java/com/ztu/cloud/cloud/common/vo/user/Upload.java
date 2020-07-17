package com.ztu.cloud.cloud.common.vo.user;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jager
 * @description 上传信息
 * @date 2020/06/27-11:52
 **/
@Data
public class Upload {
	private Set<Integer> index;

	public Upload() {
		this.index = new HashSet<>();
	}

	public Upload(Set<Integer> index) {
		this.index = index;
	}
}
