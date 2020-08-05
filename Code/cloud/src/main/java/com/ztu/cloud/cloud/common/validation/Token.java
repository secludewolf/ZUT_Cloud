package com.ztu.cloud.cloud.common.validation;

import com.ztu.cloud.cloud.common.validation.validator.TokenValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Jager
 * @description 验证Token有效性
 * @date 2020/8/5-15:30
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = TokenValidator.class)
public @interface Token {
    String role();

    String message() default "Token错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
