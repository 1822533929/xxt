<template>
  <div class="orders">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <div class="search-inputs">
        <el-input
          v-model="searchForm.title"
          placeholder="请输入商品名称"
          class="search-input"
          clearable
        />
        <el-input
          v-model="searchForm.orderId"
          placeholder="请输入订单编号"
          class="search-input"
          clearable
        />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button 
        type="danger" 
        @click="handleBatchDelete" 
        :disabled="!selectedRows.length"
      >
        批量删除
      </el-button>
    </div>

    <!-- 表格 -->
    <el-table 
      :data="tableData" 
      style="width: 100%" 
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="订单编号" width="120" />
      <el-table-column prop="title" label="商品名称" min-width="120">
        <template #default="{ row }">
          <el-button 
            link 
            type="primary" 
            @click="handleTitleClick(row.travelid)"
          >
            {{ row.title }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="商品图片" width="120">
        <template #default="{ row }">
          <el-image 
            :src="getImageUrl(row.cover)" 
            fit="cover"
            style="width: 80px; height: 60px; border-radius: 4px;"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="money" label="商品单价" width="100">
        <template #default="{ row }">
          <span class="price">¥{{ row.money }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="quantity" label="购买数量" width="100" />
      <el-table-column label="总价" width="100">
        <template #default="{ row }">
          <span class="price">¥{{ (row.money * row.quantity).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="购买人" width="120" />
      <el-table-column prop="orderdate" label="购买时间" width="180" />
      <el-table-column prop="status" label="订单状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      class="pagination"
      :total="total"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture } from '@element-plus/icons-vue'
import { get, post } from '@/common'
import { API_BASE_URL } from '@/common/constants'
import { useRouter } from 'vue-router'

const router = useRouter()

// 数据
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const selectedRows = ref([])
const searchForm = reactive({
  title: '',
  orderId: ''
})

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return API_BASE_URL + url
}

// 获取状态标签类型
const getStatusType = (status) => {
  const types = {
    '待支付': 'warning',
    '已完成': 'success',
    '已取消': 'info'
  }
  return types[status] || 'info'
}

// 获取状态显示文本
const getStatusText = (status) => {
  const texts = {
    '待支付': '待支付',
    '已完成': '已完成',
    '已取消': '已取消'
  }
  return texts[status] || status
}

// 获取订单列表
const getTableData = async () => {
  loading.value = true
  try {
    const result = await get('/orders/getAllOrders', {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      title: searchForm.title,
      orderId: searchForm.orderId
    })
    if (result.code === 200) {
      tableData.value = result.data.list
      total.value = result.data.total
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  getTableData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.title = ''
  searchForm.orderId = ''
  handleSearch()
}

// 处理选择行变化
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const result = await post('/orders/deleteOrder/' + row.id)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getTableData()
      } else {
        ElMessage.error(result.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 处理批量删除
const handleBatchDelete = () => {
  if (!selectedRows.value.length) return
  ElMessageBox.confirm('确定要删除选中的订单吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const result = await post('/orders/delete/batch', ids)
      if (result.code === 200) {
        ElMessage.success('删除成功')
        getTableData()
      } else {
        ElMessage.error(result.msg || '删除失败')
      }
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  getTableData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getTableData()
}

// 处理商品名称点击
const handleTitleClick = (travelId) => {
  router.push(`/user/travel-detail/${travelId}`)
}

onMounted(() => {
  getTableData()
})
</script>

<style scoped>
.orders {
  padding: 20px;
  background-color: #f5f7fa;
}

.search-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.search-inputs {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 200px;
}

.operation-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
  background-color: #fff;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  background-color: #fff;
  padding: 15px 20px;
  border-radius: 8px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
}

:deep(.el-tag--warning) {
  background-color: #fdf6ec;
  border-color: #faecd8;
  color: #e6a23c;
}

:deep(.el-tag--success) {
  background-color: #f0f9eb;
  border-color: #e1f3d8;
  color: #67c23a;
}

:deep(.el-tag--info) {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}
</style> 