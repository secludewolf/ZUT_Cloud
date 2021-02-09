<template>
  <div style="height: 100%;width: 100%">
    <div style="margin-bottom: 20px;">
      <a-form :form="form" layout="inline" @submit="handleSubmit">
        <a-form-model-item label="状态">
          <a-select v-decorator="['status']" placeholder="账号状态" style="width: 150px;">
            <a-select-option value="1">
              正常
            </a-select-option>
            <a-select-option value="-1">
              停用
            </a-select-option>
            <a-select-option value="0">
              无限制
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-item label="时间范围">
          <a-range-picker v-decorator="['range-picker']"/>
        </a-form-item>
        <a-form-item
          label="手机号">
          <a-input placeholder="手机号"
                   v-decorator="['phone']"/>
        </a-form-item>
        <a-form-item
          label="邮箱">
          <a-input placeholder="邮箱"
                   v-decorator="['email']"/>
        </a-form-item>
        <a-form-item
          label="账号">
          <a-input placeholder="账号"
                   v-decorator="['account']"/>
        </a-form-item>
<!--        <a-button type="default" style="margin-left:10px;float: right">-->
        <!--          增加-->
        <!--        </a-button>-->
        <a-button type="primary" html-type="submit" style="float: right">
          筛选
          <a-icon type="search"/>
        </a-button>
      </a-form>
    </div>
    <a-table bordered
             :columns="columns"
             :data-source="data"
             :loading="loading"
             :pagination="pagination"
             :rowKey="record => record.id"
             @change="handleTableChange">
      <template
        v-for="col in ['account', 'email', 'phone','name','password','level']"
        :slot="col"
        slot-scope="text, record, index"
      >
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
      <div slot="repoSize" slot-scope="text, record">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, 'repoSize')"
        />
        <template v-else>
          {{ getFormatSize(record.repoSize) }}
        </template>
      </div>
      <div slot="status" slot-scope="text, record">
        <template>
          {{ text === 1 ? '正常' : '异常' }}
        </template>
      </div>
      <template slot="operation" slot-scope="text, record, index">
        <div class="editable-row-operations">
        <span v-if="record.editable">
          <a @click="() => cancel(record.key)">取消</a>
          <a-popconfirm title="确定要修改吗?" @confirm="() => save(record.key)">
            <a>更新</a>
          </a-popconfirm>
        </span>
          <div v-else>
            <a :disabled="editingKey !== ''" @click="() => edit(record.key)">编辑</a>
            <a-popconfirm
              title="确定要删除此用户吗?"
              @confirm="() => onDelete(record.key)"
            >
              <a href="javascript:">删除</a>
            </a-popconfirm>
          </div>
        </div>
      </template>
    </a-table>
  </div>
</template>
<script>
import {changeUserInfoManage, deleteUserManage, getUserListManage} from "../../api/admin";
import {message} from "../../util/message";
import {getFormatSize} from "../../util/util";

const columns = [
  {
    title: '账号',
    dataIndex: 'account',
    sorter: true,
    defaultSortOrder: 'ascend',
    scopedSlots: {customRender: 'account'},
    align: "center",
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    sorter: true,
    scopedSlots: {customRender: 'email'},
    align: "center",
  },
  {
    title: '手机',
    dataIndex: 'phone',
    sorter: true,
    scopedSlots: {customRender: 'phone'},
    align: "center",
  },
  {
    title: '昵称',
    dataIndex: 'name',
    sorter: true,
    scopedSlots: {customRender: 'name'},
    align: "center",
  },
  {
    title: '密码',
    dataIndex: 'password',
    scopedSlots: {customRender: 'password'},
    align: "center",
  },
  {
    title: '仓库大小',
    dataIndex: 'repoSize',
    scopedSlots: {customRender: 'repoSize'},
    align: "center",
  },
  {
    title: '等级',
    dataIndex: 'level',
    sorter: true,
    align: "center",
  },
  {
    title: '状态',
    dataIndex: 'status',
    sorter: true,
    scopedSlots: {customRender: 'status'},
    align: "center",
  },
  {
    title: '操作',
    dataIndex: 'operation',
    width: '150px',
    scopedSlots: {customRender: 'operation'},
    align: "center",
  },
];

export default {
  name: "User",
  mounted() {
    //请求数据
    this.request(this.pagination);
  },
  data() {
    return {
      form: this.$form.createForm(this, {name: 'user_filter'}),
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
        data += "&sortKey=" + pagination.sortKey;
      }
      if (pagination.sortType != null) {
        if (pagination.sortType === "ascend") {
          data += "&sortType=asc";
        } else {
          data += "&sortType=desc";
        }
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values);
          if (values != null) {
            if (values.account != null) {
              data += "&account=" + values.account;
            }
            if (values.phone != null) {
              data += "&phone=" + values.phone;
            }
            if (values.email != null) {
              data += "&email=" + values.email;
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
      getUserListManage(data, handler, catcher);
    },
    handleChange(value, key, column) {
      const newData = [...this.data];
      const target = newData.filter(item => key === item.key)[0];
      if (target) {
        target[column] = value;
        this.data = newData;
      }
    },
    onDelete(key) {
      const data = {
        id: key,
      };
      const handler = () => {
        const temp = [...this.data];
        this.data = temp.filter(item => item.key !== key);
        message("删除成功")
      };
      const catcher = (code, content) => {
        message(content, "warning");
      };
      deleteUserManage(data, handler, catcher);
    },
    edit(key) {
      const newData = [...this.data];
      const target = newData.filter(item => key === item.key)[0];
      this.editingKey = key;
      if (target) {
        target.editable = true;
        this.data = newData;
      }
      console.log(this.data);
    },
    save(key) {
      const newData = [...this.data];
      const newCacheData = [...this.cacheData];
      const target = newData.filter(item => key === item.key)[0];
      const targetCache = newCacheData.filter(item => key === item.key)[0];
      if (target && targetCache) {
        const data = {
          id: target.id,
          account: target.account,
          phone: target.phone,
          email: target.email,
          name: target.name,
          password: target.password,
          repoSize: target.repoSize,
          level: target.level,
          status: target.status
        };
        const handler = (data) => {
          console.log(data);
          delete target.editable;
          this.data = newData;
          Object.assign(targetCache, target);
          this.cacheData = newCacheData;
          message("修改成功")
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        changeUserInfoManage(data, handler, catcher);
      }
      this.editingKey = '';
    },
    cancel(key) {
      const newData = [...this.data];
      const target = newData.filter(item => key === item.key)[0];
      this.editingKey = '';
      if (target) {
        Object.assign(target, this.cacheData.filter(item => key === item.key)[0]);
        delete target.editable;
        this.data = newData;
      }
    },
    getFormatSize(size) {
      return getFormatSize(size, 0);
    }
  },
};
</script>
<style scoped>
.editable-row-operations a {
  margin: 0 8px;
}
</style>
