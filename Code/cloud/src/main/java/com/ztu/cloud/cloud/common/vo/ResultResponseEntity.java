package com.ztu.cloud.cloud.common.vo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * @author Jager
 * @description 返回体
 * @date 2019/12/27-7:51
 **/
public class ResultResponseEntity extends ResponseEntity<Object> {
	public ResultResponseEntity(Object body, HttpStatus status) {
		super(body, status);
	}

	public ResultResponseEntity(Object body, MultiValueMap<String, String> headers, HttpStatus status) {
		super(body, headers, status);
	}
}
