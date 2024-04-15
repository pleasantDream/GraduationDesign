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


// 获取用户体格测量数据
import { getPhycialService } from '@/api/record.js'
import usePhysicalStore from '@/stores/physical.js';
const physicalStore = usePhysicalStore();
const getPhysical = async () => {
    let result = await getPhycialService();
    // alert(result.id)
    // 数据存储到Pinia中
    physicalStore.setPhysical(result);
}
getPhysical();


</script>

<template>
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
                <el-sub-menu>
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>体检分析</span>
                    </template>
                    <el-menu-item index="/record/RecordPhysical">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>体格测量</span>
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
                        <span>血压测量</span>
                    </el-menu-item>
                    <el-menu-item index="/record/RecordTemperature">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>体温测量</span>
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
                <el-menu-item index="/user/PwdRest">
                    <el-icon>
                        <EditPen />
                    </el-icon>
                    <span>重置密码</span>
                </el-menu-item>
                <!-- <el-sub-menu>
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item>
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item>
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <el-menu-item>
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu> -->
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div><strong>智慧健康</strong></div>
                <el-dropdown placement="bottom-end">
                    <span class="el-dropdown__box">
                        <el-avatar :src="avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="profile" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="password" :icon="EditPen">重置密码</el-dropdown-item>
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
            background: url('@/assets/logo.png') no-repeat center / 120px auto;
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