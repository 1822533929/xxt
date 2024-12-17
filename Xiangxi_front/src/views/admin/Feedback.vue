<template>
  <div class="feedback-container">
    <div class="header">
      <div class="header-right">
        <el-select v-model="filterStatus" placeholder="状态筛选" class="filter-select" @change="handleFilterChange">
          <el-option label="全部" value="" />
          <el-option label="待处理" value="待处理" />
          <el-option label="已处理" value="已处理" />
        </el-select>
      </div>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading" :empty-text="loading ? '加载中...' : '暂无数据'">
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="type" label="反馈类型" width="120">
        <template #default="{ row }">
          <el-tag :type="getFeedbackTypeTag(row.type)">
            {{ getFeedbackTypeText(row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="150" show-overflow-tooltip />
      <el-table-column prop="content" label="反馈内容" min-width="200" show-overflow-tooltip />
      <el-table-column prop="createTime" label="提交时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleDetail(row)">查看</el-button>
          <el-button 
            type="success" 
            link 
            v-if="row.status === '待处理'"
            @click="handleReply(row)"
          >
            回复
          </el-button>
          <el-button 
            type="danger" 
            link 
            @click="handleDelete(row)"
            :loading="row.deleting"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      :total="total"
      v-model:current-page="currentPage"
      :page-size="pageSize"
      :page-sizes="[5, 10, 20]"
      v-model:page-size="pageSize"
      @current-change="handlePageChange"
      @size-change="handlePageChange"
      layout="total, sizes, prev, pager, next"
      background
    />

    <!-- 反馈详情对话框 -->
    <el-dialog
      title="反馈详情"
      v-model="detailDialogVisible"
      width="600px"
    >
      <div class="feedback-detail">
        <div class="detail-item">
          <span class="label">用户名：</span>
          <span>{{ currentFeedback.username }}</span>
        </div>
        <div class="detail-item">
          <span class="label">反馈类型：</span>
          <el-tag :type="getFeedbackTypeTag(currentFeedback.type)">
            {{ getFeedbackTypeText(currentFeedback.type) }}
          </el-tag>
        </div>
        <div class="detail-item">
          <span class="label">标题：</span>
          <span>{{ currentFeedback.title }}</span>
        </div>
        <div class="detail-item">
          <span class="label">反馈内容：</span>
          <span>{{ currentFeedback.content }}</span>
        </div>
        <div class="detail-item" v-if="currentFeedback.imageUrl">
          <span class="label">图片：</span>
          <el-image
            :src="getImageUrl(currentFeedback.imageUrl)"
            :preview-src-list="[getImageUrl(currentFeedback.imageUrl)]"
            fit="cover"
            class="detail-image"
            @error="handleImageError"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
                <span>加载失败</span>
              </div>
            </template>
          </el-image>
        </div>
        <div class="detail-item">
          <span class="label">联系方式：</span>
          <span>{{ currentFeedback.contact }}</span>
        </div>
        <div class="detail-item">
          <span class="label">提交时间：</span>
          <span>{{ currentFeedback.createTime }}</span>
        </div>
        <div class="detail-item" v-if="currentFeedback.reply">
          <span class="label">回复内容：</span>
          <span>{{ currentFeedback.reply }}</span>
        </div>
      </div>
    </el-dialog>

    <!-- 回复对话框 -->
    <el-dialog
      title="回复反馈"
      v-model="replyDialogVisible"
      width="500px"
    >
      <div class="feedback-info">
        <p><strong>用户名：</strong>{{ currentFeedback.username }}</p>
        <p><strong>反馈内容：</strong>{{ currentFeedback.content }}</p>
      </div>
      <el-form :model="replyForm" label-width="80px">
        <el-form-item label="回复内容">
          <el-input
            type="textarea"
            v-model="replyForm.content"
            rows="4"
            placeholder="请输入回复内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { get, post, del } from '@/common'
import { Picture } from '@element-plus/icons-vue'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(5)
const filterStatus = ref('')

// 对话框相关
const detailDialogVisible = ref(false)
const replyDialogVisible = ref(false)
const currentFeedback = reactive({})
const replyForm = reactive({
  content: ''
})

// 工具方法
const getFeedbackTypeTag = (type) => {
  const tags = {
    'suggestion': 'success',   // 功能建议success
    'bug': 'warning',  // 问题报告warning
    'complaint': 'danger',      // 投诉danger
    'other': 'info' //其他info
  }
  return tags[type] || ''
}

const getFeedbackTypeText = (type) => {
  const texts = {
    'suggestion': '功能建议',
    'bug': '问题报告',
    'complaint': '投诉',
    'other': '其他'
  }
  return texts[type] || '未知'
}

const getStatusType = (status) => {
  const types = {
    '待处理': 'info',     // 待处理
    '已处理': 'success'   // 已处理
  }
  return types[status] || ''
}

const getStatusText = (status) => {
  const texts = {
    '待处理': '待处理',
    '已处理': '已处理'
  }
  return texts[status] || '未知'
}

// 方法
const handleDetail = (row) => {
  Object.assign(currentFeedback, row)
  detailDialogVisible.value = true
}

const handleReply = (row) => {
  Object.assign(currentFeedback, row)
  replyForm.content = ''
  if (row.reply) {
    replyForm.content = row.reply
  }
  replyDialogVisible.value = true
}

const submitReply = () => {
  if (!replyForm.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  // 调用回复接口
  const params = new FormData()
  params.append('id', currentFeedback.id)
  params.append('reply', replyForm.content.trim())
  
  post('/feedback/reply', params)
    .then(result => {
      if (result.code === 200) {
        ElMessage.success('回复成功')
        replyDialogVisible.value = false
        getFeedbackList() // 刷新列表
      } else {
        ElMessage.error(result.msg || '回复失败')
      }
    })
    .catch(() => {
      ElMessage.error('回复失败，请稍后重试')
    })
}

// 获取反馈列表
const getFeedbackList = async () => {
  loading.value = true
  try {
    const params = {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      status: filterStatus.value
    }
    const url = filterStatus.value 
      ? '/feedback/filterFeedback' 
      : '/feedback/getFeedbackList'
    
    if (filterStatus.value) {
      params.status = filterStatus.value
    }
    
    const result = await get(url, params)
    if (result.code === 200) {
      tableData.value = result.data.list
      total.value = result.data.total
    } else {
      ElMessage.error(result.msg || '获取反馈列表失败')
    }
  } catch (error) {
    console.error("获取反馈列表错误:", error)
    ElMessage.error('获取反馈列表失败')
  } finally {
    loading.value = false
  }
}

// 修改分页处理方法
const handlePageChange = (page) => {
  currentPage.value = page
  getFeedbackList()
}

// 监听状态筛选变化
const handleFilterChange = () => {
  currentPage.value = 1 // 重置到第一页
  console.log('筛选状态:', filterStatus.value)
  getFeedbackList()
}

// 删除方法
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除这条反馈吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      // 调用删除接口
      const params = new FormData()
      params.append('id', row.id)
      post('/feedback/deleteFeedback', params)
        .then(result => {
          if (result.code === 200) {
            ElMessage.success('删除成功')
            getFeedbackList() // 重新加载数据
          } else {
            ElMessage.error(result.msg || '删除失败')
          }
        }).catch(() => {
          ElMessage.error('删除失败')
        })
    })
    .catch(() => {
      // 取消删除
    })
}

// 页面加载时获取数据
onMounted(() => {
  getFeedbackList()
})

// 处理图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  // 如果是完整URL则直接返回
  if (url.startsWith('http')) return url
  // 否则拼接API基础路径
  return import.meta.env.VITE_API_BASE_URL + url
}

// 处理图片加载错误
const handleImageError = () => {
  console.error('图片加载失败')
}
</script>

<style scoped>
.feedback-container {
  padding: 24px;/*24*/
  height: 100%;
  box-sizing: border-box;
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
}

.filter-select {
  width: 120px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.feedback-detail {
  padding: 20px;
}

.detail-item {
  margin-bottom: 15px;
  display: flex;
  align-items: flex-start;
}

.detail-item .label {
  width: 100px;
  color: #606266;
}

.detail-image {
  max-width: 300px;
  max-height: 300px;
  border-radius: 4px;
}

.feedback-info {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.feedback-info p {
  margin: 8px 0;
  color: #606266;
}

.feedback-info strong {
  color: #303133;
  margin-right: 8px;
}

.image-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 300px;
  height: 200px;
  background-color: #f5f7fa;
  color: #909399;
}

.image-error .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
}
</style> 