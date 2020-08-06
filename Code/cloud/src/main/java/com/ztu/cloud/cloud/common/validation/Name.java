package com.ztu.cloud.cloud.common.validation;

import com.ztu.cloud.cloud.common.validation.validator.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Jager
 * @description 名称检验
 * @date 2020/8/6-10:33
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = NameValidator.class)
public @interface Name {
    String type();

	String message() default "名称包含非法字符";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
