<template>
  <a-layout id="changePassword" style="height: 100%;background:white;">
    <a-layout-header
      style="position:relative;margin:0 0 6px 0;padding:0;background:white;box-shadow:0 0 6px 0 #B39696;">
      <Header></Header>
    </a-layout-header>
    <a-layout-content style="position:relative;margin:0;padding:0;">
      <div style="width: 800px;margin: 0 auto;">
        <div style="width: 500px;margin: 0 auto;padding-top: 50px;">
          <a-form :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="原始密码">
              <a-input placeholder="请输入密码"
                       type="password"
                       v-model="oldPassword"/>
            </a-form-item>
            <a-form-item label="新密码">
              <a-input placeholder="请输入密码"
                       type="password"
                       v-model="newPassword"/>
            </a-form-item>
            <a-form-item label="确认密码">
              <a-input placeholder="请输入密码"
                       type="password"
                       v-model="checkPassword"/>
            </a-form-item>
            <a-form-item style="text-align: center;">
              <router-link to="/user">
                <a-button style="width: 150px;">
                  取消
                </a-button>
              </router-link>
              <a-button type="primary" style="margin-left: 10px;width: 150px;"
                        @click="changePassword">
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
  import {changeUserInfo, changeUserPassword} from "../api/user";

  export default {
    name: "ChangePassword",
    components: {
      Header,
    }, data() {
      return {
        oldPassword: "",
        newPassword: "",
        checkPassword: "",
      }
    },
    methods: {
      changePassword() {
        const parent = this;
        const data = {
          oldPassword: this.oldPassword,
          newPassword: this.newPassword,
        };
        const handler = (data) => {
          parent.$message.success("修改成功!");
        };
        const catcher = (code, content) => {
          parent.$message.warn(content);
        };
        changeUserPassword(data, handler, catcher);
      }
    },
  }
</script>

<style scoped>

</style>
