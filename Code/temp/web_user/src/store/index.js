import Vue from 'vue'
import Vuex from 'vuex'
import {removeTypeTheme} from "ant-design-vue/lib/icon/utils";

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {},
  state: {
    user: {},
    repository: {},
  },
  mutations: {},
  actions: {
    Login(context, parameters) {
    },
    Register(context, parameters) {
    },
    forget(context, parameters) {
    },
    getRepository(context, parameters) {
    },
    getUser(context, parameters) {
    }
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
