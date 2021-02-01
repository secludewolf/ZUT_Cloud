package com.ztu.cloud.cloud.common.log;

import java.lang.annotation.*;

/**
 * @author Jager
 * @description 日主描述
 * @date 2021/01/31-10:25
 **/
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Target({ElementType.METHOD})  //注解放置的目标位置
@Documented
public @interface SysLog {
    //操作模块
    String modul() default "";

    //操作类型
    String type() default "";

    //操作说明
    String descrption() default "";
}
