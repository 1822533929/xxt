<template>
  <div class="announcements">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <div class="search-form">
        <div class="search-group">
          <el-input
            v-model="searchForm.title"
            placeholder="请输入公告标题"
            clearable
            class="search-input"
          />
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </div>
      </div>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="operation-card">
      <div class="operation-buttons">
        <el-button type="primary" @click="handleAdd">发布公告</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="!selectedRows.length">
          批量删除
        </el-button>
      </div>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card">
      <el-table 
        :data="tableData" 
        style="width: 100%" 
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="公告标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="content" label="公告内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="time" label="发布时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="公告标题" required>
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="公告内容" required>
          <el-input
            type="textarea"
            v-model="form.content"
            :rows="6"
            placeholder="请输入公告内容"
          />
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
import { get, post } from '@/common'

// 搜索表单
const searchForm = reactive({
  title: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const selectedRows = ref([])

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('发布公告')
const form = reactive({
  id: '',
  title: '',
  content: '',
  time: ''
})

// 获取公告列表
const getNoticeList = async () => {
  loading.value = true
  try {
    const result = await get(`/notice/selectAll?currentPage=${currentPage.value}&pageSize=${pageSize.value}`)
    if (result.code === 200) {
      tableData.value = result.data.list
      total.value = result.data.total
    }
  } catch (error) {
    console.error('获取公告列表失败:', error)
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

// 修改搜索方法
const handleSearch = async () => {
  if (!searchForm.title) {
    getNoticeList()
    return
  }
  
  loading.value = true
  try {
    const result = await get(`/notice/search?keyword=${searchForm.title}`)
    if (result.code === 200) {
      tableData.value = result.data
      total.value = result.data.length
      currentPage.value = 1
    }
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败')
  } finally {
    loading.value = false
  }
}

// 修改重置方法
const resetSearch = () => {
  searchForm.title = ''
  currentPage.value = 1
  getNoticeList()
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleAdd = () => {
  dialogTitle.value = '发布公告'
  Object.assign(form, {
    id: '',
    title: '',
    content: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑公告'
  Object.assign(form, {
    id: row.id,
    title: row.title,
    content: row.content
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该公告吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const result = await get(`/notice/delete?id=${row.id}`)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getNoticeList()
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

const handleBatchDelete = () => {
  if (!selectedRows.value.length) return
  ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条公告吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const result = await post('/notice/delete/batch', ids, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      if (result.code === 200) {
        ElMessage.success('批量删除成功')
        getNoticeList()
      }
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

const handleSubmit = async () => {
  if (!form.title.trim()) {
    ElMessage.warning('请输入公告标题')
    return
  }
  if (!form.content.trim()) {
    ElMessage.warning('请输入公告内容')
    return
  }

  try {
    const submitData = {
      id: form.id || null,
      title: form.title.trim(),
      content: form.content.trim()
    }

    const url = form.id ? '/notice/update' : '/notice/add'
    const result = await post(url, submitData, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    if (result.code === 200) {
      ElMessage.success(form.id ? '更新成功' : '发布成功')
      dialogVisible.value = false
      getNoticeList()
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getNoticeList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getNoticeList()
}

// 初始化
getNoticeList()
</script>

<style scoped>
.announcements {
  padding: 20px;
  background-color: #f5f7fa;
  /*background-color: #f0f2f5;*/
  min-height: calc(100vh - 60px);
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.search-form {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-group {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 300px;
}

.operation-buttons {
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 