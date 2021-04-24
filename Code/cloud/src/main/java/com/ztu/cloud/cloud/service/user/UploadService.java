package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jager
 * @description 上传业务接口
 * @date 2020/06/27-09:33
 **/
public interface UploadService {
    /**
     * 上传文件
     *
     * @param token    用户Token
     * @param block    文件块
     * @param fileName 文件名
     * @param blockMd5 文件块MD5
     * @param fileMd5  文件MD5
     * @param index    文件块号
     * @param length   文件总块数
     * @return 已接收的文件块号
     */
    ResultResponseEntity upload(String token, String fileName, MultipartFile block, String blockMd5, String fileMd5,
                                Integer index, Integer length);
}
