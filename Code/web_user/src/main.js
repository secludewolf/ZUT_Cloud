import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import axios from 'axios'
import VueAxios from 'vue-axios'
import 'element-ui/lib/theme-chalk/index.css'

//Mock测试
//TODO 自动Token登陆(Token登陆无法通过Mock模拟)
// import "./mock/mock";

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(VueAxios, axios);
new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
});
