package com.ztu.cloud.cloud.common.bean.redis;

import lombok.Data;

/**
 * @author Jager
 * @description 下载信息
 * @date 2020/06/27-11:02
 **/
@Data
public class Download {
    private String id;
    private String fileId;
    private String shareId;
    private String name;
}
