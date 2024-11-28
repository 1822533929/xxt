<template>
  <div class="announcements">
    <div class="header">
      <h2>系统公告管理</h2>
      <el-button type="primary" @click="handleAdd">发布公告</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="公告标题" />
      <el-table-column prop="type" label="公告类型" width="120">
        <template #default="{ row }">
          <el-tag :type="getAnnouncementTypeTag(row.type)">
            {{ getAnnouncementTypeText(row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
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
      v-model:page-size="pageSize"
      @current-change="handlePageChange"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="700px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="公告标题">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="公告类型">
          <el-select v-model="form.type" placeholder="请选择公告类型">
            <el-option label="系统通知" :value="1" />
            <el-option label="活动公告" :value="2" />
            <el-option label="更新公告" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告内容">
          <el-input
            type="textarea"
            v-model="form.content"
            :rows="8"
            placeholder="请输入公告内容"
          />
        </el-form-item>
        <el-form-item label="发布状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">立即发布</el-radio>
            <el-radio :label="0">保存草稿</el-radio>
          </el-radio-group>
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
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 表格数据
const tableData = ref([
  {
    id: 1,
    title: '系统维护通知',
    type: 1,
    content: '系统将于2024年3月20日凌晨2点进行维护升级...',
    createTime: '2024-03-15 10:00:00',
    status: 1
  }
])
const loading = ref(false)
const total = ref(100)
const currentPage = ref(1)
const pageSize = ref(10)

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('发布公告')
const form = reactive({
  title: '',
  type: 1,
  content: '',
  status: 1
})

// 工具方法
const getAnnouncementTypeTag = (type) => {
  const tags = {
    1: '',        // 系统通知
    2: 'success', // 活动公告
    3: 'warning'  // 更新公告
  }
  return tags[type] || ''
}

const getAnnouncementTypeText = (type) => {
  const texts = {
    1: '系统通知',
    2: '活动公告',
    3: '更新公告'
  }
  return texts[type] || '未知'
}

// 方法
const handleAdd = () => {
  dialogTitle.value = '发布公告'
  Object.assign(form, {
    title: '',
    type: 1,
    content: '',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑公告'
  Object.assign(form, {
    title: row.title,
    type: row.type,
    content: row.content,
    status: row.status
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该公告吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  })
}

const handleSubmit = () => {
  if (!form.title.trim()) {
    ElMessage.warning('请输入公告标题')
    return
  }
  if (!form.content.trim()) {
    ElMessage.warning('请输入公告内容')
    return
  }
  ElMessage.success(dialogTitle.value === '发布公告' ? '发布成功' : '更新成功')
  dialogVisible.value = false
}

const handlePageChange = (page) => {
  currentPage.value = page
  // 这里添加获取数据的逻辑
}
</script>

<style scoped>
.announcements {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-input__wrapper) {
  width: 100%;
}

:deep(.el-textarea__inner) {
  font-family: inherit;
}
</style> 