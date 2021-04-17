package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.FileReport;
import com.ztu.cloud.cloud.common.bean.mysql.ShareReport;
import com.ztu.cloud.cloud.common.bean.mysql.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jager
 * @description 文件举报数据库接口
 * @date 2021/02/01-11:26
 **/
@Mapper
@Component
public interface ShareReportMapper {
    /**
     * 保存分享举报信息
     *
     * @param shareReport 分享举报信息
     * @return 插入结果
     */
    int insertShareReport(ShareReport shareReport);

    /**
     * 获取举报列表
     *
     * @param shareId 分享Id
     * @return 举报列表
     */
    List<ShareReport> getShareReportListByFileId(String shareId);
}
