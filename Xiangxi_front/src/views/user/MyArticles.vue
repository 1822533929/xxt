<template>
  <div class="my-articles">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-input
        v-model="searchQuery"
        placeholder="搜索文章标题..."
        class="search-input"
        clearable
        @input="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </el-card>

    <!-- 操作栏 -->
    <el-card class="operation-card">
      <div class="operation-buttons">
        <el-button type="primary" @click="handlePublish">
          <el-icon><Edit /></el-icon>
          发布攻略
        </el-button>
        <el-button 
          type="danger" 
          @click="handleBatchDelete"
          :disabled="selectedArticles.length === 0"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
      </div>
    </el-card>

    <!-- 文章列表 -->
    <el-card v-loading="loading" class="articles-card">
      <el-table 
        :data="articleList" 
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <div class="title-cell">{{ row.title }}</div>
          </template>
        </el-table-column>

        <el-table-column label="封面" width="120">
          <template #default="{ row }">
            <el-image
                :src="getImageUrl(row.cover)"
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

        <el-table-column prop="descr" label="描述" min-width="300">
          <template #default="{ row }">
            <div class="descr-cell">{{ row.descr }}</div>
          </template>
        </el-table-column>

        <el-table-column label="内容" width="100">
          <template #default="{ row }">
            <el-button 
              link 
              type="primary" 
              @click="viewContent(row)"
            >
              查看内容
            </el-button>
          </template>
        </el-table-column>
        
        <el-table-column prop="likes" label="点赞" width="100">
          <template #default="{ row }">
            <span class="likes-count">
              <el-icon><Star /></el-icon>
              {{ row.likes || 0 }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column prop="date" label="发布时间" width="180" />
        
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button 
              link 
              type="primary" 
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button 
              link 
              type="danger" 
              @click="handleDelete(row)"
            >
              删除
            </el-button>
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
    <!-- 编辑对话框 -->
    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="800px"
        :before-close="handleDialogClose"
        :destroy-on-close="true"
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
                :src="form.coverPreview || getImageUrls(form.cover)"
                class="upload-preview"
            >
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容" required>
          <div class="editor-container">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
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


    <!-- 内容预览对话框 -->
    <el-dialog
      v-model="contentDialogVisible"
      title="文章内容"
      width="60%"
    >
      <div class="content-preview" v-html="selectedContent"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted, reactive, shallowRef, onBeforeUnmount} from 'vue'
import { useRouter } from 'vue-router'
import { get, post } from '@/common'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Edit, Delete, Picture, Star, Plus } from '@element-plus/icons-vue'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const router = useRouter()
const loading = ref(false)
const articleList = ref([])
const selectedArticles = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchQuery = ref('')
const contentDialogVisible = ref(false)
const selectedContent = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('编辑攻略')
const form = reactive({
  id: null,
  title: '',
  descr: '',
  cover: '',
  coverPreview: '',
  content: '',
})
const handleCoverChange = (file) => {
  form.cover = file
  form.coverPreview = URL.createObjectURL(file.raw)
}
const editorRef = shallowRef()
const mode = 'default'

// 工具栏配置
const toolbarConfig = {
  excludeKeys: []
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
    formData.append('path',"articleContent")//富文本图片上传路径
    const result = await post('/upload/image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': 'Bearer ' + localStorage.getItem('token'),
      }
    })

    if (result.code === 200 && result.data) {
      console.log('Upload result:', result)
      if (!result.data) {
        throw new Error('图片上传失败：服务器返回空路径')
      }
      return result.data
    }
    throw new Error(result.msg || '上传失败')
  } catch (error) {
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
          // 只更新 src 属性，保留其他属性
          img.setAttribute('src', imageUrl)
          console.log('Updated image src:', imageUrl)
        } catch (error) {
          console.error('处理图片失败:', error)
        }
      }
    }

    const processedContent = div.innerHTML
    console.log('Processed content:', processedContent)
    return processedContent
  } catch (error) {
    console.error('处理内容中的图片失败:', error)
    throw error
  }
}
// 获取图片URL
const getImageUrls = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  // 确保 url 以 / 开头
  const path = url.startsWith('/') ? url : '/' + url
  // console.log('Getting image URL:', path)
  return import.meta.env.VITE_API_BASE_URL + path
}
const handleEdit = (row) => {
  // 如果编辑器已存在，先销毁
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }

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
const handleCreated = (editor) => {
  editorRef.value = editor
}
const handleSubmit = async () => {
  try {
    const url = '/article/update'
    // 处理内容中的图片
    const processedContent = await processContent(form.content)

    const formData = new FormData()
    formData.append('title', form.title)
    formData.append('descr', form.descr)
    formData.append('content', processedContent)
    formData.append('id', form.id)


    if (form.cover && form.cover.raw) {
      formData.append('image', form.cover.raw)
    }

    const result = await post(url, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    if (result.code === 200) {
      ElMessage.success('更新成功')
      dialogVisible.value = false
      getArticleList()
    } else {
      ElMessage.error(result.msg || ('更新失败'))
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}
// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
})


//
// 获取文章列表
const getArticleList = async () => {
  loading.value = true
  try {
    //
    const params = {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
    }
    let url = '/article/findByUserId'
    if (searchQuery.value) {
      url = '/article/search'
      params.keyword = searchQuery.value
    }
    const result = await get(url, params)
    if (result.code === 200) {
      if (searchQuery.value){
        articleList.value = result.data
        total.value = result.data.length
      }else {
        articleList.value = result.data.list
        total.value = result.data.total
      }
    }
  } catch (error) {
    console.error('获取文章列表失败:', error)
    ElMessage.error('获取文章列表失败')
  } finally {
    loading.value = false
  }
}

const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

const handleSearch = () => {
  currentPage.value = 1
  getArticleList()
}

const handlePublish = () => {
  router.push('/user/publish-guide')
}

// const handleEdit = (row) => {
//   router.push(`/user/edit-guide/${row.id}`)
// }

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这篇文章吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const result = await post(`/article/delete/${row.id}`)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getArticleList()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const handleBatchDelete = () => {
  if (selectedArticles.value.length === 0) return
  
  ElMessageBox.confirm('确定要删除选中的文章吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const ids = selectedArticles.value.map(item => item.id)
      const result = await post('/article/delete/batch', ids)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getArticleList()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const handleSelectionChange = (selection) => {
  selectedArticles.value = selection
}

const viewContent = (row) => {
  selectedContent.value = row.content
  contentDialogVisible.value = true
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getArticleList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getArticleList()
}

const handleDialogClose = (done) => {
  // 关闭对话框时销毁编辑器
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
  // 重置表单
  Object.assign(form, {
    id: null,
    title: '',
    descr: '',
    cover: '',
    coverPreview: '',
    content: '',
  })
  done()
}

onMounted(() => {
  getArticleList()
})
</script>

<style scoped>
.my-articles {
  padding: 20px;
  background-color: #f5f7fa;
}

.search-card,
.operation-card,
.articles-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.search-input {
  max-width: 300px;
}

.operation-buttons {
  display: flex;
  gap: 10px;
}

.title-cell,
.descr-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.cover-image {
  width: 80px;
  height: 60px;
  border-radius: 4px;
}

.image-error {
  width: 80px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
}

.likes-count {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #606266;
}

.content-preview {
  max-height: 500px;
  overflow-y: auto;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}
/**/
.editor-container {
  border: 1px solid #ccc;
  z-index: 100;
  background-color: #fff;
}
.upload-icon {
  font-size: 28px;
  color: #8c939d;
}
.upload-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
/**/
:deep(.w-e-scroll) {
  height: 450px !important;
}
</style> 