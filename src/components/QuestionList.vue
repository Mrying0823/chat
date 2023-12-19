<template>
  <el-space direction="vertical" style="display: block;" size="default">
    <el-table
        :data="tableData"
        stripe
        border
        class="list"
        height="540"
        :default-sort="{ order: 'ascending' }"
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
      <el-table-column prop="createdDate" label="创建日期" width="120" align="center" :resizable="false" sortable>
        <template #default="scope">
          <el-tag
              size="small"
              effect="light"
          >
            {{ scope.row.createdDate }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="updatedDate" label="修改日期" width="120" align="center" :resizable="false" sortable>
        <template #default="scope">
          <el-tag
              size="small"
              effect="light"
          >
            {{ scope.row.updatedDate }}
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
      <el-table-column label="操作" width="100" align="center" :resizable="false" v-if="edit">
        <template #default="scope">
          <!-- scope.$index： 表示当前行的索引，即在整个表格中的行号，从 0 开始计数。-->
          <!-- scope.row： 表示当前行的数据对象，即当前行的数据。-->
          <el-button
              size="small"
              type="plain"
              @click="handleEdit(scope.row,$event)"
          >
            编辑
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
import {ElConfigProvider} from 'element-plus'
// 引入中文包
import zhCn from "element-plus/es/locale/lang/zh-cn";
import {useQuestionData} from "@/store/questionData";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const storeQuestion = useQuestionData();

// eslint-disable-next-line no-unused-vars
const props = defineProps(['totalRows', "tableData", "edit"]);

const pageNo = ref(storeQuestion.pageNo);

const pageSize = ref(storeQuestion.pageSize);

// 查看详细
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

// 编辑问题
const handleEdit = (question,event) => {
  // 按钮失焦
  event.target.blur();
  if(event.target.nodeName === "SPAN") {
    event.target.parentNode.blur();
  }

  storeQuestion.currentEditQuestion.id = question.id;
  storeQuestion.currentEditQuestion.question = question.question;
  storeQuestion.currentEditQuestion.stage = question.stage;
  storeQuestion.currentEditQuestion.subject = question.subject;
  storeQuestion.currentEditQuestion.difficulty = question.difficulty;
  storeQuestion.currentEditQuestion.content = question.content;

  router.push({
    path: '/question/editQuestion'
  });
}

const handleSizeChange = () => {
  storeQuestion.pageSize = pageSize.value;
}

const handleCurrentChange = () => {
  storeQuestion.pageNo = pageNo.value;
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
</script>

<style scoped>
.list {
  width: 100%;
}
</style>