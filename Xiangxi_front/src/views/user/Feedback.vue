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

    <!-- 我的反馈列表 -->
    <div class="my-feedback">
      <h3>我的反馈</h3>
      <el-table :data="myFeedbacks" style="width: 100%">
        <el-table-column prop="type" label="类型" width="120">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.type)">{{ getTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180" />
      </el-table>
    </div>
  </div>
</template>

<script>
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from "element-plus";
import { get, post } from "@/common"


export default {

  name: 'Feedback',
  components: {
    Plus
  },
  data() {
    return {
      // @RequestParam("image") MultipartFile image,
      // @RequestParam("userId") Integer userId,
      // @RequestParam("type") String type,
      // @RequestParam("title") String title,
      // @RequestParam("content") String content,
      // @RequestParam(value = "contact", required = false) String contact
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
      myFeedbacks: [
        {
          id: 1,
          type: 'suggestion',
          title: '关于网站功能的建议',
          status: '待处理',
          createTime: '2024-03-20 14:30:00'
        },
        // 更多反馈数据...
      ]
    }
  },

  methods: {
    handleImageChange(file) {
      console.log('选择图片:', file)
    },
    submitFeedback() {
      this.$refs.feedbackFormRef.validate((valid) => {
        if (valid) {
          const formData = new FormData()
          formData.append('type', this.feedbackForm.type)
          formData.append('title', this.feedbackForm.title)
          formData.append('content', this.feedbackForm.content)
          formData.append('contact', this.feedbackForm.contact)
          
          // 如果有图片，添加图片
          if (this.feedbackForm.images.length > 0) {
            this.feedbackForm.images.forEach(image => {
              formData.append('image', image.raw)
            })
          }
          post('/feedback/add', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }).then(result => {
            if (result.code === 200) {
              ElMessage.success('提交反馈成功!')
              this.resetForm()
              this.getFeedbackList() // 刷新列表
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
    },
    getTagType(type) {
      const types = {
        suggestion: 'success',
        bug: 'danger',
        complaint: 'warning',
        other: 'info'
      }
      return types[type]
    },
    getTypeName(type) {
      const names = {
        suggestion: '功能建议',
        bug: '问题报告',
        complaint: '投诉',
        other: '其他'
      }
      return names[type]
    },
    getStatusType(status) {
      return status === '待处理' ? 'warning' : 'success'
    },
    getFeedbackList() {
      get('/feedback/list').then(result => {
        if (result.code === 200) {
          this.myFeedbacks = result.data
        }
      })
    },
    created() {
      this.getFeedbackList()
    }
  }

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

.my-feedback {
  margin-top: 40px;
}

.my-feedback h3 {
  margin-bottom: 20px;
}
</style> 