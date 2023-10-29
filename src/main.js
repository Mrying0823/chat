import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 引入element icon
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/assets/css/chatGPT.css'
import '@/assets/css/chatGPTSidebar.css'

// 创建应用实例
const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 安装 Element Plus 插件
app.use(ElementPlus)
// 使用 Vue Router
app.use(router);
// 挂载到 DOM
app.mount('#app');