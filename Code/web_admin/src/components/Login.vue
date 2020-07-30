<template>
  <div style="height: 100%;width: 100%;">
    <a-form
      id="components-form-demo-normal-login"
      :form="loginForm"
      class="login-form"
      @submit="login"
      style="position: relative;top:30%;bottom:0;left:0;right:0;margin:0 auto;width: 25%;"
    >
      <a-form-item>
        <a-input
          v-decorator="[
          'account',
          { rules: [{ required: true, message: '账号不能为空!' }] },
        ]"
          placeholder="请输入账号"
        >
          <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)"/>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-input
          v-decorator="[
          'password',
          { rules: [{ required: true, message: '密码不能为空!' }] },
        ]"
          type="password"
          placeholder="请输入密码"
        >
          <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)"/>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-checkbox
          v-decorator="['rememberMe',{valuePropName: 'checked',initialValue: false,},]">
          记住我
        </a-checkbox>
        <router-link class="login-form-forgot" to="/forget">
          忘记密码
        </router-link>
        <a-button type="primary" html-type="submit" class="login-form-button">
          登陆
        </a-button>
        <router-link to="/register">
          注册
        </router-link>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  import {loginByAccount} from "../api/admin";
  import {message} from "../util/message";

  export default {
    name: "Login",
    beforeCreate() {
      // this.loginForm = this.$form.createForm(this, {name: 'normal_login'});
    },
    data() {
      return {
        loginForm: this.$form.createForm(this, {name: 'normal_login'}),
      }
    },
    methods: {
      login(e) {
        e.preventDefault();
        this.loginForm.validateFields((err, values) => {
          if (!err) {
            const data = {
              account: values.account,
              password: values.password,
              rememberMe: values.rememberMe
            };
            const handler = (data) => {
              console.log(data);
              this.$router.push("/index");
            };
            const catcher = (code, content) => {
              message(content);
            };
            loginByAccount(data, handler, catcher);
          }
        });
      },
    },
  };
</script>
<style>
  #components-form-demo-normal-login .login-form {
    max-width: 300px;
  }

  #components-form-demo-normal-login .login-form-forgot {
    float: right;
  }

  #components-form-demo-normal-login .login-form-button {
    width: 100%;
  }
</style>
