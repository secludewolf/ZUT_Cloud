import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import * as echarts from 'echarts';

Vue.config.productionTip = false;
Vue.prototype.$echarts = echarts;

router.beforeEach((to, from, next) => {
  /* 路由发生变化修改页面title */
  if (to.meta.title) {
    document.title = to.meta.title + " | 控制台";
  }
  next()
})

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
