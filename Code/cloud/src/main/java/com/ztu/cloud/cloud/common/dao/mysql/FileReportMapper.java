package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.FileReport;
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
public interface FileReportMapper {
    /**
     * 保存文件举报信息
     *
     * @param fileReport 文件举报信息
     * @return 插入结果
     */
    int insertFileReport(FileReport fileReport);
}
