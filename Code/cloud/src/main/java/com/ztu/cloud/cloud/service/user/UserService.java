package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.dto.common.ChangePassword;
import com.ztu.cloud.cloud.common.dto.common.ForgetEmail;
import com.ztu.cloud.cloud.common.dto.common.LoginAccount;
import com.ztu.cloud.cloud.common.dto.common.LoginEmail;
import com.ztu.cloud.cloud.common.dto.user.user.ChangeUserInfo;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterAccount;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterEmail;
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
     * @param parameter 请求数据 email 邮箱 password 密码 rememberMe 记住我
     * @return 用户信息, 仓库信息以及更新后的Token
     */
    ResultResponseEntity loginByEmail(LoginEmail parameter);

    /**
     * 通过账号,密码登陆账户
     *
     * @param parameter 请求数据 account 账号 password 密码 rememberMe 记住我
     * @return 用户信息, 仓库信息以及更新后的Token
     */
    ResultResponseEntity loginByAccount(LoginAccount parameter);

    /**
     * 通过邮箱重置密码
     *
     * @param parameter 请求数据 email 邮箱
     * @return 成功或失败
     */
    ResultResponseEntity forgetEmail(ForgetEmail parameter);

    /**
     * 通过邮箱,密码注册账号
     *
     * @param parameter 请求数据 name 昵称 account 账号 email 邮箱 password 密码
     * @return 成功或失败
     */
    ResultResponseEntity registerByEmail(RegisterEmail parameter);

    /**
     * 通过账号,密码注册账号
     *
     * @param parameter 请求数据 name 昵称 account 账号 password 密码
     * @return 成功或失败
     */
    ResultResponseEntity registerByAccount(RegisterAccount parameter);

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
     * @param token     用户Token
     * @param parameter 请求数据 id 用户ID account 账号 email 邮箱 phone 手机 name 昵称
     * @return 用户信息
     */
    ResultResponseEntity changeUserInfo(String token, ChangeUserInfo parameter);

    /**
     * 修改用户密码
     *
     * @param token     用户Token
     * @param parameter 请求数据 id 用户ID oldPassword 原密码 newPassword 新密码
     * @return 成功或失败
     */
    ResultResponseEntity changeUserPassword(String token, ChangePassword parameter);
}
