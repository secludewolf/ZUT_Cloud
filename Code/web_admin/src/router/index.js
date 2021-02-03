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
import Log from "../components/logManager/Log";

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
            parent: '账号管理',
            title: '用户',
          },
        }, {
          path: "/adminManager",
          name: "adminManager",
          component: Admin,
          meta: {
            parent: '账号管理',
            title: '管理员',
          },
        }, {
          path: "/fileManager",
          name: "fileManager",
          component: File,
          meta: {
            parent: '数据管理',
            title: '文件',
          },
        }, {
          path: "/shareManager",
          name: "shareManager",
          component: Share,
          meta: {
            parent: '数据管理',
            title: '分享',
          },
        }, {
          path: "/userInform",
          name: "userInform",
          component: UserInform,
          meta: {
            parent: '通知管理',
            title: '用户',
          },
        }, {
          path: "/adminInform",
          name: "adminInform",
          component: AdminInform,
          meta: {
            parent: '通知管理',
            title: '管理员',
          },
        }, {
          path: "/createInform",
          name: "createInform",
          component: CreateInform,
          meta: {
            parent: '通知管理',
            title: '创建',
          },
        }, {
          path: "/log",
          name: "log",
          component: Log,
          meta: {
            parent: '日志管理',
            title: '日志',
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
