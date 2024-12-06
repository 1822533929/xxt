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
  </div>
</template>

<script>
import fenghuangImage from '@/images/carousel/fenghuang.jpg'
import dehangImage from '@/images/carousel/dehang.jpg'
import zhangjiajieImage from '@/images/carousel/zhangjiajie.jpg'
export default {
  name: 'UserHome',
  data() {
    return {
      carouselItems: [
        {
          id: 1,
          title: '凤凰古城',
          description: '湘西最著名的旅游景点',
          imageUrl: fenghuangImage  // 需要替换为实际图片路径
        },
        {
          id: 2,
          title: '德夯苗寨',
          description: '体验原真的苗族文化',
          imageUrl: dehangImage  // 需要替换为实际图片路径
        },
        {
          id: 3,
          title: '张家界',
          description: '世界自然遗产',
          imageUrl: zhangjiajieImage  // 需要替换为实际图片路径
        }
      ]
    }
  }
}
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
</style> 