<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h2>系统首页</h2>
    </div>
    
    <!-- 数据概览卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="data-header">
            <span class="title">总用户数</span>
            <el-icon class="icon"><User /></el-icon>
          </div>
          <div class="data-content">
            <div class="number">1,234</div>
            <div class="compare">
              较上月 <span class="up">+12.5%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="data-header">
            <span class="title">游记总数</span>
            <el-icon class="icon"><Document /></el-icon>
          </div>
          <div class="data-content">
            <div class="number">856</div>
            <div class="compare">
              较上月 <span class="up">+8.3%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="data-header">
            <span class="title">订单数量</span>
            <el-icon class="icon"><ShoppingCart /></el-icon>
          </div>
          <div class="data-content">
            <div class="number">328</div>
            <div class="compare">
              较上月 <span class="down">-2.1%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="data-header">
            <span class="title">总收入(元)</span>
            <el-icon class="icon"><Money /></el-icon>
          </div>
          <div class="data-content">
            <div class="number">45,678</div>
            <div class="compare">
              较上月 <span class="up">+15.7%</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>订单趋势</span>
              <el-radio-group v-model="orderTimeRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">全年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-content">
            <!-- 这里放订单趋势图表 -->
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>用户分布</span>
            </div>
          </template>
          <div class="chart-content">
            <!-- 这里放用户分布图表 -->
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'Dashboard',
  setup() {
    const orderTimeRange = ref('week')

    return {
      orderTimeRange
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.dashboard-header {
  margin-bottom: 24px;
}

.dashboard-header h2 {
  font-size: 24px;
  font-weight: 500;
  color: #1f2f3d;
  margin: 0;
}

.data-card {
  margin-bottom: 20px;
}

.data-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.data-header .title {
  font-size: 16px;
  color: #606266;
}

.data-header .icon {
  font-size: 24px;
  color: #409EFF;
}

.data-content .number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.data-content .compare {
  font-size: 14px;
  color: #909399;
}

.compare .up {
  color: #67C23A;
}

.compare .down {
  color: #F56C6C;
}

.chart-row {
  margin-top: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-header span {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.chart-content {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}
</style> 