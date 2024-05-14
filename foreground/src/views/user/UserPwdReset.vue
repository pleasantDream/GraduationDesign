<script setup>
import { ref } from 'vue'

// 修改密码的数据模型
const pwdModel = ref({
    "oldPwd": '',
    "newPwd": '',
    "rePwd": ''
})

const rules = {
    oldPwd: [
        { required: true, message: '请输入旧密码', trigger: 'blur' },
    ],
    newPwd: [
        {
            required: true,
            message: '请输入新密码',
            trigger: 'blur'
        },
        {
            pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,16}$/,
            message: '密码必须包含数字、大写字母、小写字母,且长度为5到16位',
            trigger: 'blur'
        }
    ],
    rePwd: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        {
            pattern: /^\S{5,16}$/,
            message: '密码必须是5-16位的非空字符串',
            trigger: 'blur'
        },
        {
            validator: (rule, value, callback) => {
                if (!value) {
                    callback(new Error('请确认新密码'));
                } else if (pwdModel.value.rePwd !== pwdModel.value.newPwd) {
                    callback(new Error('确认密码不同'));
                } else {
                    callback();
                }
            },
            trigger: 'blur'
        }
    ]
}


// 修改用户密码
import useUserInfoStore from '@/stores/userInfo.js'
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore();
const UserInfoStore = useUserInfoStore();
import { useRouter } from 'vue-router';
const router = useRouter();
import { userPasswordUpdateService } from '@/api/user.js'
import { ElMessage } from 'element-plus';
const userPwdUpdate = async () => {
    let result = await userPasswordUpdateService(pwdModel.value);
    if(result.code == 0){
        ElMessage.success('修改成功');
        // 清空Pinia中的数据
        tokenStore.removeToken()
        UserInfoStore.removeInfo()
        // 跳转到登录页面
        router.push('/login')
    }else{
        ElMessage.error(result);
    }
 

}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>修改密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="pwdModel" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="旧密码" prop="oldPwd">
                        <el-input v-model="pwdModel.oldPwd" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPwd">
                        <el-input v-model="pwdModel.newPwd" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="rePwd">
                        <el-input v-model="pwdModel.rePwd" type="password"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="userPwdUpdate">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>