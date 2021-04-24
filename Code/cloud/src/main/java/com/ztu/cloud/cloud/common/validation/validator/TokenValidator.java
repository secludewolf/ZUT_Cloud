package com.ztu.cloud.cloud.common.validation.validator;

import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.util.TokenUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Jager
 * @description Token验证器
 * @date 2020/8/5-15:38
 **/
public class TokenValidator implements ConstraintValidator<Token, Object> {

    private String role;

    @Override
    public void initialize(Token token) {
        this.role = token.role();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            try {
                return this.role.equals(TokenUtil.getRole((String) value));
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
