<template>
  <a-table :columns="columns" :data-source="data" bordered
           :loading="loading"
           :pagination="pagination"
           @change="handleTableChange">
    <template
      v-for="col in ['account', 'email', 'phone','name','password','level','status']"
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
    <template slot="operation" slot-scope="text, record, index">
      <div class="editable-row-operations">
        <span v-if="record.editable">
          <a @click="() => cancel(record.key)">取消</a>
          <a @click="() => save(record.key)">更新</a>
          <!--          <a-popconfirm title="确定要修改吗?" @confirm="() => save(record.key)">-->
          <!--            <a>更新</a>-->
          <!--          </a-popconfirm>-->
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
</template>
<script>
  import {changeAdminInfoManage, changeUserInfoManage, deleteAdminManage, getAdminListManage} from "../../api/admin";
  import {message} from "../../util/message";

  const columns = [
    {
      title: '账号',
      dataIndex: 'account',
      scopedSlots: {customRender: 'account'},
      align: "center",
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      scopedSlots: {customRender: 'email'},
      align: "center",
    },
    {
      title: '手机',
      dataIndex: 'phone',
      scopedSlots: {customRender: 'phone'},
      align: "center",
    },
    {
      title: '昵称',
      dataIndex: 'name',
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
      title: '等级',
      dataIndex: 'level',
      scopedSlots: {customRender: 'level'},
      align: "center",
    },
    {
      title: '状态',
      dataIndex: 'status',
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
    name: "Admin",
    mounted() {
      this.request();
    },
    data() {
      return {
        columns,
        data: [],
        cacheData: {},
        editingKey: '',
        pagination: {},
        loading: false,
      };
    },
    methods: {
      handleTableChange(pagination, filters, sorter) {
        console.log(pagination);
        const pager = {...this.pagination};
        pager.current = pagination.current;
        this.pagination = pager;
        this.request({
          results: pagination.pageSize,
          page: pagination.current,
          sortField: sorter.field,
          sortOrder: sorter.order,
          ...filters,
        });
      },
      request() {
        this.loading = true;
        const data = this.pagination.current != null ? this.pagination.current : 1;
        const handler = (data) => {
          console.log(data);
          for (let i = 0; i < data.adminList.length; i++) {
            data.adminList[i].key = data.adminList[i].id;
          }
          this.loading = false;
          const pagination = {...this.pagination};
          pagination.pageSize = 20;
          pagination.total = data.adminCount;
          this.data = data.adminList;
          this.cacheData = data.adminList.map(item => ({...item}));
          this.pagination = pagination;
        };
        const catcher = (code, content) => {
          message(content, "warning")
        };
        getAdminListManage(data, handler, catcher);
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
        const handler = (data) => {
          const temp = [...this.data];
          this.data = temp.filter(item => item.key !== key);
          message("删除成功")
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        deleteAdminManage(data, handler, catcher);
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
          changeAdminInfoManage(data, handler, catcher);
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
    },
  };
</script>
<style scoped>
  .editable-row-operations a {
    margin: 0 8px;
  }
</style>
