<template>
    <div ref="echartsContainer" style="width: 600px; height: 400px;"></div>
</template>

<script setup>
import { ref,onMounted } from 'vue'

import usePhysicalStore from '@/stores/physical.js';
import { ElMessage } from 'element-plus';
const physicalStore = usePhysicalStore();
const physical = ref({ ...physicalStore.physical })

import * as echarts from 'echarts';

// 在setup()函数中，mounted()钩子不再需要，因为逻辑和视图都在一个地方了
// 使用ref()函数创建一个响应式引用，用来引用echarts容器
const echartsContainer = ref(null);

// 使用onMounted()函数来执行初始化图表的逻辑
onMounted(() => {
    // 初始化图表
    const myChart = echarts.init(echartsContainer.value);

    // 指定图表的配置项和数据
    const option = {
        title: {
            text: '体格测量'
        },
        tooltip: {
            trigger: 'axis',
        },
        legend: {
            data: ['用户', '标准'],
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
            data: ["身高", "体重", "BMI"]
        },
        yAxis: {
            type: 'value',
        },
        series: [{
            name: '用户',
            type: 'bar',
            data: [physical.value.height * 100, physical.value.weight * 2, physical.value.bmi]
        },
        {
            name: '标准',
            type: 'bar',
            data: [170, 58, 22]
        }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
});
</script>
