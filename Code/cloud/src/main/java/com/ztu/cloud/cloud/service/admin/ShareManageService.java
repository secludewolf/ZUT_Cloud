package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 分享业务接口
 * @date 2020/06/28-13:23
 **/
public interface ShareManageService {
	/**
	 * 获取分享列表
	 *
	 * @param token 管理员Token
	 * @param pageNumber 页数
	 * @param pageSize   分页大小
	 * @param sortKey    排序主键
	 * @param sortType   排序类型
	 * @return 分享列表
	 */
	ResultResponseEntity getShareList(String token, Integer pageNumber, Integer pageSize, String sortKey, String sortType);

	/**
	 * 获取分享信息
	 *
	 * @param token   管理员Token
	 * @param shareId 分享ID
	 * @return 分享信息
	 */
	ResultResponseEntity getShare(String token, String shareId);

	/**
	 * 删除分享
	 *
	 * @param token   管理员Token
	 * @param shareId 分享ID
	 * @return 删除结果
	 */
	ResultResponseEntity deleteShare(String token, String shareId);
}
