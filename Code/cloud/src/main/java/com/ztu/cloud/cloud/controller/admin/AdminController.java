package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.dto.admin.*;
import com.ztu.cloud.cloud.common.dto.common.ChangePassword;
import com.ztu.cloud.cloud.common.dto.common.ForgetEmail;
import com.ztu.cloud.cloud.common.dto.common.LoginAccount;
import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.AdminService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 管理员信息管理接口
 * @date 2020/06/24-10:07
 **/
@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {
    AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 通过Token登陆,并获取管理员信息
     *
     * @param token 管理员Token
     * @return 管理员信息以及更新后的Token
     */
    @SysLog(descrption = "管理员Token登陆", type = "登陆", modul = "管理员模块")
    @GetMapping("/login/token")
    public ResultResponseEntity
    loginByToken(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token) {
        return this.adminService.loginByToken(token);
    }

    /**
     * 通过账号,密码登陆账户
     *
     * @param parameter 请求数据 {account 账号 password 密码}
     * @return 管理员信息以及更新后的Token
     */
    @SysLog(descrption = "管理员账号登陆", type = "登陆", modul = "管理员模块")
    @PostMapping("/login/account")
    public ResultResponseEntity loginByAccount(@RequestBody @NotNull @Valid LoginAccount parameter) {
        return this.adminService.loginByAccount(parameter);
    }

    /**
     * 通过邮箱重置密码
     *
     * @param parameter 请求数据 {email 邮箱}
     * @return 成功或失败
     */
    @SysLog(descrption = "管理员重置密码", type = "重置密码", modul = "管理员模块")
    @PostMapping("/forget/email")
    public ResultResponseEntity forgetEmail(@RequestBody @Valid ForgetEmail parameter) {
        return this.adminService.forgetEmail(parameter);
    }

    /**
     * 通过账号,密码注册账号
     *
     * @param parameter 请求数据 {name 用户名 account 账号 password 密码 email 邮箱 phone 手机 key 授权码}
     * @return 成功或失败
     */
    @SysLog(descrption = "管理员注册", type = "注册", modul = "管理员模块")
    @PutMapping("/register/account")
    public ResultResponseEntity registerByAccount(@RequestBody @Valid RegisterAdmin parameter) {
        return this.adminService.registerByAccount(parameter);
    }

    /**
     * 获取管理员信息
     *
     * @param token   管理员Token
     * @param adminId 管理员ID
     * @return 管理员信息
     */
    @SysLog(descrption = "获取管理员个人信息", type = "获取个人信息", modul = "管理员模块")
    @GetMapping("/info/{adminId}")
    public ResultResponseEntity getAdminInfo(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable("adminId") Integer adminId) {
        return this.adminService.getAdminInfo(token, adminId);
    }

    /**
     * 修改管理员信息
     *
     * @param token     管理员Token
     * @param parameter 请求数据 {id 管理员ID account 账号 email 邮箱 phone 手机 name 用户名}
     * @return 管理员信息
     */
    @SysLog(descrption = "修改管理员个人信息", type = "修改个人信息", modul = "管理员模块")
    @PatchMapping("/info")
    public ResultResponseEntity changeAdminInfo(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid ChangeAdminInfo parameter) {
        return this.adminService.changeAdminInfo(token, parameter);
    }

    /**
     * 修改管理员密码
     *
     * @param token     管理员Token
     * @param parameter 请求数据 id 管理员ID oldPassword 原密码 newPassword 新密码
     * @return 成功或失败
     */
    @SysLog(descrption = "修改管理员密码", type = "修改密码", modul = "管理员模块")
    @PatchMapping("/password")
    public ResultResponseEntity changeAdminPassword(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid ChangePassword parameter) {
        return this.adminService.changeAdminPassword(token, parameter);
    }

    /**
     * 创建用户
     *
     * @param token     管理员Token
     * @param parameter 请求数据 name 用户名 account 账号 password 密码 email 邮箱 phone 手机
     * @return 成功或失败
     */
    @SysLog(descrption = "管理员创建用户", type = "账号管理", modul = "管理员模块")
    @PutMapping("/user")
    public ResultResponseEntity createUserManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid CreateUser parameter) {
        return this.adminService.createUserManage(token, parameter);
    }

    /**
     * 创建管理员
     *
     * @param token     管理员Token
     * @param parameter 请求数据 name 用户名 account 账号 password 密码 email 邮箱 phone 手机 key 授权码
     * @return 成功或失败
     */
    @SysLog(descrption = "管理员创建管理员", type = "账号管理", modul = "管理员模块")
    @PutMapping("/admin")
    public ResultResponseEntity createAdminManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid CreateAdmin parameter) {
        return this.adminService.createAdminManage(token, parameter);
    }

    /**
     * 删除用户
     *
     * @param token     管理员Token
     * @param parameter 请求数据 id 用户ID
     * @return 成功或失败
     */
    @SysLog(descrption = "管理员删除用户", type = "账号管理", modul = "管理员模块")
    @DeleteMapping("/user")
    public ResultResponseEntity deleteUserManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid DeleteUser parameter) {
        return this.adminService.deleteUserManage(token, parameter);
    }

    /**
     * 删除管理员
     *
     * @param token     管理员Token
     * @param parameter 请求数据 id 管理员ID
     * @return 成功或失败
     */
    @SysLog(descrption = "管理员删除管理员", type = "账号管理", modul = "管理员模块")
    @DeleteMapping("/admin")
    public ResultResponseEntity deleteAdminManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid DeleteAdmin parameter) {
        return this.adminService.deleteAdminManage(token, parameter);
    }

    /**
     * 获取用户列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @param pageSize   分页大小
     * @param sortKey    排序Key
     * @param sortType   排序类型
     * @param status     状态
     * @param startTime  起始时间
     * @param endTime    终止时间
     * @param phone      手机
     * @param email      邮箱
     * @param account    账号
     * @return 用户列表
     */
    @SysLog(descrption = "分页获取管理员列表", type = "账号管理", modul = "管理员模块")
    @GetMapping("/user/list/{pageNumber}")
    public ResultResponseEntity getUserListManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable("pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20", required = false) @Min(1) Integer pageSize,
            @RequestParam(name = "sortKey", defaultValue = "id", required = false) String sortKey,
            @RequestParam(value = "sortType", defaultValue = "asc", required = false) String sortType,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) Long startTime,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "account", required = false) String account) {
        return this.adminService.getUserListManage(token, pageNumber, pageSize, sortKey, sortType, status, startTime, endTime, phone, email, account);
    }

    /**
     * 获取管理员列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @param pageSize   分页大小
     * @param sortKey    排序Key
     * @param sortType   排序类型
     * @param status     状态
     * @param startTime  起始时间
     * @param endTime    终止时间
     * @param phone      手机
     * @param email      邮箱
     * @param account    账号
     * @return 管理员列表
     */
    @SysLog(descrption = "分页获取管理员列表", type = "账号管理", modul = "管理员模块")
    @GetMapping("/admin/list/{pageNumber}")
    public ResultResponseEntity getAdminListManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable("pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20", required = false) @Min(1) Integer pageSize,
            @RequestParam(name = "sortKey", defaultValue = "id", required = false) String sortKey,
            @RequestParam(value = "sortType", defaultValue = "asc", required = false) String sortType,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) Long startTime,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "account", required = false) String account) {
        return this.adminService.getAdminListManage(token, pageNumber, pageSize, sortKey, sortType, status, startTime, endTime, phone, email, account);
    }

    /**
     * 获取用户信息
     *
     * @param token  管理员Token
     * @param userId 用户ID
     * @return 用户信息
     */
    @SysLog(descrption = "管理员获取用户信息", type = "账号管理", modul = "管理员模块")
    @GetMapping("/user/{userId}/info")
    public ResultResponseEntity getUserInfoManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Integer userId) {
        return this.adminService.getUserInfoManage(token, userId);
    }

    /**
     * 获取管理员信息
     *
     * @param token   管理员Token
     * @param adminId 管理员ID
     * @return 成功或失败
     */
    @SysLog(descrption = "管理员获取管理员信息", type = "账号管理", modul = "管理员模块")
    @GetMapping("/admin/{adminId}/info")
    public ResultResponseEntity getAdminInfoManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable("adminId") @NotNull(message = "管理员ID不能为空") Integer adminId) {
        return this.adminService.getAdminInfoManage(token, adminId);
    }

    /**
     * 修改用户信息
     *
     * @param token     管理员Token
     * @param parameter 请求数据 id 用户ID account 账号 email 邮箱 phone 手机 name 昵称 password 密码 status 状态 level 等级 repoSize 仓库大小
     * @return 用户信息
     */
    @SysLog(descrption = "管理员修改用户信息", type = "账号管理", modul = "管理员模块")
    @PatchMapping("/user")
    public ResultResponseEntity changeUserInfoManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid ChangeUserInfoManage parameter) {
        return this.adminService.changeUserInfoManage(token, parameter);
    }

    /**
     * 修改管理员信息
     *
     * @param token     管理员Token
     * @param parameter 请求数据 id 管理员ID account 账号 email 邮箱 phone 手机 password 密码 name 昵称 status 状态 level 等级
     * @return 用户信息
     */
    @SysLog(descrption = "管理员修改管理员信息", type = "账号管理", modul = "管理员模块")
    @PatchMapping("/admin")
    public ResultResponseEntity changeAdminInfoManage(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @RequestBody @Valid ChangeAdminInfoManage parameter) {
        return this.adminService.changeAdminInfoManage(token, parameter);
    }
}
