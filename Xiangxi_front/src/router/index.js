import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import AdminLayout from '../views/admin/AdminLayout.vue'
import Home from "@/views/user/Home.vue";
import { BASE_URL, LOGIN_ROUTE_NAME } from '@/common/constants';

const routes = [
  //默认路由定位到登录页面
    {
    path: '/',
    redirect: '/login'
  },
    {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/user',
    component: Home,
    children: [
      {
        path: '',
        redirect: 'home'
      },
      {
        path: 'home',
        name: 'UserHome',
        component: () => import('../views/user/UserHome.vue')  // 首页内容组件
      },
      {
        path: 'guides',
        name: 'UserGuides',
        component: () => import('../views/user/TravelGuides.vue')
      },
      {
        path: 'routes',
        name: 'UserRoutes',
        component: () => import('../views/user/TravelRoutes.vue')
      },
      {
        path: 'travel',
        name: 'UserTravel',
        component: () => import('../views/user/Travel.vue')
      },
      {
        path: 'travel-detail/:id',
        name: 'TravelDetail',
        component: () => import('../views/user/TravelDetail.vue')
      },
      {
        path: 'feedback',
        name: 'UserFeedback',
        component: () => import('../views/user/Feedback.vue')
      },
      {
        path: 'article-detail/:id',
        name: 'ArticleDetail',
        component: () => import('../views/user/ArticleDetail.vue')
      }
    ]
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminLayout,
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/admin/Dashboard.vue'),
        meta: { title: '系统首页' }
      },
      // 信息管理
      {
        path: 'travel-notes',
        name: 'TravelNotes',
        component: () => import('../views/admin/TravelNotes.vue'),
        meta: { title: '游记信息' }
      },
      {
        path: 'travel-guides',
        name: 'TravelGuides',
        component: () => import('../views/admin/TravelGuides.vue'),
        meta: { title: '旅游攻略' }
      },
      {
        path: 'travel-routes',
        name: 'TravelRoutes',
        component: () => import('../views/admin/TravelRoutes.vue'),
        meta: { title: '旅游路线' }
      },
      {
        path: 'travel-products',
        name: 'TravelProducts',
        component: () => import('../views/admin/TravelProducts.vue'),
        meta: { title: '旅游商品' }
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('../views/admin/Orders.vue'),
        meta: { title: '旅游订单' }
      },
      {
        path: 'feedback',
        name: 'Feedback',
        component: () => import('../views/admin/Feedback.vue'),
        meta: { title: '用户反馈' }
      },
      {
        path: 'announcements',
        name: 'Announcements',
        component: () => import('../views/admin/Announcements.vue'),
        meta: { title: '系统公告' }
      },
      // 用户管理
      {
        path: 'admin-users',
        name: 'AdminUsers',
        component: () => import('../views/admin/AdminUsers.vue'),
        meta: { title: '管理员信息' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('../views/admin/Users.vue'),
        meta: { title: '用户信息' }
      }
    ]
  }
]

// const router = createRouter({
//   history: createWebHistory(),
//   routes
// })

const router = createRouter({
  history: createWebHistory(BASE_URL),
  routes
});

export default router 