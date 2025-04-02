<template>
  <div class="my-orders">
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

    <!-- 订单列表 -->
    <div class="orders-list" v-loading="loading">
      <div class="order-item" v-for="order in orderList" :key="order.id">
        <div class="order-header">
          <span class="order-number">订单编号：{{ order.id }}</span>
          <div class="order-status">
            <el-tag :type="getStatusType(order.status)">{{ order.status }}</el-tag>
            <span v-if="order.status === '待支付'" class="remaining-time" :class="{ 'expiring': isExpiring(order) }">
              剩余：{{ getRemainingTime(order) }}
            </span>
          </div>
        </div>
        
        <div class="order-content">
          <div class="product-info">
            <el-image 
              :src="getImageUrl(order.cover)" 
              fit="cover"
              class="product-image"
              @click="handleTitleClick(order.travelid)"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            
            <div class="product-details">
              <div class="product-title" @click="handleTitleClick(order.travelid)">
                {{ order.title }}
              </div>
              <div class="product-price">
                <span class="label">单价：</span>
                <span class="price">¥{{ order.money }}</span>
              </div>
              <div class="product-quantity">
                <span class="label">数量：</span>
                <span>{{ order.quantity }}</span>
              </div>
            </div>
          </div>

          <div class="order-info">
            <div class="total-price">
              <span class="label">总价：</span>
              <span class="price">¥{{ (order.money * order.quantity).toFixed(2) }}</span>
            </div>
            <div class="order-date">{{ order.orderdate }}</div>
          </div>

          <div class="order-actions">
            <el-button 
              v-if="order.status === '待支付'"
              type="primary" 
              @click="handlePay(order)"
            >
              前往支付
            </el-button>
            <el-button 
              v-if="order.status === '待支付'"
              type="danger" 
              @click="handleCancel(order)"
            >
              取消订单
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <el-pagination
      v-if="total > 0"
      class="pagination"
      :total="total"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20]"
      layout="total, sizes, prev, pager, next"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 支付对话框 -->
    <el-dialog
      title="订单支付"
      v-model="payDialogVisible"
      width="400px"
      center
    >
      <div class="pay-content">
        <div class="qr-code">
          <el-image
            :src=PayCode
            fit="cover"
            style="width: 200px; height: 200px"
          />
        </div>
        <div class="pay-info">
          <div class="pay-amount">支付金额：¥{{ currentOrder?.money * currentOrder?.quantity }}</div>
          <div class="pay-tip">请使用微信扫码支付</div>
        </div>
      </div>
      <template #footer>
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPay">确认已支付</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture } from '@element-plus/icons-vue'
import { get, post } from '@/common'
import { API_BASE_URL } from '@/common/constants'
import { useRouter } from 'vue-router'
import PayCode from '@/images/icon/支付码.jpg'

const router = useRouter()

// 数据
const loading = ref(false)
const orderList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = reactive({
  title: '',
  orderId: ''
})

// 添加支付相关的数据
const payDialogVisible = ref(false)
const currentOrder = ref(null)

// 添加定时器引用
const timer = ref(null)

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

// 获取订单列表
const getOrderList = async () => {
  loading.value = true
  try {
    const result = await get('/orders/getUserOrders', {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      title: searchForm.title,
      orderId: searchForm.orderId
    })
    if (result.code === 200) {
      orderList.value = result.data.list
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
  getOrderList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.title = ''
  searchForm.orderId = ''
  handleSearch()
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  getOrderList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getOrderList()
}

// 处理商品点击
const handleTitleClick = (travelId) => {
  router.push(`/user/travel-detail/${travelId}`)
}

// 处理支付
const handlePay = (order) => {
  currentOrder.value = order
  payDialogVisible.value = true
}

// 添加确认支付方法
const confirmPay = async () => {
  try {
    const result = await get('/orders/updateOrder', { id: currentOrder.value.id })
    if (result.code === 200) {
      ElMessage.success('支付成功')
      payDialogVisible.value = false
      getOrderList() // 刷新订单列表
    } else {
      ElMessage.error(result.msg || '支付失败')
    }
  } catch (error) {
    console.error('支付失败:', error)
    ElMessage.error('支付失败')
  }
}

// 处理取消订单
const handleCancel = (order) => {
  ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const result = await get('/orders/cancelOrder', { id: order.id })
      if (result.code === 200) {
        ElMessage.success('订单已取消')
        getOrderList()
      } else {
        ElMessage.error(result.msg || '取消失败')
      }
    } catch (error) {
      console.error('取消订单失败:', error)
      ElMessage.error('取消订单失败')
    }
  })
}

// 计算剩余时间
const getRemainingTime = (order) => {
  if (order.status !== '待支付' || !order.expireTime) return ''
  
  const expireTime = new Date(order.expireTime.replace(/-/g, '/'))
  const now = new Date()
  const diff = expireTime - now

  if (diff <= 0) return '已过期'

  const minutes = Math.floor(diff / 1000 / 60)
  const seconds = Math.floor((diff / 1000) % 60)
  return `${minutes}分${seconds}秒`
}

// 判断是否即将过期（小于5分钟）
const isExpiring = (order) => {
  if (order.status !== '待支付' || !order.expireTime) return false
  
  const expireTime = new Date(order.expireTime.replace(/-/g, '/'))
  const now = new Date()
  const diff = expireTime - now
  
  return diff > 0 && diff <= 5 * 60 * 1000
}

// 检查订单是否过期
const checkOrderExpiry = async (orderId) => {
  try {
    const result = await get('/orders/checkOrderExpiry', { orderId })
    if (result.code === 200 && result.data === '订单已过期') {
      getOrderList() // 刷新订单列表
    }
  } catch (error) {
    console.error('检查订单过期失败:', error)
  }
}

// 启动定时器，定期检查订单状态和更新剩余时间
const startTimer = () => {
  timer.value = setInterval(() => {
    orderList.value.forEach(order => {
      if (order.status === '待支付') {
        checkOrderExpiry(order.id)
      }
    })
  }, 1000) // 每秒更新一次
}

onMounted(() => {
  getOrderList()
  startTimer()
})

// 组件卸载前清除定时器
onBeforeUnmount(() => {
  if (timer.value) {
    clearInterval(timer.value)
  }
})
</script>

<style scoped>
.my-orders {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.search-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-inputs {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 200px;
}

.orders-list {
  margin-bottom: 20px;
}

.order-item {
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.order-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.order-header {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fafafa;
}

.order-number {
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.order-content {
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.product-info {
  display: flex;
  gap: 20px;
  flex: 1;
  min-width: 0;
}

.product-image {
  width: 100px;
  height: 100px;
  border-radius: 4px;
  cursor: pointer;
  object-fit: cover;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.product-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
  min-width: 0;
}

.product-title {
  font-size: 16px;
  color: #303133;
  cursor: pointer;
  font-weight: 500;
  margin-bottom: 4px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
  max-height: 2.8em;
}

.product-title:hover {
  color: #409EFF;
}

.label {
  color: #909399;
  font-size: 14px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}

.order-info {
  text-align: center;
  margin: 0 40px;
  min-width: 120px;
}

.total-price {
  margin-bottom: 8px;
  font-size: 16px;
  font-weight: bold;
}

.order-date {
  color: #909399;
  font-size: 14px;
  margin-top: 4px;
}

.order-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-width: 100px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  padding: 15px 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

:deep(.el-button--primary) {
  padding: 8px 20px;
}

:deep(.el-button--danger) {
  padding: 8px 20px;
}

:deep(.el-tag) {
  padding: 0 12px;
  height: 24px;
  line-height: 22px;
  font-size: 12px;
}

.pay-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}

.qr-code {
  margin-bottom: 20px;
}

.pay-info {
  text-align: center;
}

.pay-amount {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 10px;
}

.pay-tip {
  color: #909399;
  font-size: 14px;
}

.order-status {
  display: flex;
  align-items: center;
  gap: 10px;
}

.remaining-time {
  font-size: 14px;
  color: #909399;
}

.remaining-time.expiring {
  color: #f56c6c;
  animation: blink 1s infinite;
}

@keyframes blink {
  0% { opacity: 1; }
  50% { opacity: 0.5; }
  100% { opacity: 1; }
}

.notice-title {
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-right: 15px;
}

.content-preview {
  white-space: pre-wrap;
  word-break: break-all;
  word-wrap: break-word;
  line-height: 1.6;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  max-height: 400px;
  overflow-y: auto;
}
</style> 