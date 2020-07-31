import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/Login'
import Register from "../views/Register"
import Forget from "../views/Forget"
import Index from "../views/Index";
import IndexExplorer from "../components/index/Explorer";
import ShareExplorer from "../components/share/Explorer";
import ShareList from "../components/shareList/ShareList";
import User from "../components/user/User";
import ChangePassword from "../components/user/ChangePassword";
import NotFoundTarget from "../views/404"

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
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
      path: '/',
      redirect: '/index',
      component: Index,
      children: [
        {
          path: '/index',
          name: 'index',
          component: IndexExplorer,
          meta: {
            title: "主页"
          },
        }, {
          path: '/share',
          name: 'share',
          component: ShareExplorer,
          meta: {
            title: "分享"
          },
        }, {
          path: '/shareList',
          name: 'shareList',
          component: ShareList,
          meta: {
            title: "分享列表"
          },
        }, {
          path: '/user',
          name: 'user',
          component: User,
          meta: {
            title: "用户信息"
          },
        }, {
          path: '/changePassword',
          name: 'changePassword',
          component: ChangePassword,
          meta: {
            title: "分享列表"
          },
        },
      ],
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
