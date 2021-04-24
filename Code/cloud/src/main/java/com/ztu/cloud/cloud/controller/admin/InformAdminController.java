package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.dto.admin.CreateInform;
import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.InformManageService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 通知管理
 * @date 2020/06/28-17:46
 **/
@RestController
@Validated
public class InformAdminController {
    InformManageService informManageService;

    public InformAdminController(InformManageService informManageService) {
        this.informManageService = informManageService;
    }

    /**
     * 创建用户通知
     *
     * @param token     管理员Token
     * @param parameter 请求参数 header 标题 content 内容 validTime 有消息
     * @return 通知信息
     */
    @SysLog(descrption = "管理员创建用户通知", type = "通知管理", modul = "管理员模块")
    @PutMapping("/admin/inform/user")
    public ResultResponseEntity createUserInform(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid CreateInform parameter) {
        return this.informManageService.createUserInform(token, parameter);
    }

    /**
     * 创建管理员通知
     *
     * @param token     管理员Token
     * @param parameter 请求参数 header 标题 content 内容 validTime 有消息
     * @return 通知信息
     */
    @SysLog(descrption = "管理员创建管理员通知", type = "通知管理", modul = "管理员模块")
    @PutMapping("/admin/inform/admin")
    public ResultResponseEntity createAdminInform(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid CreateInform parameter) {
        return this.informManageService.createAdminInform(token, parameter);
    }

    /**
     * 获取通知列表
     *
     * @param token 管理员Token
     * @return 通知列表
     */
    @SysLog(descrption = "管理员获取通知列表", type = "通知管理", modul = "管理员模块")
    @GetMapping("/admin/inform")
    public ResultResponseEntity
    getInformList(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.informManageService.getInformList(token);
    }

    /**
     * 获取通知信息
     *
     * @param token    管理员Token
     * @param informId 通知ID
     * @return 通知信息
     */
    @SysLog(descrption = "管理员获取通知信息", type = "通知管理", modul = "管理员模块")
    @GetMapping("/admin/inform/{informId}")
    public ResultResponseEntity getInform(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
                                          @PathVariable @NotBlank(message = "通知ID不能为空") String informId) {
        return this.informManageService.getInform(token, informId);
    }

    /**
     * 修改通知状态
     *
     * @param token    管理员Token
     * @param informId 通知ID
     * @param status   通知状态
     * @return 是否成功
     */
    @SysLog(descrption = "管理员修改通知信息", type = "通知管理", modul = "管理员模块")
    @PatchMapping("/admin/inform/{informId}/{status}")
    public ResultResponseEntity changeInformStatus(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable @NotBlank(message = "通知ID不能为空") String informId,
            @PathVariable @NotNull(message = "状态不能为空") Integer status) {
        return this.informManageService.changeInformStatus(token, informId, status);
    }

    /**
     * 获取通知列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @param pageSize   分页大小
     * @param sortKey    排序主键
     * @param sortType   排序类型
     * @return 文件列表
     */
    @SysLog(descrption = "管理员获取通知列表", type = "通知管理", modul = "管理员模块")
    @GetMapping("/admin/inform/admin/{pageNumber}")
    public ResultResponseEntity getAdminInformList(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20", required = false) @Min(1) Integer pageSize,
            @RequestParam(name = "sortKey", defaultValue = "id", required = false) String sortKey,
            @RequestParam(value = "sortType", defaultValue = "asc", required = false) String sortType) {
        return this.informManageService.getAdminInformList(token, pageNumber, pageSize, sortKey, sortType);
    }

    /**
     * 获取通知列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @param pageSize   分页大小
     * @param sortKey    排序主键
     * @param sortType   排序类型
     * @return 文件列表
     */
    @SysLog(descrption = "管理员获取通知列表", type = "通知管理", modul = "管理员模块")
    @GetMapping("/admin/inform/user/{pageNumber}")
    public ResultResponseEntity getUserInformList(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20", required = false) @Min(1) Integer pageSize,
            @RequestParam(name = "sortKey", defaultValue = "id", required = false) String sortKey,
            @RequestParam(value = "sortType", defaultValue = "asc", required = false) String sortType) {
        return this.informManageService.getUserInformList(token, pageNumber, pageSize, sortKey, sortType);
    }

    /**
     * @param token    管理员Token
     * @param role     类型
     * @param informId 通知ID
     * @return 删除结果
     */
    @SysLog(descrption = "管理员删除通知", type = "通知管理", modul = "管理员模块")
    @DeleteMapping("/admin/inform/{role}/{informId}")
    public ResultResponseEntity deleteInform(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
                                             @PathVariable @NotBlank(message = "类型不能为空") String role,
                                             @PathVariable @NotBlank(message = "通知ID不能为空") String informId) {
        return this.informManageService.deleteInform(token, role, informId);
    }
}
