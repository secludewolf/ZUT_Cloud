<template>
  <div class="main" v-on:keyup.enter="login">
    <div class="login">
      <div class="logo">
        <img src="../assets/logo.png" alt="Logo">
      </div>
      <el-form ref="form" style="text-align: center;padding: 30px">
        <div style="margin: 10px">
          <el-input maxlength="16" placeholder="请输入账号" v-model="account" style="width: 300px">
            <template slot="prepend">账号</template>
          </el-input>
        </div>
        <div style="margin: 10px">
          <el-input maxlength="16" placeholder="请输入密码" v-model="password" style="width: 300px" show-password>
            <template slot="prepend">密码</template>
          </el-input>
        </div>
        <div style="margin: 10px">
          <el-button type="primary" style="width: 300px" v-on:click="login">登陆</el-button>
        </div>
        <div style="margin: 10px">
          <router-link to="/register">
            <el-button style="width: 300px">注册</el-button>
          </router-link>
        </div>
        <router-link to="/forget" style="font-size: 12px;">忘记密码</router-link>
      </el-form>
    </div>
  </div>
</template>

<script>
  import {loginByAccount} from "../api/user";
  import {message} from "../util/message";

  export default {
    name: "Login",
    data() {
      return {
        account: '',
        password: ''
      }
    },
    methods: {
      login: function () {
        console.log("登陆");
        if (this.account === "" || this.account == null) {
          message("账号不能为空", "warning");
          return;
        }
        if (this.password === "" || this.password == null) {
          message("密码不能为空", "warning");
          return;
        }
        const data = {account: this.account, password: this.password};
        const handler = (data) => {
          this.$router.push({name: "index", params: {user: data["user"], repository: data["repository"]}});
          message("登陆成功");
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        loginByAccount(data, handler, catcher);
      }
    }
  }
</script>

<style scoped>
  .main {
    position: relative;
    text-align: center;
    height: 100vh;
  }

  .logo {
    width: 250px;
    display: inline-block;
  }

  .login img {
    margin: auto;
    width: auto;
    height: auto;
    max-width: 100%;
    max-height: 100%;
  }

  .login {
    position: relative;
    top: 15%;
  }
</style>
