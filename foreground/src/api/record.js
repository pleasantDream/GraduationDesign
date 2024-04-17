// 导入request.js请求工具
import request from '../utils/request.js';

// 获取用户体格测量数据
export const getPhycialService = ()=>{
    return request.get('/record/physical/get');
}

// 获取用户血压测量数据
export const getPressureService = () => {
    return request.get('/record/pressure/get');
}

// 获取用户血液分析数据
export const getBloodService = () => {
    return request.get('/record/blood/get');
}

// 获取用户血液分析数据
export const getTemperatureService = () => {
    return request.get('/record/temperature/get');
}

// 获取用户尿液分析数据
export const getUrineService = () => {
    return request.get('/record/urine/get');
}