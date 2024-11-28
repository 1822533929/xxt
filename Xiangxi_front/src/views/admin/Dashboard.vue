<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>总用户数</span>
            </div>
          </template>
          <div class="card-content">
            <el-statistic :value="1234">
              <template #title>
                <div style="display: inline-flex; align-items: center">
                  较昨日
                  <el-tag type="success" style="margin-left: 8px">+5%</el-tag>
                </div>
              </template>
            </el-statistic>
          </div>
        </el-card>
      </el-col>
      <!-- 可以添加更多统计卡片 -->
    </el-row>
  </div>
</template>

<script setup>
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.card-header {
  font-weight: bold;
}

.card-content {
  text-align: center;
}
</style> 