package com.ztu.cloud.cloud.common.vo.admin;

import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description DS图表数据
 * @date 2021/04/14-10:53
 **/
@Data
public class DsData {
    List<String> name;
    List<Integer> list;

    public DsData() {
    }

    public DsData(List<String> name, List<Integer> list) {
        this.name = name;
        this.list = list;
    }
}
