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
                        <el-input v-model="blood.gender"></el-input>
                    </el-form-item>
                    <el-form-item label="年龄">
                        <el-input v-model="blood.age"></el-input>
                    </el-form-item>
                    <el-form-item label="血红蛋白(g/l)">
                        <el-input v-model="blood.hb"></el-input>
                    </el-form-item>
                    <el-form-item label="白细胞(10^9/l)">
                        <el-input v-model="blood.wbc"></el-input>
                    </el-form-item>
                    <el-form-item label="血小板(10^9/l)">
                        <el-input v-model="blood.plt"></el-input>
                    </el-form-item>
                    <el-form-item label="血糖(mg/dl)">
                        <el-input v-model="blood.glucose"></el-input>
                    </el-form-item>
                    <el-form-item label="分析和建议">
                        <el-input v-model="blood.result" type="textarea" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="bloodAdd()">新增</el-button>
                        <el-button type="primary" @click="bloodUpdate()" style="margin-left: 24px;">提交修改</el-button>
                        <el-button type="primary" @click="userInfoUpdate" style="margin-left: 34px;">咨询</el-button>
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
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue';
import { addBloodService, getBloodService, UpdateBloodService } from '@/api/record.js';

var flag = 1; // 为0表示数据库中没有该用户这个项目的体检数据,可以新增
const bloodAdd = async () => {
    console.log(flag)
    if (flag == 1) {
        ElMessage.error("已有体格测量记录,请点击提交修改");
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
        ElMessage.error("未有体格测量记录,请点击新增");
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
</script>
