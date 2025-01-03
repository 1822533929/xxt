<template>
  <div class="home-container">
    <!-- 顶部欢迎栏 -->
    <div class="welcome-bar">
      <div class="welcome-text">欢迎来到湘西旅游网</div>
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
        <el-input
          v-model="searchText"
          placeholder="搜索..."
          prefix-icon="el-icon-search"
          class="search-input"
        />
        
        <el-dropdown trigger="click">
          <div class="user-info">
            <el-avatar :size="40" :src="userAvatar"/>
            <span class="username">{{ username }}</span>
          </div>
          
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>我的游记</el-dropdown-item>
              <el-dropdown-item @click="myorders">我的订单</el-dropdown-item>
              <el-dropdown-item>我的反馈</el-dropdown-item>
              <el-dropdown-item>个人资料</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
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
          <p>帮助中心</p>
          <p>隐私政策</p>
        </div>
      </div>
      <div class="copyright">
        <p>© 2024 吉首大学 版权所有</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      searchText: '',
      username: '用户名',
      userAvatar: '',
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    handleLogout() {
      this.$router.push('/login')
    },
    myorders() {
      this.$router.push('/user/myorders')
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
}

.welcome-bar {
  height: 40px;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.welcome-text {
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

.search-input {
  width: 200px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 8px;
}

.username {
  font-size: 14px;
  color: #333;
}

.main-content {
  min-height: calc(100vh - 100px - 300px); /* 减去头部和底部的高度 */
  /*padding: 20px;*/
  /*max-width: 1200px;*/
  width: 100%;
  margin: 0 auto;
}

/* 添加底部样式 */
.footer {
  background-color: #2c3e50;
  color: #ffffff;
  padding: 40px 0 20px;
  margin-top: 40px;
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
</style>