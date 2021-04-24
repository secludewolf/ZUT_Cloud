package com.ztu.cloud.cloud.common.bean.mongodb;

import lombok.Data;

import java.util.Map;

/**
 * @author Jager
 * @description 用户通知关系文档
 * @date 2020/06/22-10:18
 **/
@Data
public class UserInform {
    private int id;
    private Map<String, Integer> status;

    public UserInform() {
    }

    public UserInform(int id, Map<String, Integer> status) {
        this.id = id;
        this.status = status;
    }
}
