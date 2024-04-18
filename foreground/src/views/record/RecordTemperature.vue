<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>体温测量</span>
            </div>
        </template>
        <el-row>
            <!-- span总和为24则充满一行 -->
            <el-col :span="10">
                <el-form label-width="100px" size="large" :model="temperature">
                    <el-form-item label="性别">
                        <el-input v-model="temperature.gender"></el-input>
                    </el-form-item>
                    <el-form-item label="年龄">
                        <el-input v-model="temperature.age"></el-input>
                    </el-form-item>
                    <el-form-item label="高压">
                        <el-input v-model="temperature.temperature"></el-input>
                    </el-form-item>
                    <el-form-item label="分析和建议">
                        <el-input v-model="temperature.result" type="textarea" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="physicalUpdate">提交修改</el-button>
                        <el-button type="primary" @click="userInfoUpdate" style="margin-left: 145px;">咨询</el-button>
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
import { getTemperatureService } from '@/api/record.js';

// 保存血压测量数据
const temperature = ref({});

const getTemperatureData = async () => {
    try {
        const result = await getTemperatureService();
        temperature.value = result;
        initECharts(); // 数据加载完成后初始化 ECharts
    } catch (error) {
        console.error('Failed to fetch temperature data:', error);
    }
};

// 使用onMounted()钩子来在组件挂载后执行异步获取物理数据的操作
onMounted(getTemperatureData);

import * as echarts from 'echarts';

// 使用ref()函数创建一个响应式引用，用来引用echarts容器
const echartsContainer = ref(null);

// 初始化图表
const initECharts = () => {
    if (!temperature.value) {
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
            data: ["体温"]
        },
        yAxis: {
            type: 'value',
        },

        series: [{
            name: "较低",
            type: 'bar',
            data: [36.1]
        },
        {
            name: "用户",
            type: 'bar',
            data: [temperature.value.temperature]
        },
        {
            name: "较高",
            type: 'bar',
            data: [37.2]
        }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
};
</script>
