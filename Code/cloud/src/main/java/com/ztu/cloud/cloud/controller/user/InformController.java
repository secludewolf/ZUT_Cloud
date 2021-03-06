package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.InformService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 通知接口
 * @date 2020/06/28-16:48
 **/
@RestController
@Validated
public class InformController {
    InformService informService;

    public InformController(InformService informService) {
        this.informService = informService;
    }

    /**
     * 获取通知列表
     *
     * @param token 用户Token
     * @return 通知列表
     */
    @SysLog(descrption = "用户获取通知列表", type = "通知管理", modul = "用户模块")
    @GetMapping("/inform")
    public ResultResponseEntity getInformList(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token) {
        return this.informService.getInformList(token);
    }

    /**
     * 获取通知信息
     *
     * @param token    用户Token
     * @param informId 通知ID
     * @return 通知信息
     */
    @SysLog(descrption = "用户获取通知信息", type = "通知管理", modul = "用户模块")
    @GetMapping("/inform/{informId}")
    public ResultResponseEntity getInform(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
                                          @PathVariable @NotBlank(message = "通知ID不能为空") String informId) {
        return this.informService.getInform(token, informId);
    }

    /**
     * 修改通知状态
     *
     * @param token    用户Token
     * @param informId 通知ID
     * @param status   通知状态
     * @return 是否成功
     */
    @SysLog(descrption = "用户修改通知状态", type = "通知管理", modul = "用户模块")
    @PatchMapping("/inform/{informId}/{status}")
    public ResultResponseEntity changeInformStatus(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
                                                   @PathVariable @NotBlank(message = "通知ID不能为空") String informId, @PathVariable @NotNull(message = "状态不能为空") Integer status) {
        return this.informService.changeInformStatus(token, informId, status);
    }
}
