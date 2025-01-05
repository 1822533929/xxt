<template>
  <div class="travel-routes">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <div class="search-inputs">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入路线名称"
          class="search-input"
          clearable
        />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd">新增路线</el-button>
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
      <el-table-column prop="name" label="路线名称" min-width="150" />
      <el-table-column prop="days" label="天数" width="80" />
      <el-table-column prop="tips" label="小提示" min-width="150" />
      <el-table-column label="路线内容" width="100">
        <template #default="{ row }">
          <el-button link type="primary" @click="showContent(row)">
            查看内容
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="图片" width="120">
        <template #default="{ row }">
          <el-image 
            :src="getImageUrl(row.img)" 
            fit="cover"
            style="width: 80px; height: 60px; border-radius: 4px;"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="location" label="地点" min-width="120" />
      <el-table-column prop="longitude" label="经度" width="100" />
      <el-table-column prop="latitude" label="纬度" width="100" />
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
      :total="total"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="路线名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="天数" prop="days">
          <el-input-number v-model="form.days" :min="1" />
        </el-form-item>
        <el-form-item label="小提示" prop="tips">
          <el-input v-model="form.tips" type="textarea" rows="2" />
        </el-form-item>
        <el-form-item label="路线内容" prop="content">
          <el-input v-model="form.content" type="textarea" rows="4" />
        </el-form-item>
        <el-form-item label="图片" prop="img">
          <el-upload
            class="product-uploader"
            action="#"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleImageChange"
            :before-upload="beforeUpload"
          >
            <img 
              v-if="form.tempPreviewUrl || form.img" 
              :src="form.tempPreviewUrl || getImageUrl(form.img)" 
              class="preview-image" 
            />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 内容查看对话框 -->
    <el-dialog
      title="路线内容"
      v-model="contentDialogVisible"
      width="600px"
    >
      <div class="content-preview">{{ currentContent }}</div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture, Plus } from '@element-plus/icons-vue'
import { get, post } from '@/common'
import { API_BASE_URL } from '@/common/constants'

// 数据
const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const selectedRows = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增路线')
const contentDialogVisible = ref(false)
const currentContent = ref('')
const formRef = ref(null)

const searchForm = reactive({
  keyword: ''
})

const form = reactive({
  name: '',
  days: 1,
  tips: '',
  content: '',
  img: '',
  tempPreviewUrl: '',
  location: '',
  longitude: '',
  latitude: ''
})

const rules = {
  name: [{ required: true, message: '请输入路线名称', trigger: 'blur' }],
  days: [{ required: true, message: '请输入天数', trigger: 'blur' }],
  location: [{ required: true, message: '请输入地点', trigger: 'blur' }]
}

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return API_BASE_URL + url
}

// 获取数据
const getTableData = async () => {
  loading.value = true
  try {
    const result = await get('/routes/selectAll', {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchForm.keyword
    })
    if (result.code === 200) {
      tableData.value = result.data.list
      total.value = result.data.total
    }
  } catch (error) {
    console.error('获取路线列表失败:', error)
    ElMessage.error('获取路线列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  getTableData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.keyword = ''
  handleSearch()
}

// 处理选择行变化
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 显示内容
const showContent = (row) => {
  currentContent.value = row.content
  contentDialogVisible.value = true
}

// 处理图片选择
const handleImageChange = (file) => {
  const isImage = file.raw.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  form.img = file.raw
  form.tempPreviewUrl = URL.createObjectURL(file.raw)
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
  }
  return false
}

// CRUD操作
const handleAdd = () => {
  dialogTitle.value = '新增路线'
  Object.assign(form, {
    name: '',
    days: 1,
    tips: '',
    content: '',
    img: '',
    location: '',
    longitude: '',
    latitude: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑路线'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该路线吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const result = await post('/routes/delete/' + row.id)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getTableData()
      } else {
        ElMessage.error(result.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

const handleBatchDelete = () => {
  if (!selectedRows.value.length) return
  ElMessageBox.confirm('确定要删除选中的路线吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const result = await post('/routes/delete/batch', ids)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getTableData()
      } else {
        ElMessage.error(result.msg || '删除失败')
      }
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const formData = new FormData()
        formData.append('name', form.name)
        formData.append('days', form.days)
        formData.append('tips', form.tips)
        formData.append('content', form.content)
        formData.append('location', form.location)
        formData.append('longitude', form.longitude)
        formData.append('latitude', form.latitude)
        
        if (form.id) {
          formData.append('id', form.id)
        }
        
        if (form.img instanceof File) {
          formData.append('image', form.img)
        } else if (form.img) {
          formData.append('img', form.img)
        }

        const url = form.id ? '/routes/update' : '/routes/add'
        const result = await post(url, formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })

        if (result.code === 200) {
          ElMessage.success(form.id ? '更新成功' : '添加成功')
          dialogVisible.value = false
          getTableData()
        } else {
          ElMessage.error(result.msg || (form.id ? '更新失败' : '添加失败'))
        }
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

// 重置表单时清除临时预览
const handleDialogClose = () => {
  if (form.tempPreviewUrl) {
    URL.revokeObjectURL(form.tempPreviewUrl)
    form.tempPreviewUrl = ''
  }
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  getTableData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getTableData()
}

onMounted(() => {
  getTableData()
})
</script>

<style scoped>
.travel-routes {
  padding: 20px;
}

.search-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-inputs {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 200px;
}

.operation-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

.product-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 150px;
  transition: border-color 0.3s;
}

.product-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 200px;
  height: 150px;
  text-align: center;
  line-height: 150px;
}

.preview-image {
  width: 200px;
  height: 150px;
  display: block;
  object-fit: cover;
}

.content-preview {
  white-space: pre-wrap;
  line-height: 1.6;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  max-height: 400px;
  overflow-y: auto;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
}
</style> 