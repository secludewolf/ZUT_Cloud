package com.ztu.cloud.cloud.common.log;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.util.ServletUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Instant;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Jager
 * @description 系统日志切面
 * @date 2021/01/31-10:27
 **/
@Aspect
@Component
public class SysLogAspect {

    private final com.ztu.cloud.cloud.common.bean.mysql.SysLog sysLog = new com.ztu.cloud.cloud.common.bean.mysql.SysLog();

    /**
     * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     **/
    private final ApplicationContext applicationContext;

    public SysLogAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /***
     * 定义controller切入点拦截规则，拦截SysLog注解的方法
     */
    @Pointcut("@annotation(com.ztu.cloud.cloud.common.log.SysLog)")
    public void sysLogAspect() {
    }

    /***
     * 拦截控制层的操作日志
     * @param joinPoint 切入点
     */
    @Before(value = "sysLogAspect()")
    public void recordLog(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        SysLog annotation = method.getAnnotation(SysLog.class);
        long beginTime = Instant.now().toEpochMilli();
        Integer memberId = null;
        String memberType = null;
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            try {
                memberId = TokenUtil.getId((String) args[0]);
                memberType = TokenUtil.getRole((String) args[0]);
            } catch (Exception ignored) {
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        sysLog.setMemberId(memberId);
        sysLog.setMemberType(memberType);
        sysLog.setRequestIp(ServletUtil.getIpAddress(request));
        sysLog.setActionUrl(String.valueOf(request.getRequestURL()));
        sysLog.setActionMethod(request.getMethod());
        sysLog.setOptionType(annotation.type());
        sysLog.setOptionDesc(annotation.descrption());
        sysLog.setOptionModel(annotation.modul());
        sysLog.setClassPath(joinPoint.getTarget().getClass().getName());
        sysLog.setRequestMethod(joinPoint.getSignature().getName());
        sysLog.setParameter(Arrays.toString(args));
        long endTime = Instant.now().toEpochMilli();
        sysLog.setStartTime(beginTime);
        sysLog.setFinishTime(endTime);
        sysLog.setConsumingTime(endTime - beginTime);
    }

    /**
     * 返回通知
     *
     * @param ret 返回结果
     */
    @AfterReturning(returning = "ret", pointcut = "sysLogAspect()")
    public void doAfterReturning(Object ret) {
        ResultResponseEntity result = (ResultResponseEntity) ret;
        if (result.getStatusCode().value() == 200) {
            sysLog.setStatus(1);
        } else {
            sysLog.setStatus(-1);
            sysLog.setMessage(result.toString());
        }
        applicationContext.publishEvent(new SysLogEvent(sysLog));
    }

    /**
     * 异常通知
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        sysLog.setStatus(-1);
        sysLog.setMessage(e.getMessage());
        applicationContext.publishEvent(new SysLogEvent(sysLog));
    }

}
