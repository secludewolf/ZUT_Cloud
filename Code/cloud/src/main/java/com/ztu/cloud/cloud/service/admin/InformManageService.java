package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.dto.admin.CreateInform;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 通知管理接口
 * @date 2020/06/28-17:46
 **/
public interface InformManageService {
    /**
     * 创建用户通知
     *
     * @param token
     *            管理员Token
     * @param parameter
     *            请求参数 header 标题 content 内容 validTime 有消息
     * @return 通知信息
     */
    ResultResponseEntity createUserInform(String token, CreateInform parameter);

    /**
     * 创建管理员通知
     *
     * @param token
     *            管理员Token
     * @param parameter
     *            请求参数 header 标题 content 内容 validTime 有消息
     * @return 通知信息
     */
    ResultResponseEntity createAdminInform(String token,CreateInform parameter);

    /**
     * 获取通知列表
     *
     * @param token
     *            管理员Token
     * @return 通知列表
     */
    ResultResponseEntity getInformList(String token);

    /**
     * 获取通知信息
     *
     * @param token
     *            管理员Token
     * @param informId
     *            通知ID
     * @return 通知信息
     */
    ResultResponseEntity getInform(String token, String informId);

    /**
     * 修改通知状态
     *
     * @param token
     *            管理员Token
     * @param informId
     *            通知ID
     * @param status
     *            通知状态
     * @return 是否成功
     */
    ResultResponseEntity changeInformStatus(String token, String informId, int status);
}
