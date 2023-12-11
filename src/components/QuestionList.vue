<template>
  <el-space direction="vertical" style="display: block;" size="default">
    <el-table
        :data="tableData"
        stripe
        border
        class="list"
        height="540"
    >
      <el-table-column prop="subject" label="专题" width="160" show-overflow-tooltip align="center">
        <template #default="scope">
          <el-tag
              size="small"
              effect="plain"
          >
            {{ scope.row.subject }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="question" label="题目" show-overflow-tooltip width="300" align="center"/>
      <el-table-column prop="content" label="内容" show-overflow-tooltip align="center"/>
      <el-table-column
          prop="difficulty"
          label="难度"
          align="center"
          width="100"
      >
        <template #default="scope">
          <el-tag
              :type="difficultyType(scope.row.difficulty)"
              disable-transitions
              effect="light"
          >
            {{ scope.row.difficulty }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="详细" width="100" align="center">
        <template #default="scope">
          <el-button
              size="small"
              type="plain"
              @click="handleEdit(scope.$index, scope.row)"
          >
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <ElConfigProvider :locale="zhCn">
      <el-pagination
          v-model:current-page="pageNo"
          v-model:page-size="pageSize"
          :page-sizes="[50, 30, 20, 10]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalRows"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="display: flex; justify-content: center;user-select: none"

      />
    </ElConfigProvider>
  </el-space>
</template>

<script setup>
// ElConfigProvider 组件
import {ElConfigProvider, ElMessage} from 'element-plus'
// 引入中文包
import zhCn from "element-plus/es/locale/lang/zh-cn";
import {onMounted, ref, watchEffect} from "vue";
import {doPost} from "@/axios/httpRequest";
import {useLoadingDisplay} from "@/store/loadingDisplay";
import {useQuestionData} from "@/store/questionData";

const loadingDisplay = useLoadingDisplay();

const storeQuestion = useQuestionData();

function doPageChangePost() {
  doPost("/questionList/queryQuestionList",{pageNo: pageNo.value,
                                                      pageSize: pageSize.value,
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

const handleSizeChange = () => {
  doPageChangePost();
}

const handleCurrentChange = () => {
  doPageChangePost();
}

const difficultyType = (difficulty) => {
    switch (difficulty) {
      case '简单':
        return 'info';
      case '普通':
        return 'primary';
      case '困难':
        return 'warning';
      case '专家':
        return 'error';
      default:
        return '';
    }
};

let tableData = ref([]);

let totalRows = ref(0);

let pageNo = ref(1);

let pageSize = ref(10);

onMounted(() => {
  loadingDisplay.display = true;

  doPageChangePost();

  watchEffect(() => {
    if(storeQuestion) {
      doPageChangePost();
    }
  });
})
</script>

<style scoped>
.list {
  width: 100%;
}
</style>