import Vue from 'vue'
import Router from 'vue-router'
import User from '../components/userManager/User'
import Admin from '../components/userManager/Admin'
import File from '../components/fileManager/File'
import Share from "../components/fileManager/Share";
import UserInform from "../components/informManager/UserInform";
import AdminInform from "../components/informManager/AdminInform";
import CreateInform from "../components/informManager/CreateInform";
import Index from "../components/Index";
import AdminInfo from "../components/admin/AdminInfo";
import AdminPassword from "../components/admin/AdminPassword";
import Login from "../components/Login";
import Register from "../components/Register";
import Forget from "../components/Forget";

Vue.use(Router);

export default new Router({
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
    }
  ]
})
