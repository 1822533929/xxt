<template>
  <div class="news-page">
    <div class="news-container">
      <div class="section-header">
        <h2>全部动态</h2>
      </div>
      
      <div class="news-grid">
        <div v-for="news in newsList" :key="news.id" class="news-item" @click="viewNews(news)">
          <el-image 
            :src="getImageUrl(news.cover)" 
            class="news-image"
            fit="cover"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          <div class="news-content">
            <h3 class="news-title">{{ news.title }}</h3>
            <p class="news-date">{{ news.publishTime }}</p>
            <div class="divider"></div>
            <p class="news-summary">{{ news.summary }}</p>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[8, 12, 16]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { get } from '@/common'
import { Picture } from '@element-plus/icons-vue'

const router = useRouter()
const newsList = ref([])
const currentPage = ref(1)
const pageSize = ref(8)
const total = ref(0)

const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

const getNewsList = async () => {
  try {
    const result = await get(`/news/getNewsList?currentPage=${currentPage.value}&pageSize=${pageSize.value}`)
    if (result.code === 200) {
      newsList.value = result.data.list
      total.value = result.data.total
    }
  } catch (error) {
    console.error('获取新闻列表失败:', error)
  }
}

const viewNews = (news) => {
  router.push(`/user/news-detail/${news.id}`)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getNewsList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getNewsList()
}

onMounted(() => {
  getNewsList()
})
</script>

<style scoped>
.news-page {
  padding: 20px;
  min-height: calc(100vh - 60px);
  background-color: #f5f7fa;
}

.news-container {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  padding: 20px;
}

.section-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #f0f2f5;
}

.section-header h2 {
  font-size: 20px;
  color: #303133;
  margin: 0;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.news-item {
  display: flex;
  background: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
}

.news-item:hover {
  transform: translateY(-5px);
}

.news-image {
  width: 200px;
  height: 150px;
  object-fit: cover;
}

.image-error {
  width: 200px;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  color: #909399;
}

.news-content {
  flex: 1;
  padding: 15px;
  display: flex;
  flex-direction: column;
}

.news-title {
  font-size: 16px;
  color: #303133;
  margin: 0 0 10px 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-date {
  color: #909399;
  font-size: 12px;
  margin: 0;
}

.divider {
  height: 1px;
  background: #dcdfe6;
  margin: 10px 0;
}

.news-summary {
  color: #606266;
  font-size: 14px;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  padding-top: 20px;
}
</style> 