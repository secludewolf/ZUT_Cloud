package com.ztu.cloud.cloud.common.log;

import com.ztu.cloud.cloud.common.bean.mysql.SysLog;
import com.ztu.cloud.cloud.service.admin.SysLogService;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @author Jager
 * @description 注解形式的监听 异步监听日志事件
 * @date 2021/01/31-11:25
 **/

@Component
public class SysLogListener {
    private final SysLogService sysLogService;

    public SysLogListener(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        SysLog sysLog = (SysLog) event.getSource();
        // 保存日志
        sysLogService.save(sysLog);
    }
}
