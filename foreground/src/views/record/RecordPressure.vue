<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>血压测量</span>
            </div>
        </template>
        <el-row>
            <!-- span总和为24则充满一行 -->
            <el-col :span="10">
                <el-form label-width="100px" size="large" :model="pressure">
                    <el-form-item label="性别">
                        <el-input v-model="pressure.gender"></el-input>
                    </el-form-item>
                    <el-form-item label="年龄">
                        <el-input v-model="pressure.age"></el-input>
                    </el-form-item>
                    <el-form-item label="高压">
                        <el-input v-model="pressure.highPressure"></el-input>
                    </el-form-item>
                    <el-form-item label="低压">
                        <el-input v-model="pressure.lowPressure"></el-input>
                    </el-form-item>
                    <el-form-item label="分析和建议">
                        <el-input v-model="pressure.result" type="textarea" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="pressureAdd()">新增</el-button>
                        <el-button type="primary" @click="pressureUpdate()" style="margin-left: 35px;">提交修改</el-button>
                        <el-button type="primary" @click="getHistory()" style="margin-left: 40px;">咨询</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
            <!-- 与左边的col相距32px -->
            <el-col :span="12" style="margin-left: 32px;">
                <div ref="echartsContainer" style="width: 500px; height: 380px;"></div>
            </el-col>
        </el-row>
        <!-- 咨询抽屉 -->
        <el-drawer v-model="visibleDrawer" title="体检咨询" direction="rtl" size="50%" :before-close="closeDrawer"
            style="border-radius: 15px;">
            <template style="margin-bottom:0px !important" #header>
                <h3>血压分析咨询</h3>
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
                                <p class="timeShow">{{ $filters.formatTime(message.createTime) }}</p>
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

</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue';
import { getPressureService, addPressureService, UpdatePressureService, chatService, getHistoryService, countService } from '@/api/record.js';

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
var startRow = 0;  // 默认分页查询五行,startRow为分页查询的开始行
const getHistory = async () => {
    let result = await getHistoryService(startRow, "血压分析");
    let restCount = await countService(startRow, "血压分析");
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
    let result = await chatService(inputText.value, "血压分析");
    inputText.value = "";
    messages.value.push({ text: result, isMe: false })
    /*添加滚轮自动滚到最底部的功能*/
}

// 查看更多
const getMore = async () => {
    histories.value.splice(0, histories.value.length);  //清空数组
    startRow += 5;
    let result = await getHistoryService(startRow, "血压分析");
    let restCount = await countService(startRow, "血压分析");
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
const pressureAdd = async () => {
    console.log(flag)
    if (flag == 1) {
        ElMessage.error("已有血压测量记录,请点击提交修改");
        return;
    }
    let result = await addPressureService(pressure.value);
    if (result.code == 0) {
        ElMessage.success('新增成功');
    } else {
        ElMessage.error(result);
    }
}
const pressureUpdate = async () => {
    if (flag == 0) {
        ElMessage.error("未有血压测量记录,请点击新增");
        return;
    }
    let result = await UpdatePressureService(pressure.value);
    if (result.code == 0) {
        ElMessage.success('修改成功');
    } else {
        ElMessage.error(result);
    }
}


// 保存血压测量数据
const pressure = ref({});

const getPressureData = async () => {
    try {
        const result = await getPressureService();
        pressure.value = result;
        initECharts(); // 数据加载完成后初始化 ECharts
    } catch (error) {
        console.error('Failed to fetch pressure data:', error);
    }
};

// 使用onMounted()钩子来在组件挂载后执行异步获取物理数据的操作
onMounted(getPressureData);

import * as echarts from 'echarts';

// 使用ref()函数创建一个响应式引用，用来引用echarts容器
const echartsContainer = ref(null);

// 初始化图表
const initECharts = () => {
    if (!pressure.value) {
        flag = 0;
        pressure.value = {
            gender: "",
            age: "",
            hightPressure: "",
            lowPressure: ""
        }
        return; // 如果身体测量数据还未加载完成，则不进行初始化操作
    }

    // 创建图表实例
    const myChart = echarts.init(echartsContainer.value);

    // 指定图表的配置项和数据
    const option = {
        title: {
            text: '血压测量'
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
            data: ["低压", "高压"]
        },
        yAxis: {
            type: 'value',
        },

        series: [{
            name: "较低",
            type: 'bar',
            data: [60, 90]
        },
        {
            name: "用户",
            type: 'bar',
            data: [pressure.value.lowPressure, pressure.value.highPressure]
        },
        {
            name: "较高",
            type: 'bar',
            data: [80, 120]
        }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
};
</script>

<style lang="scss" scoped>
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
