package com.ztu.cloud.cloud.service.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.stereotype.Component;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.File;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserFileMapper;
import com.ztu.cloud.cloud.common.dto.user.repository.*;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.user.RepositoryInfo;
import com.ztu.cloud.cloud.util.*;

/**
 * @author Jager
 * @description 仓库操作业务层实现
 * @date 2020/06/25-08:48
 **/
@Component
public class RepositoryServiceImpl implements RepositoryService {
    UserRepositoryDao userRepositoryDao;
    UserFileMapper userFileDao;
    FileMapper fileDao;
    ShareMapper shareDao;

    public RepositoryServiceImpl(UserRepositoryDao userRepositoryDao, UserFileMapper userFileDao, FileMapper fileDao,
        ShareMapper shareDao) {
        this.userRepositoryDao = userRepositoryDao;
        this.userFileDao = userFileDao;
        this.fileDao = fileDao;
        this.shareDao = shareDao;
    }

    /**
     * 获取仓库信息
     *
     * @param token
     *            用户Token
     * @param repositoryId
     *            仓库ID
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity getRepository(String token, String repositoryId) {
        int id = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(id);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(repositoryId)) {
            return ResultConstant.NO_ACCESS;
        }
        return ResultUtil.createResult("查询成功", new RepositoryInfo(repository));
    }

    /**
     * 创建文件
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID fileId 文件ID name 文件名 path 保存路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity createFile(String token, CreateFile parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        if (!ForbiddenUtil.isFileNameValid(parameter.getName())) {
            return ResultConstant.NAME_INVALID;
        }
        String fileId = parameter.getFileId();
        com.ztu.cloud.cloud.common.bean.mysql.File file = this.fileDao.getFileById(fileId);
        // 判断目标文件是否存在
        if (file == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        // 判断是否超出仓库大小
        if ((repository.getUseSize() + file.getSize()) > repository.getRepoSize()) {
            return ResultConstant.REPOSITORY_FULL;
        }
        Folder folder = getFolder(repository, parameter.getPath());
        // 判断目标路径是否存在
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        if (folder.getFiles() == null) {
            folder.setFiles(new HashMap<>(1));
        }
        // 判断文件名是否已被使用
        if (RepositoryUtil.nameIsExist(parameter.getName(), folder)) {
            return ResultConstant.FILE_EXISTED;
        }
        // 创建新的用户文件关系
        UserFile userFile = new UserFile(userId, fileId, parameter.getPath());
        this.userFileDao.insertUserFile(userFile);
        // 增加文件引用数
        this.fileDao.fileQuoteNumberAdd(file.getId(), 1);
        String[] temp = parameter.getName().split("\\.");
        String type = temp[temp.length - 1];
        // 将文件插入文件夹
        folder.getFiles().put(parameter.getName(), new com.ztu.cloud.cloud.common.bean.mongodb.inside.File(
            parameter.getFileId(), userFile.getId(), parameter.getName(), parameter.getPath(), type, file.getSize()));
        // 更新仓库以使用空间大小
        repository.setUseSize(repository.getUseSize() + file.getSize());
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        this.userRepositoryDao.updateUseSizeById(repository.getId(), repository.getUseSize());
        return ResultUtil.createResult("创建成功", new RepositoryInfo(repository));
    }

    /**
     * 创建文件夹
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID name 文件名 path 保存路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity createFolder(String token, CreateFolder parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        if (!ForbiddenUtil.isFileNameValid(parameter.getName())) {
            return ResultConstant.NAME_INVALID;
        }
        Folder folder = getFolder(repository, parameter.getPath());
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        // TODO 配置文件
        // 判断是否超出最大深度
        if (folder.getDepth() >= 64) {
            return ResultConstant.FOLDER_DEPTH_TOO_BIG;
        }
        if (folder.getFolders() == null) {
            folder.setFolders(new HashMap<>(1));
        }
        // 判断文件名是否被使用
        if (RepositoryUtil.nameIsExist(parameter.getName(), folder)) {
            return ResultConstant.FOLDER_EXISTED;
        }
        folder.getFolders().put(parameter.getName(),
            new Folder(parameter.getName(), parameter.getPath(), folder.getDepth() + 1));
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        return ResultUtil.createResult("创建成功", new RepositoryInfo(repository));
    }

    /**
     * 移动文件
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID name 文件名 oldPath 原路径 newPath 新路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity moveFile(String token, MoveFile parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        Folder oldFolder = getFolder(repository, parameter.getOldPath());
        if (oldFolder == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        File file = oldFolder.getFiles().get(parameter.getName());
        if (file == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        Folder newFolder = getFolder(repository, parameter.getNewPath());
        if (newFolder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        if (newFolder.getFiles() == null) {
            newFolder.setFiles(new HashMap<>(1));
        }
        // 判断文件名是否被使用
        if (RepositoryUtil.nameIsExist(parameter.getName(), newFolder)) {
            return ResultConstant.FILE_EXISTED;
        }
        file.setPath(parameter.getNewPath());
        file.setChangeTime(System.currentTimeMillis());
        // 修改对应用户文件关系
        this.userFileDao.updateUserFilePath(file.getUserFileId(), parameter.getNewPath());
        oldFolder.getFiles().remove(file.getName());
        newFolder.getFiles().put(file.getName(), file);
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        return ResultUtil.createResult("创建成功", new RepositoryInfo(repository));
    }

    /**
     * 移动文件夹
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID name 文件名 oldPath 原路径 newPath 新路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity moveFolder(String token, MoveFolder parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        // 判断是否文件嵌套
        if (parameter.getNewPath().length() >= (parameter.getOldPath() + "/" + parameter.getName()).length()
            && (parameter.getOldPath() + "/" + parameter.getName()).equals(
                parameter.getNewPath().substring(0, (parameter.getOldPath() + "/" + parameter.getName()).length()))) {
            return ResultConstant.PATH_INVALID;
        }
        // 获取源文件父文件夹
        Folder oldFolder = getFolder(repository, parameter.getOldPath());
        if (oldFolder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        if (oldFolder.getFolders() == null) {
            oldFolder.setFolders(new HashMap<>(1));
        }
        // 获取源文件
        Folder folder = oldFolder.getFolders().get(parameter.getName());
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        // 获取新文件父文件夹
        Folder newFolder = getFolder(repository, parameter.getNewPath());
        if (newFolder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        if (newFolder.getFolders() == null) {
            newFolder.setFolders(new HashMap<>(1));
        }
        // 判断文件名是否被使用
        if (RepositoryUtil.nameIsExist(folder.getName(), newFolder)) {
            return ResultConstant.FOLDER_EXISTED;
        }
        LinkedList<File> files = new LinkedList<>();
        LinkedList<Folder> folders = new LinkedList<>();
        getFileList(folder, files);
        getFolderList(folder, folders);
        int depthDiffer = oldFolder.getDepth() - newFolder.getDepth();
        for (Folder temp : folders) {
            temp.setDepth(temp.getDepth() - depthDiffer);
            // TODO 配置文件
            // 判断是否超出最大深度
            if (temp.getDepth() >= 64) {
                return ResultConstant.FOLDER_DEPTH_TOO_BIG;
            }
            temp.setPath(temp.getPath().replaceFirst(parameter.getOldPath(), parameter.getNewPath()));
        }
        for (File file : files) {
            // 修改文件路径
            file.setPath(file.getPath().replaceFirst(parameter.getOldPath(), parameter.getNewPath()));
            // 修改对应用户文件关系
            this.userFileDao.updateUserFilePath(file.getUserFileId(), file.getPath());
        }
        // 修改文件夹深度
        folder.setDepth(folder.getDepth() - depthDiffer);
        // 修改文件夹路径
        folder.setPath(folder.getPath().replaceFirst(parameter.getOldPath(), parameter.getNewPath()));
        // 修改文件修改时间
        folder.setChangeTime(System.currentTimeMillis());
        // 移除源文件
        oldFolder.getFolders().remove(folder.getName());
        // 增加新文件
        newFolder.getFolders().put(parameter.getName(), folder);
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        return ResultUtil.createResult("移动成功", new RepositoryInfo(repository));
    }

    /**
     * 复制文件
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID name 文件名 oldPath 原路径 newPath 新路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity copyFile(String token, CopyFile parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        Folder oldFolder = getFolder(repository, parameter.getOldPath());
        if (oldFolder == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        File file = oldFolder.getFiles().get(parameter.getName());
        if (file == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        // 判断是否超出仓库大小
        if ((repository.getUseSize() + file.getSize()) > repository.getRepoSize()) {
            return ResultConstant.REPOSITORY_FULL;
        }
        Folder newFolder = getFolder(repository, parameter.getNewPath());
        if (newFolder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        if (newFolder.getFiles() == null) {
            newFolder.setFiles(new HashMap<>(1));
        }
        // 判断文件名是否被使用
        if (RepositoryUtil.nameIsExist(parameter.getName(), newFolder)) {
            return ResultConstant.FILE_EXISTED;
        }
        File newFile = file.clone();
        if (newFile == null) {
            return ResultConstant.SERVER_ERROR;
        }
        // 新增用户文件关系
        UserFile userFile = new UserFile(userId, newFile.getId(), parameter.getNewPath());
        this.userFileDao.insertUserFile(userFile);
        // 增加文件引用数量
        this.fileDao.fileQuoteNumberAdd(file.getId(), 1);
        // 将文件插入文件夹
        newFile.setPath(parameter.getNewPath());
        newFile.setUserFileId(userFile.getId());
        newFile.setChangeTime(System.currentTimeMillis());
        newFolder.getFiles().put(newFile.getName(), newFile);
        // 更新仓库已用空间大小
        repository.setUseSize(repository.getUseSize() + file.getSize());
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        this.userRepositoryDao.updateUseSizeById(repository.getId(), repository.getUseSize());
        return ResultUtil.createResult("复制成功", new RepositoryInfo(repository));
    }

    /**
     * 复制文件夹
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID name 文件名 oldPath 原路径 newPath 新路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity copyFolder(String token, CopyFolder parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        // 判断是否文件嵌套
        if (parameter.getNewPath().length() >= (parameter.getOldPath() + "/" + parameter.getName()).length()
            && (parameter.getOldPath() + "/" + parameter.getName()).equals(
                parameter.getNewPath().substring(0, (parameter.getOldPath() + "/" + parameter.getName()).length()))) {
            return ResultConstant.PATH_INVALID;
        }
        Folder oldParent = getFolder(repository, parameter.getOldPath());
        if (oldParent == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        if (oldParent.getFolders() == null) {
            oldParent.setFolders(new HashMap<>(1));
        }
        Folder folder = oldParent.getFolders().get(parameter.getName());
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        Folder newParent = getFolder(repository, parameter.getNewPath());
        if (newParent == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        if (newParent.getFolders() == null) {
            newParent.setFolders(new HashMap<>());
        }
        // 判断文件名是否被使用
        if (RepositoryUtil.nameIsExist(folder.getName(), newParent)) {
            return ResultConstant.FOLDER_EXISTED;
        }
        LinkedList<File> files = new LinkedList<>();
        LinkedList<Folder> folders = new LinkedList<>();
        Folder newFolder = folder.clone();
        getFileList(newFolder, files);
        getFolderList(newFolder, folders);
        long size = 0;
        int depthDiffer = oldParent.getDepth() - newParent.getDepth();
        for (Folder temp : folders) {
            temp.setDepth(temp.getDepth() - depthDiffer);
            // TODO 配置文件
            // 判断是否超出最大深度
            if (temp.getDepth() >= 64) {
                return ResultConstant.FOLDER_DEPTH_TOO_BIG;
            }
            temp.setPath(temp.getPath().replaceFirst(parameter.getOldPath(), parameter.getNewPath()));
        }
        for (File file : files) {
            size += file.getSize();
            file.setPath(file.getPath().replaceFirst(parameter.getOldPath(), parameter.getNewPath()));
            UserFile userFile = new UserFile(userId, file.getId(), parameter.getNewPath());
            this.userFileDao.insertUserFile(userFile);
            file.setUserFileId(userFile.getId());
            this.fileDao.fileQuoteNumberAdd(file.getId(), 1);
        }
        // 判断是否超出仓库大小
        if ((repository.getUseSize() + size) > repository.getRepoSize()) {
            return ResultConstant.REPOSITORY_FULL;
        }
        // 修改文件夹深度
        newFolder.setDepth(newFolder.getDepth() - depthDiffer);
        newFolder.setPath(folder.getPath().replaceFirst(parameter.getOldPath(), parameter.getNewPath()));
        newFolder.setChangeTime(System.currentTimeMillis());
        newParent.getFolders().put(parameter.getName(), newFolder);
        // 更新仓库已用空间大小
        repository.setUseSize(repository.getUseSize() + size);
        this.userRepositoryDao.updateUseSizeById(repository.getId(), repository.getUseSize());
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        return ResultUtil.createResult("复制成功", new RepositoryInfo(repository));
    }

    /**
     * 文件重命名
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID oldName 原名称 newName 新名称 path 路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity renameFile(String token, RenameFile parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        if (!ForbiddenUtil.isFileNameValid(parameter.getNewName())) {
            return ResultConstant.NAME_INVALID;
        }
        Folder folder = getFolder(repository, parameter.getPath());
        if (folder == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        if (folder.getFiles() == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        // 判断文件名是否被使用
        if (RepositoryUtil.nameIsExist(parameter.getNewName(), folder)) {
            return ResultConstant.FILE_EXISTED;
        }
        File file = folder.getFiles().get(parameter.getOldName());
        if (file == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        file.setName(parameter.getNewName());
        file.setChangeTime(System.currentTimeMillis());
        folder.getFiles().remove(parameter.getOldName());
        folder.getFiles().put(parameter.getNewName(), file);
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        return ResultUtil.createResult("修改成功", new RepositoryInfo(repository));
    }

    /**
     * 文件夹重命名
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID oldName 原名称 newName 新名称 path 路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity renameFolder(String token, RenameFolder parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        if (!ForbiddenUtil.isFileNameValid(parameter.getNewName())) {
            return ResultConstant.NAME_INVALID;
        }
        Folder parent = getFolder(repository, parameter.getPath());
        if (parent == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        if (parent.getFolders() == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        // 判断文件名是否已经被使用
        if (RepositoryUtil.nameIsExist(parameter.getNewName(), parent)) {
            return ResultConstant.FOLDER_EXISTED;
        }
        Folder folder = parent.getFolders().get(parameter.getOldName());
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        LinkedList<File> files = new LinkedList<>();
        LinkedList<Folder> folders = new LinkedList<>();
        getFileList(folder, files);
        getFolderList(folder, folders);
        for (File file : files) {
            file.setPath(file.getPath().replaceFirst(folder.getPath() + "/" + parameter.getOldName(),
                folder.getPath() + "/" + parameter.getNewName()));
            this.userFileDao.updateUserFilePath(file.getUserFileId(), file.getPath());
        }
        for (Folder temp : folders) {
            temp.setPath(temp.getPath().replaceFirst(folder.getPath() + "/" + parameter.getOldName(),
                folder.getPath() + "/" + parameter.getNewName()));
        }
        folder.setName(parameter.getNewName());
        folder.setChangeTime(System.currentTimeMillis());
        parent.getFolders().remove(parameter.getOldName());
        parent.getFolders().put(parameter.getNewName(), folder);
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        return ResultUtil.createResult("修改成功", new RepositoryInfo(repository));
    }

    /**
     * 删除文件
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID isFile 是否是文件 name 文件名 path 保存路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity deleteFromRepository(String token, DeleteFileToRecyclebin parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        Folder parent = getFolder(repository, parameter.getPath());
        if (parent == null) {
            return ResultConstant.FOLDER_EXISTED;
        }
        if (parameter.isFile()) {
            if (parent.getFiles() == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            File file = parent.getFiles().get(parameter.getName());
            if (file == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            String uuid = CommonUtil.getUuid();
            if (repository.getRecycleBin().getFiles() == null) {
                repository.getRecycleBin().setFiles(new HashMap<>(1));
            }
            repository.getRecycleBin().getFiles().put(uuid, file);
            parent.getFiles().remove(file.getName());
        } else {
            if (parent.getFolders() == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            Folder folder = parent.getFolders().get(parameter.getName());
            if (folder == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            String uuid = CommonUtil.getUuid();
            if (repository.getRecycleBin().getFolders() == null) {
                repository.getRecycleBin().setFolders(new HashMap<>(1));
            }
            repository.getRecycleBin().getFolders().put(uuid, folder);
            parent.getFolders().remove(folder.getName());
        }
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
        return ResultUtil.createResult("删除成功", new RepositoryInfo(repository));
    }

    /**
     * 恢复文件
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID isFile 是否是文件 recycleId 回收站ID
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity restoreFromRecycleBin(String token, RestoreFile parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        if (parameter.isFile()) {
            if (repository.getRecycleBin().getFiles() == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            File file = repository.getRecycleBin().getFiles().get(parameter.getRecycleId());
            if (file == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            Folder folder = getFolder(repository, file.getPath());
            if (folder == null) {
                return ResultConstant.FOLDER_NOT_EXISTED;
            }
            if (folder.getFiles() == null) {
                folder.setFiles(new HashMap<>(1));
            }
            if (RepositoryUtil.nameIsExist(file.getName(), folder)) {
                return ResultConstant.FILE_EXISTED;
            }
            folder.getFiles().put(file.getName(), file);
            repository.getRecycleBin().getFiles().remove(parameter.getRecycleId());
        } else {
            if (repository.getRecycleBin().getFolders() == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            Folder folder = repository.getRecycleBin().getFolders().get(parameter.getRecycleId());
            if (folder == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            Folder parent = getFolder(repository, folder.getPath());
            if (parent == null) {
                return ResultConstant.FOLDER_NOT_EXISTED;
            }
            if (parent.getFolders() == null) {
                parent.setFolders(new HashMap<>(1));
            }
            if (RepositoryUtil.nameIsExist(folder.getName(), parent)) {
                return ResultConstant.FOLDER_EXISTED;
            }
            parent.getFolders().put(folder.getName(), folder);
            repository.getRecycleBin().getFolders().remove(parameter.getRecycleId());
        }
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
        return ResultUtil.createResult("恢复成功", new RepositoryInfo(repository));
    }

    /**
     * 删除文件
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID isFile 是否是文件 recycleId 回收站ID
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity deleteFromRecycleBin(String token, DeleteFileFromRecyclebin parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        if (parameter.isFile()) {
            if (repository.getRecycleBin().getFiles() == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            File file = repository.getRecycleBin().getFiles().get(parameter.getRecycleId());
            if (file == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            if (file.getShareIdList() != null) {
                for (String shareId : file.getShareIdList()) {
                    this.shareDao.updateShareStatus(shareId, -1);
                }
            }
            repository.setUseSize(repository.getUseSize() - file.getSize());
            this.userFileDao.deleteUserFile(file.getUserFileId());
            this.fileDao.fileQuoteNumberSub(file.getId(), 1);
            repository.getRecycleBin().getFiles().remove(parameter.getRecycleId());
        } else {
            if (repository.getRecycleBin().getFolders() == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            Folder folder = repository.getRecycleBin().getFolders().get(parameter.getRecycleId());
            if (folder == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            LinkedList<File> files = new LinkedList<>();
            getFileList(folder, files);
            long size = 0;
            for (File file : files) {
                size += file.getSize();
                if (file.getShareIdList() != null) {
                    for (String shareId : file.getShareIdList()) {
                        this.shareDao.updateShareStatus(shareId, -1);
                    }
                }
                this.userFileDao.deleteUserFile(file.getUserFileId());
                this.fileDao.fileQuoteNumberSub(file.getId(), 1);
            }
            repository.setUseSize(repository.getUseSize() - size);
            repository.getRecycleBin().getFolders().remove(parameter.getRecycleId());
        }
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
        this.userRepositoryDao.updateUseSizeById(repository.getId(), repository.getUseSize());
        return ResultUtil.createResult("删除成功", new RepositoryInfo(repository));
    }

    /**
     * 删除文件
     *
     * @param token
     *            用户Token
     * @param parameter
     *            请求参数 repositoryId 仓库ID
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity cleanRecycleBin(String token, CleanRecyclebin parameter) {

        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(parameter.getRepositoryId())) {
            return ResultConstant.NO_ACCESS;
        }
        long size = 0;
        if (repository.getRecycleBin().getFiles() != null) {
            Collection<File> values = repository.getRecycleBin().getFiles().values();
            for (File file : values) {
                size += file.getSize();
                if (file.getShareIdList() != null) {
                    for (String shareId : file.getShareIdList()) {
                        this.shareDao.updateShareStatus(shareId, -1);
                    }
                }
                this.userFileDao.deleteUserFile(file.getUserFileId());
                this.fileDao.fileQuoteNumberSub(file.getId(), 1);
            }
            repository.getRecycleBin().getFiles().clear();
        }
        if (repository.getRecycleBin().getFolders() != null) {
            Collection<Folder> values = repository.getRecycleBin().getFolders().values();
            for (Folder folder : values) {
                LinkedList<File> files = new LinkedList<>();
                getFileList(folder, files);
                for (File file : files) {
                    size += file.getSize();
                    if (file.getShareIdList() != null) {
                        for (String shareId : file.getShareIdList()) {
                            this.shareDao.updateShareStatus(shareId, -1);
                        }
                    }
                    this.userFileDao.deleteUserFile(file.getUserFileId());
                    this.fileDao.fileQuoteNumberSub(file.getId(), 1);
                }
            }
            repository.getRecycleBin().getFolders().clear();
        }
        repository.setUseSize(repository.getUseSize() - size);
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
        this.userRepositoryDao.updateUseSizeById(repository.getId(), repository.getUseSize());
        return ResultUtil.createResult("删除成功", new RepositoryInfo(repository));
    }

    private Folder getFolder(UserRepository repository, String path) {
        String[] list = path.split("/");
        if (!"root".equals(list[1])) {
            return null;
        }
        Folder folder = repository.getFolder();
        for (int i = 2; folder != null && i < list.length; i++) {
            folder = folder.getFolders().get(list[i]);
        }
        return folder;
    }

    private void getFileList(Folder folder, Collection<File> files) {
        if (folder.getFiles() != null) {
            Collection<File> values = folder.getFiles().values();
            files.addAll(values);
        }
        if (folder.getFolders() != null) {
            Collection<Folder> values = folder.getFolders().values();
            for (Folder temp : values) {
                getFileList(temp, files);
            }
        }
    }

    private void getFolderList(Folder folder, Collection<Folder> folders) {
        if (folder.getFolders() != null) {
            Collection<Folder> values = folder.getFolders().values();
            folders.addAll(values);
            for (Folder temp : values) {
                getFolderList(temp, folders);
            }
        }
    }
}
