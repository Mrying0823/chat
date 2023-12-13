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
      <el-col :span="4" v-if="difficultySearch">
        <el-space direction="vertical" alignment="normal">
          <span>创建日期</span>
          <el-select
              v-model="difficulty"
              placeholder="请选择日期"
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
      <el-col :span="4" v-if="difficultySearch">
        <el-space direction="vertical" alignment="normal">
          <span>修改日期</span>
          <el-select
              v-model="difficulty"
              placeholder="请选择日期"
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
import {ref} from "vue";
import {useQuestionData} from "@/store/questionData";

// eslint-disable-next-line no-unused-vars
const props = defineProps(['subjectSearch', 'keywordSearch', 'difficultySearch']);

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

const keyword = ref("");

const subject = ref("");
</script>

<style scoped>
</style>