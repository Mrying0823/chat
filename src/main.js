import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import Vuex from "vuex";
import store from './store'
// 引入element icon
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/assets/css/chatGPT.css'
import '@/assets/css/chatGPTSidebar.css'
import '@/assets/fonts/fontawesome-webfont.eot'
import '@/assets/fonts/fontawesome-webfont.svg'
import '@/assets/fonts/fontawesome-webfont.ttf'
import '@/assets/fonts/fontawesome-webfont.woff'
import '@/assets/css/navbar/font-awesome.min.css'
import '@/assets/css/navbar/nav.css'
import '@/assets/css/navbar/animations-3.css'
import '@/assets/css/navbar/normalize.css'
import '@/assets/css/navbar/main-versions.css'
import '@/assets/css/floatNavbar.css'
import particlesJS from "particles.js";

// 创建应用实例
const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 安装 Element Plus 插件
app.use(ElementPlus)
// 使用 Vue Router
app.use(router);
// 使用 Vuex
app.use(Vuex);
app.use(store);
app.use(particlesJS);
// 挂载到 DOM
app.mount('#app');