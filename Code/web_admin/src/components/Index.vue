<template>
  <a-layout style="height: 100%;">
    <a-layout-sider v-model="collapsed">
      <Menu :collapsed=collapsed></Menu>
    </a-layout-sider>
    <a-layout style="height: 100%;">
      <a-layout-header style="background: #fff; padding: 0">
        <a-icon
          class="trigger"
          :type="collapsed ? 'menu-unfold' : 'menu-fold'"
          @click="() => (this.collapsed = !this.collapsed)"
        />
        <span>Header</span>
        <a-badge :count="unReadInformNumber" style="float: right;margin: 16px 40px 0 0;">
          <a-button @click="showDrawer">
            通知
          </a-button>
        </a-badge>
        <router-link to="/admin">
          <span style="float: right;margin-right: 30px;">管理员</span>
        </router-link>
        <a-drawer
          title="通知列表"
          placement="right"
          width="348"
          :closable="false"
          :visible="visible"
          :after-visible-change="afterVisibleChange"
          @close="onClose"
        >
          <div v-for="(value,index) in informList" :key="index">
            <a-card :title="value.header" style="width: 300px">
              <a slot="extra" v-if="value.status === 0" @click="changeInformStatus(index)">已读</a>
              <p>{{value.content}}</p>
            </a-card>
            <br/>
          </div>
        </a-drawer>
      </a-layout-header>
      <a-layout-content style="height: 100%;;margin: 24px 16px 0">
        <div style="padding: 24px;background: #fff;">
          <router-view/>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script>
  import Menu from "../components/common/Menu";
  import {changeInformStatus, getInformList} from "../api/inform";
  import {message} from "../util/message";

  export default {
    name: 'Index',
    components: {
      Menu
    },
    mounted() {
      const handler = (data) => {
        this.informList = data.informList;
      };
      const catcher = (code, content) => {
        message(content, "warning");
      };
      getInformList(handler, catcher);
    },
    data() {
      return {
        collapsed: false,
        visible: false,
        informList: []
      }
    },
    computed: {
      unReadInformNumber() {
        let count = 0;
        for (let i = 0; i < this.informList.length; i++) {
          console.log(this.informList[i].status);
          if (this.informList[i].status === 0) {
            count++;
          }
        }
        return count;
      }
    },
    methods: {
      changeInformStatus(index) {
        console.log(index);
        const data = this.informList[index].id + "/1";
        const handler = (data) => {
          this.informList[index].status = 1;
        };
        const catcher = (code, content) => {
          message(content, "warning")
        };
        changeInformStatus(data, handler, catcher);
      },
      afterVisibleChange(val) {
        // console.log('visible', val);
      },
      showDrawer() {
        this.visible = true;
      },
      onClose() {
        this.visible = false;
      },
    }
  }
</script>

<style scoped>
  .trigger {
    font-size: 18px;
    line-height: 64px;
    padding: 0 24px;
    cursor: pointer;
    transition: color 0.3s;
  }

  .trigger:hover {
    color: #1890ff;
  }
</style>
