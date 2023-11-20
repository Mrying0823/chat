<template>
  <el-container>
    <el-main>
      <QuillEditor
          style="height: 74vh"
          v-model:content="content"
          ref="myQuillEditor"
          :options="editorOption"
          contentType="html"
          @update:content="setValue"
      >
      </QuillEditor>
    </el-main>
    <el-footer class="chat-footer">
      <div class="borderNone" :class="this.$store.getters.getDarkMode ? 'night-mode-chat-input': 'chat-input'">
        <el-input class="message-input" type="textarea" v-model="message" @keydown="handleKeyDown" placeholder="你可以向 chatgpt 提问..." :autosize="{ minRows: 1, maxRows: 2}"></el-input>
        <el-button type="text" @click="onSendMessage(message)" :disabled="generating" :loading="generating"><el-icon v-show="!generating"><Position /></el-icon></el-button>
      </div>
    </el-footer>
  </el-container>
</template>

<script setup>
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
// eslint-disable-next-line no-unused-vars
import {computed, getCurrentInstance, nextTick, onMounted, ref, watch} from "vue";
import store from "@/store";
import { debounce } from 'lodash';

const content = ref('')
const myQuillEditor = ref()

const titleConfig = [
  { Choice: '.ql-save', title: '保存' },
  { Choice: '.ql-insertMetric', title: '跳转配置' },
  { Choice: '.ql-bold', title: '加粗' },
  { Choice: '.ql-italic', title: '斜体' },
  { Choice: '.ql-underline', title: '下划线' },
  { Choice: '.ql-header', title: '段落格式' },
  { Choice: '.ql-strike', title: '删除线' },
  { Choice: '.ql-blockquote', title: '块引用' },
  { Choice: '.ql-code', title: '插入代码' },
  { Choice: '.ql-code-block', title: '插入代码段' },
  { Choice: '.ql-font', title: '字体' },
  { Choice: '.ql-size', title: '字体大小' },
  { Choice: '.ql-list[value="ordered"]', title: '编号列表' },
  { Choice: '.ql-list[value="bullet"]', title: '项目列表' },
  { Choice: '.ql-direction', title: '文本方向' },
  { Choice: '.ql-header[value="1"]', title: 'h1' },
  { Choice: '.ql-header[value="2"]', title: 'h2' },
  { Choice: '.ql-align', title: '对齐方式' },
  { Choice: '.ql-color', title: '字体颜色' },
  { Choice: '.ql-background', title: '背景颜色' },
  { Choice: '.ql-image', title: '图像' },
  { Choice: '.ql-video', title: '视频' },
  { Choice: '.ql-link', title: '添加链接' },
  { Choice: '.ql-formula', title: '插入公式' },
  { Choice: '.ql-clean', title: '清除字体格式' },
  { Choice: '.ql-script[value="sub"]', title: '下标' },
  { Choice: '.ql-script[value="super"]', title: '上标' },
  { Choice: '.ql-indent[value="-1"]', title: '向左缩进' },
  { Choice: '.ql-indent[value="+1"]', title: '向右缩进' },
  { Choice: '.ql-header .ql-picker-label', title: '标题大小' },
  { Choice: '.ql-header .ql-picker-item[data-value="1"]', title: '标题一' },
  { Choice: '.ql-header .ql-picker-item[data-value="2"]', title: '标题二' },
  { Choice: '.ql-header .ql-picker-item[data-value="3"]', title: '标题三' },
  { Choice: '.ql-header .ql-picker-item[data-value="4"]', title: '标题四' },
  { Choice: '.ql-header .ql-picker-item[data-value="5"]', title: '标题五' },
  { Choice: '.ql-header .ql-picker-item[data-value="6"]', title: '标题六' },
  { Choice: '.ql-header .ql-picker-item:last-child', title: '标准' },
  { Choice: '.ql-size .ql-picker-item[data-value="small"]', title: '小号' },
  { Choice: '.ql-size .ql-picker-item[data-value="large"]', title: '大号' },
  { Choice: '.ql-size .ql-picker-item[data-value="huge"]', title: '超大号' },
  { Choice: '.ql-size .ql-picker-item:nth-child(2)', title: '标准' },
  { Choice: '.ql-align .ql-picker-item:first-child', title: '居左对齐' },
  { Choice: '.ql-align .ql-picker-item[data-value="center"]', title: '居中对齐' },
  { Choice: '.ql-align .ql-picker-item[data-value="right"]', title: '居右对齐' },
  { Choice: '.ql-align .ql-picker-item[data-value="justify"]', title: '两端对齐' }
];

const editorOption = {
  modules: {
    toolbar: [
      ['save'],
      ['bold', 'italic', 'underline', 'strike'],
      ['blockquote', 'code-block'],
      [{ 'header': 1 }, { 'header': 2 }],
      [{ 'list': 'ordered'}, { 'list': 'bullet' }],
      [{ 'script': 'sub'}, { 'script': 'super' }],
      [{ 'indent': '-1'}, { 'indent': '+1' }],
      [{ 'direction': 'rtl' }],
      [{ 'size': ['small', false, 'large', 'huge'] }],
      [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
      [{ 'color': [] }, { 'background': [] }],
      [{ 'font': [] }],
      [{ 'align': [] }],
      ['clean'],
    ]
  },
  placeholder: "请输入正文",
  theme: 'snow'
};

const setValue = () => {
  const text = myQuillEditor.value.getHTML();
  store.commit("updateLastSelectedNote",text);
};

const noteContent = computed(() => {
  return store.state.lastSelectedNote;
});

// 防抖输入函数，用户暂停输入一段时间后 setHTML
const debouncedSetHTML = debounce((newVal) => {
  myQuillEditor.value.setHTML(newVal);
  // 使用 console.log 打印出实例对象可以看到它的使用方法
  myQuillEditor.value.getQuill().setSelection(newVal.length,newVal.length);
}, 500);

watch(noteContent, (newVal) => {
  debouncedSetHTML(newVal);
}, { deep: true ,immediate: true });

const initButton = () => {
  const editorButton = document.querySelector('.ql-save');
  if (editorButton) {
    editorButton.innerHTML = '<i class="fa fa-save" style="font-size: 16px; padding-top: 0.2vh; color: #4b5563; user-select: none"></i>';
  }
};

onMounted(() => {

  const noteContent = store.getters.getLastSelectedNote;

  myQuillEditor.value.setHTML(noteContent);

  // 工具栏鼠标悬停提示
  for (let item of titleConfig) {
    let tip = document.querySelector('.ql-toolbar ' + item.Choice)
    if (!tip) continue
    tip.setAttribute('title', item.title)
  }

  // 初始化自定义工具
  initButton();
});
</script>

<style scoped>
.borderNone >>>.el-textarea__inner {
  border: 0;
  resize: none;
  box-shadow: 0 0 0 0;
}
</style>