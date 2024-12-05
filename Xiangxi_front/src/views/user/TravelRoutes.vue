<template>
  <div class="travel-routes">
    <div class="page-header">
      <h2>旅游路线</h2>
      <div class="header-right">
        <el-select v-model="filterDays" placeholder="行程天数">
          <el-option label="1-3天" value="1-3" />
          <el-option label="4-7天" value="4-7" />
          <el-option label="7天以上" value="7+" />
        </el-select>
        <el-input
          v-model="searchQuery"
          placeholder="搜索路线..."
          prefix-icon="el-icon-search"
          class="search-input"
        />
      </div>
    </div>

    <div class="routes-content">
      <el-table :data="routes" style="width: 100%">
        <el-table-column prop="name" label="路线名称" />
        <el-table-column prop="days" label="行程天数" width="100" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="scope">
            <span class="price">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="路线描述" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewRoute(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
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
  name: 'TravelRoutes',
  data() {
    return {
      searchQuery: '',
      filterDays: '',
      routes: [
        {
          id: 1,
          name: '湘西风光三日游',
          days: 3,
          price: 1299,
          description: '游览凤凰古城、德夯苗寨、芙蓉镇等景点...'
        },
        // 更多路线数据...
      ],
      total: 100
    }
  },
  methods: {
    viewRoute(route) {
      console.log('查看路线详情:', route)
    },
    handlePageChange(page) {
      console.log('切换到页码:', page)
    }
  }
}
</script>

<style scoped>
.travel-routes {
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

.price {
  color: #f56c6c;
  font-weight: bold;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 