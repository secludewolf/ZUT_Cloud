package com.ztu.cloud.cloud.service.common;

import com.ztu.cloud.cloud.util.StoreUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Jager
 * @description 定时任务
 * @date 2021/04/22-16:07
 **/
@Component
@EnableScheduling
public class ScheduleTask {
    StoreUtil storeUtil;

    public ScheduleTask(StoreUtil storeUtil) {
        this.storeUtil = storeUtil;
    }

    /**
     * 间隔十分钟自动清理临时文件
     */
    @Scheduled(fixedRate = 10 * 60 * 1000)
    private void cleanTempFile() {
        System.out.println("清理临时文件");
        String[] names = this.storeUtil.getTempFileNames();
        for (String name : names) {
            System.out.println(name);
            long lastModifiedTime = this.storeUtil.getTempFileLastModifiedTime(name);
            if (System.currentTimeMillis() - lastModifiedTime > 2 * 60 * 60 * 1000) {
                this.storeUtil.deleteTempFile(name);
            }
        }
    }
}
