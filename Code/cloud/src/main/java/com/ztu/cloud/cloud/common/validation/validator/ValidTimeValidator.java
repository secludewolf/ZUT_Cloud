package com.ztu.cloud.cloud.common.validation.validator;

import com.ztu.cloud.cloud.common.validation.ValidTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Jager
 * @description 有效期验证器
 * @date 2020/8/5-15:38
 **/
public class ValidTimeValidator implements ConstraintValidator<ValidTime, Object> {

    private long min;

    @Override
    public void initialize(ValidTime validTime) {
        this.min = validTime.min();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (value instanceof Long) {
            return ((Long)value) > System.currentTimeMillis() + min;
        } else {
            return false;
        }
    }
}
