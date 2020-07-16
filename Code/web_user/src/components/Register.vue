<template>
  <div class="main" v-on:keyup.enter="register">
    <div class="register">
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label-width="0">
          <el-button id="account" v-bind:type="typeAccount" v-on:click="select">账号注册</el-button>
          <el-button id="email" v-bind:type="typeEmail" v-on:click="select">邮箱注册</el-button>
        </el-form-item>
        <el-form-item label="用户邮箱" prop="email" v-if="!isAccount">
          <el-input maxlength="30" placeholder="请输入邮箱" v-model="form.email">
          </el-input>
        </el-form-item>
        <el-form-item label="用户昵称" prop="name">
          <el-input maxlength="16" placeholder="请输入昵称" v-model="form.name">
          </el-input>
        </el-form-item>
        <el-form-item label="用户账号" prop="account">
          <el-input maxlength="16" placeholder="请输入账号" v-model="form.account">
          </el-input>
        </el-form-item>
        <el-form-item label="用户密码" prop="password">
          <el-input maxlength="16" placeholder="请输入密码" v-model="form.password" show-password>
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input maxlength="16" placeholder="请输入密码" v-model="form.checkPassword" show-password>
          </el-input>
        </el-form-item>
        <el-form-item label-width="0">
          <router-link to="/login">
            <el-button>登陆</el-button>
          </router-link>
          <el-button type="primary" v-on:click="register">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import {message} from "../util/message";
  import {registerByAccount, registerByEmail} from "../api/user";

  export default {
    name: "Register",
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
        form: {
          email: '',
          name: '',
          account: '',
          password: '',
          checkPassword: ''
        },
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
      select: function (event) {
        if (event.currentTarget.id === "account") {
          this.isAccount = true;
          this.typeAccount = "primary";
          this.typeEmail = "";
        } else {
          this.isAccount = false;
          this.typeAccount = "";
          this.typeEmail = "primary";
        }
      },
      register() {
        const handler = () => {
          this.$router.push("login");
          message("注册成功!");
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        if (this.isAccount) {
          const data = {
            name: this.form.name,
            account: this.form.account,
            password: this.form.password
          };
          registerByAccount(data, handler, catcher);
        } else {
          const data = {
            name: this.form.name,
            email: this.form.email,
            account: this.form.account,
            password: this.form.password
          };
          registerByEmail(data, handler, catcher);
        }
      },
    }
  }
</script>

<style scoped>
  .main {
    height: 100vh;
    position: relative;
  }

  .register {
    position: relative;
    top: 15%;
  }

  .el-form {
    margin: auto;
    text-align: center;
    padding: 20px 50px;
    width: 400px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
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
