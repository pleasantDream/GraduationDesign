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

// 新增用户温度测量
export const addTemperatureService = (TemperatureData) => {
    return request.post('/record/temperature', TemperatureData);
}

// 获取用户温度测量数据
export const getTemperatureService = () => {
    return request.get('/record/temperature/get');
}

// 更新用户体温测量数据
export const UpdateTemperatureService = (TemperatureData) => {
    return request.post('/record/temperature/update', TemperatureData);
}

// 新增用户尿液分析
export const addUrineService = (UrineData) => {
    return request.post('/record/urine', UrineData);
}

// 获取用户尿液分析数据
export const getUrineService = () => {
    return request.get('/record/urine/get');
}

// 更新用户体温测量数据
export const UpdateUrineService = (UrineData) => {
    return request.post('/record/urine/update', UrineData);
}

// 获取用户对应体检项目的咨询历史
export const getHistoryService = (startRow,item) => {
    const params = new URLSearchParams();
    params.append("startRow", startRow);
    params.append("item", item);
    return request.post('/record/history', params);
}

// 咨询
export const chatService = (question, item)=>{
    const params = new URLSearchParams();
    params.append("question", question);
    params.append("item", item);
    return request.post('/record/chat', params);
}

// 剩余记录条数
export const countService = (startRow, item)=>{
    const params = new URLSearchParams();
    params.append("startRow", startRow);
    params.append("item", item);
    return request.post('/record/count', params); 
}

// 获取体检记录
export const getRecordService = (item) =>{
    return request.get(`/record/getRecord?item=${item}`);
}