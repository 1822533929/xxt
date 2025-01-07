<template>
  <div class="travel-routes">
    <!-- 顶部背景区域 -->
    <div class="hero-section">
      <div class="hero-content">
<!--        <img src="./src/images/background/dehang.jpg">-->
        <h1>探索湘西精选路线</h1>
        <p>发现最美的湘西风光，体验独特的民族文化</p>
      </div>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="filter-section">
      <div class="filter-container">
        <el-select v-model="filterDays" placeholder="行程天数" class="filter-item">
          <el-option label="全部" value="" />
          <el-option label="1-3天" value="1-3" />
          <el-option label="4-7天" value="4-7" />
          <el-option label="7天以上" value="7+" />
        </el-select>
        <el-input
          v-model="searchQuery"
          placeholder="搜索路线..."
          prefix-icon="Search"
          class="search-input"
        />
      </div>
    </div>

    <!-- 路线列表 -->
    <div class="routes-grid">
      <div v-for="route in filteredRoutes" :key="route.id" class="route-card">
        <div class="route-image-wrapper" @click="viewRoute(route)">
          <el-image 
            :src="getImageUrl(route.img)" 
            fit="cover"
            class="route-image"
          >
            <template #error>
              <div class="image-placeholder">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </div>
        <div class="route-info">
          <h3 class="route-name">{{ route.name }}</h3>
          <p class="route-tips">{{ route.tips }}</p>
          <div class="route-meta">
            <span class="route-days">{{ route.days }}天</span>
            <span class="route-location">{{ route.location }}</span>
          </div>
          <el-button type="primary" @click="viewRoute(route)">查看详情</el-button>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[9, 18, 36]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Picture, Search } from '@element-plus/icons-vue'
import { get } from '@/common'
import { API_BASE_URL } from '@/common/constants'

const router = useRouter()
const routes = ref([])
const searchQuery = ref('')
const filterDays = ref('')
const currentPage = ref(1)
const pageSize = ref(9)
const total = ref(0)
const loading = ref(false)

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return API_BASE_URL + url
}

// 过滤路线
const filteredRoutes = computed(() => {
  let result = routes.value

  if (searchQuery.value) {
    result = result.filter(route => 
      route.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      route.location.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  if (filterDays.value) {
    result = result.filter(route => {
      if (filterDays.value === '1-3') return route.days >= 1 && route.days <= 3
      if (filterDays.value === '4-7') return route.days >= 4 && route.days <= 7
      if (filterDays.value === '7+') return route.days > 7
      return true
    })
  }

  return result
})

// 获取路线数据
const getRoutes = async () => {
  loading.value = true
  try {
    const result = await get('/routes/selectAll', {
      currentPage: currentPage.value,
      pageSize: pageSize.value
    })
    if (result.code === 200) {
      routes.value = result.data.list
      total.value = result.data.total
    }
  } catch (error) {
    console.error('获取路线失败:', error)
  } finally {
    loading.value = false
  }
}

// 查看路线详情
const viewRoute = (route) => {
  router.push(`/user/route-detail/${route.id}`)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getRoutes()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getRoutes()
}

onMounted(() => {
  getRoutes()
})
</script>

<style scoped>
.travel-routes {
  min-height: 100vh;
}

.hero-section {
  height: 400px;
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('@/images/background/dehang.jpg');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  text-align: center;
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
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
}

.filter-section {
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.filter-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  gap: 20px;
  align-items: center;
}

.filter-item {
  width: 150px;
}

.search-input {
  width: 300px;
}

.routes-grid {
  max-width: 1200px;
  margin: 40px auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  padding: 0 20px;
}

.route-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.route-image-wrapper {
  height: 200px;
  overflow: hidden;
}

.route-image {
  width: 100%;
  height: 100%;
  transition: transform 0.3s ease;
}

.route-card:hover .route-image {
  transform: scale(1.05);
}

.route-info {
  padding: 20px;
}

.route-name {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 10px;
  color: #303133;
}

.route-tips {
  color: #606266;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 40px;
}

.route-meta {
  display: flex;
  justify-content: space-between;
  color: #909399;
  margin-bottom: 15px;
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 24px;
}
</style> 