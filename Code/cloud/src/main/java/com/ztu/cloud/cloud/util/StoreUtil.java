package com.ztu.cloud.cloud.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * @author Jager
 * @description 存储工具
 * @date 2020/06/27-10:28
 **/
@Component
public class StoreUtil {
    private String url;
    private String port;
    private String head;
    private String hdfsUrl;
    private String user;
    private Configuration configuration;
    private FileSystem fileSystem;

    public StoreUtil(@Value("${hadoop.url}") String url, @Value("${hadoop.hdfs.head}") String head, @Value("${hadoop.hdfs.port}") String port, @Value("${hadoop.hdfs.user}") String user) throws URISyntaxException, IOException, InterruptedException {
        this.url = url;
        this.head = head;
        this.port = port;
        this.hdfsUrl = head + "://" + url + ":" + port;
        this.user = user;
        // this.configuration = new Configuration();
        // this.configuration.set("fs.defaultFS", hdfsUrl);
        // this.fileSystem = FileSystem.get(new URI(hdfsUrl), this.configuration, this.user);
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public boolean checkExist(String path) {
        File file = new File(path);
        return file.exists();
        // try {
        // 	return this.fileSystem.exists(new Path(path));
        // } catch (IOException e) {
        // 	e.printStackTrace();
        // 	return false;
        // }
    }

    /**
     * 获取一个可用路径
     *
     * @return 可用路径
     */
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
        //TODO mkdir
        return new File("./SaveFolder/" + f1 + "/" + f2).getAbsolutePath() + "/";
    }

    //TODO 检测文件是否存在

    /**
     * 保存文件
     *
     * @param path        文件路径
     * @param inputStream 数据输入流
     * @return 保存结果
     */
    public boolean storeFile(String path, InputStream inputStream) {
        File file = new File(path);
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // try {
        // 	FSDataOutputStream fsDataOutputStream = this.fileSystem.create(new Path(path));
        // 	byte[] buffer = new byte[1024];
        // 	while (true) {
        // 		int bytesRead = inputStream.read(buffer);
        // 		if (bytesRead <= 0) {
        // 			break;
        // 		}
        // 		fsDataOutputStream.write(buffer, 0, bytesRead);
        // 	}
        // 	inputStream.close();
        // 	fsDataOutputStream.close();
        // 	return true;
        // } catch (IOException e) {
        // 	e.printStackTrace();
        // 	return false;
        // }
    }
    //TODO 检测文件是否存在 防止冲突

    /**
     * 获得文件流
     *
     * @param path 文件路径
     * @return 文件输出流
     */
    public InputStream getFileInputStream(String path) {
        File file = new File(path);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
        // return this.fileSystem.open(new Path(path));
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹路径
     * @return 创建结果
     */
    public boolean mkdir(String path) {
        File file = new File(path);
        return file.mkdirs();
        // try {
        // 	return this.fileSystem.mkdirs(new Path(path));
        // } catch (IOException e) {
        // 	e.printStackTrace();
        // }
    }

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @return 删除结果
     */
    public boolean delete(String path) {
        File file = new File(path);
        return file.delete();
        // try {
        // 	return this.fileSystem.delete(new Path(path), true);
        // } catch (IOException e) {
        // 	e.printStackTrace();
        // }
    }

    public boolean checkTempFile(String name) {
        String path = "./CacheFolder/";
        File file = new File(path + name);
        return file.exists();
    }

    public boolean checkTempFile(String[] names) {
        for (String name : names) {
            if (!checkTempFile(name)) {
                return false;
            }
        }
        return true;
    }

    public void storeTempFile(String name, InputStream inputStream) {
        //TODO 应该通过配置文件设置临时存储位置
        //临时存储路径
        String path = "./CacheFolder/";
        //TODO 应该用过配置文件设置临时存储空间大小
        // 1GB
        // int maxSize = 1024 * 1024 * 1024;
        // if (getTempFolderSize() > maxSize) {
        // 	return false;
        // }
        File file = new File(path + name);
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

    public File getTempFile(String name) {
        String path = "./CacheFolder/";
        File file = new File(path + name);
        if (file.exists()) {
            //刷新文件修改时间
            file.getAbsoluteFile().setLastModified(System.currentTimeMillis());
            return file;
        }
        return null;
    }

    public long getTempFileLastModifiedTime(String name) {
        String path = "./CacheFolder/";
        File file = new File(path + name);
        if (file.exists()) {
            return file.getAbsoluteFile().lastModified();
        }
        return -1;
    }

    public boolean flashTempFileLastModifiedTime(String name) {
        String path = "./CacheFolder/";
        File file = new File(path + name);
        if (file.exists()) {
            return file.getAbsoluteFile().setLastModified(System.currentTimeMillis());
        }
        return false;
    }

    public String[] getTempFileNames() {
        String path = "./CacheFolder/";
        File folder = new File(path);
        return folder.list();
    }

    public boolean uploadTempFile(String path, String name) {
        String tempPath = "./CacheFolder/";
        File file = new File(tempPath + name);
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

    public long mergeTempFile(String[] names, String md5) {
        String path = "./CacheFolder/";
        //TODO MD5 验证
        File file = new File(path + md5);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            for (String name : names) {
                File temp = new File(path + name);
                if (temp.exists()) {
                    InputStream inputStream = new FileInputStream(temp);
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead <= 0) {
                            break;
                        }
                        outputStream.write(buffer, 0, bytesRead);
                    }
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

    public void deleteTempFiles(String[] names) {
        for (String name : names) {
            deleteTempFile(name);
        }
    }

    @Async
    public void deleteTempFile(String name) {
        String path = "./CacheFolder/";
        File file = new File(path + name);
        if (file.exists()) {
            file.delete();
        }
    }

    public long getTempFolderSize() {
        //TODO 应该通过配置文件设置临时存储空间
        String path = "./CacheFolder/";
        long size = 0;
        File folder = new File(path);
        if (folder.isDirectory()) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                size += file.length();
            }
        }
        return size;
    }
}
