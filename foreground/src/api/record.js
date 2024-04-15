// 导入request.js请求工具
import request from '../utils/request.js';

// 获取用户体格测量数据
export const getPhycialService = ()=>{
    return request.get('/record/physical/get');
}