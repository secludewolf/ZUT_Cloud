package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.dto.common.ChangePassword;
import com.ztu.cloud.cloud.common.dto.common.ForgetEmail;
import com.ztu.cloud.cloud.common.dto.common.LoginAccount;
import com.ztu.cloud.cloud.common.dto.common.LoginEmail;
import com.ztu.cloud.cloud.common.dto.user.user.ChangeUserInfo;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterAccount;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterEmail;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.UserService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 用户信息管理接口
 * @date 2020/06/23-10:25
 **/
@RestController
public class UserController {
	UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 通过Token登陆,并获取用户信息
	 *
	 * @param token 用户Token
	 * @return 用户信息, 仓库信息以及更新后的Token
	 */
	@GetMapping("/login/token")
	public ResultResponseEntity loginByToken(@RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token) {
		return this.userService.loginByToken(token);
	}

	/**
	 * 通过邮箱,密码登陆账户
	 *
	 * @param parameter 请求数据
	 *                  email 邮箱
	 *                  password 密码
	 *                  rememberMe 记住我
	 * @return 用户信息, 仓库信息以及更新后的Token
	 */
	@PostMapping("/login/email")
	public ResultResponseEntity loginByEmail(@RequestBody @Valid LoginEmail parameter) {
		return this.userService.loginByEmail(parameter);
	}

	/**
	 * 通过账号,密码登陆账户
	 *
	 * @param parameter 请求数据
	 *                  account 账号
	 *                  password 密码
	 *                  rememberMe 记住我
	 * @return 用户信息, 仓库信息以及更新后的Token
	 */
	@PostMapping("/login/account")
	public ResultResponseEntity loginByAccount(@RequestBody @Valid LoginAccount parameter) {
		return this.userService.loginByAccount(parameter);
	}

	/**
	 * 通过邮箱重置密码
	 *
	 * @param parameter 请求数据
	 *                  email 邮箱
	 * @return 成功或失败
	 */
	@PostMapping("/forget/email")
	public ResultResponseEntity forgetEmail(@RequestBody @Valid ForgetEmail parameter) {
		return this.userService.forgetEmail(parameter);
	}

	/**
	 * 通过邮箱,密码注册账号
	 *
	 * @param parameter 请求数据
	 *                  name 昵称
	 *                  account 账号
	 *                  email 邮箱
	 *                  password 密码
	 * @return 成功或失败
	 */
	@PutMapping("/register/email")
	public ResultResponseEntity registerByEmail(@RequestBody @Valid RegisterEmail parameter) {
		return this.userService.registerByEmail(parameter);
	}

	/**
	 * 通过账号,密码注册账号
	 *
	 * @param parameter 请求数据
	 *                  name 昵称
	 *                  account 账号
	 *                  password 密码
	 * @return 成功或失败
	 */
	@PutMapping("/register/account")
	public ResultResponseEntity registerByAccount(@RequestBody @Valid RegisterAccount parameter) {
		return this.userService.registerByAccount(parameter);
	}

	/**
	 * 获取用户信息
	 *
	 * @param token  用户Token
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	@GetMapping("/user/info/{userId}")
	public ResultResponseEntity getUserInfo(@RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token,
	                                        @PathVariable("userId") @NotNull(message = "用户ID不能为空") int userId) {
		return this.userService.getUserInfo(token, userId);
	}

	/**
	 * 修改用户信息
	 *
	 * @param token 用户Token
	 * @param parameter  请求数据
	 *              id 用户ID
	 *              account 账号
	 *              email 邮箱
	 *              phone 手机
	 *              name 昵称
	 * @return 用户信息
	 */
	@PatchMapping("/user/info")
	public ResultResponseEntity changeUserInfo(@RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token, 
	                                           @RequestBody ChangeUserInfo parameter) {
		return this.userService.changeUserInfo(token, parameter);
	}

	/**
	 * 修改用户密码
	 *
	 * @param token 用户Token
	 * @param parameter  请求数据
	 *              id 用户ID
	 *              oldPassword 原密码
	 *              newPassword 新密码
	 * @return 成功或失败
	 */
	@PatchMapping("/user/password")
	public ResultResponseEntity changeUserPassword(@RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token,
	                                               @RequestBody ChangePassword parameter) {
		return this.userService.changeUserPassword(token, parameter);
	}
}
