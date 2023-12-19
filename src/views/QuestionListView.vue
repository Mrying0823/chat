<template>
  <NavBar/>
  <LoadingBar v-show="loadingDisplay.display"></LoadingBar>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-chat-container': 'chat-container'" style="overflow: hidden">
    <el-header>
      <SearchBar
          :subject-search="true"
          :keyword-search="true"
          :difficulty-search="true"
          :admin="true"
      />
    </el-header>
    <el-main>
      <QuestionDetails/>
      <QuestionList
          :table-data="tableData"
          :total-rows="totalRows"
          :edit="false"
      />
    </el-main>
  </el-container>
</template>

<script setup>
import NavBar from "@/components/NavBar";
import QuestionList from "@/components/QuestionList";
import SearchBar from "@/components/SearchBar";
import LoadingBar from "@/components/LoadingBar";
import {useLoadingDisplay} from "@/store/loadingDisplay";
import QuestionDetails from "@/components/QuestionDetails";
import {onMounted, ref, watchEffect} from "vue";
import {useQuestionData} from "@/store/questionData";
import {doPost, toGet} from "@/axios/httpRequest";
import {ElMessage} from "element-plus";

const loadingDisplay = useLoadingDisplay();

const storeQuestion = useQuestionData();

let tableData = ref([]);
let totalRows = ref(0);

function doPageChangePost() {
  doPost("/questionList/queryQuestionList",{pageNo: storeQuestion.pageNo,
                                                      pageSize: storeQuestion.pageSize,
                                                      subject: storeQuestion.subject,
                                                      keyword: storeQuestion.keyword,
                                                      difficulty: storeQuestion.difficulty
  }).then(response => {
    if(response && response.data.code === 200) {
      tableData.value = response.data.map.questionList;
      totalRows.value = response.data.map.totalRows;
      setTimeout(() => {
        loadingDisplay.display = false;
      },1000);
    }else {
      ElMessage({
        type: "warning",
        center: true,
        message: response.data.msg
      });
    }
  });
}

function doInitPost() {
  toGet("/questionList/forPublicQuestionList").then(response => {
    if(response && response.data.code === 200) {
      storeQuestion.subjectList = response.data.map.subjectList;
      storeQuestion.difficultyList = response.data.map.difficultyList;
      storeQuestion.stageList = response.data.map.stageList;
    }else {
      ElMessage({
        type: "warning",
        center: true,
        message: response.data.msg
      });
    }
  });
}

onMounted(() => {
  loadingDisplay.display = true;

  doPageChangePost();

  watchEffect(() => {
    if(storeQuestion) {
      doPageChangePost();
    }
  });

  doInitPost();
});
</script>

<style scoped>
</style>