package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mysql.File;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.dto.user.repository.PreviewFile;
import com.ztu.cloud.cloud.util.StoreUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * @author Jager
 * @description 文件预览业务实现
 * @date 2021/04/19-16:14
 **/
@Component
public class PreviewServiceImpl implements PreviewService {
    UserMapper userMapper;
    FileMapper fileMapper;
    StoreUtil storeUtil;
    List<String> photoTypeList;

    public PreviewServiceImpl(UserMapper userMapper, FileMapper fileMapper, StoreUtil storeUtil) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
        this.storeUtil = storeUtil;
        //TODO 配置文件获取
        this.photoTypeList = Arrays.asList("jpg", "jpeg", "gif", "png");
    }

    /**
     * 获取预览文件
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     * @return 预览信息
     */
    @Override
    public PreviewFile previewUserPhoto(String token, String repositoryId, String fileId) {
        //TODO 用户身份验证
        //TODO 用户文件权限验证
        //TODO 文件状态验证
        PreviewFile previewFile = new PreviewFile();
        File file = this.fileMapper.getFileById(fileId);
        if (file == null) {
            previewFile.setCode(0);
            previewFile.setMessage("文件不存在");
            return previewFile;
        }
        if (!this.photoTypeList.contains(file.getType().toLowerCase())) {
            previewFile.setCode(0);
            previewFile.setMessage("暂不支持" + file.getType() + "格式文件预览");
            return previewFile;
        }
        InputStream inputStream = this.storeUtil.getFile(file.getPath());
        if (inputStream == null) {
            previewFile.setCode(0);
            previewFile.setMessage("预览异常");
            return previewFile;
        }
        previewFile.setInputStream(inputStream);
        return previewFile;
    }
}
