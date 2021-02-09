package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.SysLogService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * @author Jager
 * @description 系统日志
 * @date 2021/02/01-11:57
 **/
@RestController
@RequestMapping("/admin")
@Validated
public class SysLogController {
    SysLogService sysLogService;

    public SysLogController(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
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
    @SysLog(descrption = "管理员获取日志列表", type = "日志管理", modul = "管理员模块")
    @GetMapping("/log/list/{pageNumber}")
    public ResultResponseEntity getLogListManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable("pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20", required = false) @Min(1) Integer pageSize,
            @RequestParam(name = "sortKey", defaultValue = "id", required = false) String sortKey,
            @RequestParam(value = "sortType", defaultValue = "asc", required = false) String sortType,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) Long startTime,
            @RequestParam(value = "finishTime", required = false) Long finishTime,
            @RequestParam(value = "memberType", required = false) String memberType,
            @RequestParam(value = "actionUrl", required = false) String actionUrl,
            @RequestParam(value = "requestIp", required = false) String requestIp) {
        return this.sysLogService.getSyslogListManage(token, pageNumber, pageSize, sortKey, sortType, status, startTime, finishTime, memberType, actionUrl, requestIp);
    }
}