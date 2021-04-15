package com.ztu.cloud.cloud.common.dto.condition;

import lombok.Data;

/**
 * @author Jager
 * @description 日志查询
 * @date 2021/02/01-11:40
 **/
@Data
public class SysLog {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer memberId;
    private String memberType;
    private String requestIp;
    private String actionUrl;
    private String actionMethod;
    private String optionType;
    private String optionDesc;
    private String optionModel;
    private String classPath;
    private String requestMethod;
    private String parameter;
    private String message;
    private Integer status;
    private Long startTime;
    private Long finishTime;
    private Long consumingTime;

    public SysLog() {
    }

    public SysLog(String memberType, String requestIp, String actionUrl, Integer status, Long startTime, Long finishTime) {
        this.memberType = memberType;
        this.requestIp = requestIp;
        this.actionUrl = actionUrl;
        this.status = status;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}
