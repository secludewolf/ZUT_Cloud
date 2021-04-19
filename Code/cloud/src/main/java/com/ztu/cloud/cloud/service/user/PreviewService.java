package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.dto.user.repository.PreviewFile;

/**
 * @author Jager
 * @description 预览文件业务接口
 * @date 2021/04/19-15:59
 **/
public interface PreviewService {
    /**
     * 获取预览文件
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     * @return 预览信息
     */
    PreviewFile previewUserPhoto(String token, String repositoryId, String fileId);
}
