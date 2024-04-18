// 导入request.js请求工具
import request from '../utils/request.js';

// 新增用户体格测量数据
export const addPhycialService = (physicalData) => {
    return request.post('/record/physical', physicalData);
}

// 获取用户体格测量数据
export const getPhycialService = ()=>{
    return request.get('/record/physical/get');
}

// 更新用户体格测量数据
export const UpdatePhycialService = (physicalData) => {
    return request.post('/record/physical/update', physicalData);
}


// 新增用户血压测量数据
export const addPressureService = (pressureData) => {
    return request.post('/record/pressure', pressureData);
}

// 获取用户血压测量数据
export const getPressureService = () => {
    return request.get('/record/pressure/get');
}
// 更新用户血压测量数据
export const UpdatePressureService = (pressureData) => {
    return request.post('/record/pressure/update', pressureData);
}


// 新增用户血液分析数据
export const addBloodService = (BloodData) => {
    return request.post('/record/blood', BloodData);
}

// 获取用户血液分析数据
export const getBloodService = () => {
    return request.get('/record/blood/get');
}

// 更新用户血液分析数据
export const UpdateBloodService = (BloodData) => {
    return request.post('/record/blood/update', BloodData);
}

// 获取用户血液分析数据
export const getTemperatureService = () => {
    return request.get('/record/temperature/get');
}

// 获取用户尿液分析数据
export const getUrineService = () => {
    return request.get('/record/urine/get');
}