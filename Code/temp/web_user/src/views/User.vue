<template>
  <a-layout id="Index" style="height: 100%;background:white;">
    <a-layout-header
      style="position:relative;margin:0 0 6px 0;padding:0;background:white;box-shadow:0 0 6px 0 #B39696;">
      <Header></Header>
    </a-layout-header>
    <a-layout-content style="position:relative;margin:0;padding:0;">
      <div style="width: 800px;margin: 0 auto;">
        <div style="width: 500px;margin: 0 auto;padding-top: 50px;">
          <a-form :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="用户ID">
              <span>User_{{ user.id }}</span>
            </a-form-item>
            <a-form-item label="仓库ID">
              <span>Repository_{{ user.repoId }}</span>
            </a-form-item>
            <a-form-item label="等级">
              <span>{{ user.level }}</span>
            </a-form-item>
            <a-form-item label="昵称">
              <a-input placeholder="请输入昵称"
                       v-model="user.name"
                       v-if="isChange"/>
              <span v-if="!isChange">{{ user.name }}</span>
            </a-form-item>
            <a-form-item label="账号">
              <a-input placeholder="请输入账号"
                       v-model="user.account"
                       v-if="isChange"/>
              <span v-if="!isChange">{{ user.account }}</span>
            </a-form-item>
            <a-form-item label="邮箱">
              <a-input placeholder="请输入邮箱"
                       v-model="user.email"
                       v-if="isChange"/>
              <span v-if="!isChange">{{ user.email === null ? '无' : user.email }}</span>
            </a-form-item>
            <a-form-item label="手机">
              <a-input placeholder="请输入手机"
                       v-model="user.phone"
                       v-if="isChange"/>
              <span v-if="!isChange">{{ user.phone === null ? '无' : user.phone }}</span>
            </a-form-item>
            <a-form-item label="状态">
              <span>{{ user.status }}</span>
            </a-form-item>
            <a-form-item label="注册时间">
              <span>{{ getFormatDate(user.createTime) }}</span>
            </a-form-item>
            <a-form-item label="修改时间">
              <span>{{ getFormatDate(user.changeTime) }}</span>
            </a-form-item>
            <a-form-item style="text-align: center;">
              <router-link to="/changePassword">
                <a-button type="primary" style="width: 150px;"
                          v-if="!isChange">
                  修改密码
                </a-button>
              </router-link>
              <a-button type="primary" style="margin-left: 10px;width: 150px;"
                        @click="isChange = true"
                        v-if="!isChange">
                修改信息
              </a-button>
              <a-button style="width: 150px;"
                        @click="isChange = false"
                        v-if="isChange">
                取消
              </a-button>
              <a-button type="primary" style="margin-left: 10px;width: 150px;"
                        v-if="isChange"
                        @click="changeUser">
                确认
              </a-button>
            </a-form-item>
          </a-form>
        </div>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import Header from "../components/common/Header";
  import {getFormatDate} from "../util/Utils";

  export default {
    name: "User",
    components: {
      Header,
    }, data() {
      return {
        user: {
          "id": 1,
          "repoId": "5f06b20d17e32b20664aa4c6",
          "account": "admin",
          "email": "814878826@qq.com",
          "phone": "18600000000",
          "name": "测试用户",
          "status": "正常",
          "level": "1级",
          "createTime": 1594274316583,
          "changeTime": 1594274316583
        },
        isChange: false,
      }
    },
    methods: {
      getFormatDate(date) {
        return getFormatDate(date);
      },
      changeUser() {
        console.log(123);
        this.$message.info("修改用户");
      }
    },
  }
</script>

<style scoped>

</style>
