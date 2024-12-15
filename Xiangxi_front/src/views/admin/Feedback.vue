<template>
  <div class="feedback-container">
    <div class="header">
      <div class="header-right">
        <el-select v-model="filterStatus" placeholder="状态筛选" class="filter-select">
          <el-option label="全部" value="" />
          <el-option label="待处理" :value="0" />
          <el-option label="已处理" :value="1" />
        </el-select>
      </div>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="userName" label="用户名" width="120" />
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
            v-if="row.status !== 2"
            @click="handleReply(row)"
          >
            回复
          </el-button>
          <el-button 
            type="danger" 
            link 
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      :total="total"
      v-model="currentPage"
      :page-size="pageSize"
      @current-change="handlePageChange"
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
          <span>{{ currentFeedback.userName }}</span>
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
            :src="currentFeedback.imageUrl"
            :preview-src-list="[currentFeedback.imageUrl]"
            fit="cover"
            class="detail-image"
          />
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
import { ref, reactive } from 'vue'
import { Picture } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 表格数据
const tableData = ref([
  {
    userName: '张三',
    type: 1,
    title: '商品质量问题',
    content: '订单商品质量有问题',
    imageUrl: '/img/upload/example.jpg',
    createTime: '2024-03-15 10:00:00',
    status: 0,
    reply: ''
  }
])
const loading = ref(false)
const total = ref(100)
const currentPage = ref(1)
const pageSize = ref(10)
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
    1: 'success',   // 功能建议success
    2: 'warning',  // 问题报告warning
    3: 'danger',      // 投诉danger
    4: 'info' //其他info
  }
  return tags[type] || ''
}

const getFeedbackTypeText = (type) => {
  const texts = {
    1: '功能建议',
    2: '问题报告',
    3: '投诉',
    4: '其他'
  }
  return texts[type] || '未知'
}

const getStatusType = (status) => {
  const types = {
    0: 'info',     // 待处理
    1: 'success'   // 已处理
  }
  return types[status] || ''
}

const getStatusText = (status) => {
  const texts = {
    0: '待处理',
    1: '已处理'
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
  replyDialogVisible.value = true
}

const submitReply = () => {
  if (!replyForm.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  ElMessage.success('回复成功')
  replyDialogVisible.value = false
}

const handlePageChange = (page) => {
  currentPage.value = page
  // 这里添加获取数据的逻辑
}

// 添加删除方法
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
      // TODO: 调用删除接口
      // deleteFeedback(row.id).then(() => {
      //   ElMessage.success('删除成功');
      //   // 重新加载数据
      //   loadFeedbackList();
      // });
      ElMessage.success('删除成功');
    })
    .catch(() => {
      // 取消删除
    });
};
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
</style> 