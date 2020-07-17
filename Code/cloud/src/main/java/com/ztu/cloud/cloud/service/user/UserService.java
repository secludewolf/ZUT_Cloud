package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 用户数据业务接口
 * @date 2020/06/23-10:34
 **/
public interface UserService {
	/**
	 * 通过Token登陆,并获取用户信息
	 *
	 * @param token 用户Token
	 * @return 用户信息, 仓库信息以及更新后的Token
	 */
	ResultResponseEntity loginByToken(String token);

	/**
	 * 通过邮箱,密码登陆账户
	 *
	 * @param data 请求数据
	 *             email 邮箱
	 *             password 密码
	 * @return 用户信息, 仓库信息以及更新后的Token
	 */
	ResultResponseEntity loginByEmail(String data);

	/**
	 * 通过账号,密码登陆账户
	 *
	 * @param data 请求数据
	 *             account 账号
	 *             password 密码
	 * @return 用户信息, 仓库信息以及更新后的Token
	 */
	ResultResponseEntity loginByAccount(String data);

	/**
	 * 通过邮箱重置密码
	 *
	 * @param data 请求数据
	 *             email 邮箱
	 * @return 成功或失败
	 */
	ResultResponseEntity forgetEmail(String data);

	/**
	 * 通过邮箱,密码注册账号
	 *
	 * @param data 请求数据
	 *             name 昵称
	 *             account 账号
	 *             email 邮箱
	 *             password 密码
	 * @return 成功或失败
	 */
	ResultResponseEntity registerByEmail(String data);

	/**
	 * 通过账号,密码注册账号
	 *
	 * @param data 请求数据
	 *             name 昵称
	 *             account 账号
	 *             password 密码
	 * @return 成功或失败
	 */
	ResultResponseEntity registerByAccount(String data);

	/**
	 * 获取用户信息
	 *
	 * @param token  用户Token
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	ResultResponseEntity getUserInfo(String token, int userId);

	/**
	 * 修改用户信息
	 *
	 * @param token 用户Token
	 * @param data  请求数据
	 *              id 用户ID
	 *              account 账号
	 *              email 邮箱
	 *              phone 手机
	 *              name 昵称
	 * @return 用户信息
	 */
	ResultResponseEntity changeUserInfo(String token, String data);

	/**
	 * 修改用户密码
	 *
	 * @param token 用户Token
	 * @param data  请求数据
	 *              id 用户ID
	 *              oldPassword 原密码
	 *              newPassword 新密码
	 * @return 成功或失败
	 */
	ResultResponseEntity changeUserPassword(String token, String data);
}
