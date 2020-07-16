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

    public ShareList(List<Share> shareList) {
        this.shareList = shareList;
    }
}
