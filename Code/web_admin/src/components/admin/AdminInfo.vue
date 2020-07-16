<template>
  <div style="height: 100%;width: 100%">
    <a-form style="position: relative;top:0;bottom:0;left:0;right:0;margin:0 auto;width: 400px;"
            :form="form"
            @submit="change">
      <a-form-item label="邮箱">
        <a-input v-decorator="['email']"/>
      </a-form-item>
      <a-form-item label="账号">
        <a-input v-decorator="['account']"/>
      </a-form-item>
      <a-form-item label="手机号">
        <a-input v-decorator="['phone']"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-decorator="['name']"/>
      </a-form-item>
      <a-form-item label="状态">
        <a-input :disabled="true" :value="admin.status"/>
      </a-form-item>
      <a-form-item label="等级">
        <a-input :disabled="true" :value="admin.level"/>
      </a-form-item>
      <a-form-item>
        <router-link to="/password">
          <a-button style="margin: 0 20px;width: 150px">
            修改密码
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
  //TODO 合法性检验
  import {changeAdminInfo, getAdminInfo} from "../../api/admin";
  import {message} from "../../util/message";

  export default {
    name: 'AdminInfo',
    data() {
      return {
        form: this.$form.createForm(this, {name: 'coordinated'}),
        admin: {},
      };
    },
    mounted() {
      const handler = (data) => {
        this.admin = data.admin;
        this.form.setFieldsValue({
          account: this.admin.account,
          email: this.admin.email,
          phone: this.admin.phone,
          name: this.admin.name,
        })
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      getAdminInfo(handler, catcher);
    },
    methods: {
      change(e) {
        e.preventDefault();
        this.form.validateFields((err, values) => {
          if (!err) {
            const data = {
              id: this.admin.id,
              account: this.form.getFieldValue("account"),
              email: this.form.getFieldValue("email"),
              phone: this.form.getFieldValue("phone"),
              name: this.form.getFieldValue("name"),
            };
            const handler = (data) => {
              message("修改成功")
            };
            const catcher = (code, content) => {
              message(content, "warning")
            };
            changeAdminInfo(data, handler, catcher);
          }
        });
      }
    },
  };
</script>
