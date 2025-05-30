import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'
import { get, put, del, tip } from "./common/index";
const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 配置 axios 默认值
axios.defaults.baseURL = 'http://localhost:8080/xxt/api'

app.use(router)
app.use(ElementPlus)
app.mount('#app')
