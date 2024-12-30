<template>
  <div class="travel-products">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <div class="search-input-group">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入商品名称"
          class="search-input"
        />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="left-buttons">
        <el-button type="primary" @click="handleAdd">新增商品</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="!selectedRows.length">
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 表格 -->
    <el-table 
      :data="tableData" 
      style="width: 100%" 
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="title" label="名称" min-width="120" />
      <el-table-column label="图片" width="120">
        <template #default="{ row }">
          <el-image 
            :src="getImageUrl(row.cover)" 
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
      <el-table-column prop="descr" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="money" label="价格" width="100">
        <template #default="{ row }">
          <span class="price">¥{{ row.money }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="inventory" label="库存" width="80" />
      <el-table-column prop="time" label="发布日期" width="120" />
      <el-table-column label="特色" width="120">
        <template #default="{ row }">
          <div class="tags-column">
            <el-tag 
              v-for="tag in row.tags?.split(',').filter(Boolean)"
              :key="tag"
              size="small"
              class="tag-item"
              type="success"
            >
              {{ tag }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="详细" width="80">
        <template #default="{ row }">
          <el-button link type="primary" @click="viewDetail(row)">
            查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

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
      width="700px"
      @closed="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="商品名称" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="商品图片" prop="cover">
          <el-upload
            class="product-uploader"
            action="#"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleImageChange"
            :before-upload="beforeUpload"
          >
            <img 
              v-if="form.tempPreviewUrl || form.cover" 
              :src="form.tempPreviewUrl || getImageUrl(form.cover)" 
              class="preview-image" 
            />
            <el-icon v-else class="product-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品描述" prop="descr">
          <el-input type="textarea" v-model="form.descr" rows="3" />
        </el-form-item>
        <el-form-item label="商品价格" prop="money">
          <el-input-number v-model="form.money" :min="0" :precision="2" :step="10" />
        </el-form-item>
        <el-form-item label="库存数量" prop="inventory">
          <el-input-number v-model="form.inventory" :min="0" :step="1" />
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-select
            v-model="form.selectedTags"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="请选择或创建标签"
            @change="handleTagsChange"
          >
            <el-option
              v-for="tag in availableTags"
              :key="tag"
              :label="tag"
              :value="tag"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="详细内容" prop="content">
          <div class="editor-container">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              style="height: 500px"
              v-model="form.content"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="handleCreated"
            />
          </div>
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
import { ref, reactive, onMounted, shallowRef, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture, Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { get, post, del } from '@/common'
import { API_BASE_URL } from '@/common/constants'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const router = useRouter()
const apiBaseUrl = API_BASE_URL
const formRef = ref(null)
const searchForm = reactive({
  keyword: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const selectedRows = ref([])
const availableTags = ref([])

// 表单数据
const dialogVisible = ref(false)
const dialogTitle = ref('新增商品')
const form = reactive({
  id: null,
  title: '',
  cover: '',
  descr: '',
  money: 0,
  inventory: 0,
  content: '',
  selectedTags: [],
  tags: '',
  tempImage: null,  // 用于存储临时图片文件
  tempPreviewUrl: '' // 用于存储临时预览URL
})

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  descr: [{ required: true, message: '请输入商品描述', trigger: 'blur' }],
  money: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
  inventory: [{ required: true, message: '请输入库存数量', trigger: 'blur' }]
}

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('blob:')) return url
  if (url.startsWith('http')) return url
  return apiBaseUrl + url
}

// 获取所有标签
const getAllTags = async () => {
  try {
    const result = await get('/travels/getAllTags')
    if (result.code === 200) {
      availableTags.value = result.data.map(tag => tag.tagName)
    }
  } catch (error) {
    console.error('获取标签失败:', error)
  }
}

// 获取表格数据
const getTableData = async () => {
  loading.value = true
  try {
    const result = await get('/travels/admin/selectAll', {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchForm.keyword
    })
    if (result.code === 200) {
      tableData.value = result.data.list
      total.value = result.data.total
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 处理标签变化
const handleTagsChange = (value) => {
  form.tags = value.join(',')
}

// 上传相关方法
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 添加图片选择处理方法
const handleImageChange = (file) => {
  form.tempImage = file.raw
  // 创建临时预览URL并存储
  const previewUrl = URL.createObjectURL(file.raw)
  form.tempPreviewUrl = previewUrl
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

const handleAdd = () => {
  dialogTitle.value = '新增商品'
  Object.assign(form, {
    id: null,
    title: '',
    cover: '',
    descr: '',
    money: 0,
    inventory: 0,
    content: '',
    selectedTags: [],
    tags: ''
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 处理内容中的图片
        const processedContent = await processContent(form.content)
        form.content = processedContent

        // 1. 先上传图片
        if (form.tempImage) {
          const imageFormData = new FormData()
          imageFormData.append('image', form.tempImage)
          const uploadResult = await post('/upload/image', imageFormData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          if (uploadResult.code === 200) {
            form.cover = uploadResult.data
          } else {
            ElMessage.error('图片上传失败')
            return
          }
        }
        
        // 2. 提交商品信息
        const url = form.id ? '/travels/admin/update' : '/travels/admin/add'
        const submitData = { ...form }
        // 删除临时数据
        delete submitData.tempImage
        delete submitData.tempPreviewUrl
        const result = await post(url, submitData)

        if (result.code === 200) {
          ElMessage.success(result.msg)
          dialogVisible.value = false
          // 清理临时预览URL
          if (form.tempPreviewUrl) {
            URL.revokeObjectURL(form.tempPreviewUrl)
          }
          getTableData()
        } else {
          ElMessage.error(result.msg)
        }
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败')
      }
    }
  })
}

// 添加分页方法
const handleSizeChange = (val) => {
  pageSize.value = val
  getTableData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getTableData()
}

// 添加选择行方法
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 添加查看详情方法
const viewDetail = (row) => {
  router.push(`/user/travel-detail/${row.id}`)
}

// 添加编辑方法
const handleEdit = (row) => {
  dialogTitle.value = '编辑商品'
  Object.assign(form, {
    id: row.id,
    title: row.title,
    cover: row.cover,
    descr: row.descr,
    money: Number(row.money),
    inventory: Number(row.inventory),
    content: row.content,
    selectedTags: row.tags ? row.tags.split(',') : [],
    tags: row.tags || ''
  })
  dialogVisible.value = true
}

// 添加删除方法
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const result = await del(`/travels/admin/delete/${row.id}`)
      if (result.code === 200) {
        ElMessage.success(result.msg)
        getTableData()
      } else {
        ElMessage.error(result.msg)
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 添加批量删除方法
const handleBatchDelete = () => {
  if (!selectedRows.value.length) return
  ElMessageBox.confirm('确定要删除选中的商品吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const result = await post('/travels/admin/delete/batch', ids)
      if (result.code === 200) {
        ElMessage.success(result.msg)
        getTableData()
      } else {
        ElMessage.error(result.msg)
      }
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

// 修改对话框关闭时的处理
const handleDialogClose = () => {
  // 清理临时预览URL
  if (form.tempPreviewUrl) {
    URL.revokeObjectURL(form.tempPreviewUrl)
    form.tempPreviewUrl = ''
  }
}

// 编辑器相关
const editorRef = shallowRef()
const mode = 'default'

// 工具栏配置
const toolbarConfig = {
  excludeKeys: [] // 可以配置需要排除的功能键
}

// 编辑器配置
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      // 自定义图片上传
      customUpload: async (file, insertFn) => {
        try {
          // 将文件转换为 base64
          const reader = new FileReader()
          reader.onload = (e) => {
            const base64 = e.target.result
            // 插入图片到编辑器，使用 base64
            insertFn(base64)
          }
          reader.readAsDataURL(file)
        } catch (error) {
          console.error('图片处理失败:', error)
          ElMessage.error('图片处理失败')
        }
      }
    }
  }
}

// 处理图片上传到服务器
const uploadImageToServer = async (base64Str) => {
  try {
    // 将 base64 转换为文件
    const arr = base64Str.split(',')
    const mime = arr[0].match(/:(.*?);/)[1]
    const bstr = atob(arr[1])
    let n = bstr.length
    const u8arr = new Uint8Array(n)
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n)
    }
    const file = new File([u8arr], 'image.png', { type: mime })

    // 上传文件
    const formData = new FormData()
    formData.append('image', file)
    const result = await post('/upload/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    
    if (result.code === 200 && result.data) {
      return result.data
    }
    throw new Error(result.msg || '上传失败')
  } catch (error) {
    console.error('图片上传失败:', error)
    throw error
  }
}

// 处理内容中的所有 base64 图片
const processContent = async (content) => {
  try {
    // 创建临时容器
    const div = document.createElement('div')
    div.innerHTML = content
    
    // 获取所有图片
    const images = div.getElementsByTagName('img')
    
    // 转换为数组以避免实时更新的问题
    const imageArray = Array.from(images)
    
    // 处理每个图片
    for (const img of imageArray) {
      const src = img.getAttribute('src')
      if (src && src.startsWith('data:image')) {
        try {
          const imageUrl = await uploadImageToServer(src)
          if (!imageUrl) {
            console.error('图片上传返回空URL')
            continue
          }
          img.setAttribute('src', imageUrl)
        } catch (error) {
          console.error('处理图片失败:', error)
        }
      }
    }
    
    return div.innerHTML
  } catch (error) {
    console.error('处理内容中的图片失败:', error)
    throw error
  }
}

// 页面加载时获取数据
onMounted(() => {
  getTableData()
  getAllTags()
})

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
  }
})

const handleCreated = (editor) => {
  editorRef.value = editor
}
</script>

<style scoped>
.travel-products {
  padding: 20px;
  background-color: #f5f7fa;
}

.search-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.search-input {
  width: 300px;
}

.search-buttons {
  display: flex;
  gap: 10px;
}

.operation-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.left-buttons {
  display: flex;
  gap: 10px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
  background-color: #fff;
}

.tags-column {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 4px 0;
}

.tag-item {
  margin: 2px 0;
  width: fit-content;
  display: inline-block;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  background-color: #fff;
  padding: 15px 20px;
  border-radius: 8px;
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

.search-input-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.preview-image {
  width: 200px;
  height: 150px;
  object-fit: cover;
}

.product-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 150px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-uploader:hover {
  border-color: #409EFF;
}

.product-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

:deep(.w-e-text-container) {
  min-height: 300px !important;
}
</style> 