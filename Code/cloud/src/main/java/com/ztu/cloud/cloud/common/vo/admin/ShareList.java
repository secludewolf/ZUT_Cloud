package com.ztu.cloud.cloud.common.vo.admin;

import com.ztu.cloud.cloud.common.bean.mysql.Share;
import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description 分享信息
 * @date 2020/06/28-10:50
 **/
@Data
public class ShareList {
    private List<Share> shareList;
    private long shareCount;

    public ShareList(long shareCount, List<Share> shareList) {
        this.shareCount = shareCount;
        this.shareList = shareList;
    }
}
