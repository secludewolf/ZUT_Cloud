<template>
  <div style="height: 100%;width: 100%">
    <div style="margin-bottom: 20px;">
      <a-form :form="form" layout="inline" @submit="handleSubmit">
        <a-form-model-item label="状态">
          <a-select v-decorator="['status']" placeholder="文件状态" style="width: 150px;">
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
        <a-form-model-item label="类型">
          <a-select v-decorator="['type']" placeholder="文件类型" style="width: 150px;">
            <a-select-option value="txt">
              文本文件
            </a-select-option>
            <a-select-option value="document">
              文档
            </a-select-option>
            <a-select-option value="audio">
              音频
            </a-select-option>
            <a-select-option value="video">
              视频
            </a-select-option>
            <a-select-option value="picture">
              图片
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="大小">
          <a-select v-decorator="['size']" placeholder="文件大小" style="width: 150px;">
            <a-select-option value="1">
              <1MB
            </a-select-option>
            <a-select-option value="30">
              1MB~30MB
            </a-select-option>
            <a-select-option value="50">
              30MB~50MB
            </a-select-option>
            <a-select-option value="100">
              50MB~100MB
            </a-select-option>
            <a-select-option value="1024">
              100MB~1GB
            </a-select-option>
            <a-select-option value="0">
              >1GB
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-item
          label="文件名">
          <a-input placeholder="文件名"
                   v-decorator="['name']"/>
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
        v-for="col in ['name','quoteNumber']"
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
      <div slot="size" slot-scope="text, record,col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, 'size')"
        />
        <template v-else>
          {{ getFormatSize(text) }}
        </template>
      </div>
      <div slot="type" slot-scope="text, record,col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, 'type')"
        />
        <template v-else>
          {{ text.toUpperCase() }}
        </template>
      </div>
      <template slot="operation" slot-scope="text, record, index">
        <div class="editable-row-operations">
          <!--                <span v-if="record.editable">-->
          <!--                  <a @click="() => cancel(record.key)">取消</a>-->
          <!--                  <a @click="() => save(record.key)">更新</a>-->
          <!--                  &lt;!&ndash;          <a-popconfirm title="确定要修改吗?" @confirm="() => save(record.key)">&ndash;&gt;-->
          <!--                  &lt;!&ndash;            <a>更新</a>&ndash;&gt;-->
          <!--                  &lt;!&ndash;          </a-popconfirm>&ndash;&gt;-->
          <!--                </span>-->
          <div>
            <!--          <a :disabled="editingKey !== ''" @click="() => edit(record.key)">编辑</a>-->
            <a-popconfirm
              title="确定要禁用此文件吗?"
              @confirm="() => onDelete(record.id)">
              <a-button :disabled="record.status !== 1" href="javascript:">禁用</a-button>
            </a-popconfirm>
          </div>
        </div>
      </template>
    </a-table>
  </div>
</template>
<script>
import {deleteFile, getFileList} from "../../api/file";
import {message} from "../../util/message";
import {getFormatSize} from "../../util/util";

const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    sorter: true,
    scopedSlots: {customRender: 'name'},
    align: "center",
  },
  {
    title: '大小',
    dataIndex: 'size',
    sorter: true,
    scopedSlots: {customRender: 'size'},
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
    title: '类型',
    dataIndex: 'type',
    sorter: true,
    scopedSlots: {customRender: 'type'},
    align: "center",
  },
  {
    title: '引用数',
    dataIndex: 'quoteNumber',
    sorter: true,
    scopedSlots: {customRender: 'quoteNumber'},
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
  name: "File",
  mounted() {
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
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values);
          if (values != null) {
            if (values.type != null) {
              data += "&type=" + values.type;
            }
            if (values.size != null) {
              data += "&size=" + values.size;
            }
            if (values.name != null) {
              data += "&name=" + values.name;
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
      getFileList(data, handler, catcher);
    },
    handleChange(value, key, column) {
      const newData = [...this.data];
      const target = newData.filter(item => key === item.key)[0];
      if (target) {
        target[column] = value;
        this.data = newData;
      }
    },
    onDelete(id) {
      const data = id;
      const handler = () => {
        // const temp = [...this.data];
        // this.data = temp.filter(item => item.id !== id);
        for (let i = 0; i < this.data.length; i++) {
          if (this.data[i].id === id) {
            this.data[i].status = -1;
            break;
          }
        }
        message("禁用成功")
      };
      const catcher = (code, content) => {
        message(content, "warning");
      };
      deleteFile(data, handler, catcher);
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
    getFormatSize(size) {
      return getFormatSize(size);
    }
  },
};
</script>
<style scoped>
.editable-row-operations a {
  margin: 0 8px;
}
</style>
