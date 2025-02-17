<template>
  <div class="home-container">
    <!-- 顶部欢迎栏 -->
    <div class="welcome-bar">

      <div class="welcome-text" style="color: rgb(24, 149, 0); font-size: 20px;">
        <el-image :src="xiangxi" fit="cover" style=" position: relative;
  top: 6px;"></el-image>湘西旅游信息集成与推荐系统</div>
      <div class="weather-widget">
        <iframe scrolling="no" src="https://widget.tianqiapi.com/?style=tg&skin=pitaya" frameborder="0" width="470" height="60" allowtransparency="true"></iframe>
      </div>
    </div>
    <!-- 导航栏 -->
    <div class="nav-bar">
      <div class="nav-menu">
        <el-menu 
          mode="horizontal"
          :default-active="activeMenu"
          router
          class="menu-items"
        >
          <el-menu-item index="/user/home">首页</el-menu-item>
          <el-menu-item index="/user/guides">旅游攻略</el-menu-item>
          <el-menu-item index="/user/routes">旅游路线</el-menu-item>
          <el-menu-item index="/user/travel">去旅游</el-menu-item>
          <el-menu-item index="/user/feedback">问题反馈</el-menu-item>
        </el-menu>
      </div>
      
      <div class="nav-right">
        <div class="search-container">
          <el-input
            v-model="searchText"
            placeholder="搜索景点..."
            @input="handleSearch"
            @focus="showSearchResults = true"
            class="search-input"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          
          <!-- 搜索结果下拉框 -->
          <div v-show="showSearchResults && searchResults.length > 0" class="search-results">
            <div
              v-for="item in searchResults"
              :key="item.id"
              class="search-item"
              @click="goToDetail(item)"
            >
              <el-image
                :src="getImageUrl(item.cover)"
                class="search-item-image"
                fit="cover"
              >
                <template #error>
                  <div class="image-placeholder">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="search-item-info">
                <div class="search-item-title">{{ item.title }}</div>
                <div class="search-item-desc">{{ item.descr }}</div>
              </div>
            </div>
          </div>
        </div>
        
        <el-dropdown trigger="click">
          <div class="user-info">
            <el-avatar :size="40" :src="userInfo.avatar">
              <el-icon><UserFilled /></el-icon>
            </el-avatar>
            <span class="username">{{ userInfo.name || '用户' }}</span>
          </div>
          
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToMyArticles">我的创作</el-dropdown-item>
              <el-dropdown-item @click="myorders">我的订单</el-dropdown-item>
              <el-dropdown-item @click="goToMyFeedback">我的反馈</el-dropdown-item>
              <el-dropdown-item @click="goToProfile">个人资料</el-dropdown-item>
              <el-dropdown-item @click="goToChangePassword">修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    
    <!-- 主要内容区域 -->
    <div class="main-content">
      <router-view></router-view>
    </div>

    <!-- 网站底部 -->
    <div class="background-container">
    </div>
    <div class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3>关于我们</h3>
          <p>湘西旅游网致力于为游客提供最优质的旅游服务</p>
        </div>
        <div class="footer-section">
          <h3>联系方式</h3>
          <p>电话：1760-3221897</p>
          <p>邮箱：1822533929@qq.com</p>
        </div>
        <div class="footer-section">
          <h3>快速链接</h3>
          <p >帮助中心</p>
          <p>隐私政策</p>
<!--          <a href="/user/feedback">问题反馈</a>-->
        </div>
      </div>
      <div class="copyright">
        <p>© 2024 吉首大学 版权所有</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { get, ElMessage } from '@/common'
import { UserFilled, Search, Picture } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import xiangxi from '@/images/icon/湘西.svg'

const router = useRouter()
const searchText = ref('')
const searchResults = ref([])
const showSearchResults = ref(false)

// 用户信息
const userInfo = reactive({
  name: '用户',
  avatar: ''
})
const activeMenu = ref('home'); // 设置默认值
// 获取用户信息
const getUserInfo = async () => {
  try {
    const result = await get('/user/getUserInfo')
    if (result.code === 200) {
      userInfo.name = result.data.name || '用户'
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

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('Sure-Token')
    router.push('/login')
  })
}

const myorders = () => {
  router.push('/user/myorders')
}
const goToMyArticles = () => {
  router.push('/user/my-articles')
}

const goToProfile = () => {
  router.push('/user/profile')
}

const goToChangePassword = () => {
  router.push('/user/change-password')
}

const goToMyFeedback = () => {
  router.push('/user/my-feedback')
}

// 处理搜索
const handleSearch = async () => {
  if (!searchText.value.trim()) {
    searchResults.value = []
    return
  }
  
  try {
    const result = await get(`/travels/selectAll?keyword=${searchText.value.trim()}&pageSize=5`)
    if (result.code === 200) {
      searchResults.value = result.data.list
    }
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 跳转到详情页
const goToDetail = (item) => {
  searchText.value = ''
  searchResults.value = []
  showSearchResults.value = false
  router.push(`/user/travel-detail/${item.id}`)
}

// 点击其他地方关闭搜索结果
const handleClickOutside = (event) => {
  const searchContainer = document.querySelector('.search-container')
  if (searchContainer && !searchContainer.contains(event.target)) {
    showSearchResults.value = false
  }
}

onMounted(() => {
  getUserInfo()
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
}

.welcome-bar {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.welcome-text {
  font-style: italic;
  font-size: 14px;
  color: #666;
}

.nav-bar {
  height: 60px;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-menu {
  flex: 1;
}

.menu-items {
  display: flex;
  justify-content: center;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.search-container {
  position: relative;
  width: 300px;
}

.search-input {
  width: 100%;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  margin: 0 8px;
  font-size: 14px;
  color: #333;
}

.main-content {
  /*min-height: calc(100vh - 100px - 300px); !* 减去头部和底部的高度 *!*/
  /*padding: 20px;*/
  /*max-width: 1200px;*/
  background-color: #F7F7F7;;
  width: 100%;
  margin: 0 auto;
}

/* 添加底部样式 */
.footer {
  background-color: #2c3e50;
  color: #ffffff;
  padding: 40px 0 20px;
  /*margin-top: 40px;*/
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-around;
  padding: 0 20px;
}

.footer-section {
  flex: 1;
  margin: 0 20px;
}

.footer-section h3 {
  font-size: 18px;
  margin-bottom: 15px;
}

.footer-section p {
  font-size: 14px;
  color: #cccccc;
  margin: 5px 0;
}

.copyright {
  text-align: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #3a4b5d;
}

.copyright p {
  font-size: 14px;
  color: #cccccc;
}

.weather-widget {
  display: flex;
  align-items: center;
}

.weather-widget iframe {
  margin-top: 10px;
}

.search-results {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  max-height: 400px;
  overflow-y: auto;
  z-index: 1000;
}

.search-item {
  display: flex;
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.3s;
}

.search-item:hover {
  background-color: #f5f7fa;
}

.search-item-image {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  margin-right: 10px;
}

.image-placeholder {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
}

.search-item-info {
  flex: 1;
  overflow: hidden;
}

.search-item-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.search-item-desc {
  font-size: 12px;
  color: #909399;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

:deep(.el-input__wrapper) {
  border-radius: 20px;
}
.background-container {
  background-image: url('@/images/background/mainbg.png');
  background-repeat: no-repeat;
  background-position: bottom center;
  background-size: cover;
  min-height: 55vh; /* 确保背景覆盖整个视口高度 */
}

</style>