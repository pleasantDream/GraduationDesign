//定制请求的实例
// axios对原生的Ajax进行了封装，简化书写，加速开发
import axios from 'axios';
import { useTokenStore } from '@/stores/token.js';
import { ElMessage } from 'element-plus'
//定义一个变量,记录公共的前缀  ,  baseURL
const baseURL = '/api';
const instance = axios.create({baseURL})

// 添加请求拦截器
instance.interceptors.request.use(
    (config) => {
        // 请求前的回调, 即在发送请求前做的事
        // 添加token(JWT令牌)
        const tokenStore = useTokenStore();
        // 判断有没有token
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token;
        }
        return config;
    },
    (err) => {
        // 请求错误的回调
        Promise.reject(err);
    }
)

// 由于模块加载顺序, 不能使用下面的方法来导入 router
// import { useRouter } from 'vue-router';
// const router = useRouter();
import router from '@/router';
//添加响应拦截器
instance.interceptors.response.use(
    result=>{
        return result.data;
    },
    err=>{
        // 响应状态码为401表示未登录(后端设置的),提示请登录并跳转到登录页面
        if (err.response.status === 401) {
            ElMessage.error('请先登录')
            router.push('/login')
        } else {
            ElMessage.error('服务异常')
        }
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default instance;