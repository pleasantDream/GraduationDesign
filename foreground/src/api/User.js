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