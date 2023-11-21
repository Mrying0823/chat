<template>
  <el-container>
    <el-main>
      <QuillEditor
          style="height: 74vh"
          v-model:content="content"
          ref="myQuillEditor"
          :options="editorOption"
          contentType="html"
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
import {computed, onBeforeUnmount, onMounted, ref, watch} from "vue";
import store from "@/store";
// eslint-disable-next-line no-unused-vars
import { debounce } from 'lodash';
import {ElMessage} from "element-plus";
import {doPost} from "@/axios/httpRequest";
// marked 用于将 markdown 格式文本转为 html
import * as marked from "marked";
// highlight 用于代码高亮
import hljs from 'highlight.js';
import 'highlight.js/styles/monokai-sublime.css';
import Quill from 'quill';
import Delta from 'quill-delta';

const Clipboard = Quill.import("modules/clipboard");

// 阻止粘贴行为导致的滚动条跳动
class CustomClipboard extends Clipboard {
  onPaste(e) {

    const top = window.pageYOffset;
    const left = window.pageXOffset;
    if (e.defaultPrevented || !this.quill.isEnabled()) return;
    let range = this.quill.getSelection();
    let delta = new Delta().retain(range.index);
    this.container.style.top =
        (
            window.pageYOffset ||
            document.documentElement.scrollTop ||
            document.body.scrollTop ||
            0
        ).toString() + "px";
    this.container.focus();
    setTimeout(() => {
      this.quill.selection.update(Quill.sources.SILENT);
      let convertData = this.convert();
      //循环出去文本，格式化重点
      convertData.ops = convertData.ops.map((op) => {
        return {
          insert: op.insert,
        };
      });

      delta = delta.concat(convertData).delete(range.length);
      console.log(delta)
      this.quill.updateContents(delta, Quill.sources.USER);
      this.quill.setSelection(
          delta.length() - range.length,
          Quill.sources.SILENT
      );
      let bounds = this.quill.getBounds(
          delta.length() - range.length,
          Quill.sources.SILENT
      );
      this.quill.scrollingContainer.scrollTop = bounds.top;

      // scroll window to previous position after paste
      window.scrollTo({ top, left });
    }, 1);

  }
}

//此处需要注册
Quill.register("modules/clipboard", CustomClipboard, true);

const content = ref('')
const myQuillEditor = ref()

// 添加键盘事件监听器
window.addEventListener('keydown', handleKeyDown);

// 在组件销毁前移除事件监听器，以防止内存泄漏
onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleKeyDown);
});

// 处理键盘事件
function handleKeyDown(event) {
  // 检查是否按下了 Ctrl 键
  if (event.ctrlKey) {
    // 检查是否按下了 Z 键
    if (event.key === 'z' || event.keyCode === 90) {
      // 在这里执行 Ctrl + Z 触发的操作
      undoOnCtrlZ();
    }else if(event.key === 'y' || event.keyCode === 89) {
      // 在这里执行 Ctrl + Y 触发的操作
      redoOnCtrlY();
    }else if(event.key === 's' || event.keyCode === 83) {
      // 在这里执行 Ctrl + S 触发的操作
      saveNote();
      // 阻止默认的保存操作，避免浏览器弹出保存框
      event.preventDefault();
    }
  }
}

// 撤销
function undoOnCtrlZ() {
  myQuillEditor.value.getQuill().history.undo;
}

// 重做
function redoOnCtrlY() {
  myQuillEditor.value.getQuill().history.redo;
}

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

//quill编辑器的字体
// eslint-disable-next-line no-unused-vars
const fontList = [
  "SimSun",
  "SimHei",
  "Microsoft-YaHei",
  "KaiTi",
  "FangSong",
  "Arial",
  "Times-New-Roman",
  "sans-serif",
];

/*
var font = Quill.import("formats/font");
font.whitelist = fontList; //将字体加入到白名单
Quill.register(font, true);
*/

const saveNote = () => {

  const text = myQuillEditor.value.getHTML();
  const noteId = store.getters.getLastSelectedNote.noteId;

  doPost("/note/updateNoteContent",{noteId: noteId, noteContent: text}).then(response => {
    if(response && response.data.code === 200) {
      store.commit("updateLastSelectedNote", {noteId: noteId, noteContent: text});
      ElMessage({
        message: '保存成功',
        type: 'success',
        center: true
      });
    }else {
      ElMessage({
        message: response.data.msg,
        type: 'error',
        center: true
      });
    }
  });
}

const editorOption = {
  modules: {
    toolbar: {
      container: [
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
        ['clean']
      ],
      handlers: {
        save: saveNote
      }
    },
    // 禁用 Quill 通过添加额外行来匹配这个间距，以弥补缺失的 margin/padding
    clipboard: {
      // 关闭视觉匹配，保留原始格式
      matchVisual: false,
    },
    // 语法高亮
    syntax: {
      highlight: text => hljs.highlightAuto(text).value
    },
    history: {
      delay: 2000,
      maxStack: 500,
      userOnly: true
    }
  },
  placeholder: "请输入正文",
  theme: 'snow'
};

const noteContent = computed(() => {
  return marked.parse(store.state.lastSelectedNote.noteContent);
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

  const noteContent = store.getters.getLastSelectedNote.noteContent;

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