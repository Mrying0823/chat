<template>
  <div style="padding-top: 8px;user-select: none">
    <el-row>
      <el-col :span="4">
        <el-space direction="vertical" alignment="normal">
          <span>专题</span>
          <el-select
              v-model="subject"
              placeholder="请选择专题"
              clearable
          >
            <el-option
                v-for="subject in subjectList"
                :key="subject.id"
                :label="subject.value"
                :value="subject.value"
                style="font-family: 'Source Code Pro Light',sans-serif"
            />
          </el-select>
        </el-space>
      </el-col>
      <el-col :span="4">
        <el-space direction="vertical" alignment="normal">
          <span>题目</span>
        </el-space>
        <el-input
            v-model="keyword"
            placeholder="请输入关键词"
            style="width: 225px"
        />
      </el-col>
      <el-col :span="4">
        <el-space direction="vertical" alignment="normal">
          <span>难度</span>
          <el-select
              v-model="difficulty"
              placeholder="请选择难度"
              clearable
          >
            <el-option
                v-for="difficulty in difficultyList"
                :key="difficulty.id"
                :label="difficulty.value"
                :value="difficulty.value"
            />
          </el-select>
        </el-space>
      </el-col>
      <el-col :span="4" style="padding-top: 29px">
        <el-button
            ref="resetBtn"
            size="default"
            type="plain"
            @click="searchQuestion"
        >
          查询
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {toGet} from "@/axios/httpRequest";
import {ElMessage} from "element-plus";
import {useQuestionData} from "@/store/questionData";

const storeQuestion = useQuestionData();

const resetBtn = ref();

const searchQuestion = () => {
  // 通过 ref 使按钮失焦
  resetBtn.value.ref.blur()

  storeQuestion.subject = subject.value;
  storeQuestion.keyword = keyword.value;
  storeQuestion.difficulty = difficulty.value;
}

const difficulty = ref("");

let difficultyList = ref([]);

const keyword = ref("");

const subject = ref("");

let subjectList = ref([]);

onMounted(() => {
  toGet("/questionList/forPublicQuestionList").then(response => {
    if(response && response.data.code === 200) {
      subjectList.value = response.data.map.subjectList;
      difficultyList.value = response.data.map.difficultyList;
    }else {
      ElMessage({
        type: "warning",
        center: true,
        message: response.data.msg
      });
    }
  });
});
</script>

<style scoped>
</style>