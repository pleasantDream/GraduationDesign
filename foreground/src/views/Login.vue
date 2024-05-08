<script setup>
import { User, Lock, Connection, EditPen} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { userLoginService, sendValidation, userRegisterService, userLoginByEmailService, resetPwdService } from '../api/user.js'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/token.js'
const router = useRouter();
const tokenStore = useTokenStore();

// 控制注册、账号密码登录、邮箱登录和重置密码的显示，默认显示账号密码登录
const isLogin = ref(true)
const isRegister = ref(false)
const isLoginByEmail = ref(false)
const isResetPwd = ref(false)
// 重置密码数据模型
const resetPwdData = ref({
    newPassword:"",
    rePassword: "",
    email:"",
    code:"",
    reCode:""    
})
// 重置密码发送验证码
const sendCode3 = async (email) => {
    let result = await sendValidation(email);
    resetPwdData.value.reCode = result;
}
// 重置密码
const resetPwd = async()=>{
    let result = await resetPwdService(resetPwdData.value);
    if (result.code == 0) {
        ElMessage.success('重置密码成功');
        // 显示账号密码登录表单
        isResetPwd.value=false;
        isLogin.value=true;
    } else {
        ElMessage.error(result);
    }
}
// 账号密码登录数据模型
const LoginData = ref({
    username: "",
    password: "",
})
// 邮箱登录数据模型
const LoginByEmailData = ref({
    email:"",
    reEmail:"",
    code:"",
    reCode:""
})
// 登录表单校验规则
const LoginRules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5-16位非空字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5-16位非空字符', trigger: 'blur' }
    ]
}
// 账号密码登录函数
const login = async () => {
    let result = await userLoginService(LoginData.value);
    if(result.code == 0){
        ElMessage.success('登录成功');
        // 将登录成功得到的token存储到pinia中
        tokenStore.setToken(result.data);
        // 借助路由跳转到首页
        router.push('/');
    }else{
        ElMessage.error(result);
    }
}
// 发送验证码
const sendCode2 = async (email) => {
    // 发送给用户的验证码，前端也接收。
    // 还有发送这个验证码时用的邮箱，防止用户后面修改邮箱
    LoginByEmailData.value.reEmail = email;
    let result = await sendValidation(email);
    LoginByEmailData.value.reCode = result;
}
// 邮箱登录函数
const loginByEamil = async()=>{
    let result = await userLoginByEmailService(LoginByEmailData.value);
    if (result.code == 0) {
        ElMessage.success('登录成功');
        // 将登录成功得到的token存储到pinia中
        tokenStore.setToken(result.data);
        // 借助路由跳转到首页
        router.push('/');
    } else {
        ElMessage.error(result);
    }
}

// 注册数据模型
const RegisterData = ref({
    username: "",
    password: "",
    rePassword: "",
    email: "",
    reEmail:"",
    code: "",
    reCode: ""
})
// 校验密码的函数
const checkRePassword = (rule, value, callback) => {
    if (value == '') {
        callback(new Error('请确认密码'));
        // password为响应式数据，要加.value
    } else if (value !== RegisterData.value.password) {    // !== 严格不等于，类型也要相同
        callback(new Error('两次密码不一致'));
    } else {
        callback();
    }
}
// QQ邮箱校验
const validate_QQEmail = (rule, value, callback) => {
    if (!value) {
        callback(new Error('邮箱不能为空'));
    } else if (!/^[1-9]\d{4,10}@qq\.com$/.test(value)) {
        callback(new Error('请输入正确的 QQ 邮箱格式'));
    } else {
        callback();
    }
}
// 注册表单校验规则
const RegisterRules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5-16位非空字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5-16位非空字符', trigger: 'blur' }
    ],
    rePassword: [
        { validator: checkRePassword, trigger: 'blur' },
    ],
    email: [
        { validator: validate_QQEmail, trigger: 'blur' }
    ],
    code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { min: 6, max: 6, message: '验证码为6位', trigger: 'blur' }
    ]
}
// 发送验证码
const sendCode = async (email) => {
    // 发送给用户的验证码，前端也接收。
    // 还有发送这个验证码时用的邮箱，防止用户后面修改邮箱
    RegisterData.value.reEmail = email;
    let result = await sendValidation(email);
    RegisterData.value.reCode = result;
}
// 注册函数
const register = async () => {
    let result = await userRegisterService(RegisterData.value);
    if (result.code == 0) {
        ElMessage.success('注册成功');
    } else {
        ElMessage.error(result);
    }
    // 借助路由跳转到登录页面
    // router.push('/');
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="RegisterData"
                :rules="RegisterRules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="RegisterData.username" </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="RegisterData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入确认密码"
                        v-model="RegisterData.rePassword" </el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input :prefix-icon="Connection" placeholder="请输入邮箱" v-model="RegisterData.email"></el-input>
                    <el-button class="button" type="primary" auto-insert-space
                        v-on:click="sendCode(RegisterData.email)">
                        发送验证码
                    </el-button>
                </el-form-item>
                <el-form-item prop="code">
                    <el-input :prefix-icon="EditPen" placeholder="请输入验证码" v-model="RegisterData.code"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space v-on:click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isLogin=true; isRegister=false">
                        账号密码登录 →
                    </el-link>
                    <el-link type="info" :underline="false" @click="isLoginByEmail=true; isRegister=false"
                        style="margin-left: auto;">
                        邮箱登录 →
                    </el-link>
                </el-form-item>
            </el-form>

            <!-- 账号密码登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isLogin" :model="LoginData" :rules="LoginRules">
                <el-form-item>
                    <h1>账号密码登录</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="LoginData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="LoginData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-link type="primary" :underline="false"
                            @click="isResetPwd=true; isLogin=false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space v-on:click="login">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister=true; isLogin=false">
                        注册 →
                    </el-link>
                    <el-link type="info" :underline="false" @click="isLoginByEmail=true; isLogin=false"
                        style="margin-left: auto;">
                        邮箱登录 →
                    </el-link>
                </el-form-item>
            </el-form>

            <!-- 邮箱登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isLoginByEmail" :model="LoginByEmailData"
                :rules="RegisterRules">
                <el-form-item>
                    <h1>邮箱登录</h1>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input :prefix-icon="Connection" placeholder="请输入邮箱" v-model="LoginByEmailData.email"></el-input>
                    <el-button class="button" type="primary" auto-insert-space
                        v-on:click="sendCode2(LoginByEmailData.email)">
                        发送验证码
                    </el-button>
                </el-form-item>
                <el-form-item prop="code">
                    <el-input :prefix-icon="EditPen" placeholder="请输入验证码" v-model="LoginByEmailData.code"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space v-on:click="loginByEamil">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister=true; isLoginByEmail=false">
                        注册 →
                    </el-link>
                    <el-link type="info" :underline="false" @click="isLogin=true; isLoginByEmail=false"
                        style="margin-left: auto;">
                        账号密码登录 →
                    </el-link>
                </el-form-item>
            </el-form>

            <!-- 重置密码表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isResetPwd" :model="resetPwdData"
                :rules="RegisterRules">
                <el-form-item>
                    <h1>重置密码</h1>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input :prefix-icon="Connection" placeholder="请输入邮箱" v-model="resetPwdData.email"></el-input>
                    <el-button class="button" type="primary" auto-insert-space
                        v-on:click="sendCode3(resetPwdData.email)">
                        发送验证码
                    </el-button>
                </el-form-item>
                <el-form-item prop="code">
                    <el-input :prefix-icon="EditPen" placeholder="请输入验证码" v-model="resetPwdData.code"></el-input>
                </el-form-item>
                <el-form-item prop="newPassword">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="新密码"
                        v-model="resetPwdData.newPassword"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="确认密码"
                        v-model="resetPwdData.rePassword"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="resetPwd">重置密码</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isLogin = true; isResetPwd = false">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>

        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    // .bg {
    //     background: url('@/assets/logo2.png') no-repeat 60% center / 240px auto,
    //         url('@/assets/login_bg.jpg') no-repeat center / cover;
    //     border-radius: 0 20px 20px 0;
    // }
    .bg { 
        background: url('@/assets/logo.jpg') no-repeat 60% center / 240px auto;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>