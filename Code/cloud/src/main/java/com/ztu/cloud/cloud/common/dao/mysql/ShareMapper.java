package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.Share;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jager
 * @description 分享信息表操作
 * @date 2020/06/21-18:34
 **/
@Mapper
@Component
public interface ShareMapper {

	/**
	 * 根据分享id查询分享信息
	 *
	 * @param id 分享id
	 * @return 分享信息实体类
	 */
	Share getShareById(String id);

	/**
	 * 根据用户id查询分享信息
	 *
	 * @param userId 分享id
	 * @return 分享信息实体类
	 */
	List<Share> getShareByUserId(int userId);

	/**
	 * 获取分享列表
	 *
	 * @return 分享列表
	 */
	List<Share> getShares();

	/**
	 * 获取分享数量
	 *
	 * @return 分享数量
	 */
	Long getShareCount();

	/**
	 * 更新分享信息
	 *
	 * @param share 分享信息
	 * @return 影响数量
	 */
	int updateShare(Share share);

	/**
	 * 插入一个分享信息
	 *
	 * @param share 分享信息
	 * @return 影响数量
	 */
	int insertShare(Share share);

	/**
	 * 删除分享信息
	 *
	 * @param id 分享id
	 * @return 影响数量
	 */
	int deleteShareById(String id);

	/**
	 * 更新分享状态
	 *
	 * @param id     分享ID
	 * @param status 状态
	 * @return 更新结果
	 */
	int updateShareStatus(String id, int status);
}
