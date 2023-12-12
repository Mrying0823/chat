<template>
  <el-space direction="vertical" style="display: block;" size="default">
    <el-table
        :data="tableData"
        stripe
        border
        class="list"
        height="540"
    >
      <el-table-column prop="subject" label="专题" width="160" align="center" :resizable="false">
        <template #default="scope">
          <el-tag
              size="small"
              effect="plain"
          >
            {{ scope.row.subject }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="question" label="题目" show-overflow-tooltip width="300" align="center" :resizable="false"/>
      <el-table-column
          prop="content"
          label="内容" align="center"
          :resizable="false"
      >
        <template #default="scope">
          <div style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
            {{ scope.row.content }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
          prop="difficulty"
          label="难度"
          align="center"
          width="100"
          :resizable="false"
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
      <el-table-column label="详细" width="100" align="center" :resizable="false">
        <template #default="scope">
          <!-- scope.$index： 表示当前行的索引，即在整个表格中的行号，从 0 开始计数。-->
          <!-- scope.row： 表示当前行的数据对象，即当前行的数据。-->
          <el-button
              size="small"
              type="plain"
              @click="handleDetail(scope.row,$event)"
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

const handleDetail = (question,event) => {
  // 按钮失焦
  event.target.blur();
  if(event.target.nodeName === "SPAN") {
    event.target.parentNode.blur();
  }

  storeQuestion.detailDialog = true;

  storeQuestion.question = question.question;
  storeQuestion.content = question.content;
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