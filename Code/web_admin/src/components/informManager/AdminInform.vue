<template>
  <a-table :columns="columns" :data-source="data"
           :loading="loading"
           :pagination="pagination"
           @change="handleTableChange">
    <div slot="expandedRowRender" slot-scope="record" style="margin: 0">
      <p>{{ record.content }}</p>
    </div>
    <div slot="action" slot-scope="text,record,index">
      <a-button :disabled="record.status === -1" @click="deleteInform(record.id)">禁用</a-button>
    </div>
  </a-table>
</template>
<script>
  import {deleteAdminInform, getAdminInformList} from "../../api/inform";
  import {message} from "../../util/message";
  import {getFormatDate} from "../../util/util";

  const columns = [
    {
      title: '标题',
      dataIndex: 'header',
      key: 'header',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      align: "center",
    },
    {
      title: '有效时间',
      dataIndex: 'validTime',
      key: 'validTime',
      align: "center",
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      align: "center",

    },
    {
      title: '管理员ID',
      dataIndex: 'adminId',
      key: 'adminId',
      align: "center",
    },
    {
      title: '操作',
      dataIndex: 'action',
      key: 'action',
      scopedSlots: {customRender: 'action'},
      align: "center",
    },
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
      }, request() {
        this.loading = true;
        const data = this.pagination.current != null ? this.pagination.current : 1;
        const handler = (data) => {
          const pagination = {...this.pagination};
          pagination.pageSize = 20;
          pagination.total = data.informList.length;
          this.data = data.informList;
          this.pagination = pagination;
          this.loading = false;
        };
        const catcher = (code, content) => {
          message(content, "warning")
        };
        getAdminInformList(data, handler, catcher);
      },
      deleteInform(id) {
        const data = id;
        const handler = () => {
          message("禁用成功");
          for (let i = 0; i < this.data.length; i++) {
            if (this.data[i].id === id) {
              this.data[i].status = -1;
            }
          }
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        deleteAdminInform(data, handler, catcher);
      },
      getFormatDate(date) {
        return getFormatDate(date);
      }
    }
  };
</script>
