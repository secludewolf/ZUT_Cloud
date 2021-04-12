<template>
  <div>
    <a-form :form="form" :label-col="{ span: 2 }" :wrapper-col="{ span: 6 }" @submit="handleSubmit"
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
        <a-date-picker
          format="YYYY-MM-DD HH:mm:ss"
          :disabled-date="disabledDate"
          :disabled-time="disabledDateTime"
          :show-time="{ defaultValue: moment('00:00:00', 'HH:mm:ss') }"
          v-decorator="['time', { rules: [{ required: true, message: '有效期不能为空!' }] }]"
          :locale="locale"
        />
      </a-form-item>
      <a-form-item label="内容">
        <a-textarea
          v-decorator="['content', { rules: [{ required: true, message: '内容不能为空!'},{ min: 5, message: '长度不能小于5字符'}] }]"
          style="min-height: 200px;"
        />
      </a-form-item>
      <a-form-item :wrapper-col="{offset: 2, span: 6 }">
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
          const data = {
            header: values.title,
            content: values.content,
            validTime: values.time.valueOf()
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
    moment,
    range(start, end) {
      const result = [];
      for (let i = start; i < end; i++) {
        result.push(i);
      }
      return result;
    },
    disabledDate(current) {
      return current && current < moment().endOf('day');
    },
    disabledDateTime() {
      return {
        disabledHours: () => this.range(0, 24).splice(4, 20),
        disabledMinutes: () => this.range(30, 60),
        disabledSeconds: () => [55, 56],
      };
    },
    disabledRangeTime(_, type) {
      if (type === 'start') {
        return {
          disabledHours: () => this.range(0, 60).splice(4, 20),
          disabledMinutes: () => this.range(30, 60),
          disabledSeconds: () => [55, 56],
        };
      }
      return {
        disabledHours: () => this.range(0, 60).splice(20, 4),
        disabledMinutes: () => this.range(0, 31),
        disabledSeconds: () => [55, 56],
      };
    }
  },
}
</script>

<style scoped>
</style>
