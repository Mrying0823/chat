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
import {onMounted, ref, toRaw, watch} from "vue";
import store from "@/store";

// eslint-disable-next-line no-undef
const props = defineProps(['value'])
// eslint-disable-next-line no-undef,no-unused-vars
const emit = defineEmits(['updateValue'])
const content = ref('')
const myQuillEditor = ref()
// 通过 watch 监听回显
watch(() => props.value, (val) => {
  toRaw(myQuillEditor.value).setHTML(val)
}, { deep: true })

const editorOption = {
  modules: {
    toolbar: [
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
  const text = toRaw(myQuillEditor.value).getHTML();
  store.commit("updateLastSelectedNote",text);
};

onMounted(() => {

  const noteContent = store.getters.getLastSelectedNote;

  toRaw(myQuillEditor.value).setHTML(noteContent);

});
</script>

<style scoped>
.borderNone >>>.el-textarea__inner {
  border: 0;
  resize: none;
  box-shadow: 0 0 0 0;
}
</style>