<template>
  <el-dialog v-model="storeQuestion.detailDialog" :title="storeQuestion.question" center :destroy-on-close="true">
    <el-scrollbar style="height: 63vh">
      <div
          v-html="marked.parse(storeQuestion.content)"
      />
    </el-scrollbar>
  </el-dialog>
</template>

<script setup>
import {useQuestionData} from "@/store/questionData";
// marked 用于将 markdown 格式文本转为 html
import * as marked from "marked";
// highlight 用于代码高亮
import hljs from 'highlight.js';
import 'highlight.js/styles/monokai-sublime.css';

const storeQuestion = useQuestionData();

// 设置 marked 的选项及配置
marked.setOptions({
  // 自定义高亮代码块的渲染
  // lang 用于指定代码块的语言
  langPrefix: 'hljs language-',
  highlight(code, lang) {
    const language = hljs.getLanguage(lang) ? lang : 'plaintext';
    return hljs.highlight(code, { language }).value;
  }
});
</script>

<style scoped>

</style>