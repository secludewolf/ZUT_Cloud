package com.ztu.cloud.cloud.common.vo.admin;

import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description Mouth图表数据
 * @date 2021/04/14-10:53
 **/
@Data
public class DownloadData {
    List<Data> list;
    long total = 0;

    public class Data {
        int value;
        String name;

        public Data(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
