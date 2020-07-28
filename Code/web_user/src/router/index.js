import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/Login'
import Index from '../views/Index'
import ShareList from '../views/ShareList'
import User from '../views/User'
import ChangePassword from "../views/ChangePassword"
import Share from '../views/Share'
import Register from "../views/Register"
import Forget from "../views/Forget"
import NotFoundTarget from "../views/404"

Vue.use(Router)

const router = new Router({
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
      path: '/changePassword',
      name: 'changePassword',
      component: ChangePassword,
      meta: {
        title: "修改密码",
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

// 登录状态检测
router.beforeEach(function (to, from, next) {
  if (localStorage.getItem("token") === "") {
    if (to.path !== '/login' && to.path !== '/register' && to.path !== '/forget') {
      return next('login');
    }
  }
  next();
});

export default router
