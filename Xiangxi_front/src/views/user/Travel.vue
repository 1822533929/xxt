<template>
  <div class="travel">
    <div class="page-header">
      <h2>去旅游</h2>
    </div>

    <div class="travel-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="热门景点" name="spots">
          <el-row :gutter="20">
            <el-col :span="6" v-for="spot in spots" :key="spot.id">
              <el-card class="spot-card" :body-style="{ padding: '0px' }">
                <img :src="spot.image" class="spot-image">
                <div class="spot-info">
                  <h3>{{ spot.name }}</h3>
                  <p class="price">¥{{ spot.price }}起</p>
                  <el-button type="primary" @click="bookTicket(spot)">立即预订</el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="特色体验" name="experiences">
          <el-row :gutter="20">
            <el-col :span="8" v-for="exp in experiences" :key="exp.id">
              <el-card class="experience-card">
                <img :src="exp.image" class="experience-image">
                <div class="experience-info">
                  <h3>{{ exp.title }}</h3>
                  <p class="description">{{ exp.description }}</p>
                  <div class="card-footer">
                    <span class="price">¥{{ exp.price }}起</span>
                    <el-button type="primary" size="small" @click="bookExperience(exp)">预订体验</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Travel',
  data() {
    return {
      activeTab: 'spots',
      spots: [
        {
          id: 1,
          name: '凤凰古城',
          price: 148,
          image: 'path/to/image1.jpg'
        },
        // 更多景点数据...
      ],
      experiences: [
        {
          id: 1,
          title: '苗族特色民俗体验',
          description: '体验苗族传统服饰制作、银饰打造...',
          price: 299,
          image: 'path/to/exp1.jpg'
        },
        // 更多体验数据...
      ]
    }
  },
  methods: {
    bookTicket(spot) {
      console.log('预订景点:', spot)
    },
    bookExperience(exp) {
      console.log('预订体验:', exp)
    }
  }
}
</script>

<style scoped>
.travel {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.spot-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.spot-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.spot-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.spot-info {
  padding: 15px;
  text-align: center;
}

.experience-card {
  margin-bottom: 20px;
}

.experience-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.experience-info {
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

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}
</style> 