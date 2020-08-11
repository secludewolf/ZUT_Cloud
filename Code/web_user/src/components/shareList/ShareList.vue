<template>
  <a-layout-content style="position:relative;margin:0;padding:0;">
    <a-table :columns="columns"
             :data-source="data"
             :pagination="false"
             :loading="loading"
             :rowKey="record => record.id"
    >
      <router-link :to="'/share?id=' + record.id" target="_blank" slot="name" slot-scope="text,record">
        <a-icon type="link"/>
        {{ text }}
      </router-link>
      <span slot="password" slot-scope="text">{{ text == null ? "无密码" : text }}</span>
      <span slot="createTime" slot-scope="text">{{ getFormatDate(text) }}</span>
      <span slot="validTime" slot-scope="text">{{ getFormatDate(text) }}</span>
      <span slot="status" slot-scope="text">{{ text === 1 ? "有效" : "失效" }}</span>
      <a-popconfirm
        slot="action" slot-scope="text, record, key"
        title="确定要禁用分享吗?"
        ok-text="确定"
        cancel-text="取消"
        @confirm="disableShare(key)"
      >
          <span>
            <a v-if="record.status === 1">禁用</a>
          </span>
      </a-popconfirm>
    </a-table>
  </a-layout-content>
</template>

<script>
  import {getFormatDate} from "../../util/Utils";
  import {deleteShare, getShareList} from "../../api/share";

  const columns = [
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
      scopedSlots: {customRender: 'name'},
      width: "20%",
    },
    {
      title: '密码',
      dataIndex: 'password',
      key: 'password',
      scopedSlots: {customRender: 'password'},
      align: "center",
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      scopedSlots: {customRender: 'createTime'},
      align: "center",
    },
    {
      title: '有效时间',
      dataIndex: 'validTime',
      key: 'validTime',
      scopedSlots: {customRender: 'validTime'},
      align: "center",
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      scopedSlots: {customRender: 'status'},
      align: "center",
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: {customRender: 'action'},
      align: "center",
    },
  ];
  export default {
    name: "ShareList",
    mounted() {
      this.getShareList();
    },
    data() {
      return {
        columns: columns,
        loading: true,
        data: [],
      }
    },
    methods: {
      getFormatDate(date) {
        return getFormatDate(date);
      },
      getShareList() {
        const parent = this;
        const handler = function (data) {
          parent.data = data.shareList;
          parent.loading = false;
        };
        const catcher = function (code, content) {
          parent.$message.warn(content);
          parent.loading = false;
        };
        getShareList(handler, catcher);
      },
      disableShare(index) {
        this.$message.info("禁用分享:" + this.data[index].id);
        const parent = this;
        const data = this.data[index].id;
        const handler = () => {
          parent.data[index].status = 0;
          parent.$message.success("禁用成功");
        };
        const catcher = (code, content) => {
          parent.$message.warn(content);
        };
        deleteShare(data, handler, catcher);
      },
    },
  }
</script>

<style scoped>

</style>
