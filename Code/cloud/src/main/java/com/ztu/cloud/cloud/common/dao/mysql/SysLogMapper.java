package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jager
 * @description 日志数据库接口
 * @date 2021/02/01-11:26
 **/
@Mapper
@Component
public interface SysLogMapper {
    /**
     * 保存系统日志
     *
     * @param sysLog 系统日志
     * @return 插入结果
     */
    int insertSysLog(SysLog sysLog);

    /**
     * 查询系统日志
     *
     * @param condition 条件
     * @return 系统日志列表
     */
    List<SysLog> getSysLogs(com.ztu.cloud.cloud.common.dto.condition.SysLog condition);
}
