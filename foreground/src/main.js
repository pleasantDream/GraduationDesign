import './assets/main.scss'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import App from './App.vue'


const app = createApp(App);
app.use(ElementPlus);   // 使用饿了么组件
app.mount('#app')  // 挂载
