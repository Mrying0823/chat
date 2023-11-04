<template>
  <NavBar></NavBar>
  <FloatNavbar @tran-question="sendQuestion"></FloatNavbar>
  <el-container class="chat-container">
    <el-aside class="chat-aside">
      <ChatGPTSidebar @tran-messageList="updateMessageList" @tran-conversationId="updateConversationId" :conversationId="conversationId"></ChatGPTSidebar>
    </el-aside>
      <el-container class="chat-container-message">
        <el-main class="chat-message">
          <el-scrollbar ref="chat-scrollbar">
            <ul ref="chat-ul">
              <li v-for="item in items" :key="item.messageId" :class="item.messageDirection === 1? 'chat-reply':'chat-question'">
                <div :class="item.messageDirection === 1? 'reply-info':'question-info'">
                  <div v-html="item.html? item.html : item.content || ''" :class="item.messageDirection === 1? 'reply-info-content':'question-info-content'"></div>
                </div>
              </li>
            </ul>
          </el-scrollbar>
        </el-main>
        <el-footer class="chat-footer">
          <div class="chat-input borderNone">
            <el-input class="message-input" type="textarea" v-model="message" @keydown="handleKeyDown" placeholder="请输入您的消息..." :autosize="{ minRows: 1, maxRows: 2}"></el-input>
            <el-button type="text" @click="onSendMessage(message)" :disabled="generating" :loading="generating"><el-icon v-show="!generating"><Position /></el-icon></el-button>
          </div>
        </el-footer>
      </el-container>
  </el-container>
</template>

<script>
// marked 用于将 markdown 格式文本转为 html
import * as marked from "marked";
// highlight 用于代码高亮
import hljs from "highlight.js";
import qs from "qs";
import ChatGPTSidebar from "@/components/ChatGPTSidebar";
import {doPost} from "@/axios/httpRequest";
import FloatNavbar from "@/components/FloatNavbar";
import NavBar from "@/components/NavBar";

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
  name: "ChatGPT",
  components: {NavBar, FloatNavbar, ChatGPTSidebar},
  data() {
    return {
      // 提问
      message: "",
      items: [],
      generating: false,
      usePublicApi: true,
      conversationId: ""
    }
  },

  methods: {
    // 发送问题列表里的问题
    sendQuestion(question) {
      if(question) {
        this.onSendMessage(question);
      }
    },

    // 滚动条滑至底部
    scrollToBottom() {
      this.$nextTick(() => {
        this.$refs["chat-scrollbar"].setScrollTop(this.$refs["chat-ul"].clientHeight)
      });
    },

    // 处理子组件传递过来的参数
    updateMessageList(messageList) {
      if(messageList) {
        messageList.forEach(message => {
          let words = message.content.split('');
          let html = words.join('');
          message.html = marked.parse(html);
        })
      }
      // 更新父组件的数据
      this.items = messageList;
      this.scrollToBottom();
    },

    updateConversationId(conversationId) {
        this.conversationId = conversationId;
    },

    // 阻止 el-input 回车的默认行为
    handleKeyDown(event) {
      if (event.keyCode === 13 && !event.shiftKey) {
        event.preventDefault();
        this.onSendMessage();
      }
    },

    getUuid() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (Math.random() * 16) | 0,
            v = c === 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
      })
    },

    getUser() {
      return localStorage.getItem("user")
    },

    async onSendMessage(message) {
      let this_ = this;

      if(message) {
        this_.message = message;
      }

      // 发送消息为空
      if(!this.message) {
        this.$message({
          type: "error",
          center: true,
          message: "请输入内容"
        });
        return;
      }

      // 回答未结束
      if(this.generating) {
        this.$message({
          type: "error",
          center: true,
          message: "请等待回答生成完毕"
        });
        return;
      }

      this.generating = true;

      // 提问内容格式化
      const question = {
        "messageId": "",
        "messageDirection": 0,
        "conversationId": "",
        "content": this.message,
        "createTime": 0
      }

      let words = question.content.split('');
      let html = words.join('');
      question.html = marked.parse(html);

      this_.items.push(question);

      // 获取 conversationId
      // 创建新会话
      if(!this_.conversationId) {
        await doPost("/v1/chatgpt/createConversation", {conversationType: 0, conversationName: "新建会话"}).then(response => {
          if (response && response.data.code === 200) {
            this.conversationId = response.data.conversationId;
          } else {
            this.$message({
              type: "error",
              center: true,
              message: response.data.msg
            });
          }
        });
      }

      await this.onGetMessage();
    },

    async onGetMessage() {
      let this_ = this;

      const data = {usePublicApi: this_.usePublicApi,prompt: this_.message,conversationId: this_.conversationId};
      const requestData = qs.stringify(data);

      console.log(requestData);

      let sse = new EventSource(`http://localhost:8081/api/v1/chatgpt/chat?`+requestData,{withCredentials: true});

      sse.addEventListener("open",(function () {
        console.log('open');
        this_.generating = true;
        this_.message = "";
        this_.items.push({
          "messageId": "",
          "messageDirection": 1,
          "conversationId": "",
          "content": "",
          "createTime": 0
        });
      }));

      sse.addEventListener("message",function (event) {
        if(event.data === "[DONE]") {
          sse.close();
          this_.generating = false;
          return;
        }
        let answer = JSON.parse(event.data).content;

        let last = this_.items[this_.items.length - 1];
        last.content += answer;
        let words = last.content.split('');
        let html = words.join('');
        last.html = marked.parse(html);
      });

      sse.addEventListener("error",function (event) {
        console.log("error: "+event.data);
        sse.close();

        this_.$message({
          type: "error",
          center: true,
          message: "服务繁忙，请稍后重试"
        });

        this_.generating = true;

        setTimeout(() => {
          this_.generating = false;
        },5000);
      });
    }
  },

  mounted() {
    console.log('mounted')
    let user = this.getUser();
    if (!user) {
      console.log("No user found, creating new user")
      localStorage.setItem("user", this.getUuid())
    }
    console.log('user:', this.getUser())
  }
}
</script>

<style scoped>
.borderNone >>>.el-textarea__inner {
  border: 0;
  resize: none;
  box-shadow: 0 0 0 0;
}
</style>