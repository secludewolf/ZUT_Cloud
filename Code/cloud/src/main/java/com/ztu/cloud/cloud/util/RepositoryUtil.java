package com.ztu.cloud.cloud.util;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.File;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jager
 * @description 仓库操作工具类
 * @date 2020/8/3-10:51
 **/
public class RepositoryUtil {
    /**
     * 判断文件是否存在
     *
     * @param name
     *            文件名
     * @param parent
     *            父文件夹
     * @return 是否存在
     */
    public static boolean nameIsExist(String name, Folder parent) {
        Set<String> names = new HashSet<>();
        if (parent.getFolders() != null) {
            names.addAll(parent.getFolders().keySet());
        }
        if (parent.getFiles() != null) {
            names.addAll(parent.getFiles().keySet());
        }
        for (String item : names) {
            if (item.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean pathIsNested(String source, String target) {
        return target.length() >= source.length() && source.equals(source.substring(0, target.length()));
    }

    public static Folder getFolder(UserRepository repository, String path) {
        String[] list = path.split("/");
        if (!"root".equals(list[1])) {
            return null;
        }
        Folder folder = repository.getFolder();
        for (int i = 2; folder != null && i < list.length; i++) {
            folder = folder.getFolders().get(list[i]);
        }
        assert folder != null;
        if (folder.getFiles() == null) {
            folder.setFiles(new HashMap<>(1));
        }
        if (folder.getFolders() == null) {
            folder.setFolders(new HashMap<>(1));
        }
        return folder;
    }

    public static void getFileList(Folder folder, Collection<File> files) {
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

    public static void getFolderList(Folder folder, Collection<Folder> folders) {
        if (folder.getFolders() != null) {
            Collection<Folder> values = folder.getFolders().values();
            folders.addAll(values);
            for (Folder temp : values) {
                getFolderList(temp, folders);
            }
        }
    }

    public static ResultResponseEntity repositoryIsInvalid(UserRepository repository, String id) {
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        if (repository.getStatus() != 1) {
            return ResultConstant.REPOSITORY_STATUS_ABNORMAL;
        }
        if (!repository.getId().equals(id)) {
            return ResultConstant.NO_ACCESS;
        }
        return null;
    }
}
