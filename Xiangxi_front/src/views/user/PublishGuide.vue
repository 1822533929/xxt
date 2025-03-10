<template>
  <div class="publish-guide">
    <el-card class="guide-form">
      <template #header>
        <div class="form-header">
          <h2>发布攻略</h2>
        </div>
      </template>

      <el-form :model="form" label-width="100px" ref="formRef">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入攻略标题" />
        </el-form-item>

        <el-form-item label="描述" required>
          <el-input 
            type="textarea" 
            v-model="form.descr" 
            rows="3"
            placeholder="请输入攻略简要描述" 
          />
        </el-form-item>

        <el-form-item label="封面">
          <el-upload
            class="cover-upload"
            action=""
            :auto-upload="false"
            :on-change="handleCoverChange"
            :show-file-list="false"
            accept="image/*"
          >
            <img 
              v-if="form.coverPreview" 
              :src="form.coverPreview" 
              class="upload-preview"
              alt="封面预览"
            >
            <div v-else class="upload-placeholder">
              <el-icon><Plus /></el-icon>
              <span>点击上传封面</span>
            </div>
          </el-upload>
          <div class="upload-tip">建议尺寸: 800x500px, 支持 jpg、png 格式</div>
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
              style="height: 500px"
              v-model="form.content"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>

        <el-form-item>
          <div class="form-buttons">
            <el-button type="primary" @click="handleSubmit">发布</el-button>
            <el-button @click="handleCancel">取消</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, shallowRef, reactive, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { post, get } from '@/common'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const router = useRouter()
const formRef = ref(null)
const editorRef = shallowRef()

// 编辑器配置
const mode = 'default'
const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入攻略内容...',
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

// 表单数据
const form = reactive({
  title: '',
  descr: '',
  content: '',
  cover: null,
  coverPreview: ''
})

// 处理封面上传
const handleCoverChange = (file) => {
  form.cover = file
  form.coverPreview = URL.createObjectURL(file.raw)
}

// 修改处理内容中的图片上传方法
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
          // 将 base64 转换为文件
          const arr = src.split(',')
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
          formData.append('path', 'articleContent')
          const result = await post('/upload/image', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          })
          
          if (result.code === 200 && result.data) {
            img.setAttribute('src', result.data)
          } else {
            console.error('图片上传失败:', result.msg)
          }
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

// 提交表单
const handleSubmit = async () => {
  try {
    // 处理内容中的图片
    const processedContent = await processContent(form.content)
    
    // 创建 FormData
    const formData = new FormData()
    formData.append('title', form.title)
    formData.append('descr', form.descr)
    formData.append('content', processedContent)
    
    if (form.cover && form.cover.raw) {
      formData.append('image', form.cover.raw)
    }

    const result = await post('/article/add', formData, {
      headers: { 'Content-Type': undefined }
    })
    
    if (result.code === 200) {
      ElMessage.success('发布成功')
      router.push('/user/guides')
    } else {
      ElMessage.error(result.msg || '发布失败')
    }
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败')
  }
}

const handleCancel = () => {
  router.back()
}

const handleCreated = (editor) => {
  editorRef.value = editor
}

onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
  }
})
</script>

<style scoped>
.publish-guide {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.guide-form {
  max-width: 1000px;
  margin: 0 auto;
}

.form-header {
  text-align: center;
}

.form-header h2 {
  margin: 0;
  color: #303133;
}

.cover-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 300px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color 0.3s;
}

.cover-upload:hover {
  border-color: #409eff;
}

.upload-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.upload-placeholder {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #8c939d;
}

.upload-placeholder .el-icon {
  font-size: 28px;
  margin-bottom: 8px;
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

:deep(.w-e-text-container) {
  min-height: 300px !important;
}

.form-buttons {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  margin-left: 250px;
}

.form-buttons :deep(.el-button) {
  padding: 12px 40px;
  font-size: 16px;
  min-width: 120px;
}

.upload-tip {
  margin-top: 8px;
  color: #909399;
  font-size: 12px;
}
</style> 