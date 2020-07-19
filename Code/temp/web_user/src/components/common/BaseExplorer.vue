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
            <a-card size="small" style="width: 120px;float:left;margin: 5px;" hoverable
                    v-for="(value,index) in folders"
                    :key="index">
              <img
                slot="cover"
                alt="folder"
                src="../../assets/explorer/folder.svg"
                style="width: 70px;height:70px;margin: 5px auto;"
              />
              <a-card-meta :description="value.name"
                           style="text-align: center;font-size: 12px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;"/>
            </a-card>
            <a-card size="small" style="width: 120px;float:left;margin: 5px;" hoverable
                    v-for="(value,index) in files"
                    :key="index">
              <img
                slot="cover"
                alt="file"
                src="../../assets/explorer/file_blank.svg"
                style="width: 70px;height:70px;margin: 5px auto;"
              />
              <a-card-meta :description="value.name"
                           style="text-align: center;font-size: 12px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;"/>
            </a-card>
          </a-layout-content>
          <a-layout-content id="explorer-table" v-if="view !== 'appstore'" style="height: 100%;margin: 0;padding: 0">
            <a-table
              class="table"
              :columns="columns"
              :data-source="tableSource"
              :pagination="false"
              :rowKey="record => record.name">
              <div slot="name" slot-scope="text, record">
                <img
                  slot="cover"
                  alt="file"
                  :src="getImg(record.depth == null ? record.type : 'folder')"
                  style="width: 30px;height:30px;margin-right:10px;"
                />
                <span>{{ text }}</span>
              </div>
              <span slot="size" slot-scope="text, record">{{getFormatSize(1)}}</span>
              <span slot="type" slot-scope="text, record">{{getTypeName(record)}}</span>
              <span slot="changeTime" slot-scope="text">{{getFormatDate(text)}}</span>
              <span slot="action" slot-scope="text, record">
                <a><a-icon type="download" style="padding-right:5px;"/>下载</a>
                <a-divider type="vertical"/>
                <a><a-icon type="delete" style="padding-right:5px;"/>删除</a>
              </span>
            </a-table>
          </a-layout-content>
        </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {getFormatDate, getFormatSize, getImg, getTypeName} from "../../util/Utils";

  const columns = [
    {
      title: '文件名',
      dataIndex: 'name',
      key: 'name',
      scopedSlots: {customRender: 'name'},
      width: '30%',
    },
    {
      title: '文件大小',
      dataIndex: 'size',
      key: 'size',
      scopedSlots: {customRender: 'size'},
      width: '10%',
      align: "center",
    },
    {
      title: '文件类型',
      dataIndex: 'type',
      key: 'type',
      scopedSlots: {customRender: 'type'},
      width: '20%',
      align: "center",
    },
    {
      title: '修改日期',
      key: 'changeTime',
      dataIndex: 'changeTime',
      scopedSlots: {customRender: 'changeTime'},
      width: '20%',
      align: "center",
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: {customRender: 'action'},
      width: '20%',
      align: "center",
    },
  ];
  export default {
    name: "BaseExplorer",
    created() {
    },
    mounted() {
      this.loadData();
    },
    props: {
      isRepository: {
        type: Boolean,
        default: true
      },
      isShare: {
        type: Boolean,
        default: false
      },
      isSearch: {
        type: Boolean,
        default: false
      },
      isRecycleBin: {
        type: Boolean,
        default: false
      },
      repository: {
        type: Object,
        default: () => {
          return {
            "id": "5f06b20d17e32b20664aa4c6",
            "userId": 1,
            "status": 1,
            "repoSize": 1099511627776,
            "useSize": 0,
            "folder": {
              "name": "root",
              "path": "",
              "depth": 0,
              "createTime": 1593423709223,
              "changeTime": 1593423709223,
              "status": 1,
              "folders": {
                "folder2": {
                  "name": "folder2",
                  "path": "/root",
                  "depth": 1,
                  "createTime": 1593423709223,
                  "changeTime": 1593423709223,
                  "status": 1,
                  "folders": null,
                  "files": {
                    "file4.txt": {
                      "id": "file4",
                      "userFileId": 4,
                      "shareIdList": null,
                      "name": "file4.txt",
                      "path": "/root/folder2",
                      "type": "txt",
                      "size": 1,
                      "status": 1,
                      "createTime": 1593423709223,
                      "changeTime": 1593423709223
                    }
                  }
                },
                "folder1": {
                  "name": "folder1",
                  "path": "/root",
                  "depth": 1,
                  "createTime": 1593423709223,
                  "changeTime": 1593423709223,
                  "status": 1,
                  "folders": {
                    "folder3": {
                      "name": "folder3",
                      "path": "/root/folder1",
                      "depth": 1,
                      "createTime": 1593423709223,
                      "changeTime": 1593423709223,
                      "status": 1,
                      "folders": null,
                      "files": {
                        "file5.txt": {
                          "id": "file5",
                          "userFileId": 5,
                          "shareIdList": null,
                          "name": "file5.txt",
                          "path": "/root/folder1/folder3",
                          "type": "txt",
                          "size": 1,
                          "status": 1,
                          "createTime": 1593423709223,
                          "changeTime": 1593423709223
                        }
                      }
                    }
                  },
                  "files": {
                    "file3.txt": {
                      "id": "file3",
                      "userFileId": 3,
                      "shareIdList": null,
                      "name": "file3.txt",
                      "path": "/root/folder1",
                      "type": "txt",
                      "size": 1,
                      "status": 1,
                      "createTime": 1593423709223,
                      "changeTime": 1593423709223
                    }
                  }
                }
              },
              "files": {
                "file1.txt": {
                  "id": "file1",
                  "userFileId": 1,
                  "shareIdList": null,
                  "name": "file1.txt",
                  "path": "/root",
                  "type": "txt",
                  "size": 1,
                  "status": 1,
                  "createTime": 1593423709223,
                  "changeTime": 1593423709223
                },
                "file2.txt": {
                  "id": "file2",
                  "userFileId": 2,
                  "shareIdList": null,
                  "name": "file2.txt",
                  "path": "/root",
                  "type": "txt",
                  "size": 1,
                  "status": 1,
                  "createTime": 1593423709223,
                  "changeTime": 1593423709223
                }
              }
            },
            "recycleBin": {"folders": null, "files": null}
          }
        }
      },
      password: {
        type: String,
        default: null
      }
    },
    data() {
      return {
        sort: "sort-ascending",
        view: "appstore",
        path: ["全部文件", "第一层", "第二层"],
        root: {},
        files: {},
        folders: {},
        columns: columns,
      }
    },
    computed: {
      total: function () {
        //TODO 计算文件个数
        // return Object.keys(this.files).length + Object.keys(this.folders).length;
        return 0;
      },
      tableSource: function () {
        return Object.values(this.files).concat(Object.values(this.folders));
      }
    },
    methods: {
      loadData() {
        if (this.isRecycleBin) {
          this.root = {};
          this.files = this.repository.recycleBin.files === null ? {} : this.repository.recycleBin.files;
          this.folders = this.repository.recycleBin.folders === null ? {} : this.repository.recycleBin.folders;
        } else {
          this.root = this.repository.folder;
          this.files = this.repository.folder.files === null ? {} : this.repository.folder.files;
          this.folders = this.repository.folder.folders === null ? {} : this.repository.folder.folders;
        }
      },
      getImg(type) {
        return getImg(type);
      },
      getFormatDate(date) {
        return getFormatDate(date);
      },
      getTypeName(type) {
        return getTypeName(type);
      },
      getFormatSize(size) {
        return getFormatSize(size);
      },
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

<style>
  #explorer-table td {
    padding: 5px 15px;
    overflow-wrap: break-word;
  }
</style>

<style scoped>
</style>
