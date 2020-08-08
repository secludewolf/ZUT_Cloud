package com.ztu.cloud.cloud.common.vo.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonUserInform;
import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description 用户通知列表
 * @date 2020/8/8-16:09
 **/
@Data
public class UserInformList {
    List<CommonUserInform> informList;

    public UserInformList(List<CommonUserInform> informList) {
        this.informList = informList;
    }
}
