package com.ztu.cloud.cloud.common.vo.user;

import com.ztu.cloud.cloud.common.bean.mysql.Share;
import lombok.Data;

/**
 * @author Jager
 * @description 分享仓库信息
 * @date 2020/06/28-11:16
 **/
@Data
public class ShareRepository {
    private Share share;
    private com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository shareRepository;

    public ShareRepository(Share share, com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository shareRepository) {
        this.share = share;
        this.shareRepository = shareRepository;
    }
}
