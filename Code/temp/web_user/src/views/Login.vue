<template>
  <a-layout style="background: white">
    <a-layout-content style="margin-top: 50px;">
      <div style="width: 15%;margin: 0 auto;">
        <img src="../assets/logo.png" style="height: 100%;width: 100%;" alt="Logo"/>
      </div>
      <div style="width: 370px;margin: 0 auto;padding-top: 20px;">
        <a-form
          id="login"
          :form="form"
          class="login-form"
          @submit="Login"
        >
          <a-tabs :active-key="loginMethod" @change="selectLoginMethod"
                  :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
          >
            <a-tab-pane key="account" tab="账号登陆">
              <div v-if="loginMethod==='account'">
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
              </div>
            </a-tab-pane>
            <a-tab-pane key="email" tab="邮箱登录">
              <div v-if="loginMethod==='email'">
                <a-form-item>
                  <a-input
                    v-decorator="['email',{ rules: [{ required: true, message: '邮箱不合法!', min: 6, max: 18,
                  pattern: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/ }] },]"
                    placeholder="请输入邮箱">
                    <a-icon slot="prefix" type="mail" style="color: rgba(0,0,0,.25)"/>
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
              </div>
            </a-tab-pane>
            <a-tab-pane key="phone" tab="手机登录">
              <div v-if="loginMethod==='phone'">
                <a-form-item>
                  <a-input
                    v-decorator="['phone',{ rules: [{ required: true, message: '手机号不合法!', len: 11, pattern: /^[1][3,4,5,7,8][0-9]{9}$/ }] },]"
                    placeholder="请输入手机号">
                    <a-icon slot="prefix" type="phone" style="color: rgba(0,0,0,.25)"/>
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
              </div>
            </a-tab-pane>
          </a-tabs>
          <a-form-item>
            <a-checkbox
              v-decorator="['remember',{valuePropName: 'checked',initialValue: true,},]">
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
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {loginByAccount, loginByEmail} from "../api/user";

  export default {
    name: "Login",
    beforeCreate() {
    },
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
      Login(e) {
        e.preventDefault();
        this.form.validateFields((err, values) => {
          if (!err) {
            console.log('Received values of form: ', values);
            const parent = this;
            const data = {};
            const handler = function (data) {
              console.log(data);
              parent.$store.commit("updateUser", data.user);
              parent.$store.commit("updateRepository", data.repository);
              //TODO Mock测试使用,正常使用请删除此行
              localStorage.setItem("token", "token");
              parent.$router.push("index");
              parent.$message.success("登陆成功");
            };
            const catcher = function (code, content) {
              parent.$message.warn(content);
            };
            data.password = values.password;
            if (this.loginMethod === "account") {
              data.account = values.account;
              loginByAccount(data, handler, catcher);
            } else if (this.loginMethod === "email") {
              data.email = values.email;
              loginByEmail(data, handler, catcher);
            } else if (this.loginMethod === "phone") {
              data.phone = values.phone;
              this.$message.warn("暂时不支持手机登录");
              //TODO 手机登录
            }
          }
        });
      },
    },
  }
</script>

<style scoped>
  #login .login-form {
    max-width: 300px;
  }

  #login .login-form-forgot {
    float: right;
  }

  #login .login-form-button {
    width: 100%;
  }
</style>
