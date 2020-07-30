<template>
  <div style="height: 100%;width: 100%">
    <a-form :form="registerForm" @submit="register"
            style="position: relative;top:15%;bottom:0;left:0;right:0;margin:0 auto;width: 30%;">
      <a-form-item v-bind="formItemLayout" label="邮箱">
        <a-input
          v-decorator="[
          'email',
          {
            rules: [
              {
                type: 'email',
                message: '邮箱不合法!',
              },
              {
                required: true,
                message: '请输入邮箱!',
              },
            ],
          },
        ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="账号">
        <a-input
          v-decorator="[
          'account',
          {
            rules: [{ required: true, message: '请输入账号!', whitespace: true }],
          },
        ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="手机号">
        <a-input
          v-decorator="[
          'phone',
          {
            rules: [{ required: true, message: '请输入手机号!', whitespace: true }],
          },
        ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="密码" has-feedback>
        <a-input
          v-decorator="[
          'password',
          {
            rules: [
              {
                required: true,
                message: '请输入密码!',
              },
            ],
          },
        ]"
          type="password"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="确认密码" has-feedback>
        <a-input
          v-decorator="[
          'confirmPassword',
          {
            rules: [
              {
                required: true,
                message: '请确认密码!',
              },
            ],
          },
        ]"
          type="password"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout">
      <span slot="label">
        昵称
      </span>
        <a-input
          v-decorator="[
          'name',
          {
            rules: [{ required: true, message: '请输入昵称!', whitespace: true }],
          },
        ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="授权码">
        <a-input
          v-decorator="[
          'key',
          {
            rules: [{ required: true, message: '请输入授权码!', whitespace: true }],
          },
        ]"
        />
      </a-form-item>

      <a-form-item v-bind="tailFormItemLayout">
        <router-link to="/login" style="margin: 0 20px">
          <a-button>
            登陆
          </a-button>
        </router-link>
        <a-button type="primary" style="margin: 0 20px" html-type="submit">
          注册
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  import {registerByAccount} from "../api/admin";
  import {message} from "../util/message";

  export default {
    name: 'Register',
    data() {
      return {
        formItemLayout: {
          labelCol: {
            xs: {span: 24},
            sm: {span: 8},
          },
          wrapperCol: {
            xs: {span: 24},
            sm: {span: 16},
          },
        },
        tailFormItemLayout: {
          wrapperCol: {
            xs: {
              span: 24,
              offset: 0,
            },
            sm: {
              span: 16,
              offset: 8,
            },
          },
        },
      };
    },
    beforeCreate() {
      this.registerForm = this.$form.createForm(this, {name: 'register'});
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
