<template>
  <a-layout style="position:relative;height: 100%;">
    <a-layout-header
      style="position:relative;background: white;margin:0;padding:0 10px;height: 48px;line-height: 48px;">
      <div style="float: left">
        <a-button type="primary" v-if="!isShare&&(isRepository||isSearch)" :disabled="isSearch">
          <a-icon type="cloud-upload"/>
          <span>上传</span>
          <input type="file" v-on:change="uploadFile" multiple="multiple"
                 style="position: absolute;top: 0;bottom: 0;left: 0;right: 0;height: 40px;width: 88px;opacity: 0;"/>
        </a-button>
        <a-button type="primary" v-if="!isShare&&(isRepository||isSearch)" :disabled="isSearch"
                  v-on:click="createFolderVisible = true">
          <a-icon type="folder-add"/>
          新建文件夹
        </a-button>
        <a-modal v-model="createFolderVisible"
                 title="创建文件夹"
                 okText="确认"
                 cancelText="取消"
                 :confirm-loading="createFolderLoading"
                 @cancel="()=>{this.createFolderVisible = false;this.createFolderName = '';this.createFolderLoading=false}"
                 @ok="createFolder">
          <a-form :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="文件夹名称">
              <a-input placeholder="请输入文件名" v-model="createFolderName" @keyup.enter="createFolder"/>
            </a-form-item>
          </a-form>
        </a-modal>
        <a-button type="primary" v-if="isShare" @click="saveShareShow">
          <a-icon type="save"/>
          保存分享
        </a-button>
        <a-button type="danger" v-if="isShare" @click="()=>{this.menuVisible = false;this.shareReportVisible = true;}">
          <a-icon type="alert"/>
          举报
        </a-button>
        <a-modal
          title="保存分享"
          :visible="saveShareVisible"
          @ok="saveShare"
          @cancel="saveShareVisible = false"
          v-if="!saveShareLoading"
        >
          <a-directory-tree :tree-data="treeData"
                            :defaultExpandedKeys="['/root']"
                            :defaultSelectedKeys="['/root']"
                            @select="selectFolder"
                            @expand="expandFolder"/>
          <div style="text-align: center" v-if="saveShareLoading">
            <a-spin size="large"/>
          </div>
        </a-modal>
        <a-popconfirm
          title="确定要清空回收站吗?"
          ok-text="确定"
          cancel-text="取消"
          @confirm="cleanRecycleBin"
          v-if="isRecycleBin"
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
                <a v-on:click="lastParent" style="cursor:pointer"><a>返回上一级</a></a>
                <a-icon style="margin: 0 3px;color: #1890ff;" type="line" :rotate="90"></a-icon>
              </li>
              <li style="display: inline" v-for="(value,index) in path" :key="'path-'+index">
                <a v-on:click="selectParent(index)" style="cursor:pointer "><a>{{ value }}</a></a>
                <a-icon type="right" style="margin: 0 3px;color: #1890ff;" v-if="index < path.length - 1"></a-icon>
              </li>
              <li style="display: inline;float:right;color: #666;padding-right: 10px">
                <span class="number">已全部加载，共<span> {{ total }} </span>个</span>
              </li>
            </ul>
          </div>
        </a-layout-header>
        <div class='popContainer' v-if="menuVisible" v-on:contextmenu.prevent="" v-on:click="closeMenu"/>
        <a-dropdown :visible="menuVisible"
                    :trigger="['contextmenu']"
                    v-on:contextmenu.prevent.native="showMenu"
                    v-on:click.native="closeMenu">
          <a-layout-content style="position:absolute;top: 20px;bottom:0;left:0;right:0;padding:5px;z-index: 101; ">
            <a-layout-content v-if="view === 'appstore'" style="height: 100%;margin: 0;padding: 0">
              <a-card id="folder"
                      size="small" style="width: 120px;float:left;margin: 5px;" hoverable
                      v-for="(value,index) in folders"
                      :key="index"
                      :index="index"
                      v-on:dblclick="openFile('folder',value.name)">
                <img
                  slot="cover"
                  alt="folder"
                  :src="getImg('folder')"
                  style="width: 70px;height:70px;margin: 5px auto;"
                />
                <a-card-meta :description="value.name"
                             style="text-align: center;font-size: 12px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;"/>
              </a-card>
              <a-card id="file"
                      size="small" style="width: 120px;float:left;margin: 5px;" hoverable
                      v-for="(value,index) in files"
                      :key="index"
                      :index="index"
                      v-on:dblclick="openFile(value.type,value.name)">
                <img
                  slot="cover"
                  alt="file"
                  :src="getImg(value.depth == null ? value.type : 'folder')"
                  style="width: 70px;height:70px;margin: 5px auto;"
                />
                <a-card-meta :description="value.name"
                             style="text-align: center;font-size: 12px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;"/>
              </a-card>
              <a-result title="暂无文件"
                        v-if="Object.keys(files).length === 0 && Object.keys(folders).length === 0">
                <template #icon>
                  <a-icon type="smile" theme="twoTone"/>
                </template>
              </a-result>
            </a-layout-content>
            <a-layout-content id="explorer-table" v-if="view !== 'appstore'" style="height: 100%;margin: 0;padding: 0">
              <a-table
                class="table"
                :columns="columns"
                :data-source="tableSource"
                :pagination="false"
                :rowKey="record => record.name"
                :customRow="rowEvent">
                <div slot="name" slot-scope="text, record">
                  <img
                    slot="cover"
                    alt="file"
                    :src="getImg(record.depth == null ? record.type : 'folder')"
                    style="width: 30px;height:30px;margin-right:10px;"
                  />
                  <span>{{ text }}</span>
                </div>
                <span slot="size" slot-scope="text, record">{{ getFormatSize(record.size) }}</span>
                <span slot="type"
                      slot-scope="text, record">{{ getTypeName(record.depth == null ? record.type : 'folder') }}</span>
                <span slot="changeTime" slot-scope="text">{{ getFormatDate(text) }}</span>
                <span slot="action" slot-scope="text, record, index">
                <span>
                  <a v-if="record.depth==null&&!isRecycleBin" @click="download(index)">
                  <a-icon type="download" style="padding-right:5px;"/>下载</a>
                </span>
                <a v-if="isRecycleBin" @click="restore(index)">
                  <a-icon type="rest" style="padding-right:5px;"/>恢复
                </a>
                  <a-divider v-if="record.depth==null&&isRepository||isRecycleBin" type="vertical"/>
                <a v-if="isRecycleBin||isRepository" @click="delete_(index)">
                  <a-icon type="delete" style="padding-right:5px;"/>删除</a>
              </span>
              </a-table>
            </a-layout-content>
          </a-layout-content>
          <a-menu slot="overlay" style="text-align: center">
            <a-menu-item key="1"
                         v-if="isRepository && (isFile || isFolder)"
                         @click="copy">
              复制
            </a-menu-item>
            <a-menu-item key="2"
                         v-if="isRepository && (isFile || isFolder)"
                         @click="move">
              剪贴
            </a-menu-item>
            <a-menu-item key="3"
                         v-if="isRepository"
                         :disabled="!(lastOption === 'copy' || lastOption === 'move')"
                         @click="paste">
              粘贴
            </a-menu-item>
            <a-menu-item key="4"
                         v-if="isRepository && (isFile || isFolder)"
                         @click="()=>{this.menuVisible = false;this.renameVisible = true}">
              重命名
            </a-menu-item>
            <a-menu-item key="5"
                         v-if="isRepository && isFolder"
                         @click="()=>{this.menuVisible = false;this.shareVisible = true;}">
              分享
            </a-menu-item>
            <a-menu-item key="6"
                         v-if="(isRepository || isShare || isSearch) && (isFile)"
                         @click="download">
              下载
            </a-menu-item>
            <a-menu-item key="7"
                         v-if="isRecycleBin && (isFile || isFolder)"
                         @click="restore">
              恢复
            </a-menu-item>
            <a-menu-item key="8"
                         v-if="(isRepository || isRecycleBin) && (isFile || isFolder)"
                         @click="delete_">
              删除
            </a-menu-item>
            <a-menu-item key="9"
                         v-if="isFile || isFolder"
                         @click="()=>{this.menuVisible = false;this.fileInfoVisible = true;}">
              详细信息
            </a-menu-item>
            <a-menu-item key="10"
                         v-if="(isRepository || isShare || isSearch) && (isFile)"
                         @click="()=>{this.menuVisible = false;this.fileReportVisible = true;}">
              举报
            </a-menu-item>
          </a-menu>
        </a-dropdown>
        <a-modal v-model="renameVisible"
                 title="重命名"
                 okText="确认"
                 cancelText="取消"
                 :confirm-loading="renameLoading"
                 @cancel="()=>{this.renameVisible = false;this.renameName = '';this.renameLoading=false}"
                 @ok="rename">
          <a-form :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
            <a-form-item label="新名称">
              <a-input placeholder="请输入新名称" v-model="renameName"/>
            </a-form-item>
          </a-form>
        </a-modal>
        <a-modal v-model="shareVisible"
                 title="创建分享"
                 okText="确认"
                 cancelText="取消"
                 :maskClosable="false"
                 :confirm-loading="shareLoading"
                 @cancel="()=>{this.shareVisible = false;this.shareName = '';this.sharePassword = '';this.shareTime = null;this.shareLoading = false}"
                 @ok="share">
          <a-form :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="分享名称">
              <a-input placeholder="请输入分享名称" v-model="shareName"/>
            </a-form-item>
            <a-form-item label="分享密码">
              <a-input placeholder="请输入分享密码" v-model="sharePassword"/>
            </a-form-item>
            <a-form-item label="截止日期">
              <a-date-picker
                format="YYYY-MM-DD HH:mm:ss"
                :disabled-date="disabledDate"
                :disabled-time="disabledDateTime"
                :show-time="{ defaultValue: moment('00:00:00', 'HH:mm:ss') }"
                v-model="shareTime"
                :locale="locale"
              />
            </a-form-item>
          </a-form>
        </a-modal>
        <a-modal v-model="shareCreatedVisible"
                 title="创建成功"
                 :footer="null">
          <router-link :to="shareLink" target="_blank">{{ shareLink }}</router-link>
        </a-modal>
        <a-modal v-model="fileInfoVisible"
                 title="详细信息"
                 :footer="null"
                 width="50%">
          <a-descriptions :title="target.name" size="small" bordered>
            <a-descriptions-item label="文件名" :span="3">
              {{ target.name }}
            </a-descriptions-item>
            <a-descriptions-item label="位置" :span="3">
              {{ formatPath() }}
            </a-descriptions-item>
            <a-descriptions-item label="大小" :span="1">
              {{ getFormatSize(target.size) }}
            </a-descriptions-item>
            <a-descriptions-item label="类型" :span="2">
              {{ target.type != null ? target.type.toUpperCase() : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="创建时间" :span="3">
              {{ getFormatDate(target.createTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="修改时间" :span="2">
              {{ getFormatDate(target.changeTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="状态" :span="1">
              {{ target.status === 1 ? '正常' : '异常' }}
            </a-descriptions-item>
            <a-descriptions-item label="深度" :span="2">
              {{ target.depth != null ? target.depth : '-' }}
            </a-descriptions-item>
          </a-descriptions>
        </a-modal>
        <a-modal v-model="fileReportVisible"
                 title="举报文件"
                 okText="确认"
                 cancelText="取消"
                 :maskClosable="false"
                 :confirm-loading="fileReportLoading"
                 @cancel="()=>{this.fileReportVisible = false;this.fileReportType = '';this.fileReportContent = '';this.fileReportLoading = false}"
                 @ok="fileReport">
          <a-form :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="举报类型">
              <a-select default-value="" @change="value => {this.fileReportType = value}" :value="this.fileReportType">
                <a-select-option value="侵权文件">
                  侵权文件
                </a-select-option>
                <a-select-option value="敏感文件">
                  敏感文件
                </a-select-option>
                <a-select-option value="违规文件">
                  违规文件
                </a-select-option>
                <a-select-option value="其他">
                  其他
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="详细信息">
              <a-textarea v-model="fileReportContent" placeholder="请输入详细描述" style="min-height: 200px;"/>
            </a-form-item>
          </a-form>
        </a-modal>
        <a-modal v-model="shareReportVisible"
                 title="举报文件"
                 okText="确认"
                 cancelText="取消"
                 :maskClosable="false"
                 :confirm-loading="shareReportLoading"
                 @cancel="()=>{this.shareReportVisible = false;this.shareReportType = '';this.shareReportContent = '';this.shareReportLoading = false}"
                 @ok="shareReport">
          <a-form :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="举报类型">
              <a-select default-value="" @change="value => {this.shareReportType = value}"
                        :value="this.shareReportType">
                <a-select-option value="侵权分享">
                  侵权分享
                </a-select-option>
                <a-select-option value="敏感分享">
                  敏感分享
                </a-select-option>
                <a-select-option value="违规分享">
                  违规分享
                </a-select-option>
                <a-select-option value="其他">
                  其他
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="详细信息">
              <a-textarea v-model="shareReportContent" placeholder="请输入详细描述" style="min-height: 200px;"/>
            </a-form-item>
          </a-form>
        </a-modal>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>

<script>
import {
  findKey,
  getAllFile,
  getAllFolder,
  getFormatDate,
  getFormatSize,
  getImg,
  getMd5BigFile,
  getMd5SliceFile,
  getMd5SmallFile,
  getTypeName
} from "../../util/Utils";
import locale from 'ant-design-vue/es/date-picker/locale/zh_CN';
import {
  cleanRecycleBin,
  copyFile,
  copyFolder,
  createFile,
  createFolder,
  deleteFromRecycleBin,
  deleteFromRepository, fileReport,
  moveFile,
  moveFolder,
  renameFile, renameFolder, restoreFromRecycleBin
} from "../../api/repository";
import {createShare, saveShare, shareReport} from "../../api/share";
import {uploadBigFile, uploadSmallFile} from "../../api/upload";
import {getDownloadId} from "../../api/download";
import merge from "webpack-merge";
import moment from 'moment';

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
  },
  props: {
    isRepository: {
      type: Boolean,
      default: false
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
      }
    },
    root: {
      type: Object,
      default: () => {
      }
    },
    files: {
      type: Object,
      default: () => {
      }
    }, folders: {
      type: Object,
      default: () => {
      }
    },
    password: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      locale: locale,
      sort: "sort-ascending",
      view: "appstore",
      path: ["全部文件"],
      columns: columns,
      sorted: false,
      createFolderVisible: false,
      createFolderLoading: false,
      createFolderName: "",
      menuVisible: false,
      saveShareVisible: false,
      saveShareLoading: false,
      shareCreatedVisible: false,
      shareLink: "",
      fileInfoVisible: false,
      treeData: {},
      selectFolderKey: "/root",
      isFile: false,
      isFolder: false,
      isSelect: false,
      target: {},
      targetIndex: "",
      optionTarget: {},
      optionTargetIndex: "",
      lastOption: "",
      renameVisible: false,
      renameLoading: false,
      renameName: "",
      shareVisible: false,
      shareLoading: false,
      shareName: "",
      sharePassword: "",
      shareTime: null,
      fileReportVisible: false,
      fileReportLoading: false,
      fileReportType: "",
      fileReportContent: "",
      shareReportVisible: false,
      shareReportLoading: false,
      shareReportType: "",
      shareReportContent: "",
    }
  },
  computed: {
    total: function () {
      return Object.keys(this.files).length + Object.keys(this.folders).length;
    },
    tableSource: function () {
      return Object.values(this.folders).concat(Object.values(this.files));
    }
  },
  watch: {
    //触发排序操作排序
    repository() {
      this.sortFile();
      if (this.isRepository && this.$route.query.path != null) {
        let path = this.$route.query.path.split("/");
        this.updatePath(path);
        this.selectParent(path.length - 1)
      }
    },
    isRepository() {
      this.sorted = false;
    },
    isShare() {
      this.sorted = false;
    },
    isSearch(newValue) {
      this.sorted = false;
      console.log(1, newValue);
      if (newValue) {
        console.log(2);
        this.updatePath(["全部文件"]);
      }
    },
    isRecycleBin(newValue) {
      this.sorted = false;
      if (newValue) {
        this.updatePath(["全部文件"]);
      }
    },
    files() {
      if (!this.sorted) {
        this.sortFile();
      }
    },
    folders() {
      if (!this.sorted) {
        this.sortFile();
      }
    },
    path(newValue) {

    },
  },
  methods: {
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
      const parent = this;
      const data = {
        repositoryId: parent.$store.getters.getRepositoryId,
      };
      const handler = (data) => {
        this.$emit("changeRepository", data.repository);
        this.$message.success('回收站已清空');
      };
      const catcher = (code, content) => {
        message(content, "warning");
      };
      cleanRecycleBin(data, handler, catcher);
    },
    searchFile(value) {
      if (value == null || value === "") {
        this.$emit("changeIsRepository", true);
        this.$emit("changeFiles", this.root.files);
        this.$emit("changeFolders", this.root.folders);
        return;
      }
      this.sorted = false;
      this.$emit("changeIsSearch", true);
      this.updatePath(["全部文件"]);
      let allFiles = {};
      let allFolders = {};
      getAllFile(this.root, allFiles);
      getAllFolder(this.root, allFolders);
      let files = {};
      let folders = {};
      for (let key in allFiles) {
        if (allFiles[key].name.indexOf(value) !== -1) {
          files[Math.random()] = allFiles[key];
        }
      }
      for (let key in allFolders) {
        if (allFolders[key].name.indexOf(value) !== -1) {
          folders[Math.random()] = allFolders[key];
        }
      }
      this.$emit("changeFiles", files);
      this.$emit("changeFolders", folders);
    },
    rowEvent(record, index) {
      return {
        on: {
          dblclick: () => {
            this.openFile(record.type == null ? "folder" : record.type, record.name);
          },
          mouseenter: (event) => {
            if (record.depth != null) {
              event.path[0].setAttribute("id", "folder");
              event.path[0].setAttribute("index", findKey(this.folders, this.tableSource[index]));
            } else {
              event.path[0].setAttribute("id", "file");
              event.path[0].setAttribute("index", findKey(this.files, this.tableSource[index]));
            }
          }
        },
      }
    },
    updatePath(newPath) {
      let path = "";
      for (let i = 0; i < newPath.length; i++) {
        path = path + newPath[i] + "/";
      }
      path = path.substring(0, path.length - 1);
      this.$router.push({query: merge(this.$route.query, {'path': path})})
      this.path = newPath;
    },
    openFile(type, name) {
      if (type === "folder") {
        if (!this.isRepository && !this.isShare) {
          this.$message.warn("暂不支持在搜索结果中打开文件夹");
          return;
        }
        this.sorted = false;
        this.$emit("changeFiles", this.folders[name].files == null ? {} : this.folders[name].files);
        this.$emit("changeFolders", this.folders[name].folders == null ? {} : this.folders[name].folders);
        this.path.push(name);
        this.updatePath(this.path);
      } else {
        this.$message.warn("暂不支持文件预览!");
      }
    },
    lastParent: function () {
      this.selectParent(this.path.length - 2);
    },
    selectParent(index) {
      this.sorted = false;
      if (!this.isRepository && !this.isShare) {
        this.$emit("changeIsRepository", true);
        this.$emit("changeFiles", this.root.files);
        this.$emit("changeFolders", this.root.folders);
        return;
      }
      let folders = this.repository.folder.folders;
      let files = this.repository.folder.files;
      let path = ["全部文件"];
      for (let i = 1; i <= index; i++) {
        if (folders[this.path[i]] == null) {
          this.$emit("changeIsRepository", true);
          this.$emit("changeFiles", this.root.files);
          this.$emit("changeFolders", this.root.folders);
          this.updatePath(["全部文件"]);
          return;
        }
        files = folders[this.path[i]].files;
        folders = folders[this.path[i]].folders;
        path.push(this.path[i]);
      }
      this.$emit("changeFiles", files == null ? {} : files);
      this.$emit("changeFolders", folders == null ? {} : folders);
      this.updatePath(path);
    },
    changeSort() {
      if (this.sort === "sort-ascending") {
        this.sort = "sort-descending";
        this.sortFile("descending");

      } else {
        this.sort = "sort-ascending"
        this.sortFile("ascending");
      }
    },
    sortFile() {
      this.sorted = true;
      let folders = Object.values(this.folders);
      let files = Object.values(this.files);
      if (this.sort === "sort-ascending") {
        folders.sort((a, b) => a.name > b.name ? 1 : -1);
        files.sort((a, b) => a.name > b.name ? 1 : -1);
      } else {
        folders.sort((a, b) => a.name < b.name ? 1 : -1);
        files.sort((a, b) => a.name < b.name ? 1 : -1);
      }
      let foldersMap = {};
      let filesMap = {};
      for (const index in folders) {
        foldersMap[findKey(this.folders, folders[index])] = folders[index];
      }
      for (const index in files) {
        filesMap[findKey(this.files, files[index])] = files[index];
      }
      this.$emit("changeFiles", filesMap);
      this.$emit("changeFolders", foldersMap);
    },
    changeView() {
      if (this.view === "appstore") {
        this.view = "unordered-list";
      } else {
        this.view = "appstore"
      }
    },
    getCurrentPath() {
      let folder = this.root;
      for (let i = 1; i <= this.path.length - 1; i++) {
        folder = folder.folders[this.path[i]];
      }
      return folder.path + "/" + folder.name;
    },
    uploadFile: function (event) {
      const path = this.getCurrentPath();
      console.log("在" + path + "上传文件:" + event.target.value);
      const repositoryId = this.repository.id;
      const parent = this;
      //获取文件列表
      const files = event.target.files;
      //创建上传进度列表
      for (let i = 0; i < files.length; i++) {
        const upload = this.$store.state.upload;
        upload[files[i].name + files[i].size] = {
          name: files[i].name,
          status: "normal",
          fileSize: files[i].size,
          finishSize: 0
        };
        this.$store.commit("updateUpload", Object.assign({}, upload));
      }
      for (let i = 0; i < files.length; i++) {
        //大文件上传
        if (files[i].size > 1024 * 1024 * 5) {
          const next = (md5) => {
            console.log(md5);
            const data = {
              repositoryId: parent.$store.getters.getRepositoryId,
              fileId: md5,
              name: files[i].name,
              path: path
            };
            const handler = (data) => {
              parent.$emit("changeRepository", data.repository);
              const upload = parent.$store.state.upload;
              upload[files[i].name + files[i].size].status = "success";
              upload[files[i].name + files[i].size].finishSize = files[i].size;
              this.$store.commit("updateUpload", Object.assign({}, upload));
              parent.$message.success("秒传成功");
            };
            const catcher = (code, content) => {
              //如果文件已存在
              if (code === -19) {
                parent.$message.warn(content);
                return;
              }
              let chunkSize = 1024 * 1024 * 5,
                chunks = Math.ceil(files[i].size / chunkSize),
                currentChunk = 0,
                fileReader = new FileReader();
              let start;
              let end;
              fileReader.onload = function (event) {
                let blockMd5 = getMd5SliceFile(event);
                console.log(blockMd5);
                //TODO 忽略了返回的已接收分片数据
                //上传文件分片
                uploadBigFile(files[i].slice(start, end), files[i].name, blockMd5, md5, currentChunk, chunks, (data) => {
                  //如果上传完成则尝试保存文件
                  if (data.code === 1 && data.data === null) {
                    const data = {
                      repositoryId: repositoryId,
                      fileId: md5,
                      name: files[i].name,
                      path: path
                    };
                    const handler = (data) => {
                      parent.$emit("changeRepository", data.repository);
                      const upload = parent.$store.state.upload;
                      upload[files[i].name + files[i].size].status = "success";
                      upload[files[i].name + files[i].size].finishSize = files[i].size;
                      parent.$store.commit("updateUpload", Object.assign({}, upload));
                      parent.$message.success("上传成功");
                    };
                    const catcher = (code, content) => {
                      parent.$message.warn(content);
                    };
                    createFile(data, handler, catcher);
                  }
                  currentChunk += 1;
                  if (currentChunk < chunks) {
                    const upload = parent.$store.state.upload;
                    upload[files[i].name + files[i].size].status = "active";
                    upload[files[i].name + files[i].size].finishSize = end;
                    parent.$store.commit("updateUpload", Object.assign({}, upload));
                    loadNext();
                  }
                })
              }

              function loadNext() {
                start = currentChunk * chunkSize
                end = start + chunkSize >= files[i].size ? files[i].size : start + chunkSize;
                fileReader.readAsBinaryString(File.prototype.slice.call(files[i], start, end));
              }

              loadNext();
            }
            createFile(data, handler, catcher);
          };
          getMd5BigFile(files[i], next);
        }
        //小文件上传
        else {
          const next = (md5) => {
            console.log(md5);
            const data = {
              repositoryId: parent.$store.getters.getRepositoryId,
              fileId: md5,
              name: files[i].name,
              path: path
            };
            const handler = (data) => {
              parent.$emit("changeRepository", data.repository);
              const upload = parent.$store.state.upload;
              upload[files[i].name + files[i].size].status = "success";
              upload[files[i].name + files[i].size].finishSize = files[i].size;
              parent.$store.commit("updateUpload", Object.assign({}, upload));
              parent.$message.success("秒传成功");
            };
            const catcher = () => {
              uploadSmallFile(files[i], files[i].name, md5, () => {
                const data = {
                  repositoryId: parent.$store.getters.getRepositoryId,
                  fileId: md5,
                  name: files[i].name,
                  path: path
                };
                const handler = (data) => {
                  parent.$emit("changeRepository", data.repository);
                  const upload = parent.$store.state.upload;
                  upload[files[i].name + files[i].size].status = "success";
                  upload[files[i].name + files[i].size].finishSize = files[i].size;
                  parent.$store.commit("updateUpload", Object.assign({}, upload));
                  parent.$message.success("上传成功");
                };
                const catcher = (code, content) => {
                  parent.$message.warn(content);
                };
                createFile(data, handler, catcher);
              });
            };
            createFile(data, handler, catcher);
          };
          getMd5SmallFile(files[i], next);
        }
      }
    },
    createFolder() {
      if (this.createFolderName == null || this.createFolderName === "") {
        this.$message.warn("文件夹名称为空");
        return;
      }
      this.createFolderLoading = true;
      const path = this.getCurrentPath();
      const parent = this;
      const data = {
        repositoryId: parent.$store.getters.getRepositoryId,
        name: this.createFolderName,
        path: path
      };
      const handler = (data) => {
        parent.$emit("changeRepository", data.repository);
        parent.createFolderName = "";
        parent.createFolderVisible = false;
        parent.createFolderLoading = false;
        parent.$message.success("文件夹创建成功");
      };
      const catcher = (code, content) => {
        parent.$message.warn(content);
        parent.createFolderLoading = false;
      };
      createFolder(data, handler, catcher);
    },
    showMenu(event) {
      this.closeMenu();
      let flag = null;
      for (const index in event.path) {
        if (event.path[index].id === "file") {
          flag = "file";
          this.isFile = true;
          this.targetIndex = event.path[index].getAttribute("index");
          this.target = this.files[this.targetIndex];
          break;
        } else if (event.path[index].id === "folder") {
          flag = "folder";
          this.isFolder = true;
          this.targetIndex = event.path[index].getAttribute("index");
          this.target = this.folders[this.targetIndex];
          break;
        }
      }
      this.menuVisible = true;
    },
    closeMenu() {
      this.menuVisible = false;
      this.isFile = false;
      this.isFolder = false;
    },
    copy() {
      this.lastOption = "copy";
      this.menuVisible = false;
      this.optionTarget = this.target;
      this.optionTargetIndex = this.targetIndex;
      this.$message.info("选中" + this.target.name + "进行复制操作");
    },
    move() {
      this.lastOption = "move";
      this.menuVisible = false;
      this.optionTarget = this.target;
      this.optionTargetIndex = this.targetIndex;
      this.$message.info("选中" + this.target.name + "进行移动操作");
    },
    paste() {
      const path = this.getCurrentPath();
      const parent = this;
      const data = {
        repositoryId: parent.$store.getters.getRepositoryId,
        name: this.optionTarget.name,
        oldPath: this.optionTarget.path,
        newPath: path
      };
      const handler = function (data) {
        if (parent.lastOption === "move") {
          parent.lastOption = "";
          parent.optionTarget = {};
          parent.optionTargetIndex = "";
        }
        parent.$emit("changeRepository", data.repository);
        parent.$message.success("操作成功");
      };
      const catcher = function (code, content) {
        parent.$message.warn(content);
      };
      if (this.lastOption === "copy") {
        if (this.optionTarget.depth == null) {
          copyFile(data, handler, catcher);
        } else {
          copyFolder(data, handler, catcher);
        }
      } else {
        if (this.optionTarget.depth == null) {
          moveFile(data, handler, catcher);
        } else {
          moveFolder(data, handler, catcher);
        }
      }
      this.menuVisible = false;
      this.$message.info("将位置" + this.optionTarget.path + "的" + this.optionTarget.name + (this.lastOption === "copy" ? "复制" : "移动") + "到" + path)
    },
    rename() {
      if (this.renameName === "") {
        this.$message.warn("文件名称不能为空!")
        return;
      }
      this.renameLoading = true;
      const path = this.getCurrentPath();
      const parent = this;
      const data = {
        repositoryId: parent.$store.getters.getRepositoryId,
        oldName: this.target.name,
        newName: this.renameName,
        path: path
      };
      const handler = function (data) {
        parent.$emit("changeRepository", data.repository);
        parent.renameName = "";
        parent.renameLoading = false;
        parent.renameVisible = false;
        parent.$message.success("操作成功");
      };
      const catcher = function (code, content) {
        parent.renameLoading = false;
        parent.$message.warn(content);
      };
      if (this.target.depth == null) {
        renameFile(data, handler, catcher);
      } else {
        renameFolder(data, handler, catcher);
      }
    },
    share() {
      if (this.shareName === "") {
        this.$message.warn("分享名称不能为空!")
        return;
      }
      if (this.shareName.length < 2) {
        this.$message.warn("分享名称过短!")
        return;
      }
      if (this.sharePassword === "") {
        this.$message.warn("分享密码不能为空!")
        return;
      }
      if (this.sharePassword.length < 4) {
        this.$message.warn("分享密码不能少于4位!")
        return;
      }
      if (this.shareTime == null) {
        this.$message.warn("分享失效时间不能为空!")
        return;
      }
      this.shareLoading = true;
      const parent = this;
      const data = {
        repositoryId: parent.$store.getters.getRepositoryId,
        name: this.shareName,
        path: this.target.path + "/" + this.target.name,
        password: this.sharePassword,
        validTime: this.shareTime.valueOf()
      };
      const handler = (data) => {
        parent.shareName = "";
        parent.sharePassword = "";
        parent.shareTime = null;
        parent.shareLoading = false;
        parent.shareVisible = false;
        parent.shareLink = "/share?id=" + data.share.id;
        parent.shareCreatedVisible = true;
        parent.$message.success("分享成功");
      };
      const catcher = (code, content) => {
        parent.shareLoading = false;
        parent.$message.warn(content);
      };
      createShare(data, handler, catcher);
    },
    download(index) {
      if (typeof index != "object") this.tableOptionInit(index);
      this.$message.info("下载位于" + this.target.path + "的" + this.target.name);
      this.menuVisible = false;
      const parent = this;
      const data = {
        repositoryId: null,
        userFileId: this.target.userFileId,
        fileName: this.target.name,
        shareId: null,
        password: null,
        folder: null,//暂不支持多文件下载
      };
      if (!this.isShare) {
        data.repositoryId = this.$store.getters.getRepositoryId
      } else {
        data.shareId = this.$route.query.id;
        data.password = this.password;
      }
      const handler = (data) => {
        console.log(data.id);
        window.open("/api/download/" + data.id, '_blank');
      }
      const catcher = (code, content) => {
        parent.$message.warn(content);
      }
      getDownloadId(data, handler, catcher);
    },
    restore(index) {
      if (typeof index != "object") this.tableOptionInit(index);
      this.$message.info("恢复" + this.target.name + "到" + this.target.path);
      const parent = this;
      const data = {
        repositoryId: parent.$store.getters.getRepositoryId,
        isFile: this.target.depth == null,
        recycleId: this.targetIndex
      };
      const handler = (data) => {
        parent.$emit("changeRepository", data.repository);
        parent.$message.success("操作成功");
      };
      const catcher = (code, content) => {
        parent.$message.warn(content);
      };
      restoreFromRecycleBin(data, handler, catcher);
      this.menuVisible = false;
    },
    delete_(index) {
      if (typeof index != "object") this.tableOptionInit(index);
      const parent = this;
      const path = this.getCurrentPath();
      const data = {
        repositoryId: parent.$store.getters.getRepositoryId,
        isFile: this.target.depth == null,
      };
      const handler = (data) => {
        parent.$emit("changeRepository", data.repository);
        parent.$message.success("操作成功");
      };
      const catcher = (code, content) => {
        parent.$message.warn(content);
      };
      if (this.isRepository) {
        data.name = this.target.name;
        data.path = path;
        deleteFromRepository(data, handler, catcher);
      } else if (this.isRecycleBin) {
        data.recycleId = this.targetIndex;
        deleteFromRecycleBin(data, handler, catcher);
      }
      this.menuVisible = false;
    },
    showInfo() {

    },
    tableOptionInit(index) {
      if (this.tableSource[index].depth != null) {
        this.targetIndex = findKey(this.folders, this.tableSource[index]);
        this.isFolder = true;
        this.target = this.folders[this.targetIndex];
      } else {
        this.targetIndex = findKey(this.files, this.tableSource[index]);
        this.isFile = true;
        this.target = this.files[this.targetIndex];
      }
    },
    saveShareShow() {
      this.saveShareLoading = true;
      const repository = this.repository;
      const root = repository.folder;
      this.createTreeData(null, this.$store.getters.getRoot);
      this.saveShareLoading = false;
      this.saveShareVisible = true;
    },
    createTreeData(parent, folder) {
      let current = {
        title: folder.name,
        key: folder.path + "/" + folder.name,
        children: []
      };
      if (parent === null) {
        this.treeData = [current,];
      } else {
        parent.children.push(current);
      }
      if (folder.folders === null) return;
      for (let key in folder.folders) {
        this.createTreeData(current, folder.folders[key]);
      }
    },
    saveShare() {
      const parent = this;
      const data = {
        shareId: this.$route.query.id,
        password: this.password,
        path: this.selectFolderKey
      };
      const handler = () => {
        parent.$message.success("保存成功");
        parent.saveShareVisible = false;
      };
      const catcher = (code, content) => {
        parent.$message.warn(content);
      };
      saveShare(data, handler, catcher);
    },
    selectFolder(key) {
      this.selectFolderKey = key[0];
    },
    expandFolder(event) {
    },
    formatPath() {
      if (this.target.path != null)
        return this.target.path.replace("/root", "/全部文件");
      else
        return "";
    },
    moment,
    range(start, end) {
      const result = [];
      for (let i = start; i < end; i++) {
        result.push(i);
      }
      return result;
    },
    disabledDate(current) {
      return current && current < moment().endOf('day');
    },
    disabledDateTime() {
      return {
        disabledHours: () => this.range(0, 24).splice(4, 20),
        disabledMinutes: () => this.range(30, 60),
        disabledSeconds: () => [55, 56],
      };
    },
    disabledRangeTime(_, type) {
      if (type === 'start') {
        return {
          disabledHours: () => this.range(0, 60).splice(4, 20),
          disabledMinutes: () => this.range(30, 60),
          disabledSeconds: () => [55, 56],
        };
      }
      return {
        disabledHours: () => this.range(0, 60).splice(20, 4),
        disabledMinutes: () => this.range(0, 31),
        disabledSeconds: () => [55, 56],
      };
    },
    fileReport() {
      if (this.fileReportType === "") {
        this.$message.warn("举报类型不能为空!")
        return;
      }
      this.fileReportLoading = true;
      const parent = this;
      const data = {
        fileId: this.target.id,
        type: this.fileReportType,
        content: this.fileReportContent,
      };
      this.fileReportType = "";
      this.fileReportContent = "";
      console.log(this.fileReportType, this.fileReportContent, data);
      const handler = (data) => {
        this.fileReportLoading = false;
        this.fileReportVisible = false;
        parent.$message.success("举报成功");
      };
      const catcher = (code, content) => {
        parent.fileReportLoading = false;
        parent.$message.warn(content);
      };
      fileReport(data, handler, catcher);
    },
    shareReport() {
      if (this.shareReportType === "") {
        this.$message.warn("举报类型不能为空!")
        return;
      }
      this.shareReportLoading = true;
      const parent = this;
      const data = {
        shareId: this.$route.query.id,
        type: this.shareReportType,
        content: this.shareReportContent,
      };
      this.shareReportType = "";
      this.shareReportContent = "";
      console.log(this.shareReportType, this.shareReportContent, data);
      const handler = (data) => {
        this.shareReportLoading = false;
        this.shareReportVisible = false;
        parent.$message.success("举报成功");
      };
      const catcher = (code, content) => {
        parent.shareReportLoading = false;
        parent.$message.warn(content);
      };
      shareReport(data, handler, catcher);
    },
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
div.popContainer {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0);
  z-index: 100;
}
</style>
