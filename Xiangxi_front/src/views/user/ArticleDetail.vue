<template>
  <div class="article-detail">
    <div class="article-container">
      <div class="article-header">
        <h1>{{ article.title }}</h1>
        <div class="article-meta">
          <span class="time">发布时间：{{ formatDate(article.date) }}</span>
          <div class="likes">
            <span class="like-count">{{ article.likes || 0 }}点赞</span>
            <el-button 
              type="primary" 
              :icon="Star" 
              circle 
              size="small"
              :class="{ 'liked': isLiked }"
              @click="handleLike"
            />
          </div>
        </div>
      </div>
      
      <div class="article-content">
        <img 
          v-if="article.cover" 
          :src="getImageUrl(article.cover)" 
          class="cover-image"
          @error="handleImageError"
        >
        <div class="content" v-html="article.content"></div>
      </div>
      
      <div class="article-footer">
        <el-button @click="goBack">返回列表</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { get, post } from '@/common'
import { ElMessage } from 'element-plus'
import { Star } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const article = ref({})
const isLiked = ref(false)

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
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

const handleImageError = (e) => {
  e.target.src = '/default-image.jpg'
}

const getArticleDetail = async (id) => {
  try {
    const result = await get('/article/selectById/' + id)
    if (result.code === 200) {
      article.value = result.data
      console.log('获取到的文章数据:', result.data)
    } else {
      ElMessage.error(result.msg || '获取文章详情失败')
    }
  } catch (error) {
    console.error('获取文章详情错误:', error)
    console.error('请求URL:', '/article/selectById/' + id)
    ElMessage.error('获取文章详情失败')
  }
}

const handleLike = async () => {
  if (isLiked.value) {
    ElMessage.warning('您已经点赞过了')
    return
  }
  
  try {
    const result = await post(`/article/like/${article.value.id}`)
    if (result.code === 200) {
      isLiked.value = true
      article.value.likes = (article.value.likes || 0) + 1
      ElMessage.success('点赞成功')
    } else {
      ElMessage.error(result.msg || '点赞失败')
    }
  } catch (error) {
    ElMessage.error('点赞失败')
  }
}

onMounted(() => {
  const articleId = route.params.id
  if (articleId) {
    getArticleDetail(articleId)
  }
})

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.article-detail {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.article-container {
  max-width: 800px;
  margin: 0 auto;
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.article-header {
  margin-bottom: 30px;
  text-align: center;
}

.article-header h1 {
  font-size: 28px;
  color: #303133;
  margin-bottom: 15px;
}

.article-meta {
  color: #909399;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 15px 0;
}

.article-meta span {
  margin: 0 10px;
}

.article-content {
  line-height: 1.8;
  color: #606266;
}

.cover-image {
  width: 100%;
  max-height: 700px;
  object-fit: cover;
  margin-bottom: 20px;
  border-radius: 4px;
}

.content {
  font-size: 16px;
  line-height: 1.8;
  color: #303133;
  padding: 20px 0;
}

.article-footer {
  margin-top: 30px;
  text-align: center;
}

.likes {
  display: flex;
  align-items: center;
  gap: 10px;
}

.like-count {
  color: #606266;
}

.liked {
  background-color: #f56c6c;
  border-color: #f56c6c;
}

.liked:hover {
  background-color: #f78989;
  border-color: #f78989;
}
</style> 