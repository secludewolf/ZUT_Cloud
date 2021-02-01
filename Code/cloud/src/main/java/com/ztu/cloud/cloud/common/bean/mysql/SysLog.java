package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Jager
 * @description 系统日志实体类
 * @date 2021/01/31-10:57
 **/
@Data
public class SysLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer memberId;

    /**
     * 用户类型
     */
    private String memberType;

    /**
     * 操作IP
     */
    private String requestIp;

    /**
     * 请求url
     */
    private String actionUrl;

    /**
     * 请求方法
     */
    private String actionMethod;

    /**
     * 操作类型
     */
    private String optionType;

    /**
     * 操作描述
     */
    private String optionDesc;

    /**
     * 操作模块
     */
    private String optionModel;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String parameter;

    /**
     * 错误消息
     */
    private String message;
    /**
     * 响应参数
     */
    private Integer status;
    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 完成时间
     */
    private Long finishTime;

    /**
     * 消耗时间
     */
    private Long consumingTime;
}
