<template>
  <div class="main">
    <div class="forget">
      <div class="logo">
        <img src="../assets/logo.png" alt="Logo">
      </div>
      <el-form ref="form" style="text-align: center;padding: 30px">
        <div style="margin: 10px">
          <el-input maxlength="16" placeholder="请输入邮箱" v-model="email" style="width: 300px">
            <template slot="prepend">邮箱</template>
          </el-input>
        </div>
        <div style="margin: 10px">
          <el-button type="primary" style="width: 300px" @click="forget">重置密码</el-button>
        </div>
        <div style="margin: 10px">
          <router-link to="/login">
            <el-button style="width: 300px">登陆</el-button>
          </router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
  import {message} from "../util/message";
  import {forgetEmail} from "../api/user";

  export default {
    name: "Login",
    data() {
      return {
        email: '',
      }
    },
    methods: {
      forget() {
        const data = {
          email: this.email,
        };
        const handler = () => {
          message("密码已重置,请注意查收邮件!");
        };
        forgetEmail(data, handler);
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

  .forget img {
    margin: auto;
    width: auto;
    height: auto;
    max-width: 100%;
    max-height: 100%;
  }

  .forget {
    position: relative;
    top: 15%;
  }
</style>
