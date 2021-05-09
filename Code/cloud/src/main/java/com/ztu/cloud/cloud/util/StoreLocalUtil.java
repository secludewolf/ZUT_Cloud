package com.ztu.cloud.cloud.util;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author Jager
 * @description 存储工具
 * @date 2020/06/27-10:28
 **/
@Component("local")
@ConditionalOnProperty(name = {"store.position"}, havingValue = "local", matchIfMissing = true)
public class StoreLocalUtil extends StoreUtil {

    public StoreLocalUtil() {
        super();
        System.out.println("本地存储模式!");
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    @Override
    public boolean checkExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取一个可用路径
     *
     * @return 可用路径
     */
    @Override
    public String getUsablePath() {
        File folder = new File("./SaveFolder");
        int f1 = 1;
        int f2 = 0;
        if (folder.isDirectory()) {
            File[] folders = folder.listFiles();
            for (File tempFolder : folders) {
                if (tempFolder.isDirectory()) {
                    File[] folders2 = tempFolder.listFiles();
                    for (File tempFile2 : folders2) {
                        if (tempFile2.isDirectory()) {
                            f2++;
                            String[] names = tempFile2.list();
                            int num = 0;
                            for (String name : names) {
                                num++;
                            }
                            if (num < 3) {
                                try {
                                    return tempFile2.getCanonicalPath() + "\\";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        f1 = f2 / 3 + 1;
        f2 = f2 % 3 + 1;
        folder = new File("./SaveFolder/" + f1 + "/" + f2);
        folder.mkdirs();
        return new File("./SaveFolder/" + f1 + "/" + f2).getAbsolutePath() + "/";
    }

    /**
     * 保存文件
     *
     * @param path        文件路径
     * @param inputStream 数据输入流
     * @return 保存结果
     */
    @Override
    public boolean storeFile(String path, InputStream inputStream) {
        File file = new File(path);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得文件流
     *
     * @param path 文件路径
     * @return 文件输出流
     */
    @Override
    public InputStream getFileInputStream(String path) {
        File file = new File(path);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹路径
     * @return 创建结果
     */
    @Override
    public boolean mkdir(String path) {
        File file = new File(path);
        return file.mkdirs();
    }

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @return 删除结果
     */
    @Override
    public boolean delete(String path) {
        File file = new File(path);
        return file.delete();
    }
}
