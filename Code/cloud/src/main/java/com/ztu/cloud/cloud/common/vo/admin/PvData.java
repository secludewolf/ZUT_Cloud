package com.ztu.cloud.cloud.common.vo.admin;

import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description PV图表数据
 * @date 2021/04/14-10:53
 **/
@Data
public class PvData {
    List<String> name;
    List<Integer> list;

    public PvData() {
    }

    public PvData(List<String> name, List<Integer> list) {
        this.name = name;
        this.list = list;
    }
}
