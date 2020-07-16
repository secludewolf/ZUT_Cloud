<template>
  <div class="main">
    <el-header>
      <Header v-bind:index="1+''" :name="user.name"></Header>
    </el-header>
    <div class="User">
      <el-form ref="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID">
          <el-input maxlength="16" v-model="user.id" :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input maxlength="16" placeholder="请输入昵称" v-model="user.name">
          </el-input>
        </el-form-item>
        <el-form-item label="用户账号">
          <el-input maxlength="16" placeholder="请输入账号" v-model="user.account">
          </el-input>
        </el-form-item>
        <el-form-item label="用户邮箱">
          <el-input maxlength="32" placeholder="请输入邮箱" v-model="user.email">
          </el-input>
        </el-form-item>
        <el-form-item label="用户手机">
          <el-input maxlength="16" placeholder="请输入手机" v-model="user.phone">
          </el-input>
        </el-form-item>
        <el-form-item label="用户等级">
          <el-input v-model="user.level" :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="用户状态">
          <el-input v-model="user.status" :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label-width="0">
          <router-link to="/changePassword">
            <el-button>修改密码</el-button>
          </router-link>
          <el-button type="primary" v-on:click="change">修改信息</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import Header from "../components/common/Header";
  import {changeUserInfo, loginByToken} from "../api/user";
  import {message} from "../util/message";

  export default {
    name: "User",
    created() {
      //TODO 没有直接获取当前用户信息的接口
      const handler = (data) => {
        this.user = data.user;
        console.log(this.user);
      };
      const catcher = () => {
      };
      loginByToken(handler, catcher);
    },
    components: {
      Header
    },
    data() {
      const checkPassword = (rule, value, callback) => {
        if (!value) {
          callback(new Error('必填'))
        } else if (value !== this.form.password) {
          callback(new Error('两次输入密码不相同'))
        } else {
          callback()
        }
      };
      return {
        oldPassword: '',
        password: '',
        checkPassword: '',
        user: {},
        typeAccount: "primary",
        typeEmail: "",
        isAccount: true,
        rules: {
          email: [
            {required: true, message: '请输入邮箱地址', trigger: 'blur'},
            {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
          ],
          name: [
            {required: true, message: '必填', trigger: 'blur'},
            {min: 4, max: 16, message: '长度在 4 到 16 个字符', trigger: 'blur'}
          ],
          account: [
            {required: true, message: '必填', trigger: 'blur'},
            {min: 8, max: 16, message: '长度在 8 到 16 个字符', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '必填', trigger: 'blur'},
            {min: 8, max: 16, message: '长度在 8 到 16 个字符', trigger: 'blur'}
          ],
          checkPassword: [
            {required: true, validator: checkPassword, trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      change() {
        //TODO 不能删除手机号,会检测到重复,只能改变手机号
        const data = {
          id: this.user.id,
          name: this.user.name,
          account: this.user.account,
          email: this.user.email,
          phone: this.user.phone
        };
        const handler = (data) => {
          this.user = data.user;
          message("修改成功!");
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        changeUserInfo(data, handler, catcher);
      }
    }
  }
</script>

<style scoped>
  .main {
    height: 100%;
    position: relative;
  }

  .el-header {
    position: relative;
    margin: 0;
    padding: 0;
    font-size: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }

  .User {
    padding-top: 0;
  }

  .register {
    position: relative;
    top: 15%;
  }

  .el-form {
    margin: auto;
    text-align: center;
    padding: 40px 50px;
    width: 400px;
    /*box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)*/
  }

  .el-form-item {
    width: 400px;
    text-align: center;
    /*border: black solid 1px;*/
  }

  .el-input {
    width: 300px;
  }

  .el-button {
    width: 150px;
  }
</style>
