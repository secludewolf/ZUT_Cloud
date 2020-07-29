package com.ztu.cloud.cloud.common.exception;

import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Jager
 * @description 控制器错误处理
 * @date 2019/12/26-22:34
 **/
@RestControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(RequestParameterException.class)
	public ResultResponseEntity missingRequestHeaderException(Exception e) {
		e.printStackTrace();
		return ResultConstant.REQUEST_PARAMETER_ERROR;
	}

	@ExceptionHandler(JwtException.class)
	public ResultResponseEntity tokenIllegal(Exception e) {
		e.printStackTrace();
		return ResultConstant.TOKEN_INVALID;
	}
}
