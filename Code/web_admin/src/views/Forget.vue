<template>
  <a-layout style="background: white">
    <a-layout-content style="margin-top: 50px;">
      <div style="width: 15%;min-width:300px;margin: 0 auto;">
        <img src="../assets/logo.png" style="height: 100%;width: 100%;" alt="Logo"/>
      </div>
      <a-form
        id="components-form-demo-normal-login"
        :form="forgetForm"
        class="login-form"
        @submit="forget"
        style="width: 25%;min-width:400px;margin: 50px auto;padding-top: 20px;"
      >
        <a-form-item>
          <a-input
            v-decorator="[
          'email',
          { rules: [
            {
              type: 'email',
              message: '邮箱不合法!',
            },
            {
              required: true, message: '邮箱不能为空!'
            }]
           },
        ]"
            placeholder="请输入邮箱">
            <a-icon slot="prefix" type="mail" style="color: rgba(0,0,0,.25)"/>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" class="login-form-button">
            找回
          </a-button>
          <router-link to="/login">
            登陆
          </router-link>
        </a-form-item>
      </a-form>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {message} from "../util/message";
  import {forgetEmail} from "../api/admin";

  export default {
    name: "forget",
    beforeCreate() {
    },
    data() {
      return {
        forgetForm: this.$form.createForm(this, {name: 'normal_login'}),
      }
    },
    methods: {
      forget(e) {
        e.preventDefault();
        this.forgetForm.validateFields((err, values) => {
          if (!err) {
            const data = {
              email: values.email
            };
            const handler = (data) => {
              message("账号已重置,请注意查收邮件!", "success", 6);
              this.$router.push("login");
            };
            const catcher = (code, content) => {
              message(content);
            };
            forgetEmail(data, handler, catcher);
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
