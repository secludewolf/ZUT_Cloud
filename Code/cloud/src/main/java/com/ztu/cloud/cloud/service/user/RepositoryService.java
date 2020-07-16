package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

/**
 * @author Jager
 * @description 仓库操作业务层接口
 * @date 2020/06/25-08:48
 **/
public interface RepositoryService {
    /**
     * 获取仓库信息
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @return 仓库信息
     */
    ResultResponseEntity getRepository(String token, String repositoryId);

    /**
     * 创建文件
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              fileId 文件ID
     *              name 文件名
     *              path 保存路径
     * @return 仓库信息
     */
    ResultResponseEntity createFile(String token, String data);

    /**
     * 创建文件夹
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              name 文件名
     *              path 保存路径
     * @return 仓库信息
     */
    ResultResponseEntity createFolder(String token, String data);

    /**
     * 移动文件
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              name 文件名
     *              oldPath 原路径
     *              newPath 新路径
     * @return 仓库信息
     */
    ResultResponseEntity moveFile(String token, String data);

    /**
     * 移动文件夹
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              name 文件名
     *              oldPath 原路径
     *              newPath 新路径
     * @return 仓库信息
     */
    ResultResponseEntity moveFolder(String token, String data);

    /**
     * 复制文件
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              name 文件名
     *              oldPath 原路径
     *              newPath 新路径
     * @return 仓库信息
     */
    ResultResponseEntity copyFile(String token, String data);

    /**
     * 复制文件夹
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              name 文件名
     *              oldPath 原路径
     *              newPath 新路径
     * @return 仓库信息
     */
    ResultResponseEntity copyFolder(String token, String data);

    /**
     * 文件重命名
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              oldName 原名称
     *              newName 新名称
     *              path 路径
     * @return 仓库信息
     */
    ResultResponseEntity renameFile(String token, String data);

    /**
     * 文件夹重命名
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              oldName 原名称
     *              newName 新名称
     *              path 路径
     * @return 仓库信息
     */
    ResultResponseEntity renameFolder(String token, String data);

    /**
     * 删除文件
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              isFile 是否是文件
     *              name 文件名
     *              path 保存路径
     * @return 仓库信息
     */
    ResultResponseEntity deleteFromRepository(String token, String data);

    /**
     * 回复文件
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              isFile 是否是文件
     *              recycleId 回收站ID
     * @return 仓库信息
     */
    ResultResponseEntity restoreFromRecycleBin(String token, String data);

    /**
     * 删除文件
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     *              isFile 是否是文件
     *              recycleId 回收站ID
     * @return 仓库信息
     */
    ResultResponseEntity deleteFromRecycleBin(String token, String data);

    /**
     * 删除文件
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     * @return 仓库信息
     */
    ResultResponseEntity cleanRecycleBin(String token, String data);
}
