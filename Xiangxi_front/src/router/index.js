import { createRouter, createWebHistory } from 'vue-router'
import LoginForm from '../components/LoginForm.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginForm
  },
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/admin/Dashboard.vue')
      },
      {
        path: 'travel-notes',
        name: 'TravelNotes',
        component: () => import('../views/admin/TravelNotes.vue')
      },
      {
        path: 'travel-guides',
        name: 'TravelGuides',
        component: () => import('../views/admin/TravelGuides.vue')
      },
      {
        path: 'travel-routes',
        name: 'TravelRoutes',
        component: () => import('../views/admin/TravelRoutes.vue')
      },
      {
        path: 'travel-products',
        name: 'TravelProducts',
        component: () => import('../views/admin/TravelProducts.vue')
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('../views/admin/Orders.vue')
      },
      {
        path: 'feedback',
        name: 'Feedback',
        component: () => import('../views/admin/Feedback.vue')
      },
      {
        path: 'announcements',
        name: 'Announcements',
        component: () => import('../views/admin/Announcements.vue')
      },
      {
        path: 'admin-users',
        name: 'AdminUsers',
        component: () => import('../views/admin/AdminUsers.vue')
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('../views/admin/Users.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 