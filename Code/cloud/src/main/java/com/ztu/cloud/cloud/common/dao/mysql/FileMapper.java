package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.File;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jager
 * @description 文件信息操作
 * @date 2020/06/21-12:59
 **/
@Mapper
@Component
public interface FileMapper {
    /**
     * 增加文件信息
     *
     * @param file 文件信息
     * @return 更新结果
     */
    int insertFile(File file);

    /**
     * 删除文件信息
     *
     * @param id 文件id
     * @return 更新结果
     */
    int deleteFileById(String id);

    /**
     * 更新文件信息
     *
     * @param file 文件信息
     * @return 更新结果
     */
    int updateFile(File file);

    /**
     * 获取文件信息
     *
     * @param id 文件id
     * @return 文件信息
     */
    File getFileById(String id);

    /**
     * 获取分页文件列表
     *
     * @param start 开始位置
     * @param end   结束位置
     * @return 用户列表
     */
    List<File> getFile(int start, int end);

    //TODO 只更新了FileInfo

    /**
     * 引用数增加
     *
     * @param id  文件id
     * @param num 增加数
     * @return 更新结果
     */
    int fileQuoteNumberAdd(String id, int num);

    //TODO 只更新了FileInfo

    /**
     * 引用数减少
     *
     * @param id  文件id
     * @param num 较少数
     * @return 更新结果
     */
    int fileQuoteNumberSub(String id, int num);
}
