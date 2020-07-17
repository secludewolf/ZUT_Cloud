package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.AdminService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jager
 * @description 管理员信息管理接口
 * @date 2020/06/24-10:07
 **/
@RestController
@RequestMapping("/admin")
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
	@GetMapping("/login/token")
	public ResultResponseEntity loginByToken(@RequestHeader(TokenUtil.TOKEN_HEADER) String token) {
		return this.adminService.loginByToken(token);
	}

	/**
	 * 通过账号,密码登陆账户
	 *
	 * @param data 请求数据
	 *             account 账号
	 *             password 密码
	 * @return 管理员信息以及更新后的Token
	 */
	@PostMapping("/login/account")
	public ResultResponseEntity loginByAccount(@RequestBody String data) {
		return this.adminService.loginByAccount(data);
	}

	/**
	 * 通过邮箱重置密码
	 *
	 * @param data 请求数据
	 *             email 邮箱
	 * @return 成功或失败
	 */
	@PostMapping("/forget/email")
	public ResultResponseEntity forgetEmail(@RequestBody String data) {
		return this.adminService.forgetEmail(data);
	}

	/**
	 * 通过账号,密码注册账号
	 *
	 * @param data 请求数据
	 *             name 用户名
	 *             account 账号
	 *             password 密码
	 *             email 邮箱
	 *             phone 手机
	 *             key 授权码
	 * @return 成功或失败
	 */
	@PutMapping("/register/account")
	public ResultResponseEntity registerByAccount(@RequestBody String data) {
		return this.adminService.registerByAccount(data);
	}


	/**
	 * 获取管理员信息
	 *
	 * @param token   管理员Token
	 * @param adminId 管理员ID
	 * @return 管理员信息
	 */
	@GetMapping("/info/{adminId}")
	public ResultResponseEntity getAdminInfo(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @PathVariable("adminId") int adminId) {
		return this.adminService.getAdminInfo(token, adminId);
	}

	/**
	 * 修改管理员信息
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 管理员ID
	 *              account 账号
	 *              email 邮箱
	 *              phone 手机
	 *              name 用户名
	 * @return 管理员信息
	 */
	@PatchMapping("/info")
	public ResultResponseEntity changeAdminInfo(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.adminService.changeAdminInfo(token, data);
	}

	/**
	 * 修改管理员密码
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 管理员ID
	 *              oldPassword 原密码
	 *              newPassword 新密码
	 * @return 成功或失败
	 */
	@PatchMapping("/password")
	public ResultResponseEntity changeAdminPassword(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.adminService.changeAdminPassword(token, data);
	}

	/**
	 * 创建用户
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              name 用户名
	 *              account 账号
	 *              password 密码
	 *              email 邮箱
	 *              phone 手机
	 * @return 成功或失败
	 */
	@PutMapping("/user")
	public ResultResponseEntity createUserManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.adminService.createUserManage(token, data);
	}

	/**
	 * 创建管理员
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              name 用户名
	 *              account 账号
	 *              password 密码
	 *              email 邮箱
	 *              phone 手机
	 *              key 授权码
	 * @return 成功或失败
	 */
	@PutMapping("/admin")
	public ResultResponseEntity createAdminManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.adminService.createAdminManage(token, data);
	}

	/**
	 * 删除用户
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 用户ID
	 * @return 成功或失败
	 */
	@DeleteMapping("/user")
	public ResultResponseEntity deleteUserManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.adminService.deleteUserManage(token, data);
	}

	/**
	 * 删除管理员
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 管理员ID
	 * @return 成功或失败
	 */
	@DeleteMapping("/admin")
	public ResultResponseEntity deleteAdminManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.adminService.deleteAdminManage(token, data);
	}

	/**
	 * 获取用户列表
	 *
	 * @param token      管理员Token
	 * @param pageNumber 页数
	 * @return 用户列表
	 */
	@GetMapping("/user/list/{pageNumber}")
	public ResultResponseEntity getUserListManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @PathVariable("pageNumber") int pageNumber) {
		return this.adminService.getUserListManage(token, pageNumber);
	}

	/**
	 * 获取管理员列表
	 *
	 * @param token      管理员Token
	 * @param pageNumber 页数
	 * @return 管理员列表
	 */
	@GetMapping("/admin/list/{pageNumber}")
	public ResultResponseEntity getAdminListManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @PathVariable("pageNumber") int pageNumber) {
		return this.adminService.getAdminListManage(token, pageNumber);
	}

	/**
	 * 获取用户信息
	 *
	 * @param token  管理员Token
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	@GetMapping("/user/{userId}/info")
	public ResultResponseEntity getUserInfoManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @PathVariable("userId") int userId) {
		return this.adminService.getUserInfoManage(token, userId);
	}

	/**
	 * 获取管理员信息
	 *
	 * @param token   管理员Token
	 * @param adminId 管理员ID
	 * @return 成功或失败
	 */
	@GetMapping("/admin/{adminId}/info")
	public ResultResponseEntity getAdminInfoManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @PathVariable("adminId") int adminId) {
		return this.adminService.getAdminInfoManage(token, adminId);
	}

	/**
	 * 修改用户信息
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 用户ID
	 *              account 账号
	 *              email 邮箱
	 *              phone 手机
	 *              name 昵称
	 *              password 密码
	 *              status 状态
	 *              level 等级
	 *              repoSize 仓库大小
	 * @return 用户信息
	 */
	@PatchMapping("/user")
	public ResultResponseEntity changeUserInfoManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.adminService.changeUserInfoManage(token, data);
	}

	/**
	 * 修改管理员信息
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 管理员ID
	 *              account 账号
	 *              email 邮箱
	 *              phone 手机
	 *              password 密码
	 *              name 昵称
	 *              status 状态
	 *              level 等级
	 * @return 用户信息
	 */
	@PatchMapping("/admin")
	public ResultResponseEntity changeAdminInfoManage(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.adminService.changeAdminInfoManage(token, data);
	}
}
