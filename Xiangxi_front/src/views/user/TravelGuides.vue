<template>
  <div class="travel-guides">
    <div class="page-header">
      <h2>旅游攻略</h2>
      <div class="header-right">
        <el-input
          v-model="searchQuery"
          placeholder="搜索攻略..."
          prefix-icon="el-icon-search"
          class="search-input"
        />
      </div>
    </div>

    <div class="guides-content">
      <el-row :gutter="20">
        <el-col :span="8" v-for="guide in guides" :key="guide.id">
          <el-card class="guide-card" :body-style="{ padding: '0px' }">
            <img :src="guide.image" class="guide-image">
            <div class="guide-info">
              <h3>{{ guide.title }}</h3>
              <p class="description">{{ guide.description }}</p>
              <div class="card-footer">
                <span class="time">{{ guide.createTime }}</span>
                <el-button type="text" @click="viewDetail(guide.id)">查看详情</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="pagination">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'TravelGuides',
  data() {
    return {
      searchQuery: '',
      guides: [
        {
          id: 1,
          title: '凤凰古城一日游攻略',
          description: '凤凰古城位于湖南省湘西土家族苗族自治州...',
          image: 'path/to/image1.jpg',
          createTime: '2024-03-20'
        },
        // 更多攻略数据...
      ],
      total: 100
    }
  },
  methods: {
    viewDetail(id) {
      console.log('查看攻略详情:', id)
    },
    handlePageChange(page) {
      console.log('切换到页码:', page)
    }
  }
}
</script>

<style scoped>
.travel-guides {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-right {
  display: flex;
  gap: 15px;
}

.search-input {
  width: 250px;
}

.guide-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.guide-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.guide-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.guide-info {
  padding: 15px;
}

.description {
  color: #666;
  margin: 10px 0;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.time {
  color: #999;
  font-size: 13px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 