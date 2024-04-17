<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>血液分析</span>
            </div>
        </template>
        <el-row>
            <!-- span总和为24则充满一行 -->
            <el-col :span="10">
                <el-form label-width="120px" size="large" :model="physical">
                    <el-form-item label="性别">
                        <el-input v-model="urine.gender"></el-input>
                    </el-form-item>
                    <el-form-item label="年龄">
                        <el-input v-model="urine.age"></el-input>
                    </el-form-item>
                    <el-form-item label="尿液比重">
                        <el-input v-model="urine.sg"></el-input>
                    </el-form-item>
                    <el-form-item label="尿液ph值">
                        <el-input v-model="urine.ph"></el-input>
                    </el-form-item>
                    <el-form-item label="蛋白质(mg/dl)">
                        <el-input v-model="urine.protein"></el-input>
                    </el-form-item>
                    <el-form-item label="血细胞脂酶(u/l)">
                        <el-input v-model="urine.le"></el-input>
                    </el-form-item>
                    <el-form-item label="分析和建议">
                        <el-input v-model="urine.result" type="textarea" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="physicalUpdate">提交修改</el-button>
                        <el-button type="primary" @click="userInfoUpdate" style="margin-left: 126px;">咨询</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
            <!-- 与左边的col相距32px -->
            <el-col :span="12" style="margin-left: 32px;">
                <div ref="echartsContainer" style="width: 500px; height: 450px;"></div>
            </el-col>
        </el-row>
    </el-card>

</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getUrineService } from '@/api/record.js';

// 创建响应式引用来保存体格测量数据
const urine = ref({});

// 异步获取体格测量数据
const getUrineData = async () => {
    try {
        const result = await getUrineService();
        urine.value = result;
        initECharts(); // 数据加载完成后初始化 ECharts

    } catch (error) {
        console.error('Failed to fetch blood data:', error);
    }
};

// 使用onMounted()钩子来在组件挂载后执行异步获取物理数据的操作
onMounted(getUrineData);

import * as echarts from 'echarts';

// 使用ref()函数创建一个响应式引用，用来引用echarts容器
const echartsContainer = ref(null);

// 初始化图表
const initECharts = () => {
    if (!urine.value) {
        return; // 如果身体测量数据还未加载完成，则不进行初始化操作
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
            data: ["尿液比重", "尿液ph值", "蛋白质","白细胞酯酶"]
        },
        yAxis: {
            type: 'value',
        },

        series: [{
            name: "较低",
            type: 'bar',
            data: [1.005, 4.6, null,null, ]
        },
        {
            name: "用户",
            type: 'bar',
            data: [urine.value.sg, urine.value.ph, urine.value.protein, urine.value.le]
        },
        {
            name: "较高",
            type: 'bar',
            data: [1.030,8.0,150,15]
        }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
};
</script>
