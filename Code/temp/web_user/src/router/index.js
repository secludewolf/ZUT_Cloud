import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/Login'
import Index from '../views/Index'
import Share from '../views/Share'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/index'
    }, {
      path: '/login',
      name: 'login',
      component: Login,
      meta: {
        title: "登陆"
      },
    }, {
      path: '/index',
      name: 'index',
      component: Index,
      meta: {
        title: "主页",
      },
    }, {
      path: '/share',
      name: 'share',
      component: Share,
      meta: {
        title: "分享",
      }
    }
  ]
})
