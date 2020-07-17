package com.ztu.cloud.cloud.common.bean.mongodb.inside;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;

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
		this.folders = null;
		this.files = null;
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

	public Folder(JSONObject json) {
		//TODO 根据JSON创建
	}

	@Override
	public Folder clone() {
		return new Folder(this);
	}
}
