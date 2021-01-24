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
                   v-decorator="['phone']"/>
        </a-form-item>
        <a-button type="default" style="margin-left:10px;float: right">
          增加
        </a-button>
        <a-button type="primary" html-type="submit" style="float: right">
          筛选
          <a-icon type="search"/>
        </a-button>
      </a-form>
    </div>
    <a-table :columns="columns"
             :rowKey="record => record.id"
             :data-source="data"
             :loading="loading"
             :pagination="pagination"
             @change="handleTableChange">
      <div slot="expandedRowRender" slot-scope="record" style="margin: 0">
        <p>{{ record.content }}</p>
      </div>
      <p slot="createTime" slot-scope="text,record,index">{{getFormatDate(text)}}</p>
      <p slot="validTime" slot-scope="text,record,index">{{getFormatDate(text)}}</p>
      <div slot="action" slot-scope="text,record,index">
        <a-button :disabled="record.status === -1" @click="deleteInform(record.id)">禁用</a-button>
      </div>
    </a-table>
  </div>
</template>
<script>
  import {deleteUserInform, getUserInformList} from "../../api/inform";
  import {message} from "../../util/message";
  import {getFormatDate} from "../../util/util";

  const columns = [
    {
      title: '标题',
      dataIndex: 'header',
      sorter: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      sorter: true,
      scopedSlots: {customRender: 'createTime'},
      align: "center",
    },
    {
      title: '有效时间',
      dataIndex: 'validTime',
      sorter: true,
      scopedSlots: {customRender: 'validTime'},
      align: "center",
    },
    {
      title: '状态',
      dataIndex: 'status',
      sorter: true,
      align: "center",
    },
    {
      title: '管理员ID',
      dataIndex: 'adminId',
      sorter: true,
      align: "center",
    },
    {
      title: '操作',
      dataIndex: 'action',
      scopedSlots: {customRender: 'action'},
      align: "center",
    },
  ];

  export default {
    name: "UserInform",
    data() {
      return {
        form: this.$form.createForm(this, {name: 'user_filter'}),
        data: [],
        columns: columns,
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
    mounted() {
      this.request(this.pagination);
    },
    methods: {
      handleSubmit(e) {
        e.preventDefault();
        this.form.validateFields((err, values) => {
          if (!err) {
            console.log('Received values of form: ', values);
          }
        });
      },
      handleTableChange(pagination, filters, sorter) {
        console.log(pagination);
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
        }
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
        getUserInformList(data, handler, catcher);
      },
      deleteInform(id) {
        const data = id;
        const handler = () => {
          message("禁用成功");
          for (let i = 0; i < this.data.length; i++) {
            if (this.data[i].id === id) {
              this.data[i].status = -1;
              break;
            }
          }
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        deleteUserInform(data, handler, catcher);
      },
      getFormatDate(date) {
        return getFormatDate(date);
      }
    }
  };
</script>
