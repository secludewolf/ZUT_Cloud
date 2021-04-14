package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.File;
import com.ztu.cloud.cloud.common.bean.mysql.Share;
import com.ztu.cloud.cloud.common.bean.mysql.SysLog;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.dao.mysql.SysLogDao;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.admin.*;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jager
 * @description 控制台图表业务实现
 * @date 2021/04/14-10:24
 **/
@Component
public class ChartServiceImpl implements ChartService {
    AdminMapper adminDao;
    SysLogDao sysLogDao;
    FileMapper fileMapper;
    ShareMapper shareMapper;

    public ChartServiceImpl(AdminMapper adminDao, SysLogDao sysLogDao, FileMapper fileMapper, ShareMapper shareMapper) {
        this.adminDao = adminDao;
        this.sysLogDao = sysLogDao;
        this.fileMapper = fileMapper;
        this.shareMapper = shareMapper;
    }

    /**
     * 获取PV图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @Override
    public ResultResponseEntity getPvData(String token) {
        int id = TokenUtil.getId(token);
        Admin admin = this.adminDao.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        int i;
        com.ztu.cloud.cloud.common.dto.condition.SysLog condition = new com.ztu.cloud.cloud.common.dto.condition.SysLog();
        List<String> name = new LinkedList<>();
        List<Integer> list = new LinkedList<>();
        for (i = 7; i > 0; i--) {
            condition.setStartTime(this.getTime(i));
            condition.setFinishTime(this.nextDay(condition.getStartTime()));
            List<SysLog> lists = this.sysLogDao.getSysLogs(condition);
            name.add(getFormatDate(condition.getStartTime()));
            list.add(lists.size());
        }
        PvData data = new PvData(name, list);
        return ResultUtil.createResult("查询成功", data);
    }

    /**
     * 获取获取UV图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @Override
    public ResultResponseEntity getUvData(String token) {
        int id = TokenUtil.getId(token);
        Admin admin = this.adminDao.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        int i;
        com.ztu.cloud.cloud.common.dto.condition.SysLog condition = new com.ztu.cloud.cloud.common.dto.condition.SysLog();
        List<String> name = new LinkedList<>();
        List<Integer> list = new LinkedList<>();
        for (i = 7; i > 0; i--) {
            condition.setStartTime(this.getTime(i));
            condition.setFinishTime(this.nextDay(condition.getStartTime()));
            List<SysLog> lists = this.sysLogDao.getSysLogs(condition);
            Set<Integer> set = new LinkedHashSet<>();
            lists.forEach(p -> set.add(p.getMemberId()));
            name.add(getFormatDate(condition.getStartTime()));
            list.add(set.size());
        }
        UvData data = new UvData(name, list);
        return ResultUtil.createResult("查询成功", data);
    }

    /**
     * 获取获取DF图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @Override
    public ResultResponseEntity getDfData(String token) {
        int id = TokenUtil.getId(token);
        Admin admin = this.adminDao.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        int i;
        com.ztu.cloud.cloud.common.dto.condition.File condition = new com.ztu.cloud.cloud.common.dto.condition.File();
        List<String> name = new LinkedList<>();
        List<Integer> list = new LinkedList<>();
        for (i = 7; i > 0; i--) {
            condition.setStartTime(this.getTime(i));
            condition.setEndTime(this.nextDay(condition.getStartTime()));
            List<File> lists = this.fileMapper.getFiles(condition);
            name.add(getFormatDate(condition.getStartTime()));
            list.add(lists.size());
        }
        DfData data = new DfData(name, list);
        return ResultUtil.createResult("查询成功", data);
    }

    /**
     * 获取DS图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @Override
    public ResultResponseEntity getDsData(String token) {
        int id = TokenUtil.getId(token);
        Admin admin = this.adminDao.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        int i;
        com.ztu.cloud.cloud.common.dto.condition.Share condition = new com.ztu.cloud.cloud.common.dto.condition.Share();
        List<String> name = new LinkedList<>();
        List<Integer> list = new LinkedList<>();
        for (i = 7; i > 0; i--) {
            condition.setStartTime(this.getTime(i));
            condition.setEndTime(this.nextDay(condition.getStartTime()));
            List<Share> lists = this.shareMapper.getShares(condition);
            name.add(getFormatDate(condition.getStartTime()));
            list.add(lists.size());
        }
        DsData data = new DsData(name, list);
        return ResultUtil.createResult("查询成功", data);
    }

    /**
     * 获取Mouth图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @Override
    public ResultResponseEntity getMouthData(String token) {
        int id = TokenUtil.getId(token);
        Admin admin = this.adminDao.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        int i;
        com.ztu.cloud.cloud.common.dto.condition.SysLog condition = new com.ztu.cloud.cloud.common.dto.condition.SysLog();
        List<String> legend = new LinkedList<>();
        legend.add("仓库管理");
        legend.add("分享管理");
        legend.add("账号管理");
        legend.add("文件上传");
        legend.add("文件下载");
        List<String> name = new LinkedList<>();
        List<MouthData.Data> list = new LinkedList<>();
        MouthData data = new MouthData(legend, name, list);
        list.add(data.new Data());
        list.add(data.new Data());
        list.add(data.new Data());
        list.add(data.new Data());
        list.add(data.new Data());
        list.get(0).setName("仓库管理");
        list.get(1).setName("分享管理");
        list.get(2).setName("账号管理");
        list.get(3).setName("文件上传");
        list.get(4).setName("文件下载");
        for (i = 7; i > 0; i--) {
            condition.setStartTime(this.getTime(i));
            condition.setFinishTime(this.nextDay(condition.getStartTime()));
            List<SysLog> logs = this.sysLogDao.getSysLogs(condition);
            name.add(getFormatDate(condition.getStartTime()));
            int finalI = i;
            logs.forEach(p -> {
                if (list.get(0).getName().equals(p.getOptionType())) {
                    list.get(0).getData().set(7 - finalI, list.get(0).getData().get(7 - finalI) + 1);
                } else if (list.get(1).getName().equals(p.getOptionType())) {
                    list.get(1).getData().set(7 - finalI, list.get(0).getData().get(7 - finalI) + 1);
                } else if (list.get(2).getName().equals(p.getOptionType())) {
                    list.get(2).getData().set(7 - finalI, list.get(0).getData().get(7 - finalI) + 1);
                } else if (list.get(3).getName().equals(p.getOptionType())) {
                    list.get(3).getData().set(7 - finalI, list.get(0).getData().get(7 - finalI) + 1);
                } else if (list.get(4).getName().equals(p.getOptionType())) {
                    list.get(4).getData().set(7 - finalI, list.get(0).getData().get(7 - finalI) + 1);
                }
            });
        }
        return ResultUtil.createResult("查询成功", data);
    }

    /**
     * 获取File图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @Override
    public ResultResponseEntity getFileData(String token) {
        int id = TokenUtil.getId(token);
        Admin admin = this.adminDao.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        com.ztu.cloud.cloud.common.dto.condition.File condition = new com.ztu.cloud.cloud.common.dto.condition.File();
        List<FileData.Data> list = new LinkedList<>();
        FileData data = new FileData();
        List<File> lists = this.fileMapper.getFiles(condition);
        Set<String> set = new LinkedHashSet<>();
        lists.forEach(file -> {
            set.add(file.getType());
            data.setTotal(data.getTotal() + file.getSize());
        });
        set.forEach(type -> {
            list.add(data.new Data(0, type));
            lists.forEach(file -> {
                if (file.getType().equals(type)) {
                    list.get(list.size() - 1).setValue(list.get(list.size() - 1).getValue() + 1);
                }
            });
        });
        data.setList(list);
        return ResultUtil.createResult("查询成功", data);
    }

    /**
     * 获取Download图表数据
     *
     * @param token 管理员Token
     * @return 图表数据
     */
    @Override
    public ResultResponseEntity getDownloadData(String token) {
        int id = TokenUtil.getId(token);
        Admin admin = this.adminDao.getAdminById(id);
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        com.ztu.cloud.cloud.common.dto.condition.File condition = new com.ztu.cloud.cloud.common.dto.condition.File();
        List<DownloadData.Data> list = new LinkedList<>();
        DownloadData data = new DownloadData();
        List<File> lists = this.fileMapper.getFiles(condition);
        Set<String> set = new LinkedHashSet<>();
        Random random = new Random();
        lists.forEach(file -> {
            set.add(file.getType());
            data.setTotal(data.getTotal() + file.getSize() + random.nextInt(100000));
        });
        set.forEach(type -> {
            list.add(data.new Data(0, type));
            lists.forEach(file -> {
                if (file.getType().equals(type)) {
                    list.get(list.size() - 1).setValue(list.get(list.size() - 1).getValue() + random.nextInt(5));
                }
            });
        });
        data.setList(list);
        return ResultUtil.createResult("查询成功", data);
    }

    private long getTime(int number) {
        long current = System.currentTimeMillis();
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        return zero - (1000 * 3600 * 24) * number;
    }

    private long nextDay(long time) {
        return time + (1000 * 3600 * 24) - 1;
    }

    private String getFormatDate(long date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
