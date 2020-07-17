package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 分享业务
 * @date 2020/06/28-09:50
 **/
public interface ShareService {
	/**
	 * 创建分享
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              name 分享名称
	 *              path 分享位置
	 *              password 密码
	 * @return 分享信息
	 */
	ResultResponseEntity createShare(String token, String data);

	/**
	 * 获取分享列表
	 *
	 * @param token 用户Token
	 * @return 分享列表
	 */
	ResultResponseEntity getShareList(String token);

	/**
	 * 删除信息
	 *
	 * @param token   用户Token
	 * @param shareId 分享ID
	 * @return 删除结果
	 */
	ResultResponseEntity deleteShare(String token, String shareId);

	/**
	 * 获取分享信息
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              shareId 分享ID
	 *              password 分享密码
	 * @return 分享信息
	 */
	ResultResponseEntity getShare(String token, String data);

	/**
	 * 保存分享
	 *
	 * @param token 用户信Token
	 * @param data  请求参数
	 *              shareId 分享ID
	 *              password 分享密码
	 *              path 保存位置
	 * @return 是否保存成功
	 */
	ResultResponseEntity saveShare(String token, String data);
}
