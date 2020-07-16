package com.ztu.cloud.cloud.common.vo.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonAdminInform;
import lombok.Data;

/**
 * @author Jager
 * @description 通知信息
 * @date 2020/06/28-17:20
 **/
@Data
public class Inform {
    CommonAdminInform inform;

    public Inform(CommonAdminInform inform) {
        this.inform = inform;
    }
}
