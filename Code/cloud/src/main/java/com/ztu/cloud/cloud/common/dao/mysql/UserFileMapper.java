package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Jager
 * @description 用户文件关联信息表操作
 * @date 2020/06/21-17:05
 **/
@Mapper
@Component
public interface UserFileMapper {
    /**
     * 新增文件用户
     *
     * @param userFile 用户文件关系
     * @return 更新结果
     */
    int insertUserFile(UserFile userFile);

    /**
     * 文件用户
     *
     * @param id 关系ID
     * @return 文件用户
     */
    UserFile getUserFile(long id);

    /**
     * 删除文件用户
     *
     * @param id 文件用户
     * @return 更新结果
     */
    int deleteUserFile(long id);

    /**
     * 修改关系对应路径
     *
     * @param id   关系ID
     * @param path 路径
     * @return 更新结果
     */
    int updateUserFilePath(long id, String path);
}
