<template>
  <div class="users">
    <div class="header">
      <h2>用户信息管理</h2>
      <div class="header-right">
        <el-input
          v-model="searchKey"
          placeholder="输入用户名或手机号搜索"
          class="search-input"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="filterStatus" placeholder="状态筛选" class="filter-select">
          <el-option label="全部" value="" />
          <el-option label="正常" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </div>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="nickname" label="昵称" width="120" />
      <el-table-column prop="avatar" label="头像" width="80">
        <template #default="{ row }">
          <el-avatar :size="40" :src="row.avatar" />
        </template>
      </el-table-column>
      <el-table-column prop="gender" label="性别" width="80">
        <template #default="{ row }">
          {{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="registerTime" label="注册时间" width="180" />
      <el-table-column prop="lastLoginTime" label="最后登录" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
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

    <!-- 用户详情对话框 -->
    <el-dialog
      title="用户详情"
      v-model="dialogVisible"
      width="600px"
    >
      <div class="user-detail">
        <div class="user-header">
          <el-avatar :size="80" :src="currentUser.avatar" />
          <div class="user-info">
            <h3>{{ currentUser.nickname }}</h3>
            <p>{{ currentUser.username }}</p>
          </div>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">{{ currentUser.id }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ currentUser.registerTime }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ currentUser.phone }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ currentUser.email }}</el-descriptions-item>
          <el-descriptions-item label="性别">
            {{ currentUser.gender === 1 ? '男' : currentUser.gender === 2 ? '女' : '未知' }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentUser.status === 1 ? 'success' : 'danger'">
              {{ currentUser.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最后登录">{{ currentUser.lastLoginTime }}</el-descriptions-item>
          <el-descriptions-item label="登录IP">{{ currentUser.lastLoginIp }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

// 表格数据
const tableData = ref([
  {
    id: 1,
    username: 'user001',
    nickname: '张三',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    gender: 1,
    phone: '13800138000',
    email: 'user001@example.com',
    registerTime: '2024-01-01 12:00:00',
    lastLoginTime: '2024-03-15 10:00:00',
    lastLoginIp: '192.168.1.1',
    status: 1
  }
])
const loading = ref(false)
const total = ref(100)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKey = ref('')
const filterStatus = ref('')

// 对话框相关
const dialogVisible = ref(false)
const currentUser = reactive({})

// 方法
const handleSearch = () => {
  // 这里添加搜索逻辑
  console.log('搜索关键词：', searchKey.value)
}

const handleDetail = (row) => {
  Object.assign(currentUser, row)
  dialogVisible.value = true
}

const handleToggleStatus = (row) => {
  const action = row.status === 1 ? '禁用' : '启用'
  ElMessageBox.confirm(`确定要${action}该用户吗？`, '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success(`${action}成功`)
  })
}

const handlePageChange = (page) => {
  currentPage.value = page
  // 这里添加获取数据的逻辑
}
</script>

<style scoped>
.users {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-input {
  width: 250px;
}

.filter-select {
  width: 120px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.user-detail {
  padding: 20px;
}

.user-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.user-info {
  margin-left: 20px;
}

.user-info h3 {
  margin: 0 0 8px 0;
  font-size: 18px;
}

.user-info p {
  margin: 0;
  color: #666;
}
</style> 