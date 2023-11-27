<template>
  <NavBar/>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-note-container' : 'note-container'">
    <el-aside class="note-side">
      <NoteSidebar/>
    </el-aside>
    <el-container>
      <el-main style="overflow: hidden">
        <NoteQuillEditor class="note-body" :gptMessage="gptMessage" @msg="getNoteContent"/>
      </el-main>
      <el-footer class="chat-footer">
        <div class="borderNone" :class="this.$store.getters.getDarkMode ? 'night-mode-chat-input': 'chat-input'">
          <el-input class="message-input" type="textarea" v-model="message" @keydown="handleKeyDown" placeholder="你可以向 chatgpt 提问..." :autosize="{ minRows: 1, maxRows: 2}"></el-input>
          <el-button type="text" @click="onSendMessage(message)" :disabled="generating" :loading="generating"><el-icon v-show="!generating"><Position /></el-icon></el-button>
        </div>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
import NavBar from "@/components/NavBar";
import NoteSidebar from "@/components/NoteSidebar";
import NoteQuillEditor from "@/components/NoteQuillEditor";
// marked 用于将 markdown 格式文本转为 html
import * as marked from "marked";
// highlight 用于代码高亮
import hljs from 'highlight.js';
import qs from "qs";
import {EventSourcePolyfill} from "event-source-polyfill";
import store from "@/store";
import {doPost} from "@/axios/httpRequest";
import {ElMessage} from "element-plus";

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

export default {
  name: "NoteView",
  components:{NoteQuillEditor, NoteSidebar, NavBar},
  data() {
    return {
      generating: false,
      message: "",
      gptMessage: "",
      noteContent: ""
    }
  },
  methods: {
    getNoteContent(msg) {
      this.noteContent = msg.noteContent;
    },

    autoSaveNote() {
      const noteId = store.getters.getLastSelectedNote.noteId;
      this.noteContent.concat("\n");
      doPost("/note/updateNoteContent",{noteId: noteId, noteContent: this.noteContent}).then(response => {
        if(response && response.data.code === 200) {
          store.commit("updateLastSelectedNote", {noteId: noteId, noteContent: this.noteContent});
          ElMessage({
            message: '自动保存',
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
    },

    // 阻止 el-input 回车的默认行为
    handleKeyDown(event) {
      if (event.keyCode === 13 && !event.shiftKey) {
        event.preventDefault();
        this.onSendMessage(this.message);
      }
    },
    async onSendMessage(message) {
      let this_ = this;

      if(message) {
        this_.message = message;
      }

      // 发送消息为空
      if(!this_.message) {
        this.$message({
          type: "error",
          center: true,
          message: "请输入内容"
        });
        return;
      }

      // 回答未结束
      if(this_.generating) {
        this_.$message({
          type: "error",
          center: true,
          message: "请等待回答生成完毕"
        });
        return;
      }

      this_.generating = true;

      this_.gptMessage = this_.message.concat("\n");

      await this_.onGetMessage();
    },

    async onGetMessage() {
      let this_ = this;

      const data = {usePublicApi: true,prompt: this_.message};
      const requestData = qs.stringify(data);

      const accessToken = localStorage.getItem("accessToken");

      if(accessToken) {
        const headers = {
          Authorization: `${accessToken}`
        }

        let sse = new EventSourcePolyfill(`http://localhost:8081/api/v1/chatgpt/chatForNote?` + requestData, {
          withCredentials: true,
          headers
        });

        sse.addEventListener("open", (function () {
          console.log('open');
          this_.generating = true;
          this_.message = "";
        }));

        sse.addEventListener("message", function (event) {
          if (event.data === "[DONE]") {
            sse.close();
            this_.generating = false;
            // 自动保存
            this_.autoSaveNote();
            return;
          }
          let answer = JSON.parse(event.data).content

          this_.gptMessage += answer;
        });

        sse.addEventListener("error", function (event) {
          console.log("error: " + event.data);
          sse.close();

          this_.$message({
            type: "error",
            center: true,
            message: "服务繁忙，请稍后重试"
          });

          this_.generating = true;

          setTimeout(() => {
            this_.generating = false;
          }, 5000);
        });
      }
    }
  }
}
</script>

<style scoped>
.borderNone >>>.el-textarea__inner {
  border: 0;
  resize: none;
  box-shadow: 0 0 0 0;
}

.note-container {
  width: 100%; /* 设置容器宽度为100% */
  box-sizing: border-box; /* 设置盒模型为border-box，确保内边距和边框不会增加容器的宽度 */
  height: 96vh;
  padding-top: 0.9%;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
  font-family: "Source Code Pro Light", sans-serif;
  background-color: #E6F4F1;
  position: fixed;
}

.night-mode-note-container {
  width: 100%; /* 设置容器宽度为100% */
  box-sizing: border-box; /* 设置盒模型为border-box，确保内边距和边框不会增加容器的宽度 */
  height: 96vh;
  padding-top: 0.9%;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
  font-family: "Source Code Pro Light", sans-serif;
  position: fixed;
  background-color: #18392F;
}

.note-body {
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
}

.note-side {
  width: auto;
  height: 100%;
  box-sizing: border-box;
}
</style>
