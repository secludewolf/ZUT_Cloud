package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.dto.admin.CreateInform;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.InformManageService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 通知管理
 * @date 2020/06/28-17:46
 **/
@RestController
public class InformController {
    InformManageService informManageService;

    public InformController(InformManageService informManageService) {
        this.informManageService = informManageService;
    }

    /**
     * 创建用户通知
     *
     * @param token
     *            管理员Token
     * @param parameter
     *            请求参数 header 标题 content 内容 validTime 有消息
     * @return 通知信息
     */
    @PutMapping("/admin/inform/user")
    public ResultResponseEntity createUserInform(
        @RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token,
        @RequestBody @Valid CreateInform parameter) {
        return this.informManageService.createUserInform(token, parameter);
    }

    /**
     * 创建管理员通知
     *
     * @param token
     *            管理员Token
     * @param parameter
     *            请求参数 header 标题 content 内容 validTime 有消息
     * @return 通知信息
     */
    @PutMapping("/admin/inform/admin")
    public ResultResponseEntity createAdminInform(
        @RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token,
        @RequestBody @Valid CreateInform parameter) {
        return this.informManageService.createAdminInform(token, parameter);
    }

    /**
     * 获取通知列表
     *
     * @param token
     *            管理员Token
     * @return 通知列表
     */
    @GetMapping("/admin/inform")
    public ResultResponseEntity
        getInformList(@RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token) {
        return this.informManageService.getInformList(token);
    }

    /**
     * 获取通知信息
     *
     * @param token
     *            管理员Token
     * @param informId
     *            通知ID
     * @return 通知信息
     */
    @GetMapping("/admin/inform/{informId}")
    public ResultResponseEntity getInform(
        @RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token,
        @PathVariable @NotBlank(message = "通知ID不能为空") String informId) {
        return this.informManageService.getInform(token, informId);
    }

    /**
     * 修改通知状态
     *
     * @param token
     *            管理员Token
     * @param informId
     *            通知ID
     * @param status
     *            通知状态
     * @return 是否成功
     */
    @PatchMapping("/admin/inform/{informId}/{status}")
    public ResultResponseEntity changeInformStatus(
        @RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token,
        @PathVariable @NotBlank(message = "通知ID不能为空") String informId,
        @PathVariable @NotNull(message = "状态不能为空") Integer status) {
        return this.informManageService.changeInformStatus(token, informId, status);
    }
}
