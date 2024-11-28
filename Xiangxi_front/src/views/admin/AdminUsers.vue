<template>
  <div class="admin-users">
    <div class="header">
      <h2>管理员信息管理</h2>
      <el-button type="primary" @click="handleAdd">新增管理员</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="真实姓名" width="120" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="{ row }">
          <el-tag :type="getRoleType(row.role)">
            {{ getRoleText(row.role) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="lastLoginTime" label="最后登录时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="warning" link @click="handleResetPwd(row)">重置密码</el-button>
          <el-button 
            :type="row.status === 1 ? 'danger' : 'success'" 
            link 
            @click="handleToggleStatus(row)"
          >
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      :total="total"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      @current-change="handlePageChange"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="dialogTitle === '编辑管理员'" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role">
            <el-option label="超级管理员" :value="1" />
            <el-option label="普通管理员" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="dialogTitle === '新增管理员'">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 表格数据
const tableData = ref([
  {
    id: 1,
    username: 'admin',
    realName: '张三',
    role: 1,
    phone: '13800138000',
    email: 'admin@example.com',
    lastLoginTime: '2024-03-15 10:00:00',
    status: 1
  }
])
const loading = ref(false)
const total = ref(100)
const currentPage = ref(1)
const pageSize = ref(10)

// 表单相关
const formRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新增管理员')
const form = reactive({
  username: '',
  realName: '',
  role: 2,
  phone: '',
  email: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ]
}

// 工具方法
const getRoleType = (role) => {
  return role === 1 ? 'danger' : ''
}

const getRoleText = (role) => {
  return role === 1 ? '超级管理员' : '普通管理员'
}

// 方法
const handleAdd = () => {
  dialogTitle.value = '新增管理员'
  Object.assign(form, {
    username: '',
    realName: '',
    role: 2,
    phone: '',
    email: '',
    password: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑管理员'
  Object.assign(form, {
    username: row.username,
    realName: row.realName,
    role: row.role,
    phone: row.phone,
    email: row.email
  })
  dialogVisible.value = true
}

const handleResetPwd = (row) => {
  ElMessageBox.confirm('确定要重置该管理员的密码吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('密码已重置为：123456')
  })
}

const handleToggleStatus = (row) => {
  const action = row.status === 1 ? '禁用' : '启用'
  ElMessageBox.confirm(`确定要${action}该管理员吗？`, '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success(`${action}成功`)
  })
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      ElMessage.success(dialogTitle.value === '新增管理员' ? '添加成功' : '更新成功')
      dialogVisible.value = false
    }
  })
}

const handlePageChange = (page) => {
  currentPage.value = page
  // 这里添加获取数据的逻辑
}
</script>

<style scoped>
.admin-users {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 