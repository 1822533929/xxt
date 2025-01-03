<template>
  <div class="travel-detail">
    <div class="detail-container" v-loading="loading">
      <div class="main-content">
        <!-- 左侧封面 -->
        <div class="cover-section">
          <el-image
            :src="getImageUrl(detail.cover)"
            fit="cover"
            class="cover-image"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </div>

        <!-- 右侧信息 -->
        <div class="info-section">
          <div class="title-section">
            <h1 class="title">{{ detail.title }}</h1>
            <div class="read-count">
              <el-icon><View /></el-icon>
              <span>{{ detail.readCount || 0 }}</span>
            </div>
          </div>
          
          <!-- 标签 -->
          <div class="tags">
            <el-tag 
              v-for="tag in tags" 
              :key="tag"
              class="tag"
              effect="plain"
              type="success"
              size="small"
            >
              {{ tag }}
            </el-tag>
          </div>

          <!-- 简介 -->
          <div class="description">
            {{ detail.descr }}
          </div>

          <!-- 价格和库存 -->
          <div class="price-stock">
            <div class="price">
              <span class="label">价格：</span>
              <span class="amount">¥{{ detail.money }}</span>
            </div>
            <div class="stock">
              <span class="label">剩余库存：</span>
              <span class="number">{{ detail.inventory }}张</span>
            </div>
          </div>

          <!-- 预定按钮 -->
          <div class="booking">
            <el-button type="warning" size="large" @click="handleBook">
              立即预定
            </el-button>
          </div>
        </div>
      </div>

      <!-- 图文介绍 -->
      <div class="detail-content">
        <div class="content-title">
          <h2>图文介绍</h2>
        </div>
        <div class="content-body" v-html="detail.content"></div>
      </div>
    </div>

    <!-- 订单确认对话框 -->
    <el-dialog
      title="确认订单"
      v-model="orderDialogVisible"
      width="500px"
    >
      <div class="order-form">
        <div class="order-item">
          <span class="label">商品名称：</span>
          <span class="value">{{ detail.title }}</span>
        </div>
        <div class="order-item">
          <span class="label">商品单价：</span>
          <span class="value price">¥{{ detail.money }}</span>
        </div>
        <div class="order-item">
          <span class="label">购买数量：</span>
          <el-input-number 
            v-model="orderQuantity" 
            :min="1" 
            :max="Number(detail.inventory)"
            @change="calculateTotal"
          />
        </div>
        <div class="order-item total">
          <span class="label">商品总价：</span>
          <span class="value price">¥{{ totalPrice }}</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="orderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmOrder" :loading="submitting">
          确认下单
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { get, post } from '@/common'
import { ElMessage } from 'element-plus'
import { Picture, View } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const detail = ref({})
const tags = ref([])

// 订单相关
const orderDialogVisible = ref(false)
const orderQuantity = ref(1)
const submitting = ref(false)

// 计算总价
const totalPrice = computed(() => {
  return (detail.value.money * orderQuantity.value).toFixed(2)
})

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 获取详情数据
const getDetail = async () => {
  try {
    const result = await get(`/travels/selectById/${route.params.id}`)
    if (result.code === 200) {
      detail.value = result.data
      // 获取标签
      await getTags()
      // 增加浏览量
      await addReadCount()
    }
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取详情失败')
  } finally {
    loading.value = false
  }
}

// 获取标签数据
const getTags = async () => {
  try {
    const result = await get(`/travels/selectTags/${route.params.id}`)
    if (result.code === 200) {
      tags.value = result.data || []
    }
  } catch (error) {
    console.error('获取标签失败:', error)
    tags.value = []
  }
}

// 增加浏览量
const addReadCount = async () => {
  try {
    await get(`/travels/addRead/${route.params.id}`)
  } catch (error) {
    console.error('增加浏览量失败:', error)
  }
}

// 处理预定按钮点击
const handleBook = () => {
  if (!localStorage.getItem('token')) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  orderQuantity.value = 1
  orderDialogVisible.value = true
  calculateTotal()
}

// 计算总价
const calculateTotal = () => {
  // 由computed属性totalPrice自动计算
}

// 确认下单
const confirmOrder = async () => {
  try {
    submitting.value = true
    // 使用 URLSearchParams 格式化参数
    const params = new URLSearchParams()
    params.append('travelId', detail.value.id)
    params.append('quantity', orderQuantity.value)
    params.append('money', detail.value.money)
    
    const result = await post('/orders/createOrder?' + params.toString())
    
    if (result.code === 200) {
      ElMessage.success('下单成功')
      orderDialogVisible.value = false
      // 可以跳转到订单列表页面
      // router.push('/user/myorders')
    } else {
      ElMessage.error(result.msg || '下单失败')
    }
  } catch (error) {
    console.error('下单失败:', error)
    ElMessage.error('下单失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  getDetail()
})
</script>

<style scoped>
.travel-detail {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.detail-container {
  width: 60%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
}

.main-content {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}

.cover-section {
  flex: none;
  width: 400px;
}

.cover-image {
  width: 400px;
  height: 300px;
  border-radius: 8px;
}

.info-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.title-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.title {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

.read-count {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #909399;
  font-size: 14px;
}

.read-count .el-icon {
  font-size: 16px;
}

.tags {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin: 5px 0;
}

.tag {
  margin-right: 8px;
  border-radius: 12px;
  padding: 0 12px;
}

.description {
  color: #606266;
  line-height: 1.6;
  margin: 10px 0;
  font-size: 14px;
}

.price-stock {
  background-color: #f7f8fa;
  padding: 15px;
  border-radius: 6px;
}

.price {
  margin-bottom: 10px;
}

.label {
  color: #909399;
}

.amount {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
}

.number {
  color: #67c23a;
  font-weight: bold;
}

.booking {
  margin-top: 20px;
}

.detail-content {
  margin-top: 30px;
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
}

.content-title {
  margin-bottom: 20px;
}

.content-title h2 {
  font-size: 20px;
  color: #303133;
  margin: 0;
}

.content-body {
  line-height: 1.8;
  color: #606266;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

:deep(img) {
  max-width: 100%;
  height: auto;
}

.order-form {
  padding: 20px;
}

.order-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.order-item .label {
  width: 100px;
  color: #606266;
}

.order-item .value {
  flex: 1;
}

.order-item .price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}

.order-item.total {
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
  margin-top: 20px;
}
</style> 