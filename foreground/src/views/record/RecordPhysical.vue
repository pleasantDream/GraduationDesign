<script setup>

</script>

<template>

    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>体格测量</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" label-width="100px" size="large">
                    <el-form-item label="身高">
                        身高
                    </el-form-item>
                    <el-form-item label="体重" >
                        体重
                    </el-form-item>
                    <el-form-item label="bmi">
                        bmi指数
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped></style>