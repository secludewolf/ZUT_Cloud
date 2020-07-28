<template>
  <div class="main">
    <el-header>
      <Header v-bind:index="1+''" :name="user.name"></Header>
    </el-header>
    <div class="User">
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="原始密码" prop="password">
          <el-input maxlength="16" placeholder="请输入账号" v-model="form.oldPassword">
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input maxlength="16" placeholder="请输入账号" v-model="form.password">
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="ch">
          <el-input maxlength="16" placeholder="请输入账号" v-model="form.checkPassword">
          </el-input>
        </el-form-item>
        <el-form-item label-width="0">
          <router-link to="/user">
            <el-button>取消</el-button>
          </router-link>
          <el-button type="primary" v-on:click="change">修改密码</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import Header from "../components/common/Header";
  import {changeUserPassword, loginByToken} from "../api/user";
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
        form: {
          oldPassword: '',
          password: '',
          checkPassword: '',
        },
        user: {},
        typeAccount: "primary",
        typeEmail: "",
        isAccount: true,
        rules: {
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
        const data = {
          oldPassword: this.form.oldPassword,
          newPassword: this.form.password
        };
        const handler = () => {
          message("修改成功");
        };
        changeUserPassword(data,handler);
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
    padding: 200px 50px;
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
