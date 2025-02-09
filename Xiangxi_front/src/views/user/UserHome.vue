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

    <!-- 热门攻略区域 -->
    <div class="guides-section">
      <div class="section-header">
        <h2>热门攻略</h2>
        <router-link to="/user/guides" class="more-link">
          更多<el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>

      <div class="guides-grid">
        <div v-for="guide in hotGuides"
             :key="guide.id"
             class="guide-item"
             @click="viewGuide(guide)"
        >
          <el-image
              :src="getImageUrl(guide.cover)"
              class="guide-image"
              fit="cover"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          <div class="guide-content">
            <h3 class="guide-title">{{ guide.title }}</h3>
            <div class="guide-footer">
              <span class="guide-date">{{ guide.date }}</span>
              <span class="likes">
                <el-icon><Star /></el-icon>
                {{ guide.likes }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 景点标题背景 -->
    <div class="title-background">
      <div class="title-content">
        <!--        <h1>湘西风景</h1>-->
        <!--        <p>探索湘西独特的自然与人文景观</p>-->
        <el-image :src="hottravel" fit="cover"></el-image>
      </div>
    </div>

    <!-- 热门景点区域 -->
    <div class="spots-section">
      <div class="section-header">
        <h2>热门景点</h2>
        <router-link to="/user/travel" class="more-link">
          更多<el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>
      
      <div class="spots-grid">
        <div v-for="spot in hotSpots" :key="spot.id" class="spot-item" @click="viewSpot(spot)">
          <div class="spot-image-container">
            <el-image 
              :src="getImageUrl(spot.cover)"
              class="spot-image"
              fit="cover"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <div class="spot-info">
              <h3 class="spot-title">{{ spot.title }}</h3>
              <div class="spot-description">{{ spot.descr }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 路线标题背景 -->
    <div class="title-background">
      <div class="title-content">
         <!--        <h1>精选路线</h1>-->
        <!--        <p>定制专属您的湘西之旅</p>-->
        <el-image :src="hotroute" fit="cover"></el-image>
      </div>
    </div>

    <!-- 热门路线区域 -->
    <div class="routes-section">
      <div class="section-header">
        <h2>热门路线</h2>
        <router-link to="/user/routes" class="more-link">
          更多<el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>
      
      <div class="routes-grid">
        <div v-for="route in hotRoutes" 
             :key="route.id" 
             class="route-item" 
             @click="viewRoute(route)"
        >
          <el-image 
            :src="getImageUrl(route.img)" 
            class="route-image"
            fit="cover"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          <div class="route-content">
            <h3 class="route-title">{{ route.title }}</h3>
            <div class="route-info">
              <span class="route-days">
                <el-icon><Calendar /></el-icon>&nbsp{{ route.days }}天</span>
              <span class="route-location">
                <el-icon><Location /></el-icon>
                {{ route.location }}
              </span>
            </div>
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
import { ArrowRight, Picture, Star, Location } from '@element-plus/icons-vue'
import fenghuangImage from '@/images/carousel/fenghuang.jpg'
import dehangImage from '@/images/carousel/dehang.jpg'
import zhangjiajieImage from '@/images/carousel/zhangjiajie.jpg'
import hottravel from '@/images/module/travel.png'
import hotroute from '@/images/module/route.png'

const router = useRouter()
const latestNews = ref([])
const hotSpots = ref([])
const hotGuides = ref([])
const hotRoutes = ref([])

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
    const result = await get('/news/getNewsByTime?currentPage=1&pageSize=4')
    if (result.code === 200) {
      latestNews.value = result.data.list
    }
  } catch (error) {
    console.error('获取最新动态失败:', error)
  }
}

const getHotSpots = async () => {
  try {
    const result = await get('/travels/selectAllByRead?currentPage=1&pageSize=3')
    if (result.code === 200) {
      hotSpots.value = result.data.list
    }
  } catch (error) {
    console.error('获取热门景点失败:', error)
  }
}

const getHotGuides = async () => {
  try {
    const result = await get('/article/findHot?currentPage=1&pageSize=3')
    if (result.code === 200) {
      hotGuides.value = result.data.list
    }
  } catch (error) {
    console.error('获取热门攻略失败:', error)
  }
}

const getHotRoutes = async () => {
  try {
    const result = await get('/routes/selectAll?currentPage=1&pageSize=3')
    if (result.code === 200) {
      hotRoutes.value = result.data.list
    }
  } catch (error) {
    console.error('获取热门路线失败:', error)
  }
}

const viewNews = (news) => {
  router.push(`/user/news-detail/${news.id}`)
}

const viewSpot = (spot) => {
  router.push(`/user/travel-detail/${spot.id}`)
}

const viewGuide = (guide) => {
  router.push(`/user/article-detail/${guide.id}`)
}

const viewRoute = (route) => {
  router.push(`/user/route-detail/${route.id}`)
}

onMounted(() => {
  getLatestNews()
  getHotSpots()
  getHotGuides()
  getHotRoutes()
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

.spots-section {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.spots-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.spot-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
}

.spot-image-container {
  position: relative;
  width: 100%;
  height: 250px;
}

.spot-image {
  width: 100%;
  height: 100%;
  transition: transform 0.3s ease;
}

.spot-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 15px;
  background: linear-gradient(to top, rgba(0,0,0,0.8), transparent);
  color: white;
  transform: translateY(calc(100% - 50px));
  transition: transform 0.3s ease;
}

.spot-title {
  margin: 0;
  font-size: 18px;
  margin-bottom: 10px;
}

.spot-description {
  font-size: 14px;
  opacity: 0;
  transition: opacity 0.3s ease;
  line-height: 1.4;
}

.spot-item:hover .spot-image {
  transform: scale(1.05);
}

.spot-item:hover .spot-info {
  transform: translateY(0);
}

.spot-item:hover .spot-description {
  opacity: 1;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  color: #909399;
}

.guides-section {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.guides-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.guide-item {
  background: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
}

.guide-item:hover {
  transform: translateY(-5px);
}

.guide-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.guide-content {
  padding: 15px;
}

.guide-title {
  font-size: 16px;
  color: #303133;
  margin: 0 0 10px 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.guide-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.guide-date {
  color: #909399;
  font-size: 12px;
}

.likes {
  color: #f56c6c;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.routes-section {
  max-width: 1200px;
  margin: 0px auto;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.routes-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.route-item {
  background: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
}

.route-item:hover {
  transform: translateY(-5px);
}

.route-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.route-content {
  padding: 15px;
}

.route-title {
  font-size: 16px;
  color: #303133;
  margin: 0 0 10px 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.route-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.route-days {
  color: #909399;
  font-size: 14px;
}

.route-location {
  color: #409EFF;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.title-background {
  width: 100%;
  height: 140px;
  /*background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);*/
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

.title-content {
  width: 600px;
  text-align: center;
  color: white;
}

.title-content h1 {
  font-size: 32px;
  margin-bottom: 10px;
  font-weight: 500;
}

.title-content p {
  font-size: 16px;
  opacity: 0.9;
}
</style> 