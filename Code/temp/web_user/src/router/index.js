import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/Login'
import Index from '../views/Index'
import ShareList from '../views/ShareList'
import User from '../views/User'
import Share from '../views/Share'
import Register from "../views/Register"
import Forget from "../views/Forget"
import NotFoundTarget from "../views/404"

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
      path: '/register',
      name: 'register',
      component: Register,
      meta: {
        title: "注册"
      },
    }, {
      path: '/forget',
      name: 'forget',
      component: Forget,
      meta: {
        title: "忘记密码"
      },
    }, {
      path: '/index',
      name: 'index',
      component: Index,
      meta: {
        title: "主页",
      },
    }, {
      path: '/shareList',
      name: 'shareList',
      component: ShareList,
      meta: {
        title: "分享列表",
      },
    }, {
      path: '/user',
      name: 'user',
      component: User,
      meta: {
        title: "用户信息",
      },
    }, {
      path: '/share',
      name: 'share',
      component: Share,
      meta: {
        title: "分享",
      },
    }, {
      path: '*',
      name: 'error',
      component: NotFoundTarget,
      meta: {
        title: "404",
      },
    },
  ]
})
