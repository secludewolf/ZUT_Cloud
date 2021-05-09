package com.ztu.cloud.cloud.util;

import org.apache.commons.io.IOUtils;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.util.Objects;

/**
 * @author Jager
 * @description 存储工具接口
 * @date 2021/05/09-14:55
 **/
public abstract class StoreUtil {
    String tempFolderPath;

    public StoreUtil() {
        this.tempFolderPath = "./CacheFolder/";
        File file = new File(this.tempFolderPath);
        if (file.exists()) {
            if (!file.isDirectory()) {
                System.out.println("存储重名文件阻碍项目初始化!");
            }
        } else {
            file.mkdirs();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public abstract boolean checkExist(String path);

    /**
     * 获取一个可用路径
     *
     * @return 可用路径
     */
    public abstract String getUsablePath();

    /**
     * 保存文件
     *
     * @param path        文件路径
     * @param inputStream 数据输入流
     * @return 保存结果
     */
    public abstract boolean storeFile(String path, InputStream inputStream);

    /**
     * 获得文件流
     *
     * @param path 文件路径
     * @return 文件输出流
     */
    public abstract InputStream getFileInputStream(String path);

    /**
     * 创建文件夹
     *
     * @param path 文件夹路径
     * @return 创建结果
     */
    public abstract boolean mkdir(String path);

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @return 删除结果
     */
    public abstract boolean delete(String path);

    /**
     * 判断临时文件是否存在
     *
     * @param name 文件名称
     * @return 是否存在
     */
    public boolean checkTempFile(String name) {
        File file = new File(this.tempFolderPath + name);
        return file.exists();
    }

    /**
     * 判断临时文件是否存在
     *
     * @param names 文件名称数组
     * @return 文件是否存在
     */
    public boolean checkTempFile(String[] names) {
        for (String name : names) {
            if (!checkTempFile(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 存储临时文件
     *
     * @param name        文件名称
     * @param inputStream 文件流
     */
    public void storeTempFile(String name, InputStream inputStream) {
        //临时存储路径
        File file = new File(this.tempFolderPath + name);
        if (file.exists()) {
            return;
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (true) {
                int bytesRead = inputStream.read(buffer);
                if (bytesRead <= 0) {
                    break;
                }
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取临时文件
     *
     * @param name 文件名称
     * @return 文件
     */
    public File getTempFile(String name) {
        File file = new File(this.tempFolderPath + name);
        if (file.exists()) {
            //刷新文件修改时间
            file.getAbsoluteFile().setLastModified(System.currentTimeMillis());
            return file;
        }
        return null;
    }

    /**
     * 获得临时文件上次访问时间
     *
     * @param name 文件名称
     * @return 访问时间
     */
    public long getTempFileLastModifiedTime(String name) {
        File file = new File(this.tempFolderPath + name);
        if (file.exists()) {
            return file.getAbsoluteFile().lastModified();
        }
        return -1;
    }

    /**
     * 刷新临时文件访问时间
     *
     * @param name 文件名称
     * @return 是否刷新成功
     */
    public boolean flashTempFileLastModifiedTime(String name) {
        File file = new File(this.tempFolderPath + name);
        if (file.exists()) {
            return file.getAbsoluteFile().setLastModified(System.currentTimeMillis());
        }
        return false;
    }

    /**
     * 获取所有临时文件名称
     *
     * @return 临时文件名称数组
     */
    public String[] getTempFileNames() {
        File folder = new File(this.tempFolderPath);
        return folder.list();
    }

    /**
     * 上传临时文件为永久文件
     *
     * @param path 文件存储位置
     * @param name 文件名称
     * @return 是否存储成功
     */
    public boolean uploadTempFile(String path, String name) {
        File file = new File(this.tempFolderPath + name);
        if (file.exists()) {
            try {
                return storeFile(path, new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 合并临时文件
     *
     * @param names 临时文件名称数组
     * @param md5   文件MD5
     * @return 文件大小
     */
    public long mergeTempFile(String[] names, String md5) {
        //TODO MD5 验证
        File file = new File(this.tempFolderPath + md5);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            for (String name : names) {
                File temp = new File(this.tempFolderPath + name);
                if (temp.exists()) {
                    InputStream inputStream = new FileInputStream(temp);
                    IOUtils.copy(inputStream, outputStream);
                    inputStream.close();
                } else {
                    outputStream.close();
                    return -1;
                }
                deleteTempFile(name);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return file.length();
    }

    /**
     * 删除临时文件
     *
     * @param names 文件名称数组
     */
    public void deleteTempFiles(String[] names) {
        for (String name : names) {
            deleteTempFile(name);
        }
    }

    /**
     * 删除临时文件
     *
     * @param name 文件名
     */
    @Async
    public void deleteTempFile(String name) {
        File file = new File(this.tempFolderPath + name);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取临时文件夹大小
     *
     * @return 文件大小
     */
    public long getTempFolderSize() {
        long size = 0;
        File folder = new File(this.tempFolderPath);
        if (folder.isDirectory()) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                size += file.length();
            }
        }
        return size;
    }
}
