<template>
  <div class="article-detail">
    <div class="article-container">
      <div class="article-header">
        <h1>{{ article.title }}</h1>

        <div class="article-info">
          <div class="author-info">
            <el-avatar 
              :size="40" 
              :src="getImageUrl(article.avatar)"
              class="author-avatar"
            >
              <el-icon><UserFilled /></el-icon>
            </el-avatar>
            <span class="author-name">{{ article.name || '匿名用户' }}</span>
          </div>
          <div class="meta-info">
            <span class="publish-time">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(article.date) }}
            </span>
            <span class="likes-count">
              <img src="@/assets/点赞.svg" alt="Like"  class="like-icon" />
              {{ article.likes || 0 }} 点赞
            </span>
          </div>
        </div>
      </div>
      
      <div class="article-content">
        <div class="content-wrapper">
          <img 
            v-if="article.cover" 
            :src="getImageUrl(article.cover)" 
            class="cover-image"
            @error="handleImageError"
          >
          <div class="content" v-html="article.content"></div>
        </div>
        <div class="like-button">
          <el-button
              type="primary"
              :class="{ 'liked': isLiked }"
              @click="handleArticleLike"
              size="large"
              round
          >
            <el-icon><Star /></el-icon>
            {{ isLiked ? '已点赞' : '点赞' }}
          </el-button>
        </div>
      </div>
      
      <div class="article-footer">
        <el-button type="default" @click="goBack" round>
          <el-icon><ArrowLeft /></el-icon>
          返回列表
        </el-button>
      </div>

      <!-- 评论区 -->
      <div class="comments-section">
        <h3 class="comments-title">评论区</h3>
        
        <!-- 发表评论 -->
        <div class="comment-form">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
          />
          <div class="form-footer">
            <el-button type="primary" @click="submitComment">发表评论</el-button>
          </div>
        </div>
        
        <!-- 评论列表 -->
        <div class="comments-list">
          <template v-for="comment in comments" :key="comment.id">
            <!-- 只显示顶层评论 -->
            <div v-if="!comment.parentId || comment.parentId === 0" class="comment-item">
              <!-- 主评论 -->
              <div class="comment-main">
                <el-avatar :size="40" :src="getImageUrl(comment.avatar)">
                  <el-icon><UserFilled /></el-icon>
                </el-avatar>
                <div class="comment-content">
                  <div class="comment-header">
                    <span class="username">{{ comment.name }}</span>
                    <span class="time">{{ formatDate(comment.createdAt) }}</span>
                  </div>
                  <div class="comment-text">{{ comment.content }}</div>
                  <div class="comment-actions">
                    <el-button type="text" @click="handleReply(comment)">回复</el-button>
                    <el-button 
                      type="text" 
                      @click="handleCommentLike(comment)"
                      :class="{ 'liked-comment': likedComments.has(comment.id) }"
                    >
                      <img src="@/assets/未点赞.svg" alt="Like"  class="unlike-icon" />
                      {{ comment.likes || 0 }}
                    </el-button>
                    <el-button 
                      v-if="isCurrentUser(comment.userId)" 
                      type="text" 
                      @click="handleDelete(comment)"
                    >删除</el-button>
                  </div>
                </div>
              </div>
              
              <!-- 回复框 -->
              <div v-if="replyTo?.id === comment.id" class="reply-form">
                <el-input
                  v-model="replyContent"
                  type="textarea"
                  :rows="2"
                  :placeholder="`回复 ${comment.name}...`"
                />
                <div class="form-footer">
                  <el-button @click="cancelReply">取消</el-button>
                  <el-button type="primary" @click="submitReply(comment)">回复</el-button>
                </div>
              </div>
              
              <!-- 子评论 -->
              <div v-if="comment.children && comment.children.length > 0" class="replies-list">
                <div v-for="reply in comment.children" :key="reply.id" class="reply-item">
                  <div class="comment-main reply">
                    <el-avatar :size="32" :src="getImageUrl(reply.avatar)">
                      <el-icon><UserFilled /></el-icon>
                    </el-avatar>
                    <div class="comment-content">
                      <div class="comment-header">
                        <span class="username">{{ reply.name }}</span>
                        <span class="reply-to">回复 {{ comment.name }}</span>
                        <span class="time">{{ formatDate(reply.createdAt) }}</span>
                      </div>
                      <div class="comment-text">{{ reply.content }}</div>
                      <div class="comment-actions">
                        <el-button 
                          type="text" 
                          @click="handleCommentLike(reply)"
                          :class="{ 'liked-comment': likedComments.has(reply.id) }"
                        >
                          <img src="@/assets/未点赞.svg" alt="Like"  class="unlike-icon" />
                          {{ reply.likes || 0 }}
                        </el-button>
                        <el-button 
                          v-if="isCurrentUser(reply.userId)" 
                          type="text" 
                          @click="handleDelete(reply)"
                        >删除</el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { get, post } from '@/common'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, Calendar, UserFilled, ArrowLeft } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const article = ref({})
const isLiked = ref(false)
const comments = ref([])
const newComment = ref('')
const replyTo = ref(null)
const replyContent = ref('')
const currentUser = ref(null)
const likedComments = ref(new Set())

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

const handleArticleLike = async () => {
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

const getComments = async () => {
  try {
    const result = await get(`/comment/getCommentsByArticleId?articleId=${route.params.id}`)
    if (result.code === 200) {
      // 重组评论数据结构
      const commentMap = new Map()
      const topLevelComments = []
      
      // 首先创建所有评论的映射
      result.data.forEach(comment => {
        comment.children = []
        commentMap.set(comment.id, comment)
      })
      
      // 构建评论树
      result.data.forEach(comment => {
        if (comment.parentId === 0 || !comment.parentId) {
          // 顶层评论
          topLevelComments.push(comment)
        } else {
          // 子评论
          const parentComment = commentMap.get(comment.parentId)
          if (parentComment) {
            parentComment.children.push(comment)
          }
        }
      })
      
      comments.value = topLevelComments
    }
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

const handleCommentLike = async (comment) => {
  if (likedComments.value.has(comment.id)) {
    ElMessage.warning('您已经点赞过这条评论了')
    return
  }
  
  try {
    const result = await get(`/comment/like/${comment.id}`)
    if (result.code === 200) {
      comment.likes = (comment.likes || 0) + 1
      likedComments.value.add(comment.id)
      ElMessage.success('点赞成功')
    }
  } catch (error) {
    ElMessage.error('点赞失败')
  }
}

const handleDelete = async (comment) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const result = await get(`/comment/delete/${comment.id}`)
    if (result.code === 200) {
      ElMessage.success('删除成功')
      getComments()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleReply = (comment) => {
  replyTo.value = comment
  replyContent.value = ''
}

const submitReply = async (parentComment) => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  
  try {
    const formData = new FormData()
    formData.append('articleId', route.params.id)
    formData.append('content', replyContent.value.trim())
    formData.append('parentId', parentComment.id)
    
    const result = await post('/comment/add', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    if (result.code === 200) {
      ElMessage.success('回复成功')
      replyContent.value = ''
      replyTo.value = null
      getComments()
    }
  } catch (error) {
    ElMessage.error('回复失败')
  }
}

const cancelReply = () => {
  replyTo.value = null
  replyContent.value = ''
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  try {
    const formData = new FormData()
    formData.append('articleId', route.params.id)
    formData.append('content', newComment.value.trim())
    formData.append('parentId', '0')
    
    const result = await post('/comment/add', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    if (result.code === 200) {
      ElMessage.success('评论成功')
      newComment.value = ''
      getComments()
    }
  } catch (error) {
    ElMessage.error('评论失败')
  }
}

const isCurrentUser = (userId) => {
  return currentUser.value && currentUser.value.id === userId
}

onMounted(() => {
  const articleId = route.params.id
  if (articleId) {
    getArticleDetail(articleId)
  }
  getComments()
  /**确认删除用户评论为本人**/
  get('/user/getUserInfoFromComment').then(res => {
    if (res.code === 200) {
      currentUser.value = res.data
    }
  })
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
  margin-bottom: 40px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.article-header h1 {
  font-size: 28px;
  color: #303133;
  margin-bottom: 20px;
  text-align: center;
}

.article-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.author-avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.author-name {
  font-size: 16px;
  color: #303133;
  font-weight: 500;
}

.meta-info {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 14px;
}

.meta-info .el-icon {
  margin-right: 4px;
}

.article-content {
  line-height: 1.8;
  color: #606266;
  padding: 0 20px;
}

.content-wrapper {
  margin-bottom: 20px;
}

.cover-image {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  margin-bottom: 30px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.content {
  font-size: 16px;
  line-height: 1.8;
  color: #303133;
}

.like-button {
  text-align: center;
  margin: 40px 0;
}

.like-button .el-button {
  padding: 12px 30px;
  font-size: 16px;
}

.like-button .el-icon {
  margin-right: 6px;
}

.article-footer {
  margin-top: 30px;
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.liked {
  background-color: #f56c6c;
  border-color: #f56c6c;
}

.liked:hover {
  background-color: #f78989;
  border-color: #f78989;
}

:deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
}

:deep(p) {
  margin: 16px 0;
}

.comments-section {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.comments-title {
  font-size: 20px;
  color: #303133;
  margin-bottom: 20px;
}

.comment-form {
  margin-bottom: 30px;
}

.form-footer {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}

.comments-list {
  margin-top: 20px;
}

.comment-item {
  margin-bottom: 20px;
}

.comment-main {
  display: flex;
  gap: 12px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 8px;
}

.username {
  font-weight: 500;
  color: #303133;
  margin-right: 10px;
}

.time {
  color: #909399;
  font-size: 12px;
}

.comment-text {
  color: #606266;
  line-height: 1.6;
}

.comment-actions {
  margin-top: 8px;
  display: flex;
  gap: 16px;
}

.reply-form {
  margin: 10px 0 10px 52px;
}

.replies-list {
  margin-left: 52px;
  margin-top: 10px;
  margin-bottom: 20px;
  padding-left: 10px;
  border-left: 2px solid #e4e7ed;
  background-color: #fafafa;
  border-radius: 0 4px 4px 0;
  overflow: hidden; /* 确保子评论不会溢出 */
}

.reply-item {
  margin-top: 12px;
  padding: 12px 16px;
  border-top: 1px solid #f0f2f5;
  width: 100%; /* 确保回复项占满容器宽度 */
}

.reply-item:first-child {
  border-top: none;
  padding-top: 16px;
}

.reply-item:last-child {
  padding-bottom: 16px;
}

.reply-to {
  color: #409EFF;
  font-size: 12px;
  margin-right: 10px;
  position: relative;
}
.publish-time{
  margin-top: 5px;
}
.reply-to::after {
  content: "•";
  color: #c0c4cc;
  margin-left: 10px;
}

.comment-main.reply {
  padding-right: 16px;
}
.like-icon {
  width: 20px;
  height: 20px;
  fill: #f0ad4e;
  position: relative;
  /*vertical-align: middle;*/
  top: 5px;

}
.unlike-icon {
  width: 16px;
  height: 16px;
  fill: #f0ad4e;
  vertical-align: middle;

}
</style> 