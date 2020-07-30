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
        <div style="float: right;height: 64px;padding:0 30px;background: white;line-height: 64px;">
          <a-dropdown style="padding: 3px;">
            <a class="ant-dropdown-link" @click="e => e.preventDefault()">
              <a-icon type="user" style="margin: 0 5px;"/>
              <!--              TODO 添加Vuex-->
              <!--              {{ this.$store.getters.getUserName }}-->
              管理员名称
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <router-link to="/admin">用户信息</router-link>
              </a-menu-item>
              <a-menu-item>
                <router-link to="/login" @click.native="">退出</router-link>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
          <span style="padding: 0 5px;">|</span>
          <a-badge :count="unReadInformNumber" dot>
            <a-icon type="notification" style="padding: 3px;cursor:pointer;" @click="showDrawer"/>
            <a-drawer
              title="通知"
              placement="right"
              :closable="false"
              :visible="visible"
              @close="onClose"
              width="450px"
            >
              <div v-for="(value,index) in informList"
                   :key="index">
                <a-card :title="value.header">
                  <a slot="extra"
                     v-if="value.status === 0"
                     @click="changeInformStatus(index)">已读</a>
                  <span>{{value.content}}</span>
                </a-card>
                <br/>
              </div>
            </a-drawer>
          </a-badge>
        </div>
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
