<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <div class="avatar-section">
        <el-avatar 
          :size="120" 
          :src="avatarUrl" 
          @click="handleAvatarClick"
          class="avatar"
        >
          <el-icon><UserFilled /></el-icon>
        </el-avatar>
        <div class="avatar-tip">点击更换头像</div>
        <input 
          type="file" 
          ref="fileInput" 
          style="display: none" 
          accept="image/*"
          @change="handleFileChange"
        />
      </div>

      <el-form 
        :model="userForm" 
        :rules="rules" 
        ref="formRef"
        label-width="80px"
        class="profile-form"
      >
        <el-form-item label="用户名">
          <el-input v-model="userForm.username" disabled />
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" />
        </el-form-item>
        
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>

        <el-form-item class="form-buttons">
          <el-button type="primary" @click="handleSubmit">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { get, post } from '@/common'
import { ElMessage } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'

const formRef = ref(null)
const fileInput = ref(null)
const avatarUrl = ref('')
const newAvatarFile = ref(null)

const userForm = reactive({
  username: '',
  name: '',
  phone: '',
  email: '',
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 获取用户信息
const getUserInfo = async () => {
  try {
    const result = await get('/user/getUserInfo')
    if (result.code === 200) {
      const { username, name, phone, email, avatar } = result.data
      Object.assign(userForm, { username, name, phone, email })
      if (avatar) {
        const avatarPath = avatar.replace('/img/upload/', '')
        avatarUrl.value = avatar.startsWith('http') 
          ? avatar 
          : import.meta.env.VITE_API_BASE_URL + '/img/upload/' + avatarPath
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 点击头像
const handleAvatarClick = () => {
  fileInput.value.click()
}

// 选择文件
const handleFileChange = (e) => {
  const file = e.target.files[0]
  if (file) {
    if (file.size > 2 * 1024 * 1024) {
      ElMessage.warning('图片大小不能超过2MB')
      return
    }
    newAvatarFile.value = file
    avatarUrl.value = URL.createObjectURL(file)
  }
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 创建 FormData 对象
        const formData = new FormData()
        
        // 添加用户信息
        Object.keys(userForm).forEach(key => {
          formData.append(key, userForm[key])
        })
        
        // 如果有新头像，添加到 FormData
        if (newAvatarFile.value) {
          formData.append('image', newAvatarFile.value)
        }

        // 更新用户信息
        const result = await post('/user/updateUserInfo', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })

        if (result.code === 200) {
          ElMessage.success('保存成功')
          getUserInfo() // 刷新用户信息
          newAvatarFile.value = null // 清除文件缓存
        }
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      }
    }
  })
}

onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.profile-card {
  width: 500px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar {
  cursor: pointer;
  transition: transform 0.3s;
}

.avatar:hover {
  transform: scale(1.05);
}

.avatar-tip {
  margin-top: 10px;
  color: #909399;
  font-size: 14px;
}

.profile-form {
  max-width: 400px;
  margin: 0 auto;
}

.form-buttons {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  margin-left: 60px;
}

.form-buttons :deep(.el-button) {
  padding: 12px 40px;
  font-size: 16px;
}
</style> 