package com.ztu.cloud.cloud.common.log;

import com.ztu.cloud.cloud.common.bean.mysql.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * @author Jager
 * @description 系统日志事件
 * @date 2021/01/31-11:24
 **/
public class SysLogEvent extends ApplicationEvent {
    public SysLogEvent(SysLog source) {
        super(source);
    }
}
