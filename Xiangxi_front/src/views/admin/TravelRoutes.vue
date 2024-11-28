<template>
  <div class="travel-routes">
    <div class="header">
      <h2>旅游路线管理</h2>
      <el-button type="primary" @click="handleAdd">新增路线</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="路线名称" />
      <el-table-column prop="destination" label="目的地" width="120" />
      <el-table-column prop="days" label="行程天数" width="100" />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }">
          <span>¥{{ row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="maxPeople" label="最大人数" width="100" />
      <el-table-column prop="startDate" label="出发日期" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '在售' : '下架' }}
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
        <el-form-item label="路线名称">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="目的地">
          <el-input v-model="form.destination" />
        </el-form-item>
        <el-form-item label="行程天数">
          <el-input-number v-model="form.days" :min="1" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number 
            v-model="form.price" 
            :min="0" 
            :precision="2" 
            :step="100"
          />
        </el-form-item>
        <el-form-item label="最大人数">
          <el-input-number v-model="form.maxPeople" :min="1" />
        </el-form-item>
        <el-form-item label="出发日期">
          <el-date-picker
            v-model="form.startDate"
            type="date"
            placeholder="选择日期"
          />
        </el-form-item>
        <el-form-item label="行程安排">
          <el-input type="textarea" v-model="form.itinerary" rows="4" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="在售" :value="1" />
            <el-option label="下架" :value="0" />
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
    title: '三亚5日游',
    destination: '三亚',
    days: 5,
    price: 3999.00,
    maxPeople: 30,
    startDate: '2024-04-01',
    status: 1
  }
])
const loading = ref(false)
const total = ref(100)
const currentPage = ref(1)
const pageSize = ref(10)

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增路线')
const form = reactive({
  title: '',
  destination: '',
  days: 1,
  price: 0,
  maxPeople: 1,
  startDate: '',
  itinerary: '',
  status: 1
})

// 方法
const handleAdd = () => {
  dialogTitle.value = '新增路线'
  Object.assign(form, {
    title: '',
    destination: '',
    days: 1,
    price: 0,
    maxPeople: 1,
    startDate: '',
    itinerary: '',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑路线'
  Object.assign(form, {
    title: row.title,
    destination: row.destination,
    days: row.days,
    price: row.price,
    maxPeople: row.maxPeople,
    startDate: row.startDate,
    itinerary: row.itinerary || '',
    status: row.status
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该路线吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  })
}

const handleSubmit = () => {
  ElMessage.success(dialogTitle.value === '新增路线' ? '添加成功' : '更新成功')
  dialogVisible.value = false
}

const handlePageChange = (page) => {
  currentPage.value = page
  // 这里添加获取数据的逻辑
}
</script>

<style scoped>
.travel-routes {
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