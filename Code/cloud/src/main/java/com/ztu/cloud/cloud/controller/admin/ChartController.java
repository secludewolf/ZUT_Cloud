package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.ChartService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jager
 * @description 控制台图表接口
 * @date 2021/04/14-10:22
 **/
@RestController
@Validated
public class ChartController {
    ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    /**
     * 获取PV图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @SysLog(descrption = "管理员获取PV图表数据", type = "控制台图表", modul = "控制台模块")
    @GetMapping("/admin/chart/pv")
    public ResultResponseEntity getPvData(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.chartService.getPvData(token);
    }

    /**
     * 获取获取UV图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @SysLog(descrption = "管理员获取UV图表数据", type = "控制台图表", modul = "控制台模块")
    @GetMapping("/admin/chart/uv")
    public ResultResponseEntity getUvData(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.chartService.getUvData(token);
    }

    /**
     * 获取获取DF图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @SysLog(descrption = "管理员获取DF图表数据", type = "控制台图表", modul = "控制台模块")
    @GetMapping("/admin/chart/df")
    public ResultResponseEntity getDfData(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.chartService.getDfData(token);
    }

    /**
     * 获取DS图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @SysLog(descrption = "管理员获取DS图表数据", type = "控制台图表", modul = "控制台模块")
    @GetMapping("/admin/chart/ds")
    public ResultResponseEntity getDsData(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.chartService.getDsData(token);
    }

    /**
     * 获取Mouth图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @SysLog(descrption = "管理员获取Mouth图表数据", type = "控制台图表", modul = "控制台模块")
    @GetMapping("/admin/chart/mouth")
    public ResultResponseEntity getMouthData(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.chartService.getMouthData(token);
    }

    /**
     * 获取File图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @SysLog(descrption = "管理员获取File图表数据", type = "控制台图表", modul = "控制台模块")
    @GetMapping("/admin/chart/file")
    public ResultResponseEntity getFileData(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.chartService.getFileData(token);
    }

    /**
     * 获取Download图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @SysLog(descrption = "管理员获取Download图表数据", type = "控制台图表", modul = "控制台模块")
    @GetMapping("/admin/chart/download")
    public ResultResponseEntity getDownloadData(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.chartService.getDownloadData(token);
    }


}
