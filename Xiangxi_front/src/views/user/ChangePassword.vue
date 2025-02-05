<template>
  <div class="password-container">
    <el-card class="password-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
        </div>
      </template>
      
      <el-form 
        :model="form" 
        :rules="rules" 
        ref="formRef"
        label-width="100px"
        class="password-form"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input 
            v-model="form.oldPassword" 
            type="password" 
            show-password
            placeholder="请输入原密码"
          />
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input 
            v-model="form.newPassword" 
            type="password" 
            show-password
            placeholder="请输入新密码"
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="form.confirmPassword" 
            type="password" 
            show-password
            placeholder="请再次输入新密码"
          />
        </el-form-item>

        <el-form-item class="form-buttons">
          <el-button type="primary" @click="submitForm">确认修改</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { post, ElMessage } from '@/common'

const router = useRouter()
const formRef = ref(null)

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
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
        if (value !== form.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 构造表单数据
        const formData = new FormData()
        formData.append('oldPassword', form.oldPassword.trim())
        formData.append('newPassword', form.newPassword.trim())
        
        const res = await post('/user/updatePassword', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        
        if (res.code === 200) {
          ElMessage.success('密码修改成功，请重新登录')
          localStorage.removeItem('Sure-Token')
          router.push('/login')
        } else {
          ElMessage.error(res.message || '修改密码失败')
        }
      } catch (error) {
        console.error('修改密码失败:', error)
        ElMessage.error(error.response?.data?.message || '修改密码失败，请稍后重试')
      }
    }
  })
}

const cancel = () => {
  router.back()
}
</script>

<style scoped>
.password-container {
  padding: 20px;
  display: flex;
  justify-content: center;
  /*align-items: center;*/
  /*min-height: calc(100vh - 60px);*/
  background-color: #f5f7fa;
}

.password-card {
  width: 500px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 18px;
  font-weight: bold;
}

.password-form {
  max-width: 400px;
  margin: 20px auto;
}

.form-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style> 