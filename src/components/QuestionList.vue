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
      <el-table-column prop="name" label="题目" show-overflow-tooltip width="300" align="center"/>
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
          v-model:current-page="currentPage4"
          v-model:page-size="pageSize4"
          :page-sizes="[50, 30, 20, 10]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="400"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="display: flex; justify-content: center;"

      />
    </ElConfigProvider>
  </el-space>
</template>

<script setup>
// ElConfigProvider 组件
import { ElConfigProvider } from 'element-plus'
// 引入中文包
import zhCn from "element-plus/es/locale/lang/zh-cn";

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

const tableData = [
  {
    subject: 'JAVA 基础',
    name: '什么是Java？请提供一个简要介绍。',
    content: 'Java是一种面向对象的编程语言，广泛用于开发各种应用。',
    difficulty: "简单"
  },
  {
    subject: 'JAVA 基础',
    name: '解释Java中的不同数据类型并给出示例。',
    content: 'Java中有多种数据类型，如整数、浮点数、字符等。示例：int num = 10; double value = 3.14; char ch = \'A\';',
    difficulty: "普通"
  },
  {
    subject: 'JAVA 基础',
    name: '描述Java中的面向对象编程原理。',
    content: '面向对象编程（OOP）是一种编程范式，Java通过类和对象来支持面向对象编程。',
    difficulty: "普通"
  },
  {
    subject: 'JAVA 异常',
    name: 'Java中的异常处理是如何工作的？请提供示例。',
    content: '在Java中，异常处理通过try-catch块来实现。示例： try { // 一些可能抛出异常的代码 } catch (Exception e) { // 处理异常的代码 }',
    difficulty: "困难"
  },
  {
    subject: 'JAVA 集合',
    name: '讨论Java集合框架及其组件。',
    content: 'Java集合框架提供了一套性能优越、灵活性好的数据结构和算法。主要包括List、Set、Map等。',
    difficulty: "普通"
  },
  {
    subject: 'JAVA 线程',
    name: '解释Java中多线程编程的概念。',
    content: '多线程编程是指在一个程序中同时运行多个线程，Java通过Thread类和Runnable接口支持多线程。',
    difficulty: "专家"
  },
  {
    subject: 'JAVA 文件',
    name: 'Java中的文件处理是如何工作的？请提供示例。',
    content: 'Java文件处理涉及读写文件，可以使用File类和相关的输入输出流。示例：读取文件内容、写入文件等。',
    difficulty: "普通"
  },
  {
    subject: 'JAVA 数据库',
    name: '讨论Java数据库连接（JDBC）。',
    content: 'JDBC（Java Database Connectivity）是Java访问数据库的标准接口。通过JDBC，Java应用可以与各种数据库交互。',
    difficulty: "困难"
  },
  {
    subject: 'JAVA 基础',
    name: '什么是Java Servlets？解释它们在Web开发中的用途。',
    content: 'Java Servlets是一种用于扩展服务器功能的Java类，主要用于处理HTTP请求和响应。在Web开发中，它们通常用于创建动态网页。',
    difficulty: "专家"
  },
  {
    subject: 'JAVA 基础',
    name: '什么是Java Servlets？解释它们在Web开发中的用途。',
    content: 'Java Servlets是一种用于扩展服务器功能的Java类，主要用于处理HTTP请求和响应。在Web开发中，它们通常用于创建动态网页。',
    difficulty: "专家"
  },
  {
    subject: 'JAVA 基础',
    name: '什么是Java Servlets？解释它们在Web开发中的用途。',
    content: 'Java Servlets是一种用于扩展服务器功能的Java类，主要用于处理HTTP请求和响应。在Web开发中，它们通常用于创建动态网页。',
    difficulty: "专家"
  },
  {
    subject: 'JAVA 基础',
    name: '什么是Java Servlets？解释它们在Web开发中的用途。',
    content: 'Java Servlets是一种用于扩展服务器功能的Java类，主要用于处理HTTP请求和响应。在Web开发中，它们通常用于创建动态网页。',
    difficulty: "专家"
  },
  {
    subject: 'JAVA 基础',
    name: '什么是Java Servlets？解释它们在Web开发中的用途。',
    content: 'Java Servlets是一种用于扩展服务器功能的Java类，主要用于处理HTTP请求和响应。在Web开发中，它们通常用于创建动态网页。',
    difficulty: "专家"
  },
]
</script>

<style scoped>
.list {
  width: 100%;
}
</style>