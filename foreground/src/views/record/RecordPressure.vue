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
                        <el-button type="primary" @click="userInfoUpdate" style="margin-left: 40px;">咨询</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
            <!-- 与左边的col相距32px -->
            <el-col :span="12" style="margin-left: 32px;">
                <div ref="echartsContainer" style="width: 500px; height: 380px;"></div>
            </el-col>
        </el-row>
    </el-card>

</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue';
import { getPressureService, addPressureService, UpdatePressureService } from '@/api/record.js';

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
