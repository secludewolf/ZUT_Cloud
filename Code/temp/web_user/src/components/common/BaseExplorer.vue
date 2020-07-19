<template>
  <a-layout style="position:relative;height: 100%;">
    <a-layout-header
      style="position:relative;background: white;margin:0;padding:0 10px;height: 48px;line-height: 48px;">
      <div style="float: left">
        <a-button type="primary">
          <a-icon type="cloud-upload"/>
          <span>上传</span>
          <input type="file" v-on:change="" multiple="true"
                 style="position: absolute;top: 0;bottom: 0;left: 0;right: 0;height: 40px;width: 88px;opacity: 0;"/>
        </a-button>
        <a-button type="primary">
          <a-icon type="folder-add"/>
          新建文件夹
        </a-button>
        <a-button type="primary">
          <a-icon type="save"/>
          保存分享
        </a-button>
        <a-popconfirm
          title="确定要清空回收站吗?"
          ok-text="确定"
          cancel-text="取消"
          @confirm="cleanRecycleBin"
        >
          <a-icon slot="icon" type="question-circle-o" style="color: red"/>
          <a-button type="danger">
            <a-icon type="delete"/>
            清空回收站
          </a-button>
        </a-popconfirm>
      </div>
      <div style="float: right">
        <div style="float: right;font-size: 28px">
          <a @click="changeSort">
            <a-icon :type="sort"/>
          </a>
          <a @click="changeView">
            <a-icon :type="view"/>
          </a>
        </div>
        <div style="float: right;">
          <a-input-search placeholder="请输入文件名称" @search="searchFile" style="width:300px;padding: 8px"/>
        </div>
      </div>
    </a-layout-header>
    <a-layout-content style="position:relative;background: white;">
      <a-layout>
        <a-layout-header
          style="position:relative;background: white;height:20px;line-height:20px;font-size:12px;margin: 0;padding: 0 10px;">
          <div class="path" style="height: 20px">
            <ul style="display: inline;padding: 0;color: #09AAFF;font-size: 12px;">
              <li style="display: inline" v-if="path.length !== 1">
                <a v-on:click="" style="cursor:pointer"><a>返回上一级</a></a>
                <a-icon style="margin: 0 3px;color: #1890ff;" type="line" :rotate="90"></a-icon>
              </li>
              <li style="display: inline" v-for="(value,index) in path" :key="'path-'+index">
                <a v-on:click="" style="cursor:pointer "><a>{{value}}</a></a>
                <a-icon type="right" style="margin: 0 3px;color: #1890ff;" v-if="index < path.length - 1"></a-icon>
              </li>
              <li style="display: inline;float:right;color: #666;padding-right: 10px">
                <span class="number">已全部加载，共<span> {{total}} </span>个</span>
              </li>
            </ul>
          </div>
        </a-layout-header>
        <a-layout-content style="position:absolute;top: 20px;bottom:0;left:0;right:0;padding:5px;">
          <a-layout-content v-if="view === 'appstore'" style="height: 100%;margin: 0;padding: 0">
            <a-card size="small" hoverable style="width: 120px;float:left;margin: 5px;">
              <img
                slot="cover"
                alt="folder"
                src="../../assets/explorer/folder.svg"
                style="width: 70px;height:70px;margin: 5px auto;"
              />
              <a-card-meta description="文件夹"
                           style="text-align: center;font-size: 12px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;"/>
            </a-card>
            <a-card size="small" hoverable style="width: 120px;float:left;margin: 5px;">
              <img
                slot="cover"
                alt="file"
                src="../../assets/explorer/file_blank.svg"
                style="width: 70px;height:70px;margin: 5px auto;"
              />
              <a-card-meta description="未知文件"
                           style="text-align: center;font-size: 12px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;"/>
            </a-card>
          </a-layout-content>
          <a-layout-content v-if="view !== 'appstore'" style="height: 100%;margin: 0;padding: 0">
            <a-table :columns="columns" :data-source="data" :pagination="false">
              <span slot="tags" slot-scope="tags">
                <a-tag
                  v-for="tag in tags"
                  :key="tag"
                  :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
                >
                  {{ tag.toUpperCase() }}
                </a-tag>
              </span>
              <span slot="action" slot-scope="text, record">
                <a>Invite 一 {{ record.name }}</a>
                <a-divider type="vertical"/>
                <a>Delete</a>
                <a-divider type="vertical"/>
                <a class="ant-dropdown-link"> More actions <a-icon type="down"/> </a>
              </span>
            </a-table>
          </a-layout-content>
        </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>

<script>
  const columns = [
    {
      title: '文件名',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '文件大小',
      dataIndex: 'age',
      key: 'age',
    },
    {
      title: '文件类型',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: '修改日期',
      key: 'tags',
      dataIndex: 'tags',
      scopedSlots: {customRender: 'tags'},
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: {customRender: 'action'},
    },
  ];
  const data = [
    {
      key: '文件1',
      name: '文件1',
      age: 32,
      address: 'New York No. 1 Lake Park',
      tags: ['nice', 'developer'],
    },
    {
      key: '文件2',
      name: '文件2',
      age: 42,
      address: 'London No. 1 Lake Park',
      tags: ['loser'],
    },
    {
      key: '文件夹',
      name: '文件夹',
      age: 32,
      address: 'Sidney No. 1 Lake Park',
      tags: ['cool', 'teacher'],
    },
  ];
  export default {
    name: "BaseExplorer",
    data() {
      return {
        sort: "sort-ascending",
        view: "appstore",
        path: ["全部文件", "第一层", "第二层"],
        data: data,
        columns: columns,
      }
    },
    computed: {
      total: function () {
        //TODO 计算文件个数
        // return Object.keys(this.files).length + Object.keys(this.folders).length;
        return 0;
      }
    },
    methods: {
      cleanRecycleBin() {
        this.$message.success('回收站已清空');
      },
      searchFile(value) {
        console.log("搜索文件:" + value);
      },
      changeSort() {
        if (this.sort === "sort-ascending") {
          this.sort = "sort-descending";
        } else {
          this.sort = "sort-ascending"
        }
      },
      changeView() {
        if (this.view === "appstore") {
          this.view = "unordered-list";
        } else {
          this.view = "appstore"
        }
      }
    }
  }
</script>

<style scoped>

</style>
