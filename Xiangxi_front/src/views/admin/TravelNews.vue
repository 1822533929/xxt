<template>
  <div class="travel-news">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <div class="search-form">
        <el-input
          v-model="searchForm.title"
          placeholder="请输入标题关键词"
          clearable
          class="search-input"
        />
        <div class="search-buttons">
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="operation-card">
      <div class="operation-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>添加资讯
        </el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="!selectedRows.length">
          <el-icon><Delete /></el-icon>批量删除
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
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="summary" label="摘要" min-width="200" show-overflow-tooltip />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image 
              :src="getImageUrl(row.cover)" 
              :preview-src-list="[getImageUrl(row.cover)]"
              class="cover-image"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="内容" width="100" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewContent(row)">
              查看内容
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="readCount" label="阅读量" width="100" align="center" />
        <el-table-column prop="publishTime" label="发布时间" width="160" align="center" />
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
      width="70%"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="摘要">
          <el-input type="textarea" v-model="form.summary" rows="3" />
        </el-form-item>
        <el-form-item label="封面">
          <el-upload
            class="cover-upload"
            action="#"
            :show-file-list="false"
            :on-change="handleCoverChange"
            :before-upload="beforeUpload"
            :auto-upload="false"
          >
            <img 
              v-if="form.coverPreview || form.cover" 
              :src="form.coverPreview || getImageUrl(form.cover)" 
              class="upload-image"
            >
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input type="textarea" v-model="form.content" rows="10" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看内容对话框 -->
    <el-dialog
      title="资讯内容"
      v-model="contentDialogVisible"
      width="60%"
    >
      <div v-html="selectedContent" class="content-preview"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete, Picture } from '@element-plus/icons-vue'
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
const contentDialogVisible = ref(false)
const dialogTitle = ref('新增资讯')
const selectedContent = ref('')
const form = reactive({
  title: '',
  summary: '',
  cover: '',
  content: '',
  coverFile: null,
  coverPreview: ''
})

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 获取资讯列表
const getNewsList = async () => {
  loading.value = true
  try {
    const result = await get(`/news/getNewsList?currentPage=${currentPage.value}&pageSize=${pageSize.value}&keyword=${searchForm.title}`)
    if (result.code === 200) {
      let filteredData = result.data.list
      if (searchForm.title) {
        const keyword = searchForm.title.toLowerCase()
        filteredData = filteredData.filter(item => 
          item.title.toLowerCase().includes(keyword) ||
          item.summary?.toLowerCase().includes(keyword)
        )
      }
      tableData.value = filteredData
      total.value = filteredData.length
    }
  } catch (error) {
    console.error('获取资讯列表失败:', error)
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

// 方法定义
const handleSearch = () => {
  currentPage.value = 1
  getNewsList()
}

const resetSearch = () => {
  searchForm.title = ''
  handleSearch()
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleAdd = () => {
  dialogTitle.value = '新增资讯'
  Object.assign(form, {
    title: '',
    summary: '',
    cover: '',
    content: '',
    coverFile: null,
    coverPreview: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑资讯'
  Object.assign(form, row)
  form.coverPreview = getImageUrl(row.cover)
  form.coverFile = null
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该资讯吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const result = await get(`/news/deleteNews?id=${row.id}`)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getNewsList()
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

const handleBatchDelete = () => {
  if (!selectedRows.value.length) return
  ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条资讯吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const result = await post('/news/delete/batch', ids)
      if (result.code === 200) {
        ElMessage.success('批量删除成功')
        getNewsList()
      }
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

const viewContent = (row) => {
  selectedContent.value = row.content
  contentDialogVisible.value = true
}

const handleCoverChange = (file) => {
  const isImage = file.raw.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return
  }
  if (form.coverPreview) {
    URL.revokeObjectURL(form.coverPreview)
  }
  form.coverFile = file.raw
  form.coverPreview = URL.createObjectURL(file.raw)
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  return true
}

const handleSubmit = async () => {
  try {
    const formData = new FormData()
    Object.keys(form).forEach(key => {
      if (!['coverPreview', 'coverFile', 'cover'].includes(key)) {
        formData.append(key, form[key])
      }
    })
    
    if (form.coverFile) {
      formData.append('image', form.coverFile)
    } else if (form.cover) {
      formData.append('cover', form.cover)
    }
    
    const url = form.id ? '/news/updateNews' : '/news/addNews'
    const result = await post(url, formData)
    
    if (result.code === 200) {
      ElMessage.success(form.id ? '更新成功' : '添加成功')
      dialogVisible.value = false
      if (form.coverPreview) {
        URL.revokeObjectURL(form.coverPreview)
      }
      getNewsList()
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getNewsList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getNewsList()
}

// 组件卸载时清理预览URL
onBeforeUnmount(() => {
  if (form.coverPreview) {
    URL.revokeObjectURL(form.coverPreview)
  }
})

// 初始化
getNewsList()
</script>

<style scoped>
.travel-news {
  padding: 20px;
  background-color: #f0f2f5;
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

.search-input {
  width: 300px;
}

.search-buttons {
  display: flex;
  gap: 10px;
}

.operation-buttons {
  display: flex;
  gap: 10px;
}

.cover-image {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
}

.image-error {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.cover-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-image {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.content-preview {
  max-height: 60vh;
  overflow-y: auto;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}
</style> 