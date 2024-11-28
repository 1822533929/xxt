<template>
  <div class="travel-guides">
    <div class="header">
      <h2>旅游攻略管理</h2>
      <el-button type="primary" @click="handleAdd">新增攻略</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="destination" label="目的地" width="120" />
      <el-table-column prop="category" label="分类" width="120">
        <template #default="{ row }">
          <el-tag>{{ row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column prop="views" label="浏览量" width="100" />
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
      width="600px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="目的地">
          <el-input v-model="form.destination" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category">
            <el-option label="美食" value="美食" />
            <el-option label="住宿" value="住宿" />
            <el-option label="交通" value="交通" />
            <el-option label="景点" value="景点" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" v-model="form.content" rows="4" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="发布" :value="1" />
            <el-option label="草稿" :value="0" />
          </el-select>
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
    title: '三亚必玩景点',
    destination: '三亚',
    category: '景点',
    createTime: '2024-03-15 10:00:00',
    views: 1234,
    status: 1
  }
])
const loading = ref(false)
const total = ref(100)
const currentPage = ref(1)
const pageSize = ref(10)

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增攻略')
const form = reactive({
  title: '',
  destination: '',
  category: '',
  content: '',
  status: 1
})

// 方法
const handleAdd = () => {
  dialogTitle.value = '新增攻略'
  Object.assign(form, {
    title: '',
    destination: '',
    category: '',
    content: '',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑攻略'
  Object.assign(form, {
    title: row.title,
    destination: row.destination,
    category: row.category,
    content: row.content || '',
    status: row.status
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该攻略吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  })
}

const handleSubmit = () => {
  ElMessage.success(dialogTitle.value === '新增攻略' ? '添加成功' : '更新成功')
  dialogVisible.value = false
}

const handlePageChange = (page) => {
  currentPage.value = page
  // 这里添加获取数据的逻辑
}
</script>

<style scoped>
.travel-guides {
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
</style> 