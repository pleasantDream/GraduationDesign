// 导入request.js请求工具
import request from '../utils/request.js';

// 调用后台登录接口
export const userLoginService = (loginData) => {
    const params = new URLSearchParams();
    for (let key in loginData) {
        params.append(key, loginData[key]);
    }
    return request.post('/user/login', params);
}
// 调用后台发送验证码接口
export const sendValidation = (email) => {
    return request.get(`/user/register?email=${email}`);
}

// 调用后台注册接口
export const userRegisterService = (registerData) => {
    // 借助urlSearchParams完成传递
    // registerData为json格式, 而要求的格式为： x-www-form-urlencoded
    const params = new URLSearchParams();
    for (let key in registerData) {
        params.append(key, registerData[key]);
    }
    return request.post('/user/register', params);
}

// 获取用户信息
export const userInfoService = () => {
    return request.get('/user/userInfo')
}

// 修改用户信息
export const userInfoUpdateService = (userInfoData) => {
    return request.put('/user/info/update', userInfoData)
}

// 修改用户邮箱
export const userEmailUpdateService = (emailData) => {
    return request.patch('/user/email/update', emailData);
}

// 修改用户密码
export const userPasswordUpdateService = (pwdData) => {
    // alert(pwdData)
    return request.patch('/user/password/update', pwdData);
}

// 修改头像
export const userAvatarUpdateService = (avatarUrl) => {
    const params = new URLSearchParams();
    params.append('avatarUrl', avatarUrl)
    return request.patch('/user/avatar/updata', params)
}