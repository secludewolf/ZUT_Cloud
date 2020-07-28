<template>
  <a-layout style="background: white">
    <a-layout-content style="margin-top: 50px;">
      <div style="width: 15%;margin: 0 auto;">
        <img src="../assets/logo.png" style="height: 100%;width: 100%;" alt="Logo"/>
      </div>
      <div style="width: 370px;margin: 0 auto;padding-top: 20px;">
        <a-form
          id="register"
          :form="form"
          class="login-form"
          @submit="Register"
        >
          <a-tabs :active-key="loginMethod" @change="selectLoginMethod"
                  :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
                  :animated="false"
          >
            <a-tab-pane key="account" tab="账号注册">
              <div v-if="loginMethod==='account'">
              </div>
            </a-tab-pane>
            <a-tab-pane key="email" tab="邮箱注册">
              <div v-if="loginMethod==='email'">
                <a-form-item>
                  <a-input
                    v-decorator="['email',{ rules: [{ required: true, message: '邮箱不合法!', min: 6, max: 18,
                  pattern: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/ }] },]"
                    placeholder="请输入邮箱">
                    <a-icon slot="prefix" type="mail" style="color: rgba(0,0,0,.25)"/>
                  </a-input>
                </a-form-item>
              </div>
            </a-tab-pane>
            <a-tab-pane key="phone" tab="手机注册">
              <div v-if="loginMethod==='phone'">
                <a-form-item>
                  <a-input
                    v-decorator="['phone',{ rules: [{ required: true, message: '手机号不合法!', len: 11, pattern: /^[1][3,4,5,7,8][0-9]{9}$/ }] },]"
                    placeholder="请输入手机号">
                    <a-icon slot="prefix" type="phone" style="color: rgba(0,0,0,.25)"/>
                  </a-input>
                </a-form-item>
              </div>
            </a-tab-pane>
          </a-tabs>
          <a-form-item>
            <a-input
              v-decorator="['account',{ rules: [{ required: true, message: '账号不合法!', min: 6, max: 18 }] },]"
              placeholder="请输入账号">
              <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)"/>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-input-password
              v-decorator="['password',{ rules: [{ required: true, message: '请输入密码!' }] },]"
              type="password"
              placeholder="请输入密码">
              <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)"/>
            </a-input-password>
          </a-form-item>
          <a-form-item>
            <a-input-password
              v-decorator="['checkPassword',{ rules: [{ required: true, message: '请确认密码!' }] },]"
              type="password"
              placeholder="请确认密码">
              <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)"/>
            </a-input-password>
          </a-form-item>
          <a-form-item>
            <a-input
              v-decorator="['name',{ rules: [{ required: true, message: '昵称不合法!', min: 2, max: 8 }] },]"
              placeholder="请输入昵称">
              <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)"/>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" html-type="submit" class="login-form-button">
              注册
            </a-button>
            <router-link to="/login">
              我已有账号
            </router-link>
          </a-form-item>
        </a-form>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {registerByAccount, registerByEmail} from "../api/user";

  export default {
    name: "Register",
    data() {
      return {
        form: this.$form.createForm(this),
        loginMethod: "account",
      }
    },
    methods: {
      selectLoginMethod(key) {
        this.loginMethod = key;
      },
      Register(e) {
        e.preventDefault();
        this.form.validateFields((err, values) => {
          if (!err) {
            console.log('Received values of form: ', values);
            const parent = this;
            const data = {
              account: values.account,
              password: values.password,
              name: values.name,
            };
            const handler = function (data) {
              parent.$message.success("注册成功");
              parent.$router.push("login");
            };
            const catcher = function (code, content) {
              parent.$message.warn(content);
              parent.$message.error("登陆失败");
            };
            if (this.loginMethod === "account") {
              registerByAccount(data, handler, catcher);
            } else if (this.loginMethod === "email") {
              data.email = values.email;
              registerByEmail(data, handler, catcher);
            } else if (this.loginMethod === "phone") {
              this.$message.warn("暂不支持手机注册!");
              //TODO 手机注册
            }
          }
        });
      },
    },
  }
</script>

<style scoped>
  #register .login-form {
    max-width: 300px;
  }

  #register .login-form-forgot {
    float: right;
  }

  #register .login-form-button {
    width: 100%;
  }
</style>
