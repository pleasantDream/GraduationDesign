<template>
    <el-card class="page-container" v-if="isTest">
        <template #header>
            <div class="header">
                <span>血液分析</span>
                <el-button type="primary" styel="margin-left: auto;"
                    @click="isTest = false; isRecord = true; goRecord()">体检记录</el-button>
            </div>
        </template>
        <el-row>
            <!-- span总和为24则充满一行 -->
            <el-col :span="10">
                <el-form label-width="120px" size="large" :model="blood" :rules="rules">
                    <el-form-item label="性别" prop="gender">
                        <el-input v-model="blood.gender"></el-input>
                    </el-form-item>
                    <el-form-item label="年龄" prop="age">
                        <el-input v-model="blood.age"></el-input>
                    </el-form-item>
                    <el-form-item label="血红蛋白(g/l)" prop="hb">
                        <el-input v-model="blood.hb"></el-input>
                    </el-form-item>
                    <el-form-item label="白细胞(10^11/l)" prop="wbc">
                        <el-input v-model="blood.wbc"></el-input>
                    </el-form-item>
                    <el-form-item label="血小板(10^12/l)" prop="plt">
                        <el-input v-model="blood.plt"></el-input>
                    </el-form-item>
                    <el-form-item label="血糖(mg/dl)" prop="glucose">
                        <el-input v-model="blood.glucose"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="bloodAdd()">新增</el-button>
                        <el-button type="primary" @click="bloodUpdate()" style="margin-left: 24px;">提交修改</el-button>
                        <el-button type="primary" @click="getHistory()" style="margin-left: 30px;">咨询</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
            <!-- 与左边的col相距32px -->
            <el-col :span="12" style="margin-left: 32px;">
                <div ref="echartsContainer" style="width: 500px; height: 450px;"></div>
            </el-col>
            <el-form-item label="分析和建议">
                <div class="message-text mine">{{ blood.result }}</div>
            </el-form-item>
        </el-row>
        <!-- 咨询抽屉 -->
        <el-drawer v-model="visibleDrawer" title="体检咨询" direction="rtl" size="50%" :before-close="closeDrawer"
            style="border-radius: 15px;">
            <template style="margin-bottom:0px !important" #header>
                <h3>血液分析咨询</h3>
            </template>
            <template #default>
                <div>
                    <hr>
                </div>
                <!-- 显示聊天消息的容器 -->
                <div class="message-container">
                    <div :model="count" class="more-container">
                        <button v-if="count > 0" class="more-button" @click="getMore()">查看更多</button>
                    </div>
                    <div v-for="message in messages" class="message">
                        <div v-if="message.isMe" style="display: flex; justify-content: flex-end; /* 将内容靠右对齐 */">
                            <div>
                                <p class="timeShow">{{ formatTime(message.createTime) }}</p>
                                <div class="message-text mine"> {{ message.text }}</div>
                            </div>
                            <span>
                                <img v-if="imgUrl" :src="imgUrl" class="avatar-user">
                                <img v-else :src="avatar" class="avatar-user">
                            </span>
                        </div>
                        <div v-else style="display: flex;">
                            <span>
                                <img src="@/assets/logo.jpg" class="avatar-doctor">
                            </span>
                            <div class="message-text">{{ message.text }}</div>
                        </div>
                    </div>
                </div>
            </template>
            <template #footer>
                <el-row>
                    <el-col :span="21">
                        <el-input v-model="inputText" type="text" placeholder="输入消息"
                            @keyup.enter="sendMessage()" /><!--回车事件-->
                    </el-col>
                    <el-col :span="3" style="margin-left: -17px;">
                        <el-button type="primary" @click="sendMessage()">发送</el-button>
                    </el-col>
                </el-row>
            </template>
        </el-drawer>
    </el-card>
    <el-card class="page-container" v-if="isRecord">
        <template #header>
            <div class="header">
                <span>体检记录</span>
                <el-button type="primary" style="margin-left: auto;"
                    @click="isRecord = false; isTest = true; goBack();">血压分析</el-button>
            </div>
        </template>
        <el-row>
            <el-col :span="12.3">
                <el-table :data="records" stripe style="width: 100%">
                    <el-table-column prop="time" label="时间" width="100" />
                    <el-table-column prop="hb" label="hb" width="100" />
                    <el-table-column prop="wbc" label="wbc" width="100" />
                    <el-table-column prop="plt" label="plt" width="100" />
                    <el-table-column prop="glucose" label="glucose" width="100" />
                </el-table>
            </el-col>
            <el-col :span="9">
                <div ref="echartsContainer2" style="width: 500px; height: 380px;"></div>
            </el-col>
        </el-row>
    </el-card>

</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue';
import { formatTime } from '@/utils/formatTime.js';
import { addBloodService, getBloodService, UpdateBloodService, getHistoryService, chatService, countService, getRecordService } from '@/api/record.js';
/*体检记录 */
const records = ref([]);
const goBack = async () => {
    try {
        records.value.splice(0, records.value.length);  //清空数组
        let result = await getBloodService();
        blood.value = result;
        initECharts();
    } catch (error) {
        console.error('Failed to fetch physical data:', error);
    }
}
const goRecord = async () => {
    try {
        let result = await getRecordService("tb_blood");
        result.data.forEach(item => {
            item.time = formatTime(item.time);
            records.value.push(item);
        });
        initECharts2();
    } catch (error) {
        console.error('Failed to fetch physical data:', error);
    }
}

/*体检分析和体检记录页面显示*/
const isTest = ref(true)
const isRecord = ref(false)


/* 咨询抽屉 */
const visibleDrawer = ref(false)
const histories = ref([])   // 数组
const messages = ref([])
const inputText = ref("");
const count = ref(""); // 剩余记录条数
//获取用户头像地址
import useUserInfoStore from '@/stores/userInfo.js'
const UserInfoStore = useUserInfoStore();
const imgUrl = ref(UserInfoStore.info.userPic)
// 默认分页查询五行,startRow为分页查询的开始行
var startRow = 0;  
const getHistory = async () => {
    let result = await getHistoryService(startRow, "血液分析");
    let restCount = await countService(startRow, "血液分析");
    count.value = restCount;
    result.forEach(item => {
        histories.value.push(item);
    });
    for (let i = histories.value.length - 1; i >= 0; i--) {
        messages.value.push({
            text: histories.value[i].question,
            createTime: histories.value[i].createTime, isMe: true
        });
        messages.value.push({ text: histories.value[i].answer, isMe: false });
    }
    histories.value.splice(0, histories.value.length);  //清空数组

    /*添加滚轮自动滚到最底部的功能*/

    visibleDrawer.value = true;
}
// 抽屉关闭前的回调
const closeDrawer = (done) => {
    histories.value.splice(0, histories.value.length);  //清空数组
    messages.value.splice(0, messages.value.length);
    startRow = 0;
    done();  // 允许关闭
}
// 发送消息
const sendMessage = async () => {
    messages.value.push({ text: inputText.value, isMe: true, createTime: new Date() });
    let result = await chatService(inputText.value, "血液分析");
    inputText.value = "";
    messages.value.push({ text: result, isMe: false })
    /*添加滚轮自动滚到最底部的功能*/
}

// 查看更多
const getMore = async () => {
    histories.value.splice(0, histories.value.length);  //清空数组
    startRow += 5;
    let result = await getHistoryService(startRow, "血液分析");
    let restCount = await countService(startRow, "血液分析");
    count.value = restCount;
    result.forEach(item => {
        histories.value.unshift(item);
    });
    for (let i = histories.value.length - 1; i >= 0; i--) {
        // unshift 倒插入第一个位置
        messages.value.unshift({ text: histories.value[i].answer, isMe: false });
        messages.value.unshift({
            text: histories.value[i].question,
            createTime: histories.value[i].createTime, isMe: true
        });

    }
}

/*新增和更新数据 */
var flag = 1; // 为0表示数据库中没有该用户这个项目的体检数据,可以新增
const bloodAdd = async () => {
    console.log(flag)
    if (flag == 1) {
        ElMessage.error("已有血液分析记录,请点击提交修改");
        return;
    }
    let result = await addBloodService(blood.value);
    if (result.code == 0) {
        ElMessage.success('新增成功');
    } else {
        ElMessage.error(result);
    }
}
const bloodUpdate = async () => {
    if (flag == 0) {
        ElMessage.error("未有血液分析记录,请点击新增");
        return;
    }
    let result = await UpdateBloodService(blood.value);
    if (result.code == 0) {
        ElMessage.success('修改成功');
    } else {
        ElMessage.error(result);
    }
}


// 创建响应式引用来保存测量数据
const blood = ref({});
// 前端参数校验规则
const rules = {
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
                if (value === '' || (Number.isInteger(Number(value)) && parseInt(value) >= 0 && parseInt(value) <= 200)) {
                    callback();
                } else {
                    callback(new Error('年龄必须是大于等于0小于等于200的整数'));
                }
            },
            trigger: 'blur'
        }
    ], 
    hb: [
        {
            validator: (rule, value, callback) => {
                if (value === '' || (parseFloat(value) >= 0)) {
                    callback();
                } else {
                    callback(new Error('必须为非负数'));
                }
            },
            trigger: 'blur'
        }
    ],
    wbc: [
        {
            validator: (rule, value, callback) => {
                if (value === '' || (parseFloat(value) >= 0)) {
                    callback();
                } else {
                    callback(new Error('必须为非负数'));
                }
            },
            trigger: 'blur'
        }
    ], plt: [
        {
            validator: (rule, value, callback) => {
                if (value === '' || (parseFloat(value) >= 0)) {
                    callback();
                } else {
                    callback(new Error('必须为非负数'));
                }
            },
            trigger: 'blur'
        }
    ], glucose: [
        {
            validator: (rule, value, callback) => {
                if (value === '' || (parseFloat(value) >= 0)) {
                    callback();
                } else {
                    callback(new Error('必须为非负数'));
                }
            },
            trigger: 'blur'
        }
    ],

}

// 异步获取测量数据
const getBloodData = async () => {
    try {
        const result = await getBloodService();
        blood.value = result;
        initECharts(); // 数据加载完成后初始化 ECharts
        
    } catch (error) {
        console.error('Failed to fetch blood data:', error);
    }
};

// 使用onMounted()钩子来在组件挂载后执行异步获取物理数据的操作
onMounted(getBloodData);

import * as echarts from 'echarts';

// 使用ref()函数创建一个响应式引用，用来引用echarts容器
const echartsContainer = ref(null);

// 初始化图表
const initECharts = () => {
    if (!blood.value) {
        flag = 0;
        blood.value = {
            gender:"",
            aga:"",
            hb:"",
            wbc:"",
            plt:"",
            glucose:""
        }
        return; // 如果身体测量数据还未加载完成，则不进行初始化操作
    }

    // 由于加载顺序原因,要放在这个位置
    const Data = {
        lowHb: 121,
        highHb: 151,
        lowWbc: 4.0,
        highWbc: 11.0,
        lowPlt: 150,
        hightPlt: 400,
        lowGlucose: 70,
        highGlucose: 100
    }
    if (blood.value.gender == '男') {
        Data.lowHb = 135,
        Data.highHb = 175
    }

    // 创建图表实例
    const myChart = echarts.init(echartsContainer.value);

    // 指定图表的配置项和数据
    const option = {
        title: {
            text: '血液分析'
        },
        tooltip: {
            trigger: 'axis',
        },
        legend: {
            data: ['用户', '较低', '较高'],
        },
        toolbox: {
            show: true,
            feature: {
                mark: { show: true },
                dataView: { show: true, readOnly: false },
                restore: { show: true },
                saveAsImage: { show: true },
            },
        },
        calaulable: true,
        xAxis: {
            type: 'category',
            data: ["血红蛋白", "白细胞", "血小板","血糖"]
        },
        yAxis: {
            type: 'value',
        },

        series: [{
            name: "较低",
            type: 'bar',
            data: [Data.lowHb, Data.lowWbc,Data.lowPlt, Data.lowGlucose]
        },
        {
            name: "用户",
            type: 'bar',
            data: [blood.value.hb, blood.value.wbc, blood.value.plt, blood.value.glucose]
        },
        {
            name: "较高",
            type: 'bar',
            data: [Data.highHb, Data.highWbc, Data.hightPlt, Data.highGlucose ]
        }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
};
// 体检记录的折线图
const echartsContainer2 = ref(null);
const initECharts2 = () => {
    let myChart = echarts.init(echartsContainer2.value);
    const option = {
        title: {
            text: '血液测量记录'
        },
        tooltip: {
            trigger: 'axis',
        },
        legend: {
            data: ['hb', 'wbc', 'plt', 'glucose']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },

        toolbox: {
            show: true,
            feature: {
                mark: { show: true },
                dataView: { show: true, readOnly: false },
                restore: { show: true },
                saveAsImage: { show: true },
            },
        },
        calaulable: true,
        xAxis: {
            type: 'category',
            data: records.value.map(item => item.time), // 使用 map 方法动态生成横坐标点
            axisLabel: {
                interval: 0,
                // 文字换行
                formatter: function (value) {
                    let res = ""; // 拼接加\n返回的类目项
                    let maxLength = 6; // 每项显示文字个数  数字设置几，就一行显示几个文字
                    let valLength = value.length; // X轴上的文字个数
                    let rowN = Math.ceil(valLength / maxLength); // 需要换行的行数
                    // 换行的行数大于1,
                    if (rowN > 1) {
                        for (let i = 0; i < rowN; i++) {
                            let temp = ""; //每次截取的字符串
                            let start = i * maxLength; //开始截取的位置
                            let end = start + maxLength; //结束截取的位置
                            temp = value.substring(start, end) + "\n";
                            res += temp; //拼接字符串
                        }
                        return res;
                    } else {
                        return value;
                    }
                },
            },
        },
        yAxis: {
            type: 'value',
        },

        series: [
            {
                name: 'hb',
                type: 'line',
                data: records.value.map(item => item.hb), 
            },
            {
                name: 'wbc',
                type: 'line',
                data: records.value.map(item => item.wbc), 
            },
            {
                name: 'plt',
                type: 'line',
                data: records.value.map(item => item.plt),
            },
            {
                name: 'glucose',
                type: 'line',
                data: records.value.map(item => item.glucose),
            },
        ]
    };
    myChart.setOption(option);
};


</script>

<style lang="scss" scoped>
.header {
    display: flex;
    justify-content: space-between;
}

.message-container {
    margin-bottom: 10px;
}

.message {
    padding: 5px;
    margin-bottom: 5px;
    font-size: .83rem;
}

.message-text {
    padding: 10px;
    border-radius: 9px;
    background-color: #f4f6f8;
    line-height: 1.625;
}

.mine {
    background-color: #d2f9d1;
}

.timeShow {
    color: #b7bdc6;
    font-size: .75rem;
    line-height: 0.1rem;
    text-align: right; // 靠右显示
}

.avatar-user {
    max-width: 35px;
    max-height: 35px;
    border-radius: 50%; // 圆角
    margin-top: 3px;
    margin-left: 5px
}

.avatar-doctor {
    max-width: 35px;
    max-height: 35px;
    border-radius: 50%;
    margin-right: 5px
}

.more-container {
    display: flex;
    justify-content: center;
    /* 水平居中 */
}

.more-button {
    /* 去掉边框 */
    border: none;
    /* 背景透明 */
    background-color: transparent;
    /* 鼠标指针样式 */
    cursor: pointer;
    /* 继承父元素文字颜色 */
    color: inherit;
}

.more-button:hover {
    /* 鼠标悬停时变蓝 */
    color: blue;
}

// 自定义element plus 中的el-drawer组件中的头部样式
:deep(.el-drawer__header) {
    margin-bottom: -12px;
}
</style>
