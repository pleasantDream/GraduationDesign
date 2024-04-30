<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'


// 获取用户详细信息
import { userInfoService } from '@/api/user.js';
import useUserInfoStore from '@/stores/userInfo.js'
const UserInfoStore = useUserInfoStore();
// 获取用户基本信息
const getUserInfo = async () => {
    let result = await userInfoService();
    // alert(result.data.id)
    // 数据存储到Pinia中
    UserInfoStore.setInfo(result.data);
}
getUserInfo();


// 条目被点击后调用的函数
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus'
import { useTokenStore } from '@/stores/token.js'
const router = useRouter();
const tokenStore = useTokenStore();
const handleCommand = (command) => {
    // 判断指令
    if (command === 'logout') {
        // 退出登录
        ElMessageBox.confirm(
            '确认退出登录?',
            '警告',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }
        ).then(async () => {
            // 清空Pinia中存储的token和个人信息
            tokenStore.removeToken()
            UserInfoStore.removeInfo()
            // 跳转到登录页面
            router.push('/login')
            ElMessage({
                type: 'success',
                message: '退出登录成功',
            })
        }).catch(() => {
                ElMessage({
                    type: 'info',
                    message: '退出登录取消',
                })
            })
    } else {
        // 路由
        router.push('/user/' + command); // command内容和路由表中的访问路径是一一对应的
    }

}
</script>

<template>
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <!-- <div><h3>智慧健康</h3></div> -->
            <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
                <el-sub-menu>
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>体检记录</span>
                    </template>
                    <el-menu-item index="/record/RecordPhysical">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>体格分析</span>
                    </el-menu-item>
                    <el-menu-item index="/record/RecordBlood">
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>血液分析</span>
                    </el-menu-item>
                    <el-menu-item index="/record/RecordPressure">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>血压分析</span>
                    </el-menu-item>
                    <el-menu-item index="/record/RecordTemperature">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>体温分析</span>
                    </el-menu-item>
                    <el-menu-item index="/record/RecordUrine">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>尿液分析</span>
                    </el-menu-item>
                </el-sub-menu>

                <el-menu-item index="/user/info">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>基本资料</span>
                </el-menu-item>

                <el-menu-item index="/user/emailReset">
                    <el-icon>
                        <EditPen />
                    </el-icon>
                    <span>重置邮箱</span>
                </el-menu-item>
                <el-menu-item index="/user/PwdReset">
                    <el-icon>
                        <EditPen />
                    </el-icon>
                    <span>重置密码</span>
                </el-menu-item>
                <el-menu-item index="/user/avatarReset">
                    <el-icon>
                        <EditPen />
                    </el-icon>
                    <span>更换头像</span>
                </el-menu-item>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div>智慧健康: <strong>{{ UserInfoStore.info.nickname }}</strong></div>
                <!-- 下拉菜单 -->
                <!-- command: 条目被点击后会触发，在事件函数上可以声明一个参数来接收条目对应的指令 -->
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="UserInfoStore.info.userPic ? UserInfoStore.info.userPic : avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatarReset" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="PwdReset" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>智慧健康 ©2024 Created by pleasantDream</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo.jpg') no-repeat center / 80px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>