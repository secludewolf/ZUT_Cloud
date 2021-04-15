<template>
  <div style="width: 100%;padding: 24px;background: #fff;">
    <div style="margin-bottom: 20px;">
      <a-form :form="form" layout="inline" @submit="handleSubmit">
        <a-form-model-item label="类型">
          <a-select v-decorator="['memberType']" placeholder="用户类型" style="width: 150px;">
            <a-select-option value="admin">
              管理员
            </a-select-option>
            <a-select-option value="user">
              用户
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-item label="时间范围">
          <a-range-picker v-decorator="['range-picker']"/>
        </a-form-item>
        <a-form-item
          label="地址">
          <a-input placeholder="请求地址"
                   v-decorator="['actionUrl']"/>
        </a-form-item>
        <a-form-item
          label="IP">
          <a-input placeholder="请求IP"
                   v-decorator="['requestIp']"/>
        </a-form-item>
        <a-form-model-item label="状态">
          <a-select v-decorator="['status']" placeholder="结果状态" style="width: 150px;">
            <a-select-option value="1">
              成功
            </a-select-option>
            <a-select-option value="-1">
              失败
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-button type="primary" html-type="submit" style="float: right">
          筛选
          <a-icon type="search"/>
        </a-button>
      </a-form>
    </div>
    <a-table :columns="columns"
             :data-source="data"
             :loading="loading"
             :pagination="pagination"
             :rowKey="record => record.id"
             @change="handleTableChange">
      <div slot="startTime" slot-scope="text, record,col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, 'status')"
        />
        <template v-else>
          {{ getFormatDate(text) }}
        </template>
      </div>
      <template
        v-for="col in ['actionUrl', 'optionType','optionDesc','requestMethod']"
        :slot="col"
        slot-scope="text, record, index">
        <div :key="col">
          <a-input
            v-if="record.editable"
            style="margin: -5px 0"
            :value="text"
            @change="e => handleChange(e.target.value, record.key, col)"
          />
          <template v-else>
            {{ text }}
          </template>
        </div>
      </template>
      <div slot="consumingTime" slot-scope="text, record,col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, 'status')"
        />
        <template v-else>
          {{ text == 0 ? '<1ms' : text + 'ms' }}
        </template>
      </div>
      <div slot="message" slot-scope="text, record,col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, 'status')"
        />
        <template v-else>
          <span :title=text>{{ text == null ? '无' : (text.length > 50 ? text.slice(0, 50) : text) }}</span>
        </template>
      </div>
      <div slot="status" slot-scope="text, record,col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, 'status')"
        />
        <template v-else>
          {{ text === 1 ? '正常' : '异常' }}
        </template>
      </div>
      <div slot="expandedRowRender" slot-scope="record" style="margin: 0">
        <a-descriptions size="small">
          <a-descriptions-item label="用户ID">
            {{ record.memberId }}
          </a-descriptions-item>
          <a-descriptions-item label="用户类型">
            {{ record.memberType == "admin" ? "管理员" : "用户" }}
          </a-descriptions-item>
          <a-descriptions-item label="状态">
            {{ record.status == 1 ? "正常" : "异常" }}
          </a-descriptions-item>
          <a-descriptions-item label="请求URL">
            {{ record.actionUrl }}
          </a-descriptions-item>
          <a-descriptions-item label="请求类型">
            {{ record.actionMethod }}
          </a-descriptions-item>
          <a-descriptions-item label="请求IP">
            {{ record.requestIp }}
          </a-descriptions-item>
          <a-descriptions-item label="操作类型">
            {{ record.optionType }}
          </a-descriptions-item>
          <a-descriptions-item label="操作描述">
            {{ record.optionDesc }}
          </a-descriptions-item>
          <a-descriptions-item label="操作模块">
            {{ record.optionModel }}
          </a-descriptions-item>
          <a-descriptions-item label="类路径" :span="2">
            {{ record.classPath }}
          </a-descriptions-item>
          <a-descriptions-item label="请求方法" :span="1">
            {{ record.requestMethod }}
          </a-descriptions-item>
          <a-descriptions-item label="请求参数" :span="3">
            {{ record.parameter }}
          </a-descriptions-item>
          <a-descriptions-item label="消息" :span="3">
            {{ record.message == null ? "无" : record.message }}
          </a-descriptions-item>
          <a-descriptions-item label="开始时间">
            {{ getFormatDate(record.startTime, true) }}
          </a-descriptions-item>
          <a-descriptions-item label="结束时间">
            {{ getFormatDate(record.finishTime, true) }}
          </a-descriptions-item>
          <a-descriptions-item label="响应时间">
            {{ record.consumingTime == 0 ? '<1ms' : record.consumingTime + 'ms' }}
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </a-table>
  </div>
</template>
<script>
import {message} from "../../util/message";
import {getLogListManage} from "../../api/log";
import {getFormatDate} from "../../util/util";


const columns = [
  {
    title: '时间',
    dataIndex: 'startTime',
    sorter: true,
    defaultSortOrder: 'descend',
    scopedSlots: {customRender: 'startTime'},
    align: "center",
  },
  {
    title: '地址',
    dataIndex: 'actionUrl',
    sorter: true,
    scopedSlots: {customRender: 'actionUrl'},
    align: "center",
  },
  {
    title: '类型',
    dataIndex: 'optionType',
    sorter: true,
    scopedSlots: {customRender: 'optionType'},
    align: "center",
  },
  {
    title: '描述',
    dataIndex: 'optionDesc',
    sorter: true,
    scopedSlots: {customRender: 'optionDesc'},
    align: "center",
  },
  {
    title: '方法',
    dataIndex: 'requestMethod',
    scopedSlots: {customRender: 'requestMethod'},
    align: "center",
  },
  {
    title: '耗时',
    dataIndex: 'consumingTime',
    scopedSlots: {customRender: 'consumingTime'},
    align: "center",
  },
  {
    title: '消息',
    dataIndex: 'message',
    sorter: true,
    scopedSlots: {customRender: 'message'},
    align: "center",
  },
  {
    title: '状态',
    dataIndex: 'status',
    sorter: true,
    scopedSlots: {customRender: 'status'},
    align: "center",
  },
];

export default {
  name: "Log",
  mounted() {
    this.request(this.pagination);
  },
  data() {
    return {
      form: this.$form.createForm(this, {name: 'admin_filter'}),
      columns,
      data: [],
      cacheData: {},
      editingKey: '',
      pagination: {
        showSizeChanger: true,
        showQuickJumper: true,
        showTotal: () => `共${this.pagination.total}条`,
        total: 0,
        current: 1,
        pageSize: 20,
        pageSizeOptions: ["10", "20", "30", "40", "50"],
      },
      loading: false,
    };
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          this.request(this.pagination);
        }
      });
    },
    handleTableChange(pagination, filters, sorter) {
      const pager = {...this.pagination};
      pager.current = pagination.current;
      this.pagination = pager;
      this.request({
        pageSize: pagination.pageSize,
        current: pagination.current,
        sortKey: sorter.field,
        sortType: sorter.order,
        ...filters,
      });
    },
    request(pagination) {
      this.loading = true;
      let data = pagination.current + "?pageSize=" + pagination.pageSize;
      if (pagination.sortKey != null) {
        data = data + "&sortKey=" + pagination.sortKey;
      }
      if (pagination.sortType != null) {
        if (pagination.sortType === "ascend") {
          data = data + "&sortType=asc";
        } else {
          data = data + "&sortType=desc";
        }
      } else {
        data = data + "&sortType=desc";
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values);
          if (values != null) {
            if (values.memberType != null) {
              data += "&memberType=" + values.memberType;
            }
            if (values.actionUrl != null) {
              data += "&actionUrl=" + values.actionUrl;
            }
            if (values.requestIp != null) {
              data += "&requestIp=" + values.requestIp;
            }
            if (values.status != null && values.status !== 0) {
              data += "&status=" + values.status;
            }
            if (values['range-picker'] != null) {
              data += "&startTime=" + values['range-picker'][0].valueOf();
              data += "&endTime=" + values['range-picker'][1].valueOf();

            }
          }
        }
      });
      const handler = (data) => {
        console.log(data);
        const pagination = {...this.pagination};
        pagination.pageSize = data.pageSize;
        pagination.total = data.total;
        this.data = data.result;
        this.cacheData = data.result.map(item => ({...item}));
        this.pagination = pagination;
        this.loading = false;
      };
      const catcher = (code, content) => {
        message(content, "warning")
        this.loading = false;
      };
      getLogListManage(data, handler, catcher);
    },
    handleChange(value, key, column) {
      const newData = [...this.data];
      const target = newData.filter(item => key === item.key)[0];
      if (target) {
        target[column] = value;
        this.data = newData;
      }
    },
    getFormatDate(date, showMillisecond = false) {
      return getFormatDate(date, showMillisecond);
    },
  },
};
</script>
<style scoped>
.editable-row-operations a {
  margin: 0 8px;
}
</style>
