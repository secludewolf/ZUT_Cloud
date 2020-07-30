import Vue from 'vue'
import Vuex from 'vuex'
import {loginByToken} from "../api/admin";

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {},
  state: {
    admin: {
      "id": 0,
      "account": "",
      "email": "",
      "phone": "",
      "name": "加载中...",
      "status": 0,
      "level": 0,
      "createTime": 0,
      "changeTime": 0
    },
  },
  mutations: {},
  actions: {
    loginByToken(context) {
      const handler = function (data) {
        context.state.admin = data.admin;
      };
      const catcher = function (code, content) {
        message.warn(content);
      };
      loginByToken(handler, catcher);
    },
  },
  getters: {
    getAdminId(state) {
      return state.admin.id;
    },
    getAdminName(state) {
      return state.admin.name;
    }
  },
})
