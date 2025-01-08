<template>
  <div class="route-detail">
    <!-- 标题区域 -->
    <div class="header-section">
      <div class="content-wrapper">
        <h1>{{ routeData.name }}</h1>
        <div class="route-meta">
          <span class="days">
            <el-icon><Calendar /></el-icon>
            {{ routeData.days }}天
          </span>
          <span class="location">
            <el-icon><Location /></el-icon>
            {{ routeData.location }}
          </span>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content-container">
      <div class="content-wrapper">
        <!-- 封面图片 -->
        <div class="cover-image">
          <el-image 
            :src="getImageUrl(routeData.img)" 
            fit="cover"
            :preview-src-list="[getImageUrl(routeData.img)]"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
                <span>图片加载失败</span>
              </div>
            </template>
          </el-image>
        </div>

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
          <div class="location-info"><!--这里等着插入地图-->
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
import { Calendar, Location, Picture } from '@element-plus/icons-vue'

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

.header-section {
  background-color: #fff;
  padding: 40px 0;
  border-bottom: 1px solid #ebeef5;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.content-wrapper {
  max-width: 800px;
  margin: 0 auto;
  width: 100%;
  padding: 0 20px;
}

.header-section h1 {
  font-size: 2.5em;
  margin-bottom: 20px;
  color: #303133;
}

.route-meta {
  display: flex;
  gap: 20px;
  font-size: 1.1em;
  color: #606266;
  align-items: center;
}

.route-meta .el-icon {
  margin-right: 5px;
}

.cover-image {
  margin-bottom: 40px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.cover-image .el-image {
  width: 100%;
  height: 400px;
  display: block;
}

.image-error {
  height: 400px;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.image-error .el-icon {
  font-size: 48px;
  margin-bottom: 10px;
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
  /*background-color: #f5f7fa;*/
  /*padding: 20px;*/
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