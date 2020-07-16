<template>
  <div>
    <mavon-editor v-model="inform" style="position:relative;height: 500px;width: 100%;z-index: 1"/>
    <a-form :form="form" :label-col="{ span: 2 }" :wrapper-col="{ span: 10 }" @submit="handleSubmit"
            style="padding: 30px 0 0 0; ">
      <a-form-item label="标题">
        <a-input
          v-decorator="['title', { rules: [{ required: true, message: '标题不能为空!' }] }]"
        />
      </a-form-item>
      <a-form-item label="接收人">
        <a-select v-decorator="['receiver', { rules: [{ required: true, message: '接收人不能为空!' }] }]">
          <a-select-option value="user">
            用户
          </a-select-option>
          <a-select-option value="admin">
            管理员
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="有效期" block>
        <a-range-picker
          v-decorator="['time', { rules: [{ required: true, message: '有效期不能为空!' }] }]"
        />
      </a-form-item>
      <a-form-item :wrapper-col="{ span: 6, offset: 2 }">
        <a-button type="primary" html-type="submit" block>
          发布
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>

  import {message} from "../../util/message";
  import moment from 'moment';
  import {createAdminInform, createUserInform} from "../../api/inform";

  export default {
    name: "CreateInform",
    data() {
      return {
        inform: "",
        formLayout: 'horizontal',
        form: this.$form.createForm(this, {name: 'coordinated'}),
      }
    },
    methods: {
      handleSubmit: function (e) {
        e.preventDefault();
        this.form.validateFields((err, values) => {
          if (!err) {
            if (this.inform.length === 0) {
              message("通知内容不能为空!", "warning");
              return;
            }
            const data = {
              header: values.title,
              content: this.inform,
              validTime: moment(values.time[1]).valueOf()
            };
            const handler = (data) => {
              message("创建成功");
            };
            const catcher = (code, content) => {
              message(content, "warning");
            };
            if (values.receiver === "user") {
              createUserInform(data, handler, catcher);
            } else if (values.receiver === "admin") {
              createAdminInform(data, handler, catcher);
            }
          }
        });
      },
    },
  }
</script>

<style scoped>
</style>
