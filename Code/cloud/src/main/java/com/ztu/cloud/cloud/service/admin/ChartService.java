package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 控制台图表业务接口
 * @date 2021/04/14-10:23
 **/
public interface ChartService {
    /**
     * 获取PV图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    ResultResponseEntity getPvData(String token);

    /**
     * 获取获取UV图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    ResultResponseEntity getUvData(String token);

    /**
     * 获取获取DF图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    ResultResponseEntity getDfData(String token);

    /**
     * 获取DS图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    ResultResponseEntity getDsData(String token);

    /**
     * 获取Mouth图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    ResultResponseEntity getMouthData(String token);

    /**
     * 获取File图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    ResultResponseEntity getFileData(String token);

    /**
     * 获取Download图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    ResultResponseEntity getDownloadData(String token);
}
