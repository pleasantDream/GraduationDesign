<script setup>
import { EditPen } from '@element-plus/icons-vue'
import { ref } from 'vue'
import useUserInfoStore from '@/stores/userInfo.js'
import { userEmailUpdateService, sendValidation } from '@/api/user.js'
import { ElMessage } from 'element-plus';
const userInfoStore = useUserInfoStore();
const userInfo = ref({ ...userInfoStore.info })

const EmailResetData = ref({
    email:"",
    code:"",
    recode:""
})

const rules = {
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ],
    code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { min: 6, max: 6, message: '验证码为6位', trigger: 'blur' }
    ]
}

// 发送验证码
const sendCode = async () => {
    // 发送给用户的验证码，前端也接收
    let recode = await sendValidation(EmailResetData.value.email);
    EmailResetData.value.recode = recode;
}

const userEmailUpdate = async () => {
    let result = await userEmailUpdateService(EmailResetData.value);
    if (result.code == 0) {
        ElMessage.success('修改成功');
    } else {
        ElMessage.error(result);
    }
}

</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>重置邮箱</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="原有邮箱">
                        <el-input v-model="userInfo.email" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="重置邮箱" prop="email">
                        <el-row>
                            <el-col :span="20">
                                <el-input v-model="EmailResetData.email"></el-input>
                            </el-col>
                            <el-col :span="4">
                                <el-button class="button" type="primary" auto-insert-space
                                    v-on:click="sendCode()">
                                    发送验证码
                                </el-button>
                            </el-col>
                        </el-row>
                    </el-form-item>

                    <el-form-item prop="code">
                        <el-input :prefix-icon="EditPen" placeholder="请输入验证码" v-model="EmailResetData.code"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="userEmailUpdate">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<style scoped></style>
