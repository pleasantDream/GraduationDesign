import {createRouter, createWebHistory} from 'vue-router';
// 导入组件
import LoginVue from '../views/Login.vue';
import LayoutVue from '../views/Layout.vue';

// @符号通常被配置为指向项目中的一个特定目录，例如src, 在vite.config.js文件里配置。
import RecordBlood from '@/views/record/RecordBlood.vue';
import RecordPhysical from '@/views/record/RecordPhysical.vue';
import RecordPressure from '@/views/record/RecordPressure.vue';
import RecordTemperatureVue from '@/views/record/RecordTemperature.vue';
import RecordUrineVue from '@/views/record/RecordUrine.vue';
import UserInfoVue from '@/views/user/UserInfo.vue';
import UserEmailResetVue from '@/views/user/UserEmailReset.vue'
import UserPwdResetVue from '@/views/user/UserPwdReset.vue';

// 定义路由关系
const routes = [
    { path: '/login', component: LoginVue},
    {   // redirect, 重定向
        path: '/', component: LayoutVue, redirect: '/user/info',children:[
            { path: '/record/RecordBlood', component: RecordBlood },
            { path: '/record/RecordPhysical', component: RecordPhysical },
            { path: '/record/RecordPressure', component: RecordPressure },
            { path: '/record/RecordTemperature', component: RecordTemperatureVue },
            { path: '/record/RecordUrine', component: RecordUrineVue },
            { path: '/user/info', component: UserInfoVue },
            { path: '/user/emailReset', component: UserEmailResetVue },
            { path: '/user/PwdRest', component: UserPwdResetVue }
            
        ]
    }
]
// 创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})
// 导出路由
export default router