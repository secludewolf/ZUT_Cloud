import Vue from 'vue'
import Router from 'vue-router'
import Index from "../components/Index";
import ShareList from "../components/ShareList";
import User from "../components/User";
import ChangePassword from "../components/ChangePassword";
import Share from "../components/Share";
import ShareError from "../components/error/ShareError";
import Login from "../components/Login";
import Register from "../components/Register";
import Forget from "../components/Forget";
import Error from "../components/error/Error";

Vue.use(Router);
const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
};
const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/index'
    }, {
      path: '/index',
      name: 'index',
      component: Index
    }, {
      path: '/shareList',
      name: 'shareList',
      component: ShareList
    }, {
      path: '/user',
      name: 'user',
      component: User
    }, {
      path: '/changePassword',
      name: 'changePassword',
      component: ChangePassword
    }, {
      path: '/share',
      name: 'share',
      component: Share
    }, {
      path: '/shareError',
      name: 'shareError',
      component: ShareError
    }, {
      path: '/login',
      name: 'login',
      component: Login
    }, {
      path: '/register',
      name: 'register',
      component: Register
    }, {
      path: '/forget',
      name: 'forget',
      component: Forget
    }, {
      path: '/404',
      name: '404',
      component: Error
    }, {
      path: '*',
      redirect: "/404"
    }
  ]
});
//TODO 改为使用Cookies验证登录状态

// 登录状态检测
// router.beforeEach(function (to, from, next) {
//   if (localStorage.getItem("token") === null) {
//     if (to.path !== '/login' && to.path !== '/register') {
//       return next('login');
//     }
//   }
//   next();
// });
export default router
