package com.ztu.cloud.cloud.service.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.SysLog;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.SysLogDao;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.common.PageVo;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jager
 * @description 系统日志服务实现
 * @date 2021/01/31-11:29
 **/

@Component
public class SysLogServiceImpl implements SysLogService {
    SysLogDao sysLogDao;
    AdminMapper adminDao;

    public SysLogServiceImpl(SysLogDao sysLogDao, AdminMapper adminDao) {
        this.sysLogDao = sysLogDao;
        this.adminDao = adminDao;
    }

    /**
     * 保存日志
     *
     * @param sysLog 日志实体
     */
    @Override
    public void save(SysLog sysLog) {
        this.sysLogDao.insertSysLog(sysLog);
    }

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
    @Override
    public ResultResponseEntity getSyslogListManage(String token, Integer pageNumber, Integer pageSize, String sortKey, String sortType, Integer status, Long startTime, Long finishTime, String memberType, String actionUrl, String requestIp) {
        int id = TokenUtil.getId(token);
        Admin admin = this.adminDao.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        // 权限验证
        if (admin.getLevel() <= 0) {
            return ResultConstant.NO_ACCESS;
        }
        String orderBy = sortKey + " " + sortType;
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<SysLog> result;
        com.ztu.cloud.cloud.common.dto.condition.SysLog conditionBean = new com.ztu.cloud.cloud.common.dto.condition.SysLog(memberType, requestIp, actionUrl, status, startTime, finishTime);
        try {
            result = this.sysLogDao.getSysLogs(conditionBean);
        } catch (Exception e) {
            e.printStackTrace();
            result = new LinkedList<>();
        }
        PageVo<SysLog> pageVo = new PageVo<>(new PageInfo<>(result), sortKey, sortType);
        return ResultUtil.createResult("查询成功", pageVo);
    }
}
