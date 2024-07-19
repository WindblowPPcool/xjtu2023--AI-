import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'login',
    component: () => import('@/views/LoginSwitch.vue')
  },
  {
    path: '/history',
    name: 'history',
    component: () => import('@/views/history.vue'),
    meta: {                      //加一个自定义obj
      requireuid: true,     //这个参数 true 代表需要登录才能进入A
      requirecid: true
    }
  },
  {
    path: '/set',
    name: 'set',
    component: () => import('@/views/ArticleView.vue'),
    meta: {                      //加一个自定义obj
      requireuid: true,     //这个参数 true 代表需要登录才能进入A
      requirecid: false     //这个参数 true 代表需要登录才能进入A
    }
  },
  {
    path: '/aichat',
    name: 'aichat',
    component: () => import('@/views/aichat.vue'),
    meta: {                      //加一个自定义obj
      requireuid: true,     //这个参数 true 代表需要登录才能进入A
      requirecid: true      //这个参数 true 代表需要登录才能进入A
    }
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})


export default router
