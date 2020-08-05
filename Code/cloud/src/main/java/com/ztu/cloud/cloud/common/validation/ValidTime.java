package com.ztu.cloud.cloud.common.validation;

import com.ztu.cloud.cloud.common.validation.validator.ValidTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Jager
 * @description 有效期检测
 * @date 2020/8/5-17:13
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = ValidTimeValidator.class)
public @interface ValidTime {
    // 默认最短12小时
    long min() default 43200000;

    String message() default "有效期时间过短";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
