package com.ztu.cloud.cloud.common.bean.mongodb.inside;

import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jager
 * @description 文件夹信息
 * @date 2020/06/22-10:22
 **/
@Data
public class Folder implements Cloneable {
    private String name;
    private String path;
    private int depth;
    private long createTime;
    private long changeTime;
    private int status;
    private HashMap<String, Folder> folders;
    private HashMap<String, File> files;

    public Folder() {
    }

    public Folder(String name, String path, int depth) {
        this.name = name;
        this.path = path;
        this.depth = depth;
        this.createTime = System.currentTimeMillis();
        this.changeTime = this.createTime;
        this.status = 1;
        this.folders = new HashMap<>(0);
        this.files = new HashMap<>(0);
    }

    public Folder(Folder folder) {
        this.name = folder.getName();
        this.path = folder.path;
        this.depth = folder.depth;
        this.createTime = folder.createTime;
        this.changeTime = folder.changeTime;
        this.status = folder.status;
        this.folders = new HashMap<>();
        if (folder.getFolders() != null) {
            Collection<Folder> values = folder.getFolders().values();
            for (Folder temp : values) {
                this.folders.put(temp.getName(), temp.clone());
            }
        }
        this.files = new HashMap<>();
        if (folder.getFiles() != null) {
            Collection<File> values = folder.getFiles().values();
            for (File temp : values) {
                this.files.put(temp.getName(), temp.clone());
            }
        }
    }

    public long getSize() {
        return getSize(this);
    }

    private long getSize(Folder parent) {
        long size = 0;
        if (parent.getFiles() != null) {
            for (File file : parent.getFiles().values()) {
                size += file.getSize();
            }
        }
        if (parent.getFolders() != null) {
            for (Folder folder : parent.getFolders().values()) {
                size += getSize(folder);
            }
        }
        return size;
    }

    @Override
    public Folder clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Folder(this);
    }
}
