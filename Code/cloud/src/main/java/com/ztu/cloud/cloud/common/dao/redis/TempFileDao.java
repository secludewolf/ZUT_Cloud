package com.ztu.cloud.cloud.common.dao.redis;

import com.ztu.cloud.cloud.common.bean.redis.TempFile;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Jager
 * @description 临时文件
 * @date 2020/06/27-11:16
 **/
@Component
public class TempFileDao {
    RedisTemplate<String, String> redisTemplate;

    public TempFileDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public TempFile get(String fileMd5) {
        BoundHashOperations<String, Object, Object> operations = this.redisTemplate.boundHashOps(fileMd5);
        TempFile tempFile = new TempFile((String) operations.get("fileId"), (String) operations.get("name"), (String) operations.get("type"));
        operations.expire(1, TimeUnit.DAYS);
        String saves = (String) operations.get("saves");
        if (saves == null) {
            return null;
        }
        String[] split = saves.split(",");
        Set<Integer> indexs = new HashSet<>();
        if (saves.length() != 0) {
            for (String index : split) {
                indexs.add(Integer.parseInt(index));
            }
        }
        tempFile.setSaves(indexs);
        tempFile.setLength(Integer.parseInt((String) Objects.requireNonNull(operations.get("length"))));
        return tempFile;
    }

    public boolean insert(TempFile tempFile) {
        BoundHashOperations<String, Object, Object> operations = this.redisTemplate.boundHashOps(tempFile.getFileId());
        operations.put("fileId", tempFile.getFileId());
        operations.put("name", tempFile.getName());
        operations.put("type", tempFile.getType());
        operations.put("length", tempFile.getLength() + "");
        operations.expire(1, TimeUnit.DAYS);
        StringBuilder saves = new StringBuilder();
        if (tempFile.getSaves().size() > 0) {
            for (Integer index : tempFile.getSaves()) {
                saves.append(",").append(index);
            }
            saves = new StringBuilder(saves.substring(1, saves.length()));
        }
        operations.put("saves", saves.toString());
        return true;
    }

    public boolean update(TempFile tempFile) {
        BoundHashOperations<String, Object, Object> operations = this.redisTemplate.boundHashOps(tempFile.getFileId());
        operations.put("fileId", tempFile.getFileId());
        operations.put("name", tempFile.getName());
        operations.put("type", tempFile.getType());
        operations.put("length", tempFile.getLength() + "");
        operations.expire(1, TimeUnit.DAYS);
        StringBuilder saves = new StringBuilder();
        if (tempFile.getSaves().size() > 0) {
            for (Integer index : tempFile.getSaves()) {
                saves.append(",").append(index);
            }
            saves = new StringBuilder(saves.substring(1, saves.length()));
        }
        operations.put("saves", saves.toString());
        return true;
    }

    public boolean delete(String fileId) {
        return this.redisTemplate.delete(fileId);
    }
}
