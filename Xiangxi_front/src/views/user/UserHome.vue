<template>
  <div class="user-home">
    <!-- 轮播图 -->
    <el-carousel height="400px" class="carousel">
      <el-carousel-item v-for="item in carouselItems" :key="item.id">
        <img :src="item.imageUrl" :alt="item.title" class="carousel-image">
        <div class="carousel-caption">
          <h3>{{ item.title }}</h3>
          <p>{{ item.description }}</p>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 最新动态区域 -->
    <div class="news-section">
      <div class="section-header">
        <h2>最新动态</h2>
        <router-link to="/user/news" class="more-link">
          更多<el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>
      
      <div class="news-grid">
        <div v-for="news in latestNews" :key="news.id" class="news-item" @click="viewNews(news)">
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
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { get } from '@/common'
import { ArrowRight, Picture } from '@element-plus/icons-vue'
import fenghuangImage from '@/images/carousel/fenghuang.jpg'
import dehangImage from '@/images/carousel/dehang.jpg'
import zhangjiajieImage from '@/images/carousel/zhangjiajie.jpg'

const router = useRouter()
const latestNews = ref([])

const carouselItems = [
  {
    id: 1,
    title: '凤凰古城',
    description: '湘西最著名的旅游景点',
    imageUrl: fenghuangImage
  },
  {
    id: 2,
    title: '德夯苗寨',
    description: '体验原真的苗族文化',
    imageUrl: dehangImage
  },
  {
    id: 3,
    title: '张家界',
    description: '世界自然遗产',
    imageUrl: zhangjiajieImage
  }
]

const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

const getLatestNews = async () => {
  try {
    const result = await get('/news/getNewsList?currentPage=1&pageSize=4')
    if (result.code === 200) {
      latestNews.value = result.data.list
    }
  } catch (error) {
    console.error('获取最新动态失败:', error)
  }
}

const viewNews = (news) => {
  router.push(`/user/news-detail/${news.id}`)
}

onMounted(() => {
  getLatestNews()
})
</script>

<style scoped>
.user-home {
  padding: 0;  /* 移除内边距使轮播图占满宽度 */
}

.carousel {
  width: 100%;
  margin-bottom: 20px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-caption {
  position: absolute;
  bottom: 20px;
  left: 20px;
  color: white;
  text-shadow: 0 1px 2px rgba(0,0,0,0.6);
}

.carousel-caption h3 {
  font-size: 24px;
  margin-bottom: 10px;
}

.carousel-caption p {
  font-size: 16px;
  margin: 0;
}

/* 优化轮播图指示器样式 */
:deep(.el-carousel__indicators) {
  transform: translateY(-24px);
}

:deep(.el-carousel__button) {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.7);
}

:deep(.el-carousel__indicator.is-active .el-carousel__button) {
  background-color: #fff;
}

.news-section {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #f0f2f5;
}

.section-header h2 {
  font-size: 20px;
  color: #303133;
  margin: 0;
}

.more-link {
  display: flex;
  align-items: center;
  color: #409EFF;
  text-decoration: none;
  font-size: 14px;
}

.more-link .el-icon {
  margin-left: 4px;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
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
</style> 