import Vue from 'vue'
import Router from 'vue-router'
import Index from '../components/Index'
import Share from '../components/Share'

Vue.use(Router)

export default new Router({
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
      path: '/share',
      name: 'share',
      component: Share
    }
  ]
})
