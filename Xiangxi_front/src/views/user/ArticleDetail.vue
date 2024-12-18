<template>
  <div class="article-detail">
    <div class="article-container">
      <div class="article-header">
        <h1>{{ article.title }}</h1>
        <div class="article-meta">
          <span class="time">发布时间：{{ article.createTime }}</span>
          <span class="author">作者：{{ article.author }}</span>
        </div>
      </div>
      
      <div class="article-content">
        <img v-if="article.coverImage" :src="article.coverImage" class="cover-image">
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

const route = useRoute()
const router = useRouter()

const article = ref({
  title: '凤凰古城一日游攻略',
  author: '管理员',
  createTime: '2024-03-20',
  coverImage: 'path/to/image.jpg',
  content: `
    <p>凤凰古城位于湖南省湘西土家族苗族自治州...</p>
    <p>这里��详细内容...</p>
  `
})

onMounted(() => {
  const articleId = route.params.id
  // TODO: 根据 ID 获取文章详情
  console.log('获取文章ID:', articleId)
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
  max-height: 400px;
  object-fit: cover;
  margin-bottom: 20px;
  border-radius: 4px;
}

.content {
  font-size: 16px;
}

.article-footer {
  margin-top: 30px;
  text-align: center;
}
</style> 