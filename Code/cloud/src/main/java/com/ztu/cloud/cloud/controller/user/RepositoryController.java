package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.RepositoryService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jager
 * @description 仓库操作接口
 * @date 2020/06/25-08:46
 **/
@RestController
@RequestMapping("/repository")
public class RepositoryController {
    RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * 获取仓库信息
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @return 仓库信息
     */
    @GetMapping("/{repositoryId}")
    public ResultResponseEntity getRepository(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @PathVariable("repositoryId") String repositoryId) {
        return this.repositoryService.getRepository(token, repositoryId);
    }

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
    @PutMapping("/file/create")
    public ResultResponseEntity createFile(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.createFile(token, data);
    }

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
    @PutMapping("/folder/create")
    public ResultResponseEntity createFolder(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.createFolder(token, data);
    }

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
    @PatchMapping("/file/move")
    public ResultResponseEntity moveFile(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.moveFile(token, data);
    }

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
    @PatchMapping("/folder/move")
    public ResultResponseEntity moveFolder(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.moveFolder(token, data);
    }

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
    @PatchMapping("/file/copy")
    public ResultResponseEntity copyFile(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.copyFile(token, data);
    }

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
    @PatchMapping("/folder/copy")
    public ResultResponseEntity copyFolder(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.copyFolder(token, data);
    }

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
    @PatchMapping("/file/rename")
    public ResultResponseEntity renameFile(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.renameFile(token, data);
    }

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
    @PatchMapping("/folder/rename")
    public ResultResponseEntity renameFolder(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.renameFolder(token, data);
    }

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
    @DeleteMapping
    public ResultResponseEntity deleteFromRepository(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.deleteFromRepository(token, data);
    }

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
    @PatchMapping("/recyclebin")
    public ResultResponseEntity restoreFromRecycleBin(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.restoreFromRecycleBin(token, data);
    }

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
    @DeleteMapping("/recyclebin")
    public ResultResponseEntity deleteFromRecycleBin(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.deleteFromRecycleBin(token, data);
    }

    /**
     * 删除文件
     *
     * @param token 用户Token
     * @param data  请求参数
     *              repositoryId 仓库ID
     * @return 仓库信息
     */
    @DeleteMapping("/recyclebin/clean")
    public ResultResponseEntity cleanRecycleBin(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
        return this.repositoryService.cleanRecycleBin(token, data);
    }
}
