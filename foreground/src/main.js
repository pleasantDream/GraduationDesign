import './assets/main.scss'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router/index.js'

import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'

// import ChartBlock from '@/components/ChartsBlock.vue'

const pinia = createPinia()
const persist = createPersistedState()
pinia.use(persist)
const app = createApp(App);

// 全局过滤器,格式化时间显示    已经用js文件代替了
// app.config.globalProperties.$filters = {
//     formatTime(value) {
//         if (!value) return '';
//         const date = new Date(value);
//         const year = date.getFullYear();
//         const month = date.getMonth() + 1;
//         const day = date.getDate();
//         const hours = date.getHours();
//         const minutes = date.getMinutes();
//         const seconds = date.getSeconds();
//         return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
//     }
// };

// app.use(ChartBlock)
app.use(router)
app.use(pinia)
app.use(ElementPlus);   // 使用饿了么组件
app.mount('#app')  // 挂载


