<template>
  <div class="my-feedback-container">
    <el-card class="feedback-card">
      <template #header>
        <div class="card-header">
          <span>我的反馈</span>
        </div>
      </template>

      <!-- 反馈列表 -->
      <el-table :data="feedbackList" style="width: 100%">
        <el-table-column prop="type" label="类型" width="120">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.type)">
              {{ getTypeName(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="title" label="标题" min-width="150"/>
        
        <el-table-column prop="content" label="内容" min-width="200">
          <template #default="scope">
            <div class="content-cell">{{ scope.row.content }}</div>
          </template>
        </el-table-column>
        
        <el-table-column prop="createTime" label="提交时间" width="180"/>
        
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="reply" label="回复" min-width="200">
          <template #default="scope">
            <span class="reply-text">{{ scope.row.reply || '暂无回复' }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
              :disabled="scope.row.status === '已处理'"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { get, post } from '@/common'
import { ElMessage, ElMessageBox } from 'element-plus'

const feedbackList = ref([])

// 获取反馈列表
const getFeedbackList = async () => {
  try {
    const result = await get('/feedback/getUserFeedback')
    if (result.code === 200) {
      feedbackList.value = result.data
    } else {
      ElMessage.error(result.message || '获取反馈列表失败')
    }
  } catch (error) {
    console.error('获取反馈列表失败:', error)
    ElMessage.error('获取反馈列表失败，请稍后重试')
  }
}

// 获取类型标签样式
const getTagType = (type) => {
  const types = {
    suggestion: 'success',
    bug: 'danger',
    complaint: 'warning',
    other: 'info'
  }
  return types[type] || 'info'
}

// 获取类型名称
const getTypeName = (type) => {
  const names = {
    suggestion: '功能建议',
    bug: '问题报告',
    complaint: '投诉',
    other: '其他'
  }
  return names[type] || type
}

// 获取状态标签样式
const getStatusType = (status) => {
  switch (status) {
    case '待处理':
      return 'warning'
    case '已处理':
      return 'success'
    default:
      return 'info'
  }
}

// 删除反馈
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条反馈吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const result = await post('/feedback/deleteFeedback?id='+row.id, {
      })
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getFeedbackList() // 刷新列表
      } else {
        ElMessage.error(result.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }).catch(() => {
    // 取消删除，不做任何操作
  })
}

onMounted(() => {
  getFeedbackList()
})
</script>

<style scoped>
.my-feedback-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.feedback-card {
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 18px;
  font-weight: bold;
}

.content-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px;
}

.reply-text {
  color: #606266;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
}

:deep(.el-tag) {
  border-radius: 4px;
}

:deep(.el-button--small) {
  padding: 6px 12px;
  font-size: 12px;
}
</style> 