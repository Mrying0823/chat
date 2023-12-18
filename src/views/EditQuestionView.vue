<template>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-question-edit-container': 'question-edit-container'" style="overflow: hidden">
    <el-header>
      <EditQuestionHeader></EditQuestionHeader>
    </el-header>
    <QuestionEditor
      :night-mode="this.$store.getters.getDarkMode ? '#18392F': '#fff'"
    />
  </el-container>
</template>

<script setup>
import EditQuestionHeader from "@/components/EditQuestionHeader";
import QuestionEditor from "@/components/QuestionEditor";
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