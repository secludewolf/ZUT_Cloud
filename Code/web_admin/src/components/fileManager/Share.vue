<template>
  <a-table bordered
           :columns="columns"
           :data-source="data"
           :loading="loading"
           :pagination="pagination"
           :rowKey="record => record.id"
           @change="handleTableChange">
    <template
      v-for="col in ['name', 'userId']"
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
    <div slot="password" slot-scope="text, record,col">
      <a-input
        v-if="record.editable"
        style="margin: -5px 0"
        :value="text"
        @change="e => handleChange(e.target.value, record.key, 'password')"
      />
      <template v-else>
        {{ (text === '') ? '无密码' : text }}
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
        {{ text === 1 ? '正常' : '失效' }}
      </template>
    </div>
    <div slot="validTime" slot-scope="text, record,col">
      <a-input
        v-if="record.editable"
        style="margin: -5px 0"
        :value="text"
        @change="e => handleChange(e.target.value, record.key, 'validTime')"
      />
      <template v-else>
        {{ getFormatDate(text) }}
      </template>
    </div>
    <template slot="operation" slot-scope="text, record, index">
      <div class="editable-row-operations">
        <div>
          <!--          <a :disabled="editingKey !== ''" @click="() => edit(record.key)">编辑</a>-->
          <a-popconfirm
            title="确定要禁用此分享吗?"
            @confirm="() => onDelete(record.key)">
            <a href="javascript:">禁用</a>
          </a-popconfirm>
        </div>
        <!--                <span v-if="record.editable">-->
        <!--                  <a @click="() => cancel(record.key)">取消</a>-->
        <!--                  <a @click="() => save(record.key)">更新</a>-->
        <!--                  &lt;!&ndash;          <a-popconfirm title="确定要修改吗?" @confirm="() => save(record.key)">&ndash;&gt;-->
        <!--                  &lt;!&ndash;            <a>更新</a>&ndash;&gt;-->
        <!--                  &lt;!&ndash;          </a-popconfirm>&ndash;&gt;-->
        <!--                </span>-->
      </div>
    </template>
  </a-table>
</template>
<script>
  import {deleteShare, getShareList} from "../../api/share";
  import {message} from "../../util/message";
  import {getFormatDate} from "../../util/util";

  const columns = [
    {
      title: '名称',
      dataIndex: 'name',
      scopedSlots: {customRender: 'name'},
      align: "center",
    },
    {
      title: '用户ID',
      dataIndex: 'userId',
      scopedSlots: {customRender: 'userId'},
      align: "center",
    },
    {
      title: '密码',
      dataIndex: 'password',
      scopedSlots: {customRender: 'password'},
      align: "center",
    },
    {
      title: '状态',
      dataIndex: 'status',
      scopedSlots: {customRender: 'status'},
      align: "center",
    },
    {
      title: '截止日期',
      dataIndex: 'validTime',
      scopedSlots: {customRender: 'validTime'},
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
    name: "Share",
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
          const pagination = {...this.pagination};
          pagination.pageSize = 20;
          pagination.total = data.shareCount;
          this.data = data.shareList;
          this.cacheData = data.shareList.map(item => ({...item}));
          this.pagination = pagination;
          this.loading = false;
        };
        const catcher = (code, content) => {
          message(content, "warning")
        };
        getShareList(data, handler, catcher);
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
        const data = key;
        const handler = (data) => {
          const temp = [...this.data];
          this.data = temp.filter(item => item.key !== key);
          message("删除成功")
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        deleteShare(data, handler, catcher);
      },
      edit(key) {
        const newData = [...this.data];
        const target = newData.filter(item => key === item.key)[0];
        this.editingKey = key;
        if (target) {
          target.editable = true;
          this.data = newData;
        }
      },
      save(key) {
        const newData = [...this.data];
        const newCacheData = [...this.cacheData];
        const target = newData.filter(item => key === item.key)[0];
        const targetCache = newCacheData.filter(item => key === item.key)[0];
        if (target && targetCache) {
          delete target.editable;
          this.data = newData;
          Object.assign(targetCache, target);
          this.cacheData = newCacheData;
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
      getFormatDate(date) {
        return getFormatDate(date);
      }
    },
  };
</script>
<style scoped>
  .editable-row-operations a {
    margin: 0 8px;
  }
</style>
