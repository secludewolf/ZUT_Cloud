package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 通知业务接口
 * @date 2020/06/28-16:49
 **/
public interface InformService {
	/**
	 * 获取通知列表
	 *
	 * @param token 用户Token
	 * @return 通知列表
	 */
	ResultResponseEntity getInformList(String token);

	/**
	 * 获取通知信息
	 *
	 * @param token    用户Token
	 * @param informId 通知ID
	 * @return 通知信息
	 */
	ResultResponseEntity getInform(String token, String informId);

	/**
	 * 修改通知状态
	 *
	 * @param token    用户Token
	 * @param informId 通知ID
	 * @param status   通知状态
	 * @return 是否成功
	 */
	ResultResponseEntity changeInformStatus(String token, String informId, int status);
}
