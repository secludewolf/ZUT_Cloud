package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AdminMapperTest {
    @Autowired
    AdminMapper adminMapper;


    @Test
    public void getAdminById() {
        this.adminMapper.insertAdmin(new Admin("test1", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        Admin admin = this.adminMapper.getAdminByAccount("test1");
        System.out.println(this.adminMapper.getAdminById(admin.getId()));
        this.adminMapper.deleteAdminById(admin.getId());
    }

    @Test
    public void getAdminByAccount() {
        this.adminMapper.insertAdmin(new Admin("test2", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        Admin admin = this.adminMapper.getAdminByAccount("test2");
        System.out.println(this.adminMapper.getAdminByAccount(admin.getAccount()));
        this.adminMapper.deleteAdminById(admin.getId());
    }

    @Test
    public void getAdminByEmail() {
        this.adminMapper.insertAdmin(new Admin("test3", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        Admin admin = this.adminMapper.getAdminByAccount("test3");
        System.out.println(this.adminMapper.getAdminByEmail(admin.getEmail()));
        this.adminMapper.deleteAdminById(admin.getId());
    }

    @Test
    public void getAdminByPhone() {
        this.adminMapper.insertAdmin(new Admin("test4", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        Admin admin = this.adminMapper.getAdminByAccount("test4");
        System.out.println(this.adminMapper.getAdminByPhone(admin.getPhone()));
        this.adminMapper.deleteAdminById(admin.getId());
    }

    @Test
    public void getAdminCount() {
        this.adminMapper.insertAdmin(new Admin("test4", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        Admin admin = this.adminMapper.getAdminByAccount("test4");
        System.out.println(this.adminMapper.getAdminCount());
        this.adminMapper.deleteAdminById(admin.getId());
    }

    @Test
    public void insertAdmin() {
        Admin admin = new Admin("test5", "test4", "test4", "test4", "test4", 4, 4, System.currentTimeMillis(), System.currentTimeMillis());
        this.adminMapper.insertAdmin(admin);
        int id = this.adminMapper.getAdminByAccount("test5").getId();
        System.out.println(id);
        this.adminMapper.deleteAdminById(id);
    }

    @Test
    public void insertAdmins() {
        List<Admin> admins = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        admins.add(new Admin("test6", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        admins.add(new Admin("test7", "test2", "test2", "test2", "test2", 2, 2, System.currentTimeMillis(), System.currentTimeMillis()));
        this.adminMapper.insertAdmins(admins);
        ids.add(this.adminMapper.getAdminByAccount(admins.get(0).getAccount()).getId());
        ids.add(this.adminMapper.getAdminByAccount(admins.get(1).getAccount()).getId());
        System.out.println(ids);
        this.adminMapper.deleteAdminsById(ids);
    }

    @Test
    public void deleteAdmin() {
        Admin admin = new Admin("test8", "test4", "test4", "test4", "test4", 4, 4, System.currentTimeMillis(), System.currentTimeMillis());
        this.adminMapper.insertAdmin(admin);
        int id = this.adminMapper.getAdminByAccount("test8").getId();
        System.out.println(id);
        this.adminMapper.deleteAdminById(id);
        System.out.println(this.adminMapper.getAdminById(id));
    }

    @Test
    public void deleteAdminsById() {
        List<Admin> admins = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        admins.add(new Admin("test9", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        admins.add(new Admin("test0", "test2", "test2", "test2", "test2", 2, 2, System.currentTimeMillis(), System.currentTimeMillis()));
        this.adminMapper.insertAdmins(admins);
        ids.add(this.adminMapper.getAdminByAccount(admins.get(0).getAccount()).getId());
        ids.add(this.adminMapper.getAdminByAccount(admins.get(1).getAccount()).getId());
        System.out.println(this.adminMapper.deleteAdminsById(ids));
    }

    @Test
    public void updateAdmin() {
        this.adminMapper.insertAdmin(new Admin("test11", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        Admin admin = this.adminMapper.getAdminByAccount("test11");
        System.out.println(admin);
        admin.setPassword("1234password");
        this.adminMapper.updateAdmin(admin);
        System.out.println(this.adminMapper.getAdminById(admin.getId()));
        this.adminMapper.deleteAdminById(admin.getId());
    }

    @Test
    public void updateAdminPassword() {
        this.adminMapper.insertAdmin(new Admin("test12", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        Admin admin = this.adminMapper.getAdminByAccount("test12");
        System.out.println(admin);
        admin.setPassword("1234password");
        this.adminMapper.updateAdminPassword(admin.getId(), admin.getPassword());
        System.out.println(this.adminMapper.getAdminById(admin.getId()));
        this.adminMapper.deleteAdminById(admin.getId());
    }

    @Test
    void getAdmins() {
        this.adminMapper.insertAdmin(new Admin("test12", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        Admin admin = this.adminMapper.getAdminByAccount("test12");
        List<Admin> admins = this.adminMapper.getAdmins(new com.ztu.cloud.cloud.common.dto.condition.Admin("test", "test", "test", 1, null, null));
        for (Admin admin1 : admins) {
            System.out.println(admin1);
        }
        this.adminMapper.deleteAdminById(admin.getId());
    }
}