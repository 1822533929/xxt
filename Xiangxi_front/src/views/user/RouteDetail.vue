<template>
  <div class="route-detail">
    <!-- 顶部封面区域 -->
    <div class="cover-section" :style="{ backgroundImage: `url(${getImageUrl(routeData.img)})` }">
      <div class="cover-content">
        <h1>{{ routeData.name }}</h1>
        <div class="route-meta">
          <span class="days">{{ routeData.days }}天</span>
          <span class="location">{{ routeData.location }}</span>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content-container">
      <div class="content-wrapper">
        <!-- 小贴士 -->
        <div class="tips-section" v-if="routeData.tips">
          <h2>行程小贴士</h2>
          <div class="tips-content">
            {{ routeData.tips }}
          </div>
        </div>

        <!-- 路线内容 -->
        <div class="route-content">
          <h2>行程详情</h2>
          <div class="content" v-html="routeData.content"></div>
        </div>

        <!-- 地理位置 -->
        <div class="location-section" v-if="routeData.location">
          <h2>地理位置</h2>
          <div class="location-info">
            <p>{{ routeData.location }}</p>
            <p>经度：{{ routeData.longitude }}</p>
            <p>纬度：{{ routeData.latitude }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { get } from '@/common'
import { ElMessage } from 'element-plus'

const route = useRoute()
const routeData = ref({})

const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

const getRouteDetail = async () => {
  try {
    const result = await get(`/routes/get/${route.params.id}`)
    if (result.code === 200) {
      routeData.value = result.data
      console.log('路线数据:', routeData.value)
    } else {
      ElMessage.error('获取路线详情失败')
    }
  } catch (error) {
    console.error('获取路线详情错误:', error)
    ElMessage.error('获取路线详情失败')
  }
}

onMounted(() => {
  getRouteDetail()
})
</script>

<style scoped>
.route-detail {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.cover-section {
  height: 400px;
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: flex-end;
  padding: 40px;
}

.cover-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.7));
}

.cover-content {
  position: relative;
  color: white;
  max-width: 800px;
  margin: 0 auto;
  width: 100%;
}

.cover-content h1 {
  font-size: 2.5em;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
}

.route-meta {
  display: flex;
  gap: 20px;
  font-size: 1.1em;
}

.content-container {
  padding: 40px 20px;
}

.content-wrapper {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  padding: 40px;
}

h2 {
  font-size: 1.5em;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409EFF;
}

.tips-section {
  margin-bottom: 40px;
}

.tips-content {
  background-color: #f0f9eb;
  padding: 20px;
  border-radius: 4px;
  color: #67c23a;
  line-height: 1.6;
}

.route-content {
  margin-bottom: 40px;
}

.content {
  line-height: 1.8;
  color: #606266;
}

.location-section {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
}

.location-info {
  line-height: 1.8;
  color: #606266;
}

.location-info p {
  margin-bottom: 10px;
}
</style> 