<template>
  <div class="travel">
    <!-- 顶部背景区域 -->
    <div class="hero-section">
      <div class="hero-content">
        <h1>湘西特色旅游</h1>
        <p>探索最迷人的湘西山水，感受浓郁的民族风情</p>
        <!-- 搜索框 -->
        <div class="search-box">
          <el-input
            v-model="searchQuery"
            placeholder="搜索心仪的旅游景点..."
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </div>
      </div>
    </div>

    <el-container>
      <!-- 左侧热门推荐 -->
      <el-aside width="300px" class="hot-section">
        <div class="section-title">
          <h2>热门推荐</h2>
          <img src="@/assets/hot.svg" alt="Like"  class="hot-icon" />
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

          <el-card 
            v-for="item in hotSpots" 
            :key="item.id" 
            class="hot-item"
            @click="goToDetail(item.id)"
          >
            <img :src="getImageUrl(item.cover)" class="hot-image" />
            <div class="hot-info">
              <h3>{{ item.title }}</h3>
              <div class="hot-count">
                <img src="@/assets/hot2.svg" alt="Hot" class="hot2-icon" />
                <span>{{ Math.ceil(item.heat) || 0 }}</span>
              </div>
            </div>
          </el-card>
        </div>
      </el-aside>

      <!-- 右侧全部旅游 -->
      <el-main class="main-section">
        <div class="section-title">
          <h2>全部旅游</h2>
          <div class="filter-tags">
            <el-tag 
              v-for="tag in filterTags" 
              :key="tag"
              :class="{ active: activeTag === tag }"
              @click="handleTagClick(tag)"
            >
              {{ tag }}
            </el-tag>
          </div>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="8" v-for="item in allSpots" :key="item.id">
            <el-card 
              class="spot-card" 
              :body-style="{ padding: '0px' }"
              @click="goToDetail(item.id)"
            >
              <img :src="getImageUrl(item.cover)" class="spot-image" />
              <div class="spot-info">
                <h3>{{ item.title }}</h3>
                <div class="spot-tags">
                  <el-tag 
                    v-for="tag in item.tags?.split(',')" 
                    :key="tag.trim()" 
                    size="small"
                    type="info"
                  >
                    {{ tag.trim() }}
                  </el-tag>
                </div>
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
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'

const router = useRouter()

// 数据定义
const hotSpots = ref([])
const allSpots = ref([])
const hotLoading = ref(true)
const currentPage = ref(1)
const pageSize = ref(6)
const total = ref(0)
const searchQuery = ref('')
const activeTag = ref('全部')
const filterTags = ref(['全部'])

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 获取热门旅游数据
const getHotSpots = async () => {
  try {
    const result = await get('/travels/selectAllByHeat', {
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

// 获取所有标签
const getAllTags = async () => {
  try {
    const result = await get('/travels/getAllTags')
    if (result.code === 200) {
      filterTags.value = ['全部', ...result.data]
    }
  } catch (error) {
    console.error('获取标签失败:', error)
  }
}

// 获取全部旅游数据
const getAllSpots = async () => {
  try {
    const params = new URLSearchParams({
      currentPage: currentPage.value.toString(),
      pageSize: pageSize.value.toString()
    })
    
    const result = await get('/travels/selectAll', { params })
    if (result.code === 200) {
      const spots = result.data.list
      let filteredSpots = spots
      if (searchQuery.value) {
        const keyword = searchQuery.value.toLowerCase()
        filteredSpots = spots.filter(spot => 
          spot.title.toLowerCase().includes(keyword) || 
          spot.descr?.toLowerCase().includes(keyword)
        )
      }
      
      if (activeTag.value !== '全部') {
        allSpots.value = filteredSpots.filter(spot => 
          spot.tags?.split(',').map(t => t.trim()).includes(activeTag.value)
        )
        total.value = allSpots.value.length
      } else {
        allSpots.value = filteredSpots
        total.value = filteredSpots.length
      }
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

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/user/travel-detail/${id}`)
}

const handleSearch = () => {
  currentPage.value = 1
  getAllSpots()
}

const handleTagClick = (tag) => {
  activeTag.value = tag
  currentPage.value = 1
  getAllSpots()
}

onMounted(() => {
  getAllTags()
  getHotSpots()
  getAllSpots()
})
</script>

<style scoped>
.travel {
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.hero-section {
  height: 400px;
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('@/images/background/fenghuang.jpg');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  text-align: center;
  margin-bottom: 30px;
}

.hero-content {
  max-width: 800px;
  padding: 0 20px;
}

.hero-content h1 {
  font-size: 3em;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.hero-content p {
  font-size: 1.2em;
  margin-bottom: 30px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
}

.search-box {
  display: flex;
  gap: 10px;
  justify-content: center;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  width: 400px;
  transition: all 0.3s;
}

.search-input:focus {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

:deep(.el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.9);
}

.section-title {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px solid #409EFF;
  padding-bottom: 10px;
}

.filter-tags {
  display: flex;
  gap: 10px;
}

.el-tag {
  cursor: pointer;
  transition: all 0.3s;
}

.el-tag.active {
  background-color: #409EFF;
  color: white;
  border-color: #409EFF;
}

.el-tag:hover {
  transform: translateY(-2px);
}

.hot-section {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-right: 20px;
  margin-left: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.main-section {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-right: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

.hot-count {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: #f56c6c;
  font-size: 14px;
}

.hot2-icon {
  width: 16px;
  height: 16px;
  vertical-align: middle;
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
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
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

.spot-tags {
  margin: 10px 0;
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.spot-tags .el-tag {
  margin-right: 5px;
  margin-bottom: 5px;
  background-color: #ecf5ff;
  color: #409EFF;
  border-color: #d9ecff;
}

.filter-tags .el-tag {
  cursor: pointer;
  transition: all 0.3s;
  padding: 0 15px;
  height: 32px;
  line-height: 30px;
}

.filter-tags .el-tag.active {
  background-color: #409EFF;
  color: white;
  border-color: #409EFF;
}

.filter-tags .el-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.hot-icon {
  width: 50px;
  height: 50px;
  fill: #f0ad4e;
  vertical-align: middle;
}
</style> 