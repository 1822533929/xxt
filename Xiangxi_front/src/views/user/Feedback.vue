<template>
  <div class="feedback">
    <div class="page-header">
      <h2>问题反馈</h2>
    </div>

    <div class="feedback-content">
      <el-form :model="feedbackForm" :rules="rules" ref="feedbackFormRef" label-width="100px">
        <el-form-item label="反馈类型" prop="type">
          <el-select v-model="feedbackForm.type" placeholder="请选择反馈类型">
            <el-option label="功能建议" value="suggestion" />
            <el-option label="问题报告" value="bug" />
            <el-option label="投诉" value="complaint" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>

        <el-form-item label="标题" prop="title">
          <el-input v-model="feedbackForm.title" placeholder="请输入标题" />
        </el-form-item>

        <el-form-item label="详细描述" prop="content">
          <el-input
            v-model="feedbackForm.content"
            type="textarea"
            :rows="4"
            placeholder="请详细描述您的问题或建议"
          />
        </el-form-item>

        <el-form-item label="图片上传">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-change="handleImageChange"
            :on-remove="handleImageRemove"
            :multiple="false"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="feedbackForm.contact" placeholder="请留下您的联系方式，方便我们回复" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>


  </div>
</template>

<script>
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from "element-plus";
import { get, post, getLocalToken } from "@/common"


export default {

  name: 'Feedback',
  components: {
    Plus
  },
  data() {
    return {
      feedbackForm: {
        type: '',
        title: '',
        content: '',
        contact: '',
        images: []
      },
      rules: {
        type: [{ required: true, message: '请选择反馈类型', trigger: 'change' }],
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入详细描述', trigger: 'blur' }],
        contact: [{ required: true, message: '请输入联系方式', trigger: 'blur' }]
      },

    }
  },

  methods: {
    handleImageChange(file) {
      this.feedbackForm.images = [file.raw]
    },
    handleImageRemove(file) {
      this.feedbackForm.images = []
    },
    submitFeedback() {
      this.$refs.feedbackFormRef.validate((valid) => {
        if (valid) {
          const token = getLocalToken();
          const formData = new FormData()
          formData.append('type', this.feedbackForm.type)
          formData.append('title', this.feedbackForm.title)
          formData.append('content', this.feedbackForm.content)
          formData.append('contact', this.feedbackForm.contact)
          // formData.append('userId', 1)
          
          if (this.feedbackForm.images && this.feedbackForm.images.length > 0) {
            formData.append('image', this.feedbackForm.images[0])
          } else {
            const emptyBlob = new Blob([''], { type: 'application/octet-stream' })
            const emptyFile = new File([emptyBlob], 'empty.txt', { type: 'application/octet-stream' })
            formData.append('image', emptyFile)
          }
          
          for (let pair of formData.entries()) {
            console.log(pair[0] + ': ' + pair[1])
          }
          
          post('/feedback/add', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }).then(result => {
            if (result.code === 200) {
              ElMessage.success('提交反馈成功!')
              this.resetForm()
            } else {
              ElMessage.error(result.msg || '提交失败!')
            }
          }).catch(() => {
            ElMessage.error('提交失败，请稍后重试')
          })
        }
      })
    },
    resetForm() {
      this.$refs.feedbackFormRef.resetFields()
      this.feedbackForm.images = []
      this.$refs.upload && this.$refs.upload.clearFiles()
    },

  },

}

</script>

<style scoped>
.feedback {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.feedback-content {
  max-width: 800px;
  margin: 0 auto;
}


.my-feedback h3 {
  margin-bottom: 20px;
}
</style> 