<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { userFeedbackDeleteService,userFeedbackAddService, userFeedbackgetService, userFeedbackUpdateService } from '@/api/user.js'
import { QuillEditor } from '@vueup/vue-quill'
import { formatTime } from '@/utils/formatTime.js';
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore();
// 意见反馈表单
const feedbackData = ref({
    "category":"",
    "feedbackImg": "",
    "content":"",
    "id":""
})
// 提交意见反馈
const addFeedback = async () => {
    // 去除 <p> 标签
    feedbackData.value.content = feedbackData.value.content.replace(/^<p>|<\/p>$/g, '');
    let result = await userFeedbackAddService(feedbackData.value);
    if(result.code == 0){
        ElMessage.success('提交成功');
    }else{
        ElMessage.error('提交失败');
    }
    // // 刷新当前列表
    // articleList()
}

const feedbackList = ref([]);
//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数
const state = ref('')
//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    getFeedback('all')
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    getFeedback('all')
}
const categoryData = ref('');
// 获取反馈列表数据
const getFeedback = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        category: categoryData.value ? categoryData.value :'all',
        state: state.value ? state.value : null
    }
    feedbackList.value.splice(0, feedbackList.value.length);  //清空数组
    let result = await userFeedbackgetService(params);
    // 渲染视图
    total.value = result.data.total;
    result.data.items.forEach(item => {
        item.createTime = formatTime(item.createTime);
        feedbackList.value.unshift(item);
    });

}
getFeedback();
//控制抽屉是否显示
const visibleDrawer = ref(false)
// 上传成功的回调函数
const uploadSuccess = (result) => {
    feedbackData.value.feedbackImg = result.data;
    console.log(result.data);
}

// 定义数据, 展示抽屉标题
const Drawertitle = ref('');

// 添加反馈之前清空数据模型中的数据
const clearFeedback = () => {
    feedbackData.value.category = "";
    feedbackData.value.feedbackImg = "";
    feedbackData.value.content = ""; 
}

// 修改反馈
const updateFeedback = async () => {
    // 去除 <p> 标签
    feedbackData.value.content = feedbackData.value.content.replace(/^<p>|<\/p>$/g, '');
    // 调用接口
    let result = await userFeedbackUpdateService(feedbackData.value);
    if (result.code == 0) {
        ElMessage.success('修改成功');
        // 隐藏抽屉
        visibleDrawer.value = false;
        // 刷新当前列表
        getFeedback()
    } else {
        ElMessage.error('修改失败');
    }
}
// 修改反馈之前获取要修改的反馈数据
const showDrawer = (row) => {
    feedbackData.value.category = row.category;
    feedbackData.value.feedbackImg = row.feedbackImg;
    feedbackData.value.content = row.content;
    feedbackData.value.id = row.id
    visibleDrawer.value = true;
}
// 删除反馈
const articleDelete = async (row) => {
    ElMessageBox.confirm(
        '确认删除该反馈?',
        '警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            // 调用接口
            let result = await userFeedbackDeleteService(row.id);
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            // 刷新列表
            getFeedback();
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '删除取消',
            })
        })
}


</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>意见反馈</span>
                <div class="extra">
                    <el-button type="primary"
                        @click="Drawertitle = '添加反馈', clearFeedback(), visibleDrawer = true">添加反馈</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="反馈类型">
                <el-select placeholder="请选择" v-model="categoryData" style="width: 110px;">
                    <el-option label="问题报告" value="问题报告"></el-option>
                    <el-option label="功能建议" value="功能建议"></el-option>
                    <el-option label="项目建议" value="项目建议"></el-option>
                    <el-option label="页面改进" value="页面改进"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="处理状态">
                <el-select placeholder="请选择" v-model="state" style="width: 110px;">
                    <el-option label="未查看" value="未查看"></el-option>
                    <el-option label="未处理" value="未处理"></el-option>
                    <el-option label="已处理" value="已处理"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="getFeedback(categoryData)">搜索</el-button>
                <el-button @click="categoryData = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 反馈列表 -->
        <el-table :data="feedbackList" style="width: 100%">
            <el-table-column label="反馈类型" prop="category"></el-table-column>
            <el-table-column label="反馈时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary"
                        @click="Drawertitle = '修改反馈', showDrawer(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="articleDelete(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
        <!-- 抽屉 -->
        <el-drawer v-model="visibleDrawer" :title="Drawertitle" direction="rtl" size="50%">
            <!-- 添加或修改反馈表单 -->
            <el-form :model="feedbackData" label-width="100px">
                <el-form-item label="反馈类型">
                    <el-select placeholder="请选择" v-model="feedbackData.category">
                        <el-option label="问题报告" value="问题报告"></el-option>
                        <el-option label="功能建议" value="功能建议"></el-option>
                        <el-option label="项目建议" value="项目建议"></el-option>
                        <el-option label="页面改进" value="页面改进"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="截图(非必填)">
                    <!--
                        auto-upload: 设置是否自动上传
                        action: 设置服务器接口路径
                        name: 设置上传的文件字段名
                        headers: 设置上传的请求头
                        on-success: 设置上传成功的回调函数
                      -->
                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false" action="/api/upload"
                        name="file" :headers="{ 'Authorization': tokenStore.token }" :on-success="uploadSuccess">
                        <img v-if="feedbackData.feedbackImg" :src="feedbackData.feedbackImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="具体内容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="feedbackData.content" contentType="html">
                        </quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary"
                        @click="Drawertitle == '添加反馈' ? addFeedback() : updateFeedback()">提交反馈</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>