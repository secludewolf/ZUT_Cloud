package com.ztu.cloud.cloud.common.exception;

import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.util.ResultUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jager
 * @description 控制器错误处理
 * @date 2019/12/26-22:34
 **/
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {
            BindException.class,
            ValidationException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            RequestParameterException.class
    })
    public ResultResponseEntity requestParameterException(Exception e) {
        List<String> errors = new LinkedList<>();
        /// BindException
        if (e instanceof BindException) {
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            for (FieldError error : ((BindException) e).getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
            /// MethodArgumentNotValidException
        } else if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            /// ValidationException 的子类异常ConstraintViolationException
        } else if (e instanceof ConstraintViolationException) {
            /*
             * ConstraintViolationException的e.getMessage()形如
             *     {方法名}.{参数名}: {message}
             *  这里只需要取后面的message即可
             */
            String msg = e.getMessage();
            if (msg != null) {
                int lastIndex = msg.lastIndexOf(':');
                if (lastIndex >= 0) {
                    errors.add(msg.substring(lastIndex + 1).trim());
                }
            }
            /// ValidationException 的其它子类异常
        } else {
            errors.add("处理参数时异常");
        }
        for (String error : errors) {
            if (error.toLowerCase().contains("token")) {
                return ResultConstant.TOKEN_INVALID;
            }
        }
        return ResultUtil.createResult(0, "参数错误", Collections.singletonMap("errors", errors));
    }

    @ExceptionHandler(JwtException.class)
    public ResultResponseEntity tokenIllegal(Exception e) {
        e.printStackTrace();
        return ResultConstant.TOKEN_INVALID;
    }

    @ExceptionHandler(Exception.class)
    public ResultResponseEntity unknownException(Exception e) {
        e.printStackTrace();
        return ResultConstant.SERVER_ERROR;
    }
}