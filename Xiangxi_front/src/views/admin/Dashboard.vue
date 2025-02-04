<template>
  <div class="dashboard">
    <!-- 数据统计卡片 -->
    <div class="stat-cards">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon">
            <el-icon :size="40" color="#409EFF"><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-title">攻略总数</div>
            <div class="stat-value">{{ stats.guideCount }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon">
            <el-icon :size="40" color="#67C23A"><Location /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-title">路线总数</div>
            <div class="stat-value">{{ stats.routeCount }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon">
            <el-icon :size="40" color="#E6A23C"><Goods /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-title">商品总数</div>
            <div class="stat-value">{{ stats.productCount }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon">
            <el-icon :size="40" color="#F56C6C"><List /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-title">订单总数</div>
            <div class="stat-value">{{ stats.orderCount }}</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <!-- 订单趋势图 -->
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="chart-header">
            <span>订单趋势</span>
            <el-radio-group v-model="orderChartType" size="small">
              <el-radio-button label="week">周</el-radio-button>
              <el-radio-button label="month">月</el-radio-button>
            </el-radio-group>
          </div>
        </template>
        <div class="chart" ref="orderChartRef"></div>
      </el-card>

      <!-- 商品销量排行 -->
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="chart-header">
            <span>商品销量排行</span>
          </div>
        </template>
        <div class="chart" ref="productChartRef"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from 'vue'
import { Document, Location, Goods, List } from '@element-plus/icons-vue'
import { get } from '@/common'
import * as echarts from 'echarts'

const stats = reactive({
  guideCount: 0,
  routeCount: 0,
  productCount: 0,
  orderCount: 0
})

const orderChartType = ref('week')
const orderChartRef = ref(null)
const productChartRef = ref(null)
let orderChart = null
let productChart = null

// 获取统计数据
const getStats = async () => {
  try {
    const result = await get('/stats/overview')
    if (result.code === 200) {
      Object.assign(stats, result.data)
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 获取订单趋势数据
const getOrderTrend = async () => {
  try {
    const result = await get(`/stats/orderTrend?type=${orderChartType.value}`)
    if (result.code === 200) {
      const data = result.data
      orderChart.setOption({
        xAxis: {
          data: data.map(item => item.date)
        },
        series: [{
          data: data.map(item => item.count)
        }]
      })
    }
  } catch (error) {
    console.error('获取订单趋势数据失败:', error)
  }
}

// 获取商品销量排行
const getProductRank = async () => {
  try {
    const result = await get('/stats/productRank')
    if (result.code === 200) {
      const data = result.data
      productChart.setOption({
        yAxis: {
          data: data.map(item => item.title)
        },
        series: [{
          data: data.map(item => item.sales)
        }]
      })
    }
  } catch (error) {
    console.error('获取商品销量排行数据失败:', error)
  }
}

// 初始化订单趋势图
const initOrderChart = () => {
  orderChart = echarts.init(orderChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: [120, 200, 150, 80, 70, 110, 130],
      type: 'line',
      smooth: true,
      areaStyle: {}
    }]
  }
  orderChart.setOption(option)
}

// 初始化商品销量图
const initProductChart = () => {
  productChart = echarts.init(productChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'value'
    },
    yAxis: {
      type: 'category',
      data: ['商品1', '商品2', '商品3', '商品4', '商品5']
    },
    series: [{
      type: 'bar',
      data: [120, 200, 150, 80, 70]
    }]
  }
  productChart.setOption(option)
}

onMounted(() => {
  getStats()
  initOrderChart()
  initProductChart()
  getOrderTrend()
  getProductRank()
  
  // 监听窗口大小变化
  window.addEventListener('resize', () => {
    orderChart?.resize()
    productChart?.resize()
  })
})

// 监听图表类型变化
watch(orderChartType, () => {
  getOrderTrend()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  width: 300px;
  height: 100px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.charts-container {
  display: flex;
  gap: 20px;
}

.chart-card {
  flex: 1;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart {
  height: 350px;
}
</style> 