import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Vue.config.productionTip = false;


//Mock测试
//import "./mock/mock";

Vue.use(Antd);
Vue.use(mavonEditor);

new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>'
});
