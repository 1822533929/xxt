<template>
  <div class="travel-products">
    <div class="header">
      <h2>旅游商品管理</h2>
      <el-button type="primary" @click="handleAdd">新增商品</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="category" label="分类" width="120">
        <template #default="{ row }">
          <el-tag>{{ row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }">
          <span>¥{{ row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column prop="sales" label="销量" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '在售' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      :total="total"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      @current-change="handlePageChange"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
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
import { Plus } from '@element-plus/icons-vue'

// 表格数据
const tableData = ref([
  {
    id: 1,
    name: '三亚椰子',
    category: '特产',
    price: 29.90,
    stock: 100,
    sales: 50,
    status: 1
  }
])
const loading = ref(false)
const total = ref(100)
const currentPage = ref(1)
const pageSize = ref(10)

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
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.product-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.product-uploader:hover {
  border-color: #409EFF;
}

.product-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.preview-image {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}
</style> 