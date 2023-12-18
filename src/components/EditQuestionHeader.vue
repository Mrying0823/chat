<template>
  <el-row class="fake-header">
    <el-col style="flex: 1 1 auto;">
      <el-input v-if="true" v-model="question" :maxlength="40" placeholder="请输入标题" class="page-title-input" ></el-input>
    </el-col>
    <el-col style="flex: 0 0 180px;text-align: right;">
      <el-button ref="resetBtn" type="primary" @click="confirm = true">保存</el-button>
      <el-button ref="resetBtn" @click="createQuestionCancel" style="margin-right: 5px">取消</el-button>
    </el-col>
  </el-row>
  <el-dialog
      v-model="confirm"
      title="附加信息"
      :destroy-on-close="true"
      align-center
      width="15%"
      center
  >
    <el-row>
      <el-col :span="24">
        <el-space direction="vertical" alignment="normal">
          <span>阶段</span>
          <el-select
              v-model="stage"
              placeholder="请选择阶段"
              clearable
          >
            <el-option
                v-for="stage in storeQuestion.stageList"
                :key="stage.id"
                :label="stage.value"
                :value="stage.value"
            />
          </el-select>
        </el-space>
      </el-col>
      <el-col :span="24">
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
            />
          </el-select>
        </el-space>
      </el-col>
      <el-col :span="24">
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
    </el-row>
    <template #footer>
        <el-button @click="confirm = false">
          继续编辑
        </el-button>
        <el-button type="primary" @click="createQuestionSave">
          确定
        </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { useRouter } from "vue-router";
import {ref} from "vue";
import {ElMessageBox} from "element-plus";
import {useQuestionData} from "@/store/questionData";

const storeQuestion = useQuestionData();

const router = useRouter();

const resetBtn = ref();

const question = ref(router.currentRoute.value.query.question);
const stage = ref();
const subject = ref();
const difficulty = ref();

let confirm = ref(false);

const createQuestionSave = () => {
  resetBtn.value.ref.blur();
}

const createQuestionCancel = () => {
  resetBtn.value.ref.blur();

  ElMessageBox.confirm(
      '确定要取消编辑吗？您编辑的内容将不会被保存哦~',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '继续编辑',
        type: 'warning',
      }
  ).then(() => {
        router.back()
  }).catch(() => {

  });
}
</script>

<style scoped>
.left-action-box {
  display: flex;
  border-bottom: 1px solid #f1f1f1;
  user-select: none;
}

.left-action-box .title-time-box {
  flex: 1;
  padding: 5px 0 6px 10px;
  overflow: hidden;
}

.left-action-box .title-time-box .title {
  overflow: hidden;
  white-space: nowrap;
  -o-text-overflow: ellipsis;
  text-overflow: ellipsis;
  line-height: 28px;
}

.left-action-box .title-time-box .title .text {
  vertical-align: middle;
}

.left-action-box .title-time-box .time {
  font-size: 12px;
  overflow: hidden;
  white-space: nowrap;
  -o-text-overflow: ellipsis;
  text-overflow: ellipsis;
}
</style>

<style>
.fake-header {
  color: #333;
  height: 60px !important;
  line-height: 60px !important;
  border-bottom: 0.5px solid #eaeaea;
}

.fake-header .page-title-input {
  padding: 0 6px;
  width: 100%;
  font-size: 20px;
  border-bottom: 1px solid #ddd;
}

.fake-header .page-title-input .el-input__wrapper {
  border: 0;
  box-shadow: unset;
}

.page-edit-vue .page-content-editor ol {
  list-style: decimal;
}

.page-edit-vue .page-content-editor ul {
  list-style: disc;
}
</style>