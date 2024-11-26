<template>
  <div class="login-form">
    <!-- 普通用户登录表单 -->
    <div v-if="!showRegister && !showAdminLogin">
      <div class="form-group">
        <label for="username">账号：</label>
        <input
          type="text"
          id="username"
          v-model="username"
          placeholder="请输入账号"
        />
      </div>
      <div class="form-group">
        <label for="password">密码：</label>
        <input
          type="password"
          id="password"
          v-model="password"
          placeholder="请输入密码"
        />
      </div>
      <button type="submit" class="login-btn" @click="handleLogin">登录</button>
      <div class="bottom-links">
        <a href="#" @click.prevent="showAdminLoginForm" class="admin-link">管理员登录</a>
        <div class="register-link">
          <span>没有账号？</span>
          <a href="#" @click.prevent="toggleForm">点击前往注册</a>
        </div>
      </div>
    </div>

    <!-- 管理员登录表单 -->
    <div v-if="showAdminLogin">
      <h2 class="form-title">管理员登录</h2>
      <div class="form-group">
        <label for="adminUsername">账号：</label>
        <input
          type="text"
          id="adminUsername"
          v-model="adminForm.username"
          placeholder="请输入管理员账号"
        />
      </div>
      <div class="form-group">
        <label for="adminPassword">密码：</label>
        <input
          type="password"
          id="adminPassword"
          v-model="adminForm.password"
          placeholder="请输入管理员密码"
        />
      </div>
      <button type="submit" class="login-btn" @click="handleAdminLogin">登录</button>
      <div class="register-link">
        <a href="#" @click.prevent="backToUserLogin">返回用户登录</a>
      </div>
    </div>

    <!-- 注册表单 -->
    <div v-if="showRegister">
      <div class="form-group">
        <label for="regUsername">账号：</label>
        <input
          type="text"
          id="regUsername"
          v-model="registerForm.username"
          placeholder="请输入账号"
        />
      </div>
      <div class="form-group">
        <label for="regPassword">密码：</label>
        <input
          type="password"
          id="regPassword"
          v-model="registerForm.password"
          placeholder="请输入密码"
        />
      </div>
      <div class="form-group">
        <label for="confirmPassword">确认密码：</label>
        <input
          type="password"
          id="confirmPassword"
          v-model="registerForm.confirmPassword"
          placeholder="请再次输入密码"
        />
      </div>
      <button type="submit" class="login-btn" @click="handleRegister">注册</button>
      <div class="register-link">
        <span>已有账号？</span>
        <a href="#" @click.prevent="toggleForm">返回登录</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginForm',
  data() {
    return {
      username: '',
      password: '',
      showRegister: false,
      showAdminLogin: false,
      registerForm: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      adminForm: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    handleLogin() {
      console.log('登录信息：', {
        username: this.username,
        password: this.password
      })
    },
    handleRegister() {
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        alert('两次输入的密码不一致！')
        return
      }
      console.log('注册信息：', {
        username: this.registerForm.username,
        password: this.registerForm.password
      })
    },
    toggleForm() {
      this.showRegister = !this.showRegister
      this.showAdminLogin = false
      // 切换表单时清空输入
      if (this.showRegister) {
        this.registerForm = {
          username: '',
          password: '',
          confirmPassword: ''
        }
      } else {
        this.username = ''
        this.password = ''
      }
    },
    showAdminLoginForm() {
      this.showAdminLogin = true
      this.showRegister = false
      this.adminForm = {
        username: '',
        password: ''
      }
    },
    backToUserLogin() {
      this.showAdminLogin = false
      this.showRegister = false
      this.username = ''
      this.password = ''
    },
    handleAdminLogin() {
      console.log('管理员登录信息：', {
        username: this.adminForm.username,
        password: this.adminForm.password
      })
      this.$router.push('/admin/Dashboard')
    }
  }
}
</script>

<style scoped>
.login-form {
  max-width: 500px;
  min-width: 400px;
  margin: 0 auto;
  padding: 40px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
}

label {
  width: 80px;
  margin-right: 15px;
  text-align: right;
  font-size: 16px;
}

input {
  flex: 1;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.login-btn {
  width: 100%;
  padding: 1rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1.2rem;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 10px;
}

.login-btn:hover {
  background-color: #45a049;
}

.register-link {
  text-align: right;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.register-link a {
  color: #4CAF50;
  text-decoration: none;
  margin-left: 5px;
}

.register-link a:hover {
  text-decoration: underline;
}

/* 添加确认密码输入框的样式 */
label[for="confirmPassword"] {
  width: 100px; /* 稍微加宽一点以适应文字 */
}

.bottom-links {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.admin-link {
  color: #666;
  text-decoration: none;
  font-size: 14px;
}

.admin-link:hover {
  color: #4CAF50;
  text-decoration: underline;
}

.form-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}
</style>