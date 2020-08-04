import Vue from 'vue'
import Router from 'vue-router'
import User from '../components/userManager/User'
import Admin from '../components/userManager/Admin'
import File from '../components/fileManager/File'
import Share from "../components/fileManager/Share";
import UserInform from "../components/informManager/UserInform";
import AdminInform from "../components/informManager/AdminInform";
import CreateInform from "../components/informManager/CreateInform";
import Index from "../views/Index";
import AdminInfo from "../components/admin/AdminInfo";
import AdminPassword from "../components/admin/AdminPassword";
import Login from "../views/Login";
import Register from "../views/Register";
import Forget from "../views/Forget";
import NotFoundTarget from "../views/404"

Vue.use(Router);

const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      redirect: "/index"
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        title: '登陆',
      },
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      meta: {
        title: '注册',
      },
    },
    {
      path: '/forget',
      name: 'Forget',
      component: Forget,
      meta: {
        title: '忘记密码',
      },
    },
    {
      path: '/index',
      name: "Index",
      component: Index,
      children: [
        {
          path: "/admin",
          name: "admin",
          component: AdminInfo,
          meta: {
            title: '用户信息',
          },
        }, {
          path: "/password",
          name: "password",
          component: AdminPassword,
          meta: {
            title: '修改密码',
          },
        }, {
          path: "/userManager",
          name: "userManager",
          component: User,
          meta: {
            title: '用户管理',
          },
        }, {
          path: "/adminManager",
          name: "adminManager",
          component: Admin,
          meta: {
            title: '管理员管理',
          },
        }, {
          path: "/fileManager",
          name: "fileManager",
          component: File,
          meta: {
            title: '文件管理',
          },
        }, {
          path: "/shareManager",
          name: "shareManager",
          component: Share,
          meta: {
            title: '分享管理',
          },
        }, {
          path: "/userInform",
          name: "userInform",
          component: UserInform,
          meta: {
            title: '用户通知管理',
          },
        }, {
          path: "/adminInform",
          name: "adminInform",
          component: AdminInform,
          meta: {
            title: '管理员通知管理',
          },
        }, {
          path: "/createInform",
          name: "createInform",
          component: CreateInform,
          meta: {
            title: '创建通知',
          },
        },
      ],
      redirect: "/userManager"
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
