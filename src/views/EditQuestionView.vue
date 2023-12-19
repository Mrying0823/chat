<template>
  <LoadingBar v-show="loadingDisplay.display"></LoadingBar>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-question-edit-container': 'question-edit-container'" style="overflow: hidden">
    <el-header>
      <EditQuestionHeader
        ref="editQuestionHeader"
        @saveCreateQuestion="saveCreateQuestion"
        @editQuestionSave="editQuestionSave"
        :create="isNewQuestion"
        :edit="!isNewQuestion"
      />
    </el-header>
    <QuestionEditor
      :night-mode="this.$store.getters.getDarkMode ? '#18392F': '#fff'"
      ref="questionEditor"
    />
  </el-container>
</template>

<script setup>
import EditQuestionHeader from "@/components/EditQuestionHeader";
import QuestionEditor from "@/components/QuestionEditor";
import LoadingBar from "@/components/LoadingBar";
import {ref} from "vue";
import {doPost} from "@/axios/httpRequest";
import {ElMessage} from "element-plus";
import {useLoadingDisplay} from "@/store/loadingDisplay";
import {useRouter} from "vue-router";
import {useQuestionData} from "@/store/questionData";

const loadingDisplay = useLoadingDisplay();
const editQuestionHeader = ref();
const questionEditor = ref();
const router = useRouter();

const isNewQuestion = ref(router.currentRoute.value.query.isNewQuestion);

const saveCreateQuestion = () => {
  doPost("/questionList/saveCreateQuestion",{
    question: editQuestionHeader.value.question,
    stage: editQuestionHeader.value.stage,
    subject: editQuestionHeader.value.subject,
    difficulty: editQuestionHeader.value.difficulty,
    content: questionEditor.value.markdownContent
  }).then(response => {
    if(response && response.data.code === 200) {
      loadingDisplay.display = false;

      ElMessage({
        type: "success",
        center: true,
        message: "保存成功"
      });

      router.back();
    }else {
      ElMessage({
        type: "warning",
        center: true,
        message: response.data.msg
      });
    }
  });
}

const storeQuestion = useQuestionData();

const editQuestionSave = () => {
  doPost("/questionList/saveEditQuestion",{
    id: storeQuestion.currentEditQuestion.id,
    question: editQuestionHeader.value.question,
    stage: editQuestionHeader.value.stage,
    subject: editQuestionHeader.value.subject,
    difficulty: editQuestionHeader.value.difficulty,
    content: questionEditor.value.markdownContent
  }).then(response => {
    if(response && response.data.code === 200) {
      loadingDisplay.display = false;

      ElMessage({
        type: "success",
        center: true,
        message: "修改成功"
      });

      router.back();
    }else {
      ElMessage({
        type: "warning",
        center: true,
        message: response.data.msg
      });
    }
  });
}
</script>

<style scoped>
.question-edit-container {
  width: 100%; /* 设置容器宽度为100% */
  font-family: "Source Code Pro Light", sans-serif;
  background: white;
}

.night-mode-question-edit-container {
  width: 100%; /* 设置容器宽度为100% */
  font-family: "Source Code Pro Light", sans-serif;
  color:#fff;
  background-color:#18392F;
}
</style>