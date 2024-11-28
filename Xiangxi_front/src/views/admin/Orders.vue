<template>
  <div class="orders">
    <div class="header">
      <h2>旅游订单管理</h2>
      <div class="header-right">
        <el-input
          v-model="searchKey"
          placeholder="输入订单号搜索"
          class="search-input"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="productName" label="商品名称" />
      <el-table-column prop="userName" label="用户名" width="120" />
      <el-table-column prop="amount" label="金额" width="120">
        <template #default="{ row }">
          <span class="amount">¥{{ row.amount.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="payStatus" label="支付状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.payStatus === 1 ? 'success' : 'warning'">
            {{ row.payStatus === 1 ? '已支付' : '待支付' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderStatus" label="订单状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getOrderStatusType(row.orderStatus)">
            {{ getOrderStatusText(row.orderStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
          <el-button 
            type="success" 
            link 
            v-if="row.orderStatus === 1"
            @click="handleConfirm(row)"
          >
            确认
          </el-button>
          <el-button 
            type="danger" 
            link 
            v-if="row.orderStatus === 0"
            @click="handleCancel(row)"
          >
            取消
          </el-button>
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

    <!-- 订单详情对话框 -->
    <el-dialog
      title="订单详情"
      v-model="dialogVisible"
      width="600px"
    >
      <div class="order-detail">
        <div class="detail-item">
          <span class="label">订单号：</span>
          <span>{{ currentOrder.orderNo }}</span>
        </div>
        <div class="detail-item">
          <span class="label">商品名称：</span>
          <span>{{ currentOrder.productName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">用户名：</span>
          <span>{{ currentOrder.userName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">联系电话：</span>
          <span>{{ currentOrder.phone }}</span>
        </div>
        <div class="detail-item">
          <span class="label">订单金额：</span>
          <span class="amount">¥{{ currentOrder.amount?.toFixed(2) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">支付状态：</span>
          <el-tag :type="currentOrder.payStatus === 1 ? 'success' : 'warning'">
            {{ currentOrder.payStatus === 1 ? '已支付' : '待支付' }}
          </el-tag>
        </div>
        <div class="detail-item">
          <span class="label">订单状态：</span>
          <el-tag :type="getOrderStatusType(currentOrder.orderStatus)">
            {{ getOrderStatusText(currentOrder.orderStatus) }}
          </el-tag>
        </div>
        <div class="detail-item">
          <span class="label">下单时间：</span>
          <span>{{ currentOrder.createTime }}</span>
        </div>
        <div class="detail-item">
          <span class="label">备注：</span>
          <span>{{ currentOrder.remark || '无' }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

// 表格数据
const tableData = ref([
  {
    orderNo: 'DD20240315001',
    productName: '三亚5日游',
    userName: '张三',
    amount: 3999.00,
    payStatus: 1,
    orderStatus: 1,
    createTime: '2024-03-15 10:00:00',
    phone: '13800138000',
    remark: '需要安排接送机'
  }
])
const loading = ref(false)
const total = ref(100)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKey = ref('')

// 对话框相关
const dialogVisible = ref(false)
const currentOrder = reactive({})

// 订单状态工具方法
const getOrderStatusType = (status) => {
  const types = {
    0: 'info',    // 待处理
    1: 'warning', // 待确认
    2: 'success', // 已确认
    3: 'danger'   // 已取消
  }
  return types[status] || 'info'
}

const getOrderStatusText = (status) => {
  const texts = {
    0: '待处理',
    1: '待确认',
    2: '已确认',
    3: '已取消'
  }
  return texts[status] || '未知'
}

// 方法
const handleSearch = () => {
  // 这里添加搜索逻辑
  console.log('搜索关键词：', searchKey.value)
}

const handleDetail = (row) => {
  Object.assign(currentOrder, row)
  dialogVisible.value = true
}

const handleConfirm = (row) => {
  ElMessageBox.confirm('确定要确认该订单吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('订单已确认')
  })
}

const handleCancel = (row) => {
  ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('订单已取消')
  })
}

const handlePageChange = (page) => {
  currentPage.value = page
  // 这里添加获取数据的逻辑
}
</script>

<style scoped>
.orders {
  padding: 20px;
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

.search-input {
  width: 250px;
  margin-right: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.amount {
  color: #f56c6c;
  font-weight: bold;
}

.order-detail {
  padding: 20px;
}

.detail-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.detail-item .label {
  width: 100px;
  color: #606266;
}
</style> 