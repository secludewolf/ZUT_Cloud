<template>
  <a-table :columns="columns" :data-source="data"
           :loading="loading"
           :pagination="pagination"
           @change="handleTableChange">
    <a slot="action" v-if="record.status === 0" slot-scope="record" @click="deleteInform(record.key)">禁用</a>
    <p slot="expandedRowRender" slot-scope="record" style="margin: 0">
      {{ record.content }}
    </p>
  </a-table>
</template>
<script>
  import {changeUserInformStatus, getUserInformList} from "../../api/inform";
  import {message} from "../../util/message";

  const columns = [
    {title: '标题', dataIndex: 'header', key: 'header'},
    {title: '创建时间', dataIndex: 'createTime', key: 'createTime'},
    {title: '有效时间', dataIndex: 'validTime', key: 'validTime'},
    {title: '状态', dataIndex: 'status', key: 'status'},
    {title: '管理员ID', dataIndex: 'adminId', key: 'adminId'},
    {title: 'Action', dataIndex: '', key: 'x', scopedSlots: {customRender: 'action'}},
  ];

  export default {
    name: "UserInform",
    data() {
      return {
        data: [],
        columns: columns,
        pagination: {},
        loading: false,
      };
    },
    mounted() {
      this.request();
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
      }, request(params = {}) {
        this.loading = true;
        const data = this.pagination.current != null ? this.pagination.current : 1;
        const handler = (data) => {
          for (let i = 0; i < data.informList.length; i++) {
            data.informList[i].key = data.informList[i].id;
          }
          const pagination = {...this.pagination};
          pagination.total = 20;
          this.data = data.informList;
          this.pagination = pagination;
          this.loading = false;
        };
        const catcher = (code, content) => {
          message(content, "warning")
        };
        getUserInformList(data, handler, catcher);
      },
      deleteInform(key) {
        console.log(key);
        const data = key + "/1";
        const handler = (data) => {
          message("禁用成功");
          for (let i = 0; i < this.data.length; i++) {
            if (this.data[i].key === key) {
              this.data[i].status = 1;
            }
          }
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        changeUserInformStatus(data, handler, catcher);
      }
    }
  };
</script>
