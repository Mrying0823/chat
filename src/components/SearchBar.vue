<template>
  <div style="padding-top: 8px;user-select: none">
    <el-row>
      <el-col :span="4" v-if="subjectSearch">
        <el-space direction="vertical" alignment="normal">
          <span>专题</span>
          <el-select
              v-model="subject"
              placeholder="请选择专题"
              clearable
          >
            <el-option
                v-for="subject in storeQuestion.subjectList"
                :key="subject.id"
                :label="subject.value"
                :value="subject.value"
                style="font-family: 'Source Code Pro Light',sans-serif"
            />
          </el-select>
        </el-space>
      </el-col>
      <el-col :span="4" v-if="keywordSearch">
        <el-space direction="vertical" alignment="normal">
          <span>题目</span>
        </el-space>
        <el-input
            v-model="keyword"
            placeholder="请输入关键词"
            style="width: 225px"
        />
      </el-col>
      <el-col :span="4" v-if="difficultySearch">
        <el-space direction="vertical" alignment="normal">
          <span>难度</span>
          <el-select
              v-model="difficulty"
              placeholder="请选择难度"
              clearable
          >
            <el-option
                v-for="difficulty in storeQuestion.difficultyList"
                :key="difficulty.id"
                :label="difficulty.value"
                :value="difficulty.value"
            />
          </el-select>
        </el-space>
      </el-col>
      <el-col :span="4" v-if="createdDateSearch">
        <el-space direction="vertical" alignment="normal">
          <span>创建日期</span>
          <ElConfigProvider :locale="zhCn">
            <el-date-picker
                v-model="createdDate"
                type="date"
                placeholder="请选择日期"
                size="default"
            />
          </ElConfigProvider>
        </el-space>
      </el-col>
      <el-col :span="4" v-if="updatedDateSearch">
        <el-space direction="vertical" alignment="normal">
          <span>修改日期</span>
          <ElConfigProvider :locale="zhCn">
            <el-date-picker
                v-model="updatedDate"
                type="date"
                placeholder="请选择日期"
                size="default"
            />
          </ElConfigProvider>
        </el-space>
      </el-col>
      <el-col :span="3" style="padding-top: 29px">
        <el-button
            ref="resetBtn"
            size="default"
            type="plain"
            @click="searchQuestion"
        >
          查询
        </el-button>
      </el-col>
      <el-col :span="1" style="padding-top: 29px" v-if="!admin">
        <el-button
            ref="resetBtn"
            size="default"
            type="plain"
            @click="createQuestion"
        >
          新建
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {useQuestionData} from "@/store/questionData";
// ElConfigProvider 组件
import {ElConfigProvider} from 'element-plus'
// 引入中文包
import zhCn from "element-plus/es/locale/lang/zh-cn";
import { ElMessage, ElMessageBox } from 'element-plus';
import router from "@/router";

// eslint-disable-next-line no-unused-vars
const props = defineProps(['subjectSearch', 'keywordSearch', 'difficultySearch', "createdDateSearch", "updatedDateSearch", "admin"]);

const storeQuestion = useQuestionData();

const resetBtn = ref();

const createQuestion = () => {
  // 通过 ref 使按钮失焦
  resetBtn.value.ref.blur();

  ElMessageBox.prompt("",'新建问题', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPlaceholder: "请输入你的问题",
    inputPattern: /\S/,
    inputErrorMessage: '问题不能为空',
  }).then((question) => {
    ElMessage({
    type: 'success',
    message: `跳转至编辑界面`,
    });

    router.push({
      path: "/question/editQuestion",
      query: {question: question.value}
    });
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '取消创建',
    });
  });
}

const searchQuestion = () => {
  // 通过 ref 使按钮失焦
  resetBtn.value.ref.blur();

  storeQuestion.subject = subject.value;
  storeQuestion.keyword = keyword.value;
  storeQuestion.difficulty = difficulty.value;
  storeQuestion.createdDate = createdDate.value;
  storeQuestion.updatedDate = updatedDate.value;
}

const difficulty = ref("");

const keyword = ref("");

const subject = ref("");

const createdDate = ref("");

const updatedDate = ref("");
</script>

<style>
span.el-date-picker__header-label {
  line-height: 42px;
}
</style>