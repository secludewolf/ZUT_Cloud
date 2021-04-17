package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.File;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.FileReportMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserFileMapper;
import com.ztu.cloud.cloud.common.dto.user.repository.*;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.user.RepositoryInfo;
import com.ztu.cloud.cloud.util.CommonUtil;
import com.ztu.cloud.cloud.util.RepositoryUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

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
    FileReportMapper fileReportMapper;

    public RepositoryServiceImpl(UserRepositoryDao userRepositoryDao, UserFileMapper userFileDao, FileMapper fileDao, ShareMapper shareDao, FileReportMapper fileReportMapper) {
        this.userRepositoryDao = userRepositoryDao;
        this.userFileDao = userFileDao;
        this.fileDao = fileDao;
        this.shareDao = shareDao;
        this.fileReportMapper = fileReportMapper;
    }

    /**
     * 获取仓库信息
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity getRepository(String token, String repositoryId) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, repositoryId);
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        return ResultUtil.createResult("查询成功", new RepositoryInfo(repository));
    }

    /**
     * 创建文件
     *
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID fileId 文件ID name 文件名 path 保存路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity createFile(String token, CreateFile parameter) {
        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
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
        Folder folder = RepositoryUtil.getFolder(repository, parameter.getPath());
        // 判断目标路径是否存在
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID name 文件名 path 保存路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity createFolder(String token, CreateFolder parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        Folder folder = RepositoryUtil.getFolder(repository, parameter.getPath());
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID name 文件名 oldPath 原路径 newPath 新路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity moveFile(String token, MoveFile parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        Folder oldFolder = RepositoryUtil.getFolder(repository, parameter.getOldPath());
        // 判断判断源文件是否存在
        if (oldFolder == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        File file = oldFolder.getFiles().get(parameter.getName());
        // 判断源文件是否存在
        if (file == null) {
            return ResultConstant.FILE_NOT_EXISTED;
        }
        Folder newFolder = RepositoryUtil.getFolder(repository, parameter.getNewPath());
        // 判断目标文件夹是否存在
        if (newFolder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID name 文件名 oldPath 原路径 newPath 新路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity moveFolder(String token, MoveFolder parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        // 判断是否文件嵌套
        if (RepositoryUtil.pathIsNested(parameter.getOldPath() + "/" + parameter.getName(), parameter.getNewPath())) {
            return ResultConstant.PATH_INVALID;
        }
        // 获取源文件夹的父文件夹
        Folder oldFolder = RepositoryUtil.getFolder(repository, parameter.getOldPath());
        if (oldFolder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        // 获取源文件夹
        Folder folder = oldFolder.getFolders().get(parameter.getName());
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        // 获取新文件父文件夹
        Folder newFolder = RepositoryUtil.getFolder(repository, parameter.getNewPath());
        if (newFolder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        // 判断文件名是否被使用
        if (RepositoryUtil.nameIsExist(folder.getName(), newFolder)) {
            return ResultConstant.FOLDER_EXISTED;
        }
        LinkedList<File> files = new LinkedList<>();
        LinkedList<Folder> folders = new LinkedList<>();
        RepositoryUtil.getFileList(folder, files);
        RepositoryUtil.getFolderList(folder, folders);
        int depthDiffer = oldFolder.getDepth() - newFolder.getDepth();
        for (Folder temp : folders) {
            temp.setDepth(temp.getDepth() - depthDiffer);
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID name 文件名 oldPath 原路径 newPath 新路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity copyFile(String token, CopyFile parameter) {
        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        Folder oldFolder = RepositoryUtil.getFolder(repository, parameter.getOldPath());
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
        Folder newFolder = RepositoryUtil.getFolder(repository, parameter.getNewPath());
        if (newFolder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID name 文件名 oldPath 原路径 newPath 新路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity copyFolder(String token, CopyFolder parameter) {
        int userId = TokenUtil.getId(token);
        UserRepository repository = this.userRepositoryDao.getByUserId(userId);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        // 判断是否文件嵌套
        if (RepositoryUtil.pathIsNested(parameter.getOldPath() + "/" + parameter.getName(), parameter.getNewPath())) {
            return ResultConstant.PATH_INVALID;
        }
        Folder oldParent = RepositoryUtil.getFolder(repository, parameter.getOldPath());
        if (oldParent == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        Folder folder = oldParent.getFolders().get(parameter.getName());
        if (folder == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        Folder newParent = RepositoryUtil.getFolder(repository, parameter.getNewPath());
        if (newParent == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        // 判断文件名是否被使用
        if (RepositoryUtil.nameIsExist(folder.getName(), newParent)) {
            return ResultConstant.FOLDER_EXISTED;
        }
        LinkedList<File> files = new LinkedList<>();
        LinkedList<Folder> folders = new LinkedList<>();
        Folder newFolder = folder.clone();
        RepositoryUtil.getFileList(newFolder, files);
        RepositoryUtil.getFolderList(newFolder, folders);
        long size = 0;
        int depthDiffer = oldParent.getDepth() - newParent.getDepth();
        for (Folder temp : folders) {
            temp.setDepth(temp.getDepth() - depthDiffer);
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID oldName 原名称 newName 新名称 path 路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity renameFile(String token, RenameFile parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        Folder folder = RepositoryUtil.getFolder(repository, parameter.getPath());
        if (folder == null) {
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID oldName 原名称 newName 新名称 path 路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity renameFolder(String token, RenameFolder parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        Folder parent = RepositoryUtil.getFolder(repository, parameter.getPath());
        if (parent == null) {
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
        RepositoryUtil.getFileList(folder, files);
        RepositoryUtil.getFolderList(folder, folders);
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID isFile 是否是文件 name 文件名 path 保存路径
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity deleteFromRepository(String token, DeleteFileToRecyclebin parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        System.out.println(1);
        Folder parent = RepositoryUtil.getFolder(repository, parameter.getPath());
        if (parent == null) {
            return ResultConstant.FOLDER_NOT_EXISTED;
        }
        System.out.println(2);
        if (parameter.getIsFile()) {
            System.out.println(3);
            if (parent.getFiles() == null) {
                return ResultConstant.FILE_NOT_EXISTED;
            }
            System.out.println(4);
            File file = parent.getFiles().get(parameter.getName());
            if (file == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            System.out.println(5);
            String uuid = CommonUtil.getUuid();
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
            repository.getRecycleBin().getFolders().put(uuid, folder);
            parent.getFolders().remove(folder.getName());
        }
        System.out.println(10);
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
        return ResultUtil.createResult("删除成功", new RepositoryInfo(repository));
    }

    /**
     * 恢复文件
     *
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID isFile 是否是文件 recycleId 回收站ID
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity restoreFromRecycleBin(String token, RestoreFile parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        if (parameter.getIsFile()) {
            if (repository.getRecycleBin().getFiles() == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            File file = repository.getRecycleBin().getFiles().get(parameter.getRecycleId());
            if (file == null) {
                return ResultConstant.TARGET_NOT_EXISTED;
            }
            Folder folder = RepositoryUtil.getFolder(repository, file.getPath());
            if (folder == null) {
                return ResultConstant.FOLDER_NOT_EXISTED;
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
            Folder parent = RepositoryUtil.getFolder(repository, folder.getPath());
            if (parent == null) {
                return ResultConstant.FOLDER_NOT_EXISTED;
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID isFile 是否是文件 recycleId 回收站ID
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity deleteFromRecycleBin(String token, DeleteFileFromRecyclebin parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        if (parameter.getIsFile()) {
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
            RepositoryUtil.getFileList(folder, files);
            long size = 0;
            size = deleteFile(files, size);
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
     * @param token     用户Token
     * @param parameter 请求参数 repositoryId 仓库ID
     * @return 仓库信息
     */
    @Override
    public ResultResponseEntity cleanRecycleBin(String token, CleanRecyclebin parameter) {
        UserRepository repository = getRepositoryByToken(token);
        ResultResponseEntity result = RepositoryUtil.repositoryIsInvalid(repository, parameter.getRepositoryId());
        // 判断仓库状态是否正常
        if (result != null) {
            return result;
        }
        long size = 0;
        if (repository.getRecycleBin().getFiles() != null) {
            Collection<File> values = repository.getRecycleBin().getFiles().values();
            size = deleteFile(values, size);
            repository.getRecycleBin().getFiles().clear();
        }
        if (repository.getRecycleBin().getFolders() != null) {
            Collection<Folder> values = repository.getRecycleBin().getFolders().values();
            for (Folder folder : values) {
                LinkedList<File> files = new LinkedList<>();
                RepositoryUtil.getFileList(folder, files);
                size = deleteFile(files, size);
            }
            repository.getRecycleBin().getFolders().clear();
        }
        repository.setUseSize(repository.getUseSize() - size);
        this.userRepositoryDao.updateFolderById(repository.getId(), repository.getFolder());
        this.userRepositoryDao.updateRecycleBinById(repository.getId(), repository.getRecycleBin());
        this.userRepositoryDao.updateUseSizeById(repository.getId(), repository.getUseSize());
        return ResultUtil.createResult("删除成功", new RepositoryInfo(repository));
    }

    /**
     * 举报文件
     *
     * @param token     用户Token
     * @param parameter 请求参数 shareId 文件ID type 举报类型 content 举报内容
     * @return 举报结果
     */
    @Override
    public ResultResponseEntity fileReport(String token, FileReport parameter) {
        int userId = TokenUtil.getId(token);
        this.fileReportMapper.insertFileReport(new com.ztu.cloud.cloud.common.bean.mysql.FileReport(parameter, userId));
        return ResultUtil.createResult(1, "举报成功");
    }

    private long deleteFile(Collection<File> files, long size) {
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
        return size;
    }

    private UserRepository getRepositoryByToken(String token) {
        int userId = TokenUtil.getId(token);
        return this.userRepositoryDao.getByUserId(userId);
    }

}
