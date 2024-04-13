import './assets/main.scss'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router/index.js'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
const pinia = createPinia()
const persist = createPersistedState()
pinia.use(persist)
const app = createApp(App);
app.use(router)
app.use(pinia)
app.use(ElementPlus);   // 使用饿了么组件
app.mount('#app')  // 挂载
