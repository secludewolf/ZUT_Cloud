import Vue from 'vue'
import Vuex from 'vuex'
import {loginByToken} from "../api/user";
import {message} from 'ant-design-vue';

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {},
  state: {
    user: {
      "id": 0,
      "repoId": "0",
      "account": "",
      "email": "",
      "phone": "",
      "name": "",
      "status": 0,
      "level": 0,
      "createTime": 0,
      "changeTime": 0
    },
    repository: {
      "id": 0,
      "userId": 0,
      "status": 0,
      "repoSize": 0,
      "useSize": 0,
      "folder": {
        "name": "root",
        "path": "/",
        "depth": 0,
        "createTime": 0,
        "changeTime": 0,
        "status": 1,
        "folders": {},
        "files": {}
      },
      "recycleBin": {"folders": null, "files": null}
    },
  },
  mutations: {
    updateUser(state, user) {
      state.user = user;
    },
    updateRepository(state, repository) {
      state.repository = repository;
    },
  },
  actions: {
    loginByToken(context) {
      const handler = function (data) {
        context.state.user = data.user;
        context.state.repository = data.repository;
      };
      const catcher = function (code, content) {
        message.warn(content);
      };
      loginByToken(handler, catcher);
    },
    getUserInfo(context, parameters) {

    },
    getRepository(context, parameters) {

    },
  },
  getters: {
    getUserName(state) {
      return state.user.name;
    },
    getRepositoryId(state) {
      return state.repository.id;
    },
    getRepositoryRepoSize(state) {
      return state.repository.repoSize;
    },
    getRepositoryUseSize(state) {
      return state.repository.useSize;
    }
  },
})
