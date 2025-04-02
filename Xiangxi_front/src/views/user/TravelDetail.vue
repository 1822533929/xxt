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
            <div class="icon">
              <span class="likes-count">
              <img src="@/assets/未点赞.svg" alt="Like"  class="header-like-icon" />
              {{ detail.likeCount || 0 }}
            </span>
            <div class="read-count">
              <el-icon><View /></el-icon>
              <span>{{ detail.readCount || 0 }}</span>
            </div>
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
            <pre>{{ detail.descr }}</pre>
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
        
        <!-- 添加点赞部分 -->
        <div class="like-section">
          <div class="like-button" @click="handleLike">
            <img :src="isLiked ? likedIcon : unlikedIcon" alt="Like" class="like-icon" />
<!--            <span>推荐景点</span>-->
            {{ isLiked ? '已推荐' : '推荐景点' }}
          </div>
        </div>
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
            :max="4"
            @change="handleQuantityChange"
          />
        </div>
        <span class="label">注意：单次购买量不得超过4张</span>
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
import likedIcon from '@/assets/点赞.svg';
import unlikedIcon from '@/assets/未点赞.svg';

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const detail = ref({})
const tags = ref([])

// 订单相关
const orderDialogVisible = ref(false)
const orderQuantity = ref(1)
const submitting = ref(false)

// 添加点赞相关的状态
const isLiked = ref(false)

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
  if (!localStorage.getItem('Sure-Token')) {
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

// 添加数量变化处理函数
const handleQuantityChange = (value) => {
  if (value > 4) {
    ElMessage.warning('单次最多购买4张门票')
    orderQuantity.value = 4
    return
  }
  if (value > detail.value.inventory) {
    ElMessage.warning(`当前库存仅剩${detail.value.inventory}张`)
    orderQuantity.value = detail.value.inventory
    return
  }
  calculateTotal()
}

// 修改确认下单方法
const confirmOrder = async () => {
  try {
    submitting.value = true
    
    // 添加数量检查
    if (orderQuantity.value > 4) {
      ElMessage.warning('单次最多购买4张门票')
      return
    }
    if (detail.value.inventory <= 0) {
      ElMessage.error('库存不足')
      return
    }
    if (orderQuantity.value > detail.value.inventory) {
      ElMessage.warning(`当前库存仅剩${detail.value.inventory}张`)
      return
    }

    const params = new URLSearchParams()
    params.append('travelId', detail.value.id)
    params.append('quantity', orderQuantity.value)
    params.append('money', detail.value.money)
    
    const result = await post('/orders/createOrder?' + params.toString())
    
    if (result.code === 200) {
      orderDialogVisible.value = false
      ElMessage.success('下单成功')
    }
  } catch (error) {
    console.error('下单失败:', error)
    ElMessage.error('下单失败')
  } finally {
    submitting.value = false
  }
}

// 处理点赞
const handleLike = async () => {
  if (!localStorage.getItem('Sure-Token')) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  if (isLiked.value) {
    ElMessage.warning('您已经推荐过了')
    return
  }
  try {
    const result = await get(`/travels/like/${route.params.id}`)
    if (result.code === 200) {
      isLiked.value = true
      detail.value.likeCount++
      ElMessage.success('点赞成功')
    }
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('点赞失败')
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
  /*gap: 20px;*/
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
  margin-left: 12px;
}

.tags {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin: 10px 0;
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

.description pre {
  white-space: pre-wrap;       /* 保留空格和换行符 */
  word-wrap: break-word;       /* 允许长单词换行 */
  font-family: inherit;        /* 使用与父元素相同的字体 */
  margin: 0;                   /* 移除默认边距 */
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
  /*color: #606266;*/
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
.header-like-icon{
  width: 16px;
  height: 16px;
  vertical-align: middle;
}
.likes-count{
  display: flex;
  align-items: center;
  gap: 2px;
  color: #909399;
  font-size: 14px;

}
.icon{
  display: flex;
  justify-content: flex-start;
  margin-left: auto;
  margin-right: 0;

}

.like-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.like-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 20px;
  background-color: #f5f7fa;
  cursor: pointer;
  transition: all 0.3s;
}

.like-button:hover {
  background-color: #e6e8eb;
}

.like-icon {
  width: 24px;
  height: 24px;
  vertical-align: middle;
}

</style> 