<template>
  <div class="admin-layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="logo">后台管理</div>
      <div class="user-info">
        <el-dropdown trigger="click">
          <div class="avatar-container">
            <img src="@/assets/default-avatar.png" class="avatar" />
            <span class="username">{{ adminName }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleCommand('profile')">个人资料</el-dropdown-item>
              <el-dropdown-item @click="handleCommand('password')">修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="handleCommand('logout')">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <!-- 主体内容 -->
    <div class="main-container">
      <!-- 左侧菜单 -->
      <aside class="sidebar">
        <el-menu
          :default-active="activeMenu"
          background-color="#304156"
          text-color="#fff"
          active-text-color="#409EFF"
          :unique-opened="true"
          router
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>

          <el-sub-menu index="2">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/admin/travel-notes">游记信息</el-menu-item>
            <el-menu-item index="/admin/travel-guides">旅游攻略</el-menu-item>
            <el-menu-item index="/admin/travel-routes">旅游路线</el-menu-item>
            <el-menu-item index="/admin/travel-products">旅游商品</el-menu-item>
            <el-menu-item index="/admin/orders">旅游订单</el-menu-item>
            <el-menu-item index="/admin/feedback">用户反馈</el-menu-item>
            <el-menu-item index="/admin/announcements">系统公告</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="3">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/admin-users">管理员信息</el-menu-item>
            <el-menu-item index="/admin/users">用户信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </aside>

      <!-- 右侧内容 -->
      <main class="content">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'AdminLayout',
  setup() {
    const router = useRouter()
    const adminName = ref('管理员')
    const activeMenu = ref('/admin/dashboard')

    const handleCommand = (command) => {
      switch (command) {
        case 'logout':
          router.push('/login')
          break
      }
    }

    return {
      adminName,
      activeMenu,
      handleCommand
    }
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #304156;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar-container {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 8px;
}

.username {
  font-size: 14px;
  color: #606266;
}

.main-container {
  display: flex;
  margin-top: 60px;
  height: calc(100vh - 60px);
}

.sidebar {
  width: 210px;
  background-color: #304156;
  height: 100%;
  position: fixed;
  left: 0;
  top: 60px;
  overflow-y: auto;
}

.content {
  margin-left: 210px;
  padding: 20px;
  flex: 1;
  overflow-y: auto;
  background-color: #f0f2f5;
}
</style> 