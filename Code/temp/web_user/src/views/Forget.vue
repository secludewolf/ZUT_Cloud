<template>
  <a-layout style="background: white">
    <a-layout-content style="margin-top: 50px;">
      <div style="width: 15%;margin: 0 auto;">
        <img src="../assets/logo.png" style="height: 100%;width: 100%;" alt="Logo"/>
      </div>
      <div style="width: 370px;margin: 0 auto;padding-top: 20px;">
        <a-form
          id="forget"
          :form="form"
          class="login-form"
          @submit="Register"
        >
          <a-tabs :active-key="loginMethod" @change="selectLoginMethod"
                  :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
                  :animated="false"
          >
            <a-tab-pane key="email" tab="邮箱找回">
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
            <a-tab-pane key="phone" tab="手机找回">
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
            <a-button type="primary" html-type="submit" class="login-form-button">
              重置密码
            </a-button>
            <router-link to="/login">
              登陆
            </router-link>
          </a-form-item>
        </a-form>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {forgetEmail} from "../api/user";

  export default {
    name: "Forget",
    data() {
      return {
        form: this.$form.createForm(this),
        loginMethod: "email",
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
            const data = {};
            const handler = function (data) {
              parent.$message.success("账号已重置,请注意查收邮件",10);
            };
            const catcher = function (code, content) {
              parent.$message.warn(content);
            };
            if (this.loginMethod === "email") {
              data.email = values.email;
              forgetEmail(data, handler, catcher);
            } else if (this.loginMethod === "phone") {
              this.$message.warn("暂不支持手机找回")
            }
          }
        });
      },
    },
  }
</script>

<style scoped>
  #forget .login-form {
    max-width: 300px;
  }

  #forget .login-form-forgot {
    float: right;
  }

  #forget .login-form-button {
    width: 100%;
  }
</style>
