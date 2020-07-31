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

const router =  new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      redirect: "/index"
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/forget',
      name: 'Forget',
      component: Forget
    },
    {
      path: '/index',
      name: "Index",
      component: Index,
      children: [
        {
          path: "/admin",
          name: "admin",
          component: AdminInfo
        }, {
          path: "/password",
          name: "password",
          component: AdminPassword
        }, {
          path: "/userManager",
          name: "userManager",
          component: User
        }, {
          path: "/adminManager",
          name: "adminManager",
          component: Admin
        }, {
          path: "/fileManager",
          name: "fileManager",
          component: File
        }, {
          path: "/shareManager",
          name: "shareManager",
          component: Share
        }, {
          path: "/userInform",
          name: "userInform",
          component: UserInform
        }, {
          path: "/adminInform",
          name: "adminInform",
          component: AdminInform
        }, {
          path: "/createInform",
          name: "createInform",
          component: CreateInform
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
