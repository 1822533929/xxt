<template>
  <div class="not-found">
    <div class="content">
      <h1>404</h1>
      <h2>页面不存在</h2>
      <p>抱歉，您访问的页面不存在或已被移除</p>
      <el-button type="primary" @click="goBack">返回首页</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.push('/user/home')
}
</script>

<style scoped>
.not-found {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}

.content {
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

h1 {
  font-size: 80px;
  color: #409EFF;
  margin: 0;
  line-height: 1;
}

h2 {
  font-size: 24px;
  color: #303133;
  margin: 20px 0;
}

p {
  color: #606266;
  margin-bottom: 30px;
}

.el-button {
  padding: 12px 30px;
  font-size: 16px;
}
</style>
