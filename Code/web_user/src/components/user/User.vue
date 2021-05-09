<template>
  <a-layout-content style="position:relative;margin:0;padding:0;">
    <div style="width: 800px;margin: 0 auto;">
      <a-card title="用户信息" style="width: 800px;">
        <div style="width: 600px;margin: 0 auto;">
          <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" labelAlign="left">
            <a-form-item label="用户ID">
              <span>User_{{ userClone.id }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="仓库ID">
              <span>Repository_{{ userClone.repoId }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="等级">
              <span>{{ userClone.level }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="昵称">
              <a-input placeholder="请输入昵称"
                       v-model="userClone.name"
                       v-if="isChange"/>
              <span v-if="!isChange">{{ userClone.name }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="账号">
              <a-input placeholder="请输入账号"
                       v-model="userClone.account"
                       v-if="isChange"/>
              <span v-if="!isChange">{{ userClone.account }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="邮箱">
              <a-input placeholder="请输入邮箱"
                       v-model="userClone.email"
                       v-if="isChange"/>
              <span v-if="!isChange">{{ userClone.email === null ? '无' : userClone.email }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="手机">
              <a-input placeholder="请输入手机"
                       v-model="userClone.phone"
                       v-if="isChange"/>
              <span v-if="!isChange">{{ userClone.phone === null ? '无' : userClone.phone }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="状态">
              <span>{{ userClone.status }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="注册时间">
              <span>{{ getFormatDate(userClone.createTime) }}</span>
            </a-form-item>
            <a-divider style="margin: 0"/>
            <a-form-item label="修改时间">
              <span>{{ getFormatDate(userClone.changeTime) }}</span>
            </a-form-item>
            <a-form-item style="text-align: center;" :wrapper-col="{ span: 24 }">
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
      </a-card>
    </div>
  </a-layout-content>
</template>

<script>
import {getFormatDate} from "../../util/Utils";
import {changeUserInfo} from "../../api/user";
import {mapState} from 'vuex';

export default {
  name: "User",
  mounted() {
    //防止修改穿透数据
    this.userClone = Object.assign({}, this.user)
  },
  data() {
    return {
      isChange: false,
      userClone: {},
    }
  },
  computed: {
    ...mapState(["user"]),
  },
  watch: {
    user(newValue, oldValue) {
      this.userClone = this.user;
    }
  },
  methods: {
    getFormatDate(date) {
      return getFormatDate(date);
    },
    changeUser() {
      const parent = this;
      const data = {
        id: this.userClone.id,
        name: this.userClone.name,
        account: this.userClone.account,
        email: this.userClone.email,
        phone: this.userClone.phone
      };
      const handler = (data) => {
        this.$store.commit("updateUser", data.user);
        this.isChange = false;
        parent.$message.success("修改成功!");
      };
      const catcher = (code, content) => {
        parent.$message.warn(content);
      };
      changeUserInfo(data, handler, catcher);
    }
  },
}
</script>

<style scoped>

</style>
