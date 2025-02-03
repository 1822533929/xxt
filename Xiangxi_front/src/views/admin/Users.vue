<template>
  <div class="users">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <div class="search-inputs">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入账号或名称"
          class="search-input"
          clearable
        />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd">新增用户</el-button>
      <el-button 
        type="danger" 
        @click="handleBatchDelete" 
        :disabled="!selectedRows.length"
      >
        批量删除
      </el-button>
    </div>

    <!-- 表格 -->
    <el-table 
      :data="tableData" 
      style="width: 100%" 
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="username" label="账号" min-width="120" />
      <el-table-column prop="name" label="名称" min-width="120" />
      <el-table-column label="头像" width="80">
        <template #default="{ row }">
          <el-avatar :size="40" :src="getImageUrl(row.avatar)">
            <el-icon><UserFilled /></el-icon>
          </el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 1 ? 'info' : 'success'">
            {{ row.role === 1 ? '用户' : '管理员' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话" min-width="120" />
      <el-table-column prop="email" label="邮箱" min-width="180" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      class="pagination"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      :total="total"
      layout="total, sizes, prev, pager, next"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 用户表单对话框 -->
    <el-dialog
      :title="form.id ? '编辑用户' : '新增用户'"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form 
        :model="form" 
        :rules="rules" 
        ref="formRef" 
        label-width="80px"
      >
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import { get, post } from '@/common'

const loading = ref(false)
const dialogVisible = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedRows = ref([])
const formRef = ref(null)
const tableData = ref([])

const searchForm = reactive({
  keyword: ''
})

const form = reactive({
  id: '',
  username: '',
  name: '',
  phone: '',
  email: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 获取用户列表
const getUserList = async () => {
  loading.value = true
  try {
    const result = await get('/user/selectAllUser', {
      currentPage: currentPage.value,
      pageSize: pageSize.value
    })
    if (result.code === 200) {
      tableData.value = result.data.list.filter(item => {
        const matchKeyword = searchForm.keyword.toLowerCase()
        return !searchForm.keyword || 
               item.username.toLowerCase().includes(matchKeyword) ||
               item.name.toLowerCase().includes(matchKeyword)
      })
      total.value = result.data.total
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索和重置
const handleSearch = () => {
  currentPage.value = 1
  getUserList()
}

const resetSearch = () => {
  searchForm.keyword = ''
  handleSearch()
}

// 表格选择
const handleSelectionChange = (rows) => {
  selectedRows.value = rows.map(row => row.id)
}

// 分页方法
const handleSizeChange = (val) => {
  pageSize.value = val
  getUserList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getUserList()
}

// CRUD操作
const handleAdd = () => {
  Object.assign(form, {
    id: '',
    username: '',
    name: '',
    phone: '',
    email: '',
    password: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const result = await get(`/user/deleteUser/${row.id}`)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getUserList()
      }
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) return
  ElMessageBox.confirm('确定要删除选中的用户吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const result = await post('/user/deleteUser/batch', selectedRows.value)
      if (result.code === 200) {
        ElMessage.success('批量删除成功')
        getUserList()
      }
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const url = form.id ? '/user/updateUser' : '/user/addUser'
        const result = await post(url, form)
        if (result.code === 200) {
          ElMessage.success(form.id ? '更新成功' : '添加成功')
          dialogVisible.value = false
          getUserList()
        }
      } catch (error) {
        console.error('操作失败:', error)
      }
    }
  })
}

onMounted(() => {
  getUserList()
})
</script>

<style scoped>
.users {
  padding: 20px;
}

.search-bar,
.operation-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-inputs {
  display: flex;
  gap: 10px;
}

.search-input {
  width: 200px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  background-color: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

:deep(.el-dialog__body) {
  padding: 20px 40px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style> 