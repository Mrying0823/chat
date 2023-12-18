<template>
  <div style="height: 100%;" class="page-edit-vue">
    <div style="box-sizing: border-box;overflow: hidden">
      <div style="padding: 10px;">
        <mavonEditor
            ref="mavonEditorRef"
            v-model="markdownContent"
            :toolbars="toolbars"
            :externalLink="false"
            style="height: calc(100vh - 100px);"
            :style="this.$store.getters.getDarkMode ? 'background-color: #18392F;color: #f0f0f0': ''"
            @save="createWikiSave(0)"
            @imgAdd="addMarkdownImage"
            :toolbarsBackground="nightMode"
            :editor-background="nightMode"
            :preview-background="nightMode"
            placeholder="请输入问题内容"
            class="page-content-editor wang-editor-body"
        >
          <template v-slot:left-toolbar-before>
            <button
                type="button"
                aria-hidden="true"
                title="求助 ChatGPT"
                class="op-icon"
            >
              <i class="fa fa-comment"></i>
            </button>
          </template>
          <template v-slot:left-toolbar-after>
            <button
                type="button"
                aria-hidden="true"
                title="ChatGPT 批改"
                class="op-icon"
            >
              <i class="fa fa-support"></i>
            </button>
          </template>
          <template v-slot:right-toolbar-before>
            <button
                type="button"
                aria-hidden="true"
                title="更多练习"
                class="op-icon"
            >
              <i class="fa fa-question-circle"></i>
            </button>
          </template>
        </mavonEditor>
      </div>
    </div>
  </div>
</template>

<script setup>
import {mavonEditor} from 'mavon-editor'
import 'mavon-editor/dist/markdown/github-markdown.min.css'
import 'mavon-editor/dist/css/index.css'

// eslint-disable-next-line no-unused-vars
const props = defineProps(["nightMode"]);

let toolbars = {
  bold: true, // 粗体
  italic: true, // 斜体
  header: true, // 标题
  underline: true, // 下划线
  strikethrough: true, // 中划线
  mark: true, // 标记
  superscript: true, // 上角标
  subscript: true, // 下角标
  quote: true, // 引用
  ol: true, // 有序列表
  ul: true, // 无序列表
  link: true, // 链接
  imagelink: true, // 图片链接
  code: true, // code
  table: true, // 表格
  fullscreen: true, // 全屏编辑
  readmodel: true, // 沉浸式阅读
  /* 1.3.5 */
  undo: true, // 上一步
  redo: true, // 下一步
  trash: true, // 清空
  save: true, // 保存（触发events中的save事件）
  /* 1.4.2 */
  navigation: true, // 导航目录
  /* 2.1.8 */
  alignleft: true, // 左对齐
  aligncenter: true, // 居中
  alignright: true, // 右对齐
  /* 2.2.1 */
  subfield: true, // 单双栏模式
  preview: true, // 预览
};
</script>

<style scoped>
.page-edit-vue .page-content-editor ol {
  list-style: decimal;
}

.page-edit-vue .page-content-editor ul {
  list-style: disc;
}
</style>

<style>
.page-edit-vue .icon-collapse {
  float: left;
  font-size: 25px;
  color: #aaa;
  margin-top: 8px;
  cursor: pointer;
}

.page-edit-vue .icon-collapse:hover {
  color: #eee;
}

.page-edit-vue .wiki-title {
  font-size: 20px;
}

.page-edit-vue .wiki-author {
  font-size: 14px;
  color: #888;
  padding: 20px 0;
  height: 40px;
  line-height: 40px;
}

.page-edit-vue .wiki-content {
  font-size: 14px;
}

.page-edit-vue .wiki-content.w-e-text {
  overflow-y: auto;
}

.page-edit-vue .upload-page-file .el-upload-list {
  display: none;
}

.page-edit-vue .is-link {
  color: #1e88e5;
  cursor: pointer;
}

.page-edit-vue #newPageContentDiv .w-e-text-container {
  height: 100% !important;
}

.page-edit-vue .comment-box .head {
  float: left;
  background-color: #ccc;
  border-radius: 50%;
  margin-right: 10px;
  width: 45px;
  height: 45px;
  line-height: 45px;
  text-align: center;
  color: #fff;
}

.page-edit-vue .page-content-editor {
  padding: 10px 0;
}

.page-edit-vue .markdown-body table {
  display: table;
}
</style>