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
        <!--验证码输入框-->
        <el-form-item prop="verifyCode">
          <div class="verify-code-container">
            <el-input v-model="userLoginForm.verifyCode" placeholder="请输入验证码" prefix-icon="Key" style="width: 200px"/>
            <img :src="codeSrc" alt="验证码" class="verify-code-img" @click="getCode">
          </div>
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
      <el-form v-else ref="registerFormRef" :model="registerForm" :rules="registerRules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" prefix-icon="User"/>
        </el-form-item>
        <el-form-item prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入姓名" prefix-icon="User"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码" prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号" prefix-icon="Phone"/>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱" prefix-icon="Message"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="login-button" @click="handleRegister">
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
import {setLocalToken} from "@/common/index.js";
import {LOCAL_TOKEN} from "@/common/constants.js";

const router = useRouter()
const loading = ref(false)
const isAdminLogin = ref(false)
const isRegister = ref(false)

// 用户登录表单
const userLoginFormRef = ref(null)
const userLoginForm = reactive({
  username: '',
  password: '',
  verifyCode:''
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
const verificationRule = {
  verificationCode: [
    {
      min: 4,
      max: 4,
      required: true,
      message: "请输入正确的验证码！",
      trigger: "blur"
    }
  ]
};


// 添加注册表单
const registerFormRef = ref(null)
const registerForm = reactive({
  username: '',
  name: '',
  password: '',
  confirmPassword: '',
  phone: '',
  email: ''
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
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ]
}
// 获取验证码
const codeSrc = ref(); // 验证码图片url
const getCode = () => {
  axios.get('/captcha/captchaImage', {
    withCredentials: true
  }).then(res => {
    console.log("验证码响应：", res.data);
    if (res.data.code === 200) {
      codeSrc.value = res.data.data.img;
    } else {
      ElMessage.error('获取验证码失败');
    }
  }).catch(err => {
    console.error("获取验证码错误：", err);
    ElMessage.error('获取验证码失败');
  });
}
getCode();


// 切换注册状态
const toggleRegister = () => {
  isRegister.value = !isRegister.value
  if (!isRegister.value) {
    isAdminLogin.value = false // 返回登录时默认显示用户登录
  }
}

// 处理注册
const handleRegister = () => {
  console.log("用户注册...")
  let user = new FormData();
  user.append("username", registerForm.username)
  user.append("name", registerForm.name)
  user.append("password", registerForm.password)
  user.append("phone", registerForm.phone)
  user.append("email", registerForm.email)

  // 使用 axios.post 发送消息
  axios.post("/user/register", user).then(result => {
    console.log(result.data);
    if (result.data.code==200) {
      ElMessage.success('注册成功!')
      toggleRegister()
    }else {
      ElMessage.error('注册失败，账号已存在!')
    }
  }).catch(error => {
    ElMessage.error('注册失败，服务器爆了(')
  });
}

// 切换登录类型
const toggleLoginType = () => {
  isAdminLogin.value = !isAdminLogin.value
}

// 用户登录
const handleUserLogin = () => {
  if (userLoginForm.username && userLoginForm.password && userLoginForm.verifyCode) {
    let user = new FormData();
    user.append("username", userLoginForm.username);
    user.append("password", userLoginForm.password);
    user.append("captcha", userLoginForm.verifyCode);

    // 先验证验证码
    axios.post("/captcha/check", user, {
      withCredentials: true
    }).then(checkResult => {
      if (checkResult.data.code === 200) {
        // 验证码正确，继续登录流程
        axios.post("/user/login", user).then(result => {
          if (result.data.code === 200) {
            setLocalToken(result.data.data);
            ElMessage.success('登录成功');
            router.push('/user/home');
          } else {
            ElMessage.error('登录失败');
            getCode(); // 刷新验证码
          }
        });
      } else {
        ElMessage.error(checkResult.data.msg || '验证码错误');
        getCode(); // 刷新验证码
      }
    }).catch(error => {
      ElMessage.error('服务器错误');
      getCode(); // 刷新验证码
    });
  }
}

// 管理员登录
const adminLogin = () => {
  if (adminLoginForm.username && adminLoginForm.password) {
    // 创建表单数据
    console.log("管理员登录...")
    let admin = new FormData();
    admin.append("username", adminLoginForm.username);
    admin.append("password", adminLoginForm.password);

    // 使用 axios.post 发送消息
    axios.post("/user/adminLogin", admin).then(result => {
      if (result.data.code === 200) {
        // 存储 token
        setLocalToken(result.data.data);
        // 存储用户信息
        localStorage.setItem('userInfo', JSON.stringify({
          isAdmin: true,
          ...result.data.data
        }));
        ElMessage.success('登录成功')
        router.push('/admin');
      } else {
        ElMessage.error(result.data.msg || '登录失败')
      }
    }).catch(error => {
      ElMessage.error('登录失败，服务器错误')
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

.verify-code-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.verify-code-img {
  height: 40px;
  cursor: pointer;
}
</style>