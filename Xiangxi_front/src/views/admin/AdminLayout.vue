<template>
  <div class="admin-layout">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="logo">湘西旅游管理系统</div>
      <div class="user-info">
        <el-dropdown trigger="click" @command="handleCommand">
          <div class="avatar-container">
            <el-avatar :size="40" :src="userInfo.avatar">
              <el-icon><UserFilled /></el-icon>
            </el-avatar>
            <span class="username">{{ userInfo.name || '管理员' }}</span>
            <el-icon class="el-icon--right"><CaretBottom /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人资料</el-dropdown-item>
              <el-dropdown-item command="password">修改密码</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="main-container">
      <!-- 左侧菜单 -->
      <el-aside width="210px" class="sidebar">
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
            <el-menu-item index="/admin/travel-news">湘西资讯</el-menu-item>
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
      </el-aside>

      <!-- 右侧内容 -->
      <el-main class="content">
        <router-view></router-view>
      </el-main>
    </el-container>

    <!-- 修改密码对话框 -->
    <el-dialog
      title="修改密码"
      v-model="passwordDialogVisible"
      width="400px"
    >
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePasswordSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { get } from '@/common'
import { 
  HomeFilled, 
  Document, 
  User, 
  UserFilled,
  CaretBottom
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 用户信息
const userInfo = reactive({
  name: '管理员',
  avatar: ''
})

// 获取用户信息
const getUserInfo = async () => {
  try {
    const result = await get('/user/getUserInfo')
    if (result.code === 200) {
      userInfo.name = result.data.name || '管理员'
      if (result.data.avatar) {
        const avatarPath = result.data.avatar.replace('/img/upload/', '')
        userInfo.avatar = result.data.avatar.startsWith('http') 
          ? result.data.avatar 
          : import.meta.env.VITE_API_BASE_URL + '/img/upload/' + avatarPath
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 当前激活的菜单
const activeMenu = ref(route.path)

// 修改密码相关
const passwordDialogVisible = ref(false)
const passwordFormRef = ref(null)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 方法
const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人资料功能开发中')
      break
    case 'password':
      passwordDialogVisible.value = true
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token') // 清除token
        router.push('/login')
      })
      break
  }
}

const handlePasswordSubmit = () => {
  passwordFormRef.value?.validate((valid) => {
    if (valid) {
      ElMessage.success('密码修改成功')
      passwordDialogVisible.value = false
      // 重置表单
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    }
  })
}

onMounted(() => {
  getUserInfo()
})
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
  padding: 0 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.avatar-container:hover {
  background-color: #f5f7fa;
}

.username {
  margin: 0 8px;
  font-size: 14px;
  color: #606266;
}

.main-container {
  margin-top: 60px;
  height: calc(100vh - 60px);
}

.sidebar {
  background-color: #304156;
  height: 100%;
  position: fixed;
  left: 0;
  top: 60px;
  overflow-y: auto;
}

.content {
  margin-left: 170px;/*210*/
  background-color: #f0f2f5;
  min-height: calc(100vh - 60px);
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-menu-item:hover) {
  background-color: #263445 !important;
}

:deep(.el-sub-menu__title:hover) {
  background-color: #263445 !important;
}
</style> 