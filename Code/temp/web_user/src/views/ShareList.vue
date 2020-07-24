<template>
  <a-layout id="Index" style="height: 100%;background:white;">
    <a-layout-header
      style="position:relative;margin:0 0 6px 0;padding:0;background:white;box-shadow:0 0 6px 0 #B39696;">
      <Header></Header>
    </a-layout-header>
    <a-layout-content style="position:relative;margin:0;padding:0;">
      <a-table :columns="columns"
               :data-source="data"
               :pagination="false"
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
          slot="action" slot-scope="text, record"
          title="确定要禁用分享吗?"
          ok-text="确定"
          cancel-text="取消"
          @confirm="disableShare(record.id)"
        >
          <span>
            <a v-if="record.status === 1">禁用</a>
          </span>
        </a-popconfirm>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import Header from "../components/common/Header";
  import {getFormatDate} from "../util/Utils";

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
      title: 'Action',
      key: 'action',
      scopedSlots: {customRender: 'action'},
      align: "center",
    },
  ];

  const data = [
    {
      id: "share1",
      userId: 17,
      repoId: "test",
      name: "test",
      password: null,
      status: 1,
      createTime: 1594365511023,
      validTime: 1594365511023
    }, {
      id: "share2",
      userId: 17,
      repoId: "test",
      name: "test",
      password: "123456",
      status: 0,
      createTime: 1594365511023,
      validTime: 1594365511023
    },
  ];
  export default {
    name: "ShareList",
    components: {
      Header,
    },
    data() {
      return {
        columns: columns,
        data: data,
      }
    },
    methods: {
      getFormatDate(date) {
        return getFormatDate(date);
      },
      disableShare(id) {
        this.$message.info("禁用分享:" + id);
      }
    },
  }
</script>

<style scoped>

</style>
