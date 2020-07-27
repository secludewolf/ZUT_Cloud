import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'

Vue.config.productionTip = false

router.beforeEach((to, from, next) => {
  /* 路由发生变化修改页面title */
  if (to.meta.title) {
    document.title = to.meta.title + " | Cloud";
  }
  next()
})

//Mock测试
//TODO 自动Token登陆(Token登陆无法通过Mock模拟)
import "./mock/mock";

Vue.use(Antd);

new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>'
})

