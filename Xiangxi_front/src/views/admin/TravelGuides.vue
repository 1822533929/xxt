<template>
  <div class="travel-guides">
    <div class="search-container">
      <el-input
        v-model="searchForm.keyword"
        placeholder="请输入关键词"
        class="search-input"
        clearable
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <div class="operation-container">
      <el-button type="primary" @click="handleAdd">新增攻略</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="selectedIds.length === 0">
        批量删除
      </el-button>
    </div>

    <el-table 
      :data="tableData" 
      style="width: 100%" 
      v-loading="loading"
      @selection-change="handleSelectionChange"
      class="custom-table"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="title" label="标题" min-width="150" show-overflow-tooltip />
      <el-table-column prop="descr" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column label="封面" width="100">
        <template #default="{ row }">
          <el-image
            :src="getImageUrl(row.cover)"
            :preview-src-list="[getImageUrl(row.cover)]"
            class="cover-image"
            fit="cover"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="内容" width="100">
        <template #default="{ row }">
          <el-button link type="primary" @click="viewContent(row)">
            查看内容
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="likes" label="点赞量" width="100" />
      <el-table-column prop="date" label="发布日期" width="180" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      :total="total"
      v-model:current-page="currentPage"
      :page-size="pageSize"
      :page-sizes="[5, 10, 20]"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
      layout="total, sizes, prev, pager, next"
      background
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="800px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="描述" required>
          <el-input type="textarea" v-model="form.descr" rows="3" />
        </el-form-item>
        <el-form-item label="封面">
          <el-upload
            class="cover-upload"
            action="#"
            :auto-upload="false"
            :on-change="handleCoverChange"
            :show-file-list="false"
          >
            <img 
              v-if="form.coverPreview || form.cover" 
              :src="form.coverPreview || getImageUrl(form.cover)" 
              class="upload-preview"
            >
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input type="textarea" v-model="form.content" rows="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 内容查看对话框 -->
    <el-dialog
      title="攻略内容"
      v-model="contentDialogVisible"
      width="800px"
    >
      <div class="content-preview" v-html="currentContent"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture, Plus } from '@element-plus/icons-vue'
import { get, post } from '@/common'

// 数据相关
const searchForm = reactive({
  keyword: ''
})
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const selectedIds = ref([])
const contentDialogVisible = ref(false)
const currentContent = ref('')

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增攻略')
const form = reactive({
  id: null,
  title: '',
  descr: '',
  cover: '',
  coverPreview: '',
  content: '',
})

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 获取攻略列表
const getArticleList = async () => {
  loading.value = true
  try {
    const params = {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
    }
    const url = searchForm.keyword ? '/article/search' : '/article/selectPage'
    if (searchForm.keyword) {
      params.keyword = searchForm.keyword
    }
    
    const result = await get(url, params)
    if (result.code === 200) {
      if (searchForm.keyword) {
        tableData.value = result.data
        total.value = result.data.length
      } else {
        tableData.value = result.data.list
        total.value = result.data.total
      }
    } else {
      ElMessage.error(result.msg || '获取攻略列表失败')
    }
  } catch (error) {
    console.error('获取攻略列表错误:', error)
    ElMessage.error('获取攻略列表失败')
  } finally {
    loading.value = false
  }
}

// 方法
const handleAdd = () => {
  dialogTitle.value = '新增攻略'
  Object.assign(form, {
    id: null,
    title: '',
    descr: '',
    cover: '',
    coverPreview: '',
    content: '',
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑攻略'
  Object.assign(form, {
    id: row.id,
    title: row.title,
    descr: row.descr,
    cover: row.cover,
    content: row.content || '',
  })
  dialogVisible.value = true
}

const handleSearch = () => {
  currentPage.value = 1
  getArticleList()
}

const resetSearch = () => {
  searchForm.keyword = ''
  handleSearch()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) return
  ElMessageBox.confirm('确定要删除选中的攻略吗？', '提示', {
    type: 'warning'
  }).then(() => {
    post('/article/delete/batch', selectedIds.value)
      .then(result => {
        if (result.code === 200) {
          ElMessage.success('删除成功')
          getArticleList()
          selectedIds.value = []
        } else {
          ElMessage.error(result.msg || '删除失败')
        }
      })
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该攻略吗？', '提示', {
    type: 'warning'
  }).then(() => {
    post('/article/delete/' + row.id, null)
      .then(result => {
        if (result.code === 200) {
          ElMessage.success('删除成功')
          getArticleList()
        } else {
          ElMessage.error(result.msg || '删除失败')
        }
      })
  })
}

const handleCoverChange = (file) => {
  form.cover = file
  form.coverPreview = URL.createObjectURL(file.raw)
}

const viewContent = (row) => {
  currentContent.value = row.content
  contentDialogVisible.value = true
}

const handleSubmit = () => {
  const isAdd = dialogTitle.value === '新增攻略'
  const url = isAdd ? '/article/add' : '/article/update'
  
  const formData = new FormData()
  formData.append('title', form.title)
  formData.append('descr', form.descr)
  formData.append('content', form.content)
  
  if (!isAdd) {
    formData.append('id', form.id)
  }
  
  if (form.cover && form.cover.raw) {
    formData.append('image', form.cover.raw)
  }

  post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
  .then(result => {
    if (result.code === 200) {
      ElMessage.success(isAdd ? '添加成功' : '更新成功')
      dialogVisible.value = false
      getArticleList()
    } else {
      ElMessage.error(result.msg || (isAdd ? '添加失败' : '更新失败'))
    }
  })
}

const handlePageChange = (page) => {
  currentPage.value = page
  getArticleList()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  getArticleList()
}

onMounted(() => {
  getArticleList()
})
</script>

<style scoped>
.travel-guides {
  padding: 20px;
  background-color: #f5f7fa;
}

.search-container,
.operation-container {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 300px;
}

.custom-table {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.cover-image {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  cursor: pointer;
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

.cover-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 148px;
  height: 148px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.content-preview {
  max-height: 500px;
  overflow-y: auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  line-height: 1.6;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
}
</style> 