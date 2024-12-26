<template>
  <div class="travel-products">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchForm.keyword"
        placeholder="请输入商品名称"
        class="search-input"
      />
      <div class="search-buttons">
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="left-buttons">
        <el-button type="primary" @click="handleAdd">新增商品</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="!selectedRows.length">
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 表格 -->
    <el-table 
      :data="tableData" 
      style="width: 100%" 
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="title" label="名称" min-width="120" />
      <el-table-column label="图片" width="120">
        <template #default="{ row }">
          <el-image 
            :src="getImageUrl(row.cover)" 
            fit="cover"
            style="width: 80px; height: 60px; border-radius: 4px;"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="descr" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="money" label="价格" width="100">
        <template #default="{ row }">
          <span class="price">¥{{ row.money }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="inventory" label="库存" width="80" />
      <el-table-column prop="time" label="发布日期" width="120" />
      <el-table-column label="特色" width="120">
        <template #default="{ row }">
          <div class="tags-column">
            <el-tag 
              v-for="tag in row.tags" 
              :key="tag"
              size="small"
              class="tag-item"
            >
              {{ tag }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="详细" width="80">
        <template #default="{ row }">
          <el-button link type="primary" @click="viewDetail(row)">
            查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      :total="total"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="700px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category">
            <el-option label="纪念品" value="纪念品" />
            <el-option label="特产" value="特产" />
            <el-option label="工艺品" value="工艺品" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number 
            v-model="form.price" 
            :min="0" 
            :precision="2" 
            :step="10"
          />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            class="product-uploader"
            action="#"
            :auto-upload="false"
            :show-file-list="false"
          >
            <img v-if="form.imageUrl" :src="form.imageUrl" class="preview-image" />
            <el-icon v-else class="product-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input type="textarea" v-model="form.description" rows="4" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="在售" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture, Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchForm = reactive({
  keyword: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const selectedRows = ref([])

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return import.meta.env.VITE_API_BASE_URL + url
}

// 搜索方法
const handleSearch = () => {
  // 实现搜索逻辑
}

const resetSearch = () => {
  searchForm.keyword = ''
  // 重置后刷新数据
}

// 选择行
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 批量删除
const handleBatchDelete = () => {
  if (!selectedRows.value.length) return
  ElMessageBox.confirm('确定要删除选中的商品吗？', '提示', {
    type: 'warning'
  }).then(() => {
    // 实现批量删除逻辑
  })
}

// 查看详情
const viewDetail = (row) => {
  router.push(`/user/travel-detail/${row.id}`)
}

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增商品')
const form = reactive({
  name: '',
  category: '',
  price: 0,
  stock: 0,
  imageUrl: '',
  description: '',
  status: 1
})

// 方法
const handleAdd = () => {
  dialogTitle.value = '新增商品'
  Object.assign(form, {
    name: '',
    category: '',
    price: 0,
    stock: 0,
    imageUrl: '',
    description: '',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑商品'
  Object.assign(form, {
    name: row.name,
    category: row.category,
    price: row.price,
    stock: row.stock,
    imageUrl: row.imageUrl || '',
    description: row.description || '',
    status: row.status
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  })
}

const handleSubmit = () => {
  ElMessage.success(dialogTitle.value === '新增商品' ? '添加成功' : '更新成功')
  dialogVisible.value = false
}

const handlePageChange = (page) => {
  currentPage.value = page
  // 这里添加获取数据的逻辑
}
</script>

<style scoped>
.travel-products {
  padding: 20px;
  background-color: #f5f7fa;
}

.search-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.search-input {
  width: 300px;
}

.search-buttons {
  display: flex;
  gap: 10px;
}

.operation-bar {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.left-buttons {
  display: flex;
  gap: 10px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
  background-color: #fff;
}

.tags-column {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.tag-item {
  margin: 2px 0;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  background-color: #fff;
  padding: 15px 20px;
  border-radius: 8px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
}
</style> 