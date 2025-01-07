<template>
  <div class="travel-guides">
    <div class="hero-section">
      <div class="hero-content">
        <h1>湘西旅游攻略</h1>
        <p>带上这份攻略，开始你的湘西之旅吧！</p>
        <div class="search-box">
          <el-input
            v-model="searchQuery"
            placeholder="搜索攻略..."
            class="search-input"
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>
    </div>

    <div class="guides-container">
      <div class="guides-content">
        <el-card 
          v-for="guide in articleList" 
          :key="guide.id" 
          class="guide-card" 
          @click="viewDetail(guide.id)"
          v-loading="loading"
        >
          <div class="guide-layout">
             <img :src="getImageUrl(guide.cover)" class="guide-image" @error="handleImageError">
             <div class="guide-info">
               <h3>{{ guide.title }}</h3>
               <p class="description">{{ guide.descr }}</p>
               <div class="card-footer">
                 <span class="time">{{ formatDate(guide.date) }}</span>
                 <el-button type="text">查看详情</el-button>
               </div>
             </div>
          </div>
        </el-card>
        <div v-if="loading" class="loading-placeholder">
          加载中...
        </div>
        <div v-if="!loading && articleList.length === 0" class="empty-placeholder">
          暂无数据
        </div>
      </div>
    </div>

    <div class="pagination">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { get } from '@/common'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const router = useRouter()
const searchQuery = ref('')
const articleList = ref([])
const total = ref(0)
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

const getArticleList = async () => {
  loading.value = true
  try {
    const params = {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
    }
    const url = searchQuery.value ? '/article/search' : '/article/selectPage'
    if (searchQuery.value) {
      params.keyword = searchQuery.value
    }
    const result = await get(url, params)
    if (result.code === 200) {
      if (searchQuery.value) {
        articleList.value = result.data
        total.value = result.data.length
      } else {
        articleList.value = result.data.list
        total.value = result.data.total
      }
    } else {
      ElMessage.error(result.msg || '获取攻略列表失败')
    }
  } catch (error) {
    console.error('获取攻略列表错误:', error)
    ElMessage.error('获取攻略列表失败')
  } finally {
    loading.value = false
  }
}

const viewDetail = (id) => {
  router.push(`/user/article-detail/${id}`)
}

const handlePageChange = (page) => {
  currentPage.value = page
  getArticleList()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  getArticleList()
}

const handleSearch = () => {
  currentPage.value = 1
  getArticleList()
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getImageUrl = (url) => {
  if (!url) return '/default-image.jpg'
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

const handleImageError = (e) => {
  e.target.src = '/default-image.jpg'
}

onMounted(() => {
  getArticleList()
})
</script>

<style scoped>
.travel-guides {
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.hero-section {
  height: 400px;
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('@/images/background/aizhai.jpg');
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
  justify-content: center;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  width: 500px;
}

:deep(.el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.9);
}

.guides-container {
  display: flex;
  justify-content: center;
  padding: 0 20px;
}

.guides-content {
  width: 800px;
  max-width: 100%;
}

.guide-card {
  margin-bottom: 20px;
  transition: all 0.3s;
  cursor: pointer;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.guide-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.guide-layout {
  display: flex;
  gap: 20px;
  height: 160px;
  overflow: hidden;
}

.guide-image {
  width: 200px;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
}

.guide-info {
  flex: 1;
  padding: 10px 15px;
  display: flex;
  flex-direction: column;
}

.guide-info h3 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.description {
  color: #666;
  margin: 5px 0;
  flex: 1;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
  font-size: 14px;
  line-height: 1.5;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 5px;
}

.time {
  color: #999;
  font-size: 13px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.loading-placeholder,
.empty-placeholder {
  text-align: center;
  padding: 40px 0;
  color: #909399;
  font-size: 14px;
}
</style> 