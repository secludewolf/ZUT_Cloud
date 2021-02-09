package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mysql.SysLog;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 系统日志服务
 * @date 2021/01/31-11:27
 **/
public interface SysLogService {
    /**
     * 保存日志
     *
     * @param sysLog 日志实体
     */
    public void save(SysLog sysLog);

    /**
     * 获取系统日志列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @param pageSize   分页大小
     * @param sortKey    排序Key
     * @param sortType   排序类型
     * @param status     状态
     * @param startTime  起始时间
     * @param finishTime 终止时间
     * @param memberType 会员类型
     * @param actionUrl  URL
     * @param requestIp  IP
     * @return 管理员列表
     */
    ResultResponseEntity getSyslogListManage(String token, Integer pageNumber, Integer pageSize, String sortKey, String sortType, Integer status, Long startTime, Long finishTime, String memberType, String actionUrl, String requestIp);
}
