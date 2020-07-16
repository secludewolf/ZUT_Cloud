<template>
  <div style="height: 100%;width: 100%;">
    <a-form
      style="position: relative;top:0;bottom:0;left:0;right:0;margin:0 auto;width: 400px;"
      :form="form"
      @submit="change">
      <a-form-item label="原始密码">
        <a-input v-decorator="['oldPassword']"/>
      </a-form-item>
      <a-form-item label="新密码">
        <a-input v-decorator="['newPassword']"/>
      </a-form-item>
      <a-form-item label="确认密码">
        <a-input v-decorator="['checkPassword']"/>
      </a-form-item>
      <a-form-item>
        <router-link to="/admin">
          <a-button style="margin: 0 20px;width: 150px">
            用户信息
          </a-button>
        </router-link>
        <a-button type="primary" style="margin: 0 20px;width: 150px;" html-type="submit">
          提交
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  import {message} from "../../util/message";
  import {changeAdminPassword, getAdminInfo} from "../../api/admin";

  export default {
    name: "changeAdminPassword",
    mounted() {
      const handler = (data) => {
        this.admin = data.admin;
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      getAdminInfo(handler, catcher);
    },
    data() {
      return {
        form: this.$form.createForm(this, {name: 'coordinated'}),
        admin: null,
      }
    },
    methods: {
      change(e) {
        e.preventDefault();
        this.form.validateFields((err, values) => {
          if (!err) {
            const data = {
              id: this.admin.id,
              oldPassword: this.form.getFieldValue("oldPassword"),
              newPassword: this.form.getFieldValue("newPassword")
            };
            const handler = (data) => {
              message("修改成功")
            };
            const catcher = (code, content) => {
              message(content, "warning")
            };
            console.log(data);
            changeAdminPassword(data, handler, catcher);
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
