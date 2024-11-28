<template>
  <div class="login-container">
    <div class="login-box">
      <div class="title">旅游管理系统</div>
      <!-- 用户登录表单 -->
      <el-form v-if="!isAdminLogin && !isRegister" ref="userLoginFormRef" :model="userLoginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="userLoginForm.username" placeholder="用户名" prefix-icon="User"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="userLoginForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="login-button" @click="handleUserLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 管理员登录表单 -->
      <el-form v-else-if="isAdminLogin && !isRegister" ref="adminLoginFormRef" :model="adminLoginForm" :rules="adminRules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="adminLoginForm.username" placeholder="管理员账号" prefix-icon="User"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="adminLoginForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="login-button" @click="adminLogin">
            管理员登录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 注册表单 -->
      <el-form
        v-else
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="请输入手机号"
            prefix-icon="Phone"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            class="login-button"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 底部操作区 -->
      <div class="bottom-actions">
        <div class="action-links">
          <span v-if="!isRegister" @click="toggleLoginType">
            {{ isAdminLogin ? '用户登录' : '管理员登录' }}
          </span>
          <span @click="toggleRegister">
            {{ isRegister ? '已有账号？立即登录' : '没有账号？点击注册' }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const isAdminLogin = ref(false)
const isRegister = ref(false)

// 用户登录表单
const userLoginFormRef = ref(null)
const userLoginForm = reactive({
  username: '',
  password: ''
})

// 管理员登录表单
const adminLoginFormRef = ref(null)
const adminLoginForm = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, message: '密码长度不能小于3位', trigger: 'blur' }
  ]
}
const adminRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, message: '密码长度不能小于3位', trigger: 'blur' }
  ]
}


// 添加注册表单
const registerFormRef = ref(null)
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  phone: ''
})

// 注册表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 切换注册状态
const toggleRegister = () => {
  isRegister.value = !isRegister.value
  if (!isRegister.value) {
    isAdminLogin.value = false // 返回登录时默认显示用户登录
  }
}

// 处理注册
const handleRegister = () => {
  registerFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      // 这里模拟注册请求
      setTimeout(() => {
        ElMessage.success('注册成功，请登录')
        isRegister.value = false
        loading.value = false
      }, 1000)
    }
  })
}

// 切换登录类型
const toggleLoginType = () => {
  isAdminLogin.value = !isAdminLogin.value
}

// 用户登录
const handleUserLogin = () => {
  userLoginFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      // 这里模拟用户登录请求
      setTimeout(() => {
        ElMessage.error('普通用户暂未开放')
        loading.value = false
      }, 1000)
    }
  })
}

// 管理员登录
const adminLogin = () => {
  // console.log("管理员登录")
  if (adminLoginForm.username && adminLoginForm.password) {
    // 创建表单数据
    console.log("管理员登录")
    let admin = new FormData();
    admin.append("username", adminLoginForm.username);
    admin.append("password", adminLoginForm.password);

    // 使用 axios.post 而不是直接使用 post
    axios.post("/admin/login", admin).then(result => {
      if (result.data.code==200) {
        ElMessage.success('登录成功')
        router.push('/admin');
      }else {
        ElMessage.error('登录失败')
      }
    }).catch(error => {
      ElMessage.error('登录失败，服务器爆了(')
    });
  }
};
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  background-color: #2d3a4b;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.title {
  font-size: 26px;
  color: #333;
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
}

.bottom-actions {
  margin-top: 15px;
}

.action-links {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.action-links span {
  color: #409EFF;
  cursor: pointer;
}

.action-links span:hover {
  text-decoration: underline;
}
</style>