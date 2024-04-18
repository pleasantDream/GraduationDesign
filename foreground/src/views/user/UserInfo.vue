<script setup>
import { ref } from 'vue'
import useUserInfoStore from '@/stores/userInfo.js'
import { userInfoUpdateService } from '@/api/user.js'
import { ElMessage } from 'element-plus';
const userInfoStore = useUserInfoStore();
const userInfo = ref({ ...userInfoStore.info })

const rules = {
    nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '昵称必须是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    name: [
        {  
            pattern: /^\S{0,15}$/,       // 百度搜索中国最长姓名为13个字符
            message: '姓名必须小于15个字符',
            trigger: 'blur'
        }
    ],
    gender: [
        {
            validator: (rule, value, callback) => {
                if (value !== '男' && value !== '女') {
                    callback(new Error('性别只能为"男"或"女"'));
                } else {
                    callback();
                }
            },
            trigger: 'blur'
        }
    ],
    age: [
        {
            validator: (rule, value, callback) => {
                if (value === '' || (Number.isInteger(Number(value)) && parseInt(value) > 0 && parseInt(value) < 200)) {
                    callback();
                } else {
                    callback(new Error('年龄必须是大于0小于200的整数'));
                }
            },
            trigger: 'blur'
        }
    ]
}
// 修改用户信息
const userInfoUpdate = async () => {
    // 调用接口
    let result = await userInfoUpdateService(userInfo.value);
    if (result.code == 0) {
        ElMessage.success('修改成功');
    } else {
        ElMessage.error(result);
    }
    // 修改Pinia中的个人信息
    userInfoStore.setInfo(userInfo.value);
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="nickname">
                        <el-input v-model="userInfo.nickname"></el-input>
                    </el-form-item>
                    <el-form-item label="姓名" prop="name">
                        <el-input v-model="userInfo.name"></el-input>
                    </el-form-item>
                    <el-form-item label="性别" prop="gender">
                        <el-input v-model="userInfo.gender"></el-input>
                    </el-form-item>
                    <el-form-item label="年龄" prop="age">
                        <el-input v-model="userInfo.age"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="userInfoUpdate">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped></style>