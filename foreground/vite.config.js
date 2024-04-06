import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  /*
    用代理服务的方式解决跨域请求
   */
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后台服务器所在源
        changeOrigin: true,  //true ,修改源
        rewrite: (path) => path.replace(/^\/api/, '') // '/api'替换为''
      }
    }
  }
})
