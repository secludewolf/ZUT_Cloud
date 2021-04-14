package com.ztu.cloud.cloud.common.vo.admin;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jager
 * @description Mouth图表数据
 * @date 2021/04/14-10:53
 **/
@Data
public class MouthData {
    public class Data {
        String name;
        String type = "line";
        String stack = "总量";
        List<Long> data = new LinkedList<>();

        public Data() {
            this.data.add(0L);
            this.data.add(0L);
            this.data.add(0L);
            this.data.add(0L);
            this.data.add(0L);
            this.data.add(0L);
            this.data.add(0L);
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getStack() {
            return stack;
        }

        public List<Long> getData() {
            return data;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setStack(String stack) {
            this.stack = stack;
        }

        public void setData(List<Long> data) {
            this.data = data;
        }
    }

    List<String> legend;
    List<String> name;
    List<Data> list;


    public MouthData(List<String> legend, List<String> name, List<Data> list) {
        this.legend = legend;
        this.name = name;
        this.list = list;
    }
}
