package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 管理员信息管理业务接口
 * @date 2020/06/24-10:08
 **/
public interface AdminService {
    /**
     * 通过Token登陆,并获取管理员信息
     *
     * @param token 管理员Token
     * @return 管理员信息以及更新后的Token
     */
    ResultResponseEntity loginByToken(String token);

    /**
     * 通过账号,密码登陆账户
     *
     * @param data 请求数据
     *             account 账号
     *             password 密码
     * @return 管理员信息以及更新后的Token
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
    ResultResponseEntity registerByAccount(String data);

    /**
     * 获取管理员信息
     *
     * @param token   管理员Token
     * @param adminId 管理员ID
     * @return 管理员信息
     */
    ResultResponseEntity getAdminInfo(String token, int adminId);

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
    ResultResponseEntity changeAdminInfo(String token, String data);

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
    ResultResponseEntity changeAdminPassword(String token, String data);

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
    ResultResponseEntity createUserManage(String token, String data);

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
    ResultResponseEntity createAdminManage(String token, String data);

    /**
     * 删除用户
     *
     * @param token 管理员Token
     * @param data  请求数据
     *              id 用户ID
     * @return 成功或失败
     */
    ResultResponseEntity deleteUserManage(String token, String data);

    /**
     * 删除管理员
     *
     * @param token 管理员Token
     * @param data  请求数据
     *              id 管理员ID
     * @return 成功或失败
     */
    ResultResponseEntity deleteAdminManage(String token, String data);

    /**
     * 获取用户列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @return 用户列表
     */
    ResultResponseEntity getUserListManage(String token, int pageNumber);

    /**
     * 获取管理员列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @return 管理员列表
     */
    ResultResponseEntity getAdminListManage(String token, int pageNumber);

    /**
     * 获取用户信息
     *
     * @param token  管理员Token
     * @param userId 用户ID
     * @return 用户信息
     */
    ResultResponseEntity getUserInfoManage(String token, int userId);

    /**
     * 获取管理员信息
     *
     * @param token   管理员Token
     * @param adminId 管理员ID
     * @return 成功或失败
     */
    ResultResponseEntity getAdminInfoManage(String token, int adminId);

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
    ResultResponseEntity changeUserInfoManage(String token, String data);

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
    ResultResponseEntity changeAdminInfoManage(String token, String data);
}
