package com.ztu.cloud.cloud.util;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Jager
 * @description 存储工具
 * @date 2020/06/27-10:28
 **/
@Component("hdfs")
@ConditionalOnProperty(name = {"store.position"}, havingValue = "hdfs", matchIfMissing = true)
public class StoreHdfsUtil extends StoreUtil {
    private final FileSystem fileSystem;

    public StoreHdfsUtil(@Value("${hadoop.url}") String url, @Value("${hadoop.hdfs.head}") String head, @Value("${hadoop.hdfs.port}") String port, @Value("${hadoop.hdfs.user}") String user) throws URISyntaxException, IOException, InterruptedException {
        super();
        System.out.println("HDFS存储模式!");
        String hdfsUrl = head + "://" + url + ":" + port;
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUrl);
        this.fileSystem = FileSystem.get(new URI(hdfsUrl), configuration, user);
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    @Override
    public boolean checkExist(String path) {
        try {
            return this.fileSystem.exists(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取一个可用路径
     *
     * @return 可用路径
     */
    @Override
    public String getUsablePath() {
        try {
            Path savePath = new Path("/");
            FileStatus[] fileStatuses = this.fileSystem.listStatus(savePath);
            int f1 = 1;
            int f2 = 0;
            for (FileStatus fileStatus : fileStatuses) {
                if (fileStatus.isDirectory()) {
                    FileStatus[] fileStatuses2 = this.fileSystem.listStatus(fileStatus.getPath());
                    for (FileStatus status : fileStatuses2) {
                        f2++;
                        if (status.isDirectory() && this.fileSystem.listStatus(status.getPath()).length < 3) {
                            return status.getPath().toString() + "\\";
                        }
                    }
                }
            }
            f1 = f2 / 3 + 1;
            f2 = f2 % 3 + 1;
            this.fileSystem.mkdirs(new Path("/" + f1 + "/" + f2));
            return "/" + f1 + "/" + f2 + "/";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/1/1/";
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
        try {
            FSDataOutputStream fsDataOutputStream = this.fileSystem.create(new Path(path));
            IOUtils.copy(inputStream, fsDataOutputStream);
            inputStream.close();
            fsDataOutputStream.close();
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
        try {
            return this.fileSystem.open(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹路径
     * @return 创建结果
     */
    @Override
    public boolean mkdir(String path) {
        try {
            return this.fileSystem.mkdirs(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @return 删除结果
     */
    @Override
    public boolean delete(String path) {
        try {
            return this.fileSystem.delete(new Path(path), true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
