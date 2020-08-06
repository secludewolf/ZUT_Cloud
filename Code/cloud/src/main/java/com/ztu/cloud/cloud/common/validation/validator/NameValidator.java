package com.ztu.cloud.cloud.common.validation.validator;

import com.ztu.cloud.cloud.common.validation.Name;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Jager
 * @description Name验证器
 * @date 2020/8/5-15:38
 **/
public class NameValidator implements ConstraintValidator<Name, Object> {

    private String type;

    @Override
    public void initialize(Name name) {
        this.type = name.type();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value != null) {
            if (value instanceof String) {
                String adminTypeName = "admin";
                String userTypeName = "user";
                String fileTypeName = "file";
                String name = (String)value;
                if (userTypeName.equals(type) || adminTypeName.equals(type)) {
                    return name.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0;
                } else if (fileTypeName.equals(type)) {
                    // TODO 文件名称验证
                    return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
