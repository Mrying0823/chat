<template>
  <el-dialog v-model="storeQuestion.confirmDialog" title="Shipping address" :destroy-on-close="true">
    <template #footer>
      <span>
        <el-button @click="storeQuestion.confirmDialog = false">Cancel</el-button>
        <el-button type="primary" @click="storeQuestion.confirmDialog = false">
          Confirm
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {useQuestionData} from "@/store/questionData";

// eslint-disable-next-line no-unused-vars
const storeQuestion = useQuestionData();
</script>

<style scoped>

</style>