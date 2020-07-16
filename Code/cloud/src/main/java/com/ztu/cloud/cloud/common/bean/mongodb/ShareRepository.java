package com.ztu.cloud.cloud.common.bean.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jager
 * @description 分享仓库信息文档
 * @date 2020/06/22-10:17
 **/
@Data
public class ShareRepository {
    private ObjectId id;
    private String shareId;
    private int userId;
    private List<Long> userFileIdList;
    private List<String> fileIdList;
    private Map<Integer, List<Long>> saveUserIdList;
    private Map<Integer, List<Long>> downloadUserIdList;
    private Folder folder;

    public ShareRepository() {
    }

    public ShareRepository(String shareId, int userId, List<Long> userFileIdList, List<String> fileIdList, Map<Integer, List<Long>> saveUserIdList, Map<Integer, List<Long>> downloadUserIdList, Folder folder) {
        this.shareId = shareId;
        this.userId = userId;
        this.userFileIdList = userFileIdList;
        this.fileIdList = fileIdList;
        this.saveUserIdList = saveUserIdList;
        this.downloadUserIdList = downloadUserIdList;
        this.folder = folder;
    }

    public ShareRepository(String shareId, int userId, Folder folder) {
        this.shareId = shareId;
        this.userId = userId;
        this.folder = folder;
        this.userFileIdList = new LinkedList<>();
        this.fileIdList = new LinkedList<>();
        this.saveUserIdList = new HashMap<>();
        this.downloadUserIdList = new HashMap<>();
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }
}
