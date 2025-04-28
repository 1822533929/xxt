<template>
  <div class="news-detail">
    <div class="content-wrapper">
      <!-- 标题区域 -->
      <div class="header-section">
        <h1>{{ newsData.title }}</h1>
        <div class="meta-info">
          <span class="date">{{ newsData.publishTime }}</span>
          <span class="views">阅读量: {{ newsData.readCount }}</span>
        </div>
      </div>

      <!-- 封面图片 -->
      <div class="cover-section" v-if="newsData.cover">
        <el-image 
          :src="getImageUrl(newsData.cover)"
          fit="cover"
          class="cover-image"
        >
          <template #error>
            <div class="image-error">
              <el-icon><Picture /></el-icon>
              <span>图片加载失败</span>
            </div>
          </template>
        </el-image>
      </div>

      <!-- 摘要 -->
      <div class="summary-section" v-if="newsData.summary">
        <div class="summary-content">
          {{ newsData.summary }}
        </div>
      </div>

      <!-- 内容 -->
      <div class="content-section" v-html="newsData.content"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { get } from '@/common'
import { ElMessage } from 'element-plus'
import { Picture } from '@element-plus/icons-vue'

const route = useRoute()
const newsData = ref({})

const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 获取新闻详情
const getNewsDetail = async () => {
  try {
    const result = await get(`/news/getNews?id=${route.params.id}`)
    if (result.code === 200) {
      newsData.value = result.data
      // 增加阅读量
      addReadCount()
    }
  } catch (error) {
    console.error('获取新闻详情失败:', error)
    ElMessage.error('获取新闻详情失败')
  }
}

// 增加阅读量
const addReadCount = async () => {
  try {
    await get(`/news/addRead?id=${route.params.id}`)
  } catch (error) {
    console.error('增加阅读量失败:', error)
  }
}

onMounted(() => {
  getNewsDetail()
})
</script>

<style scoped>
.news-detail {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.content-wrapper {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  padding: 40px;
}

.header-section {
  margin-bottom: 30px;
  text-align: center;
}

.header-section h1 {
  font-size: 28px;
  color: #303133;
  margin-bottom: 15px;
}

.meta-info {
  color: #909399;
  font-size: 14px;
}

.meta-info span {
  margin: 0 10px;
}

.cover-section {
  margin-bottom: 30px;
}

.cover-image {
  width: 100%;
  max-height: 400px;
  border-radius: 8px;
}

.image-error {
  width: 100%;
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
}

.summary-section {
  margin-bottom: 30px;
  padding: 20px;
  background: #f0f9eb;
  border-radius: 4px;
}

.summary-content {
  color: #67c23a;
  line-height: 1.6;
}

.content-section {
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
}

:deep(img) {
  max-width: 100%;
  height: auto;
}
</style> 