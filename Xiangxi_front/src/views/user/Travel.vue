<template>
  <div class="travel">
    <el-container>
      <!-- 左侧热门推荐 -->
      <el-aside width="300px" class="hot-section">
        <div class="section-title">
          <h2>热门推荐</h2>
        </div>
        <div class="hot-list">
          <el-skeleton :loading="hotLoading" animated :count="3" v-if="hotLoading">
            <template #template>
              <div class="hot-item-skeleton">
                <el-skeleton-item variant="image" style="width: 100%; height: 150px" />
                <el-skeleton-item variant="text" style="width: 60%" />
              </div>
            </template>
          </el-skeleton>
          
          <el-card v-for="item in hotSpots" :key="item.id" class="hot-item">
            <img :src="getImageUrl(item.image)" class="hot-image" />
            <div class="hot-info">
              <h3>{{ item.title }}</h3>
            </div>
          </el-card>
        </div>
      </el-aside>

      <!-- 右侧全部旅游 -->
      <el-main class="main-section">
        <div class="section-title">
          <h2>全部旅游</h2>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="8" v-for="item in allSpots" :key="item.id">
            <el-card class="spot-card" :body-style="{ padding: '0px' }">
              <img :src="getImageUrl(item.image)" class="spot-image" />
              <div class="spot-info">
                <h3>{{ item.title }}</h3>
                <div class="spot-footer">
                  <span class="price">¥{{ item.money }}</span>
                  <span class="remaining">剩余: {{ item.inventory }}张</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[6, 12, 24]"
            :total="total"
            layout="total, sizes, prev, pager, next"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { get } from '@/common'
import { ElMessage } from 'element-plus'

// 数据定义
const hotSpots = ref([])
const allSpots = ref([])
const hotLoading = ref(true)
const currentPage = ref(1)
const pageSize = ref(6)
const total = ref(0)

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 获取热门旅游数据
const getHotSpots = async () => {
  try {
    const result = await get('/travels/selectAllByRead', {
      params: new URLSearchParams({
        currentPage: '1',
        pageSize: '3'
      })
    })
    if (result.code === 200) {
      hotSpots.value = result.data.list
    }
  } catch (error) {
    console.error('获取热门推荐失败:', error)
  } finally {
    hotLoading.value = false
  }
}

// 获取全部旅游数据
const getAllSpots = async () => {
  try {
    const result = await get('/travels/selectAll', {
      params: new URLSearchParams({
        currentPage: currentPage.value.toString(),
        pageSize: pageSize.value.toString()
      })
    })
    if (result.code === 200) {
      allSpots.value = result.data.list
      total.value = result.data.total
    }
  } catch (error) {
    console.error('获取全部旅游失败:', error)
    ElMessage.error('获取数据失败')
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  getAllSpots()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getAllSpots()
}

// 页面加载时获取数据
onMounted(() => {
  getHotSpots()
  getAllSpots()
})
</script>

<style scoped>
.travel {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.section-title {
  margin-bottom: 20px;
  border-bottom: 2px solid #409EFF;
  padding-bottom: 10px;
}

.section-title h2 {
  margin: 0;
  color: #303133;
  font-size: 20px;
}

/* 热门推荐样式 */
.hot-section {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-right: 20px;
}

.hot-item {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.hot-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.hot-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
}

.hot-info {
  padding: 10px;
  text-align: center;
}

.hot-info h3 {
  margin: 10px 0;
  font-size: 16px;
  color: #303133;
}

/* 全部旅游样式 */
.main-section {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
}

.spot-card {
  margin-bottom: 20px;
  transition: all 0.3s;
  cursor: pointer;
}

.spot-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.spot-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.spot-info {
  padding: 15px;
}

.spot-info h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #303133;
}

.spot-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}

.remaining {
  color: #67c23a;
  font-size: 14px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 骨架屏样式 */
.hot-item-skeleton {
  margin-bottom: 20px;
}
</style> 