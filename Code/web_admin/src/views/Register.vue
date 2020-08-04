<template>
  <a-layout style="background: white">
    <a-layout-content style="margin-top: 50px;">
      <div style="width: 15%;min-width:300px;margin: 0 auto;">
        <img src="../assets/logo.png" style="height: 100%;width: 100%;" alt="Logo"/>
      </div>
      <a-form :form="registerForm" @submit="register"
              style="width: 25%;min-width:400px;margin: 0 auto;padding-top: 20px;">
        <a-form-item>
          <a-input
            v-decorator="['email',{rules: [{type: 'email',message: '邮箱不合法!',},{required: true,message: '请输入邮箱!',},],},]"
            placeholder="请输入邮箱">
            <a-icon slot="prefix" type="mail" style="color: rgba(0,0,0,.25)"/>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-input v-decorator="['account',{rules: [{ required: true, message: '请输入账号!', whitespace: true }],},]"
                   placeholder="请输入账号">
            <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)"/>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-input v-decorator="['phone',{rules: [{ required: true, message: '请输入手机号!', whitespace: true }],},]"
                   placeholder="请输入手机号">
            <a-icon slot="prefix" type="phone" style="color: rgba(0,0,0,.25)"/>
          </a-input>
        </a-form-item>
        <a-form-item has-feedback>
          <a-input v-decorator="['password',{rules: [{required: true,message: '请输入密码!',},],},]" type="password"
                   placeholder="请输入密码">
            <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)"/>
          </a-input>
        </a-form-item>
        <a-form-item has-feedback>
          <a-input v-decorator="['confirmPassword',{rules: [{required: true,message: '请确认密码!',},],},]" type="password"
                   placeholder="请确认密码">
            <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)"/>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-input v-decorator="['name',{rules: [{ required: true, message: '请输入昵称!', whitespace: true }],},]"
                   placeholder="请输入昵称">
            <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)"/>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-input v-decorator="['key',{rules: [{ required: true, message: '请输入授权码!', whitespace: true }],},]"
                   placeholder="请输入授权码">
            <a-icon slot="prefix" type="key" style="color: rgba(0,0,0,.25)"/>
          </a-input>
        </a-form-item>

        <a-form-item style="text-align: center">
          <router-link to="/login">
            <a-button style="width:150px; margin: 0 20px">
              登陆
            </a-button>
          </router-link>
          <a-button type="primary" style="width:150px; margin: 0 20px" html-type="submit">
            注册
          </a-button>
        </a-form-item>
      </a-form>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {registerByAccount} from "../api/admin";
  import {message} from "../util/message";

  export default {
    name: 'Register',
    data() {
      return {
        registerForm: this.$form.createForm(this, {name: 'register'}),
      };
    },
    methods: {
      register(e) {
        e.preventDefault();
        this.registerForm.validateFieldsAndScroll((err, values) => {
          if (!err) {
            const data = {
              account: values.account,
              email: values.email,
              phone: values.phone,
              password: values.password,
              name: values.name,
              key: values.key
            };
            const handler = (data) => {
              message("注册成功");
              this.$router.push("login");
              console.log(data);
            };
            const catcher = (code, content) => {
              message(content, "warning");
            };
            registerByAccount(data, handler, catcher);
          }
        });
      }
    },
  };
</script>
