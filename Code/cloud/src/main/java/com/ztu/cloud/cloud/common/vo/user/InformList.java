package com.ztu.cloud.cloud.common.vo.user;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonUserInform;
import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description 通知列表
 * @date 2020/06/28-17:14
 **/
@Data
public class InformList {
    List<CommonUserInform> informList;

    public InformList(List<CommonUserInform> informList) {
        this.informList = informList;
    }
}
