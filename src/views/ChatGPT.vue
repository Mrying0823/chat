<template>
  <el-main>
      <div class="chat-container">
        <el-header class="chat-header">
          <h4>ChatGPT</h4>
        </el-header>
        <div class="chat-messages">
          <ul class="chat-message-container">
            <li class="chat-message" v-for="(item,i) in items" :key="i"
                :class="item.user==='bot'? 'chat-reply':'chat-question'">
              <div v-html="item.html? item.html : item.message || ''">
              </div>
            </li>
          </ul>
        </div>
        <div class="chat-input">
          <el-input type="text" v-model="message" class="message-input" @keydown.enter="onSendMessage" placeholder="请输入您的消息..."></el-input>
          <el-button @click="onSendMessage" :disabled="generating" :loading="generating"><el-icon><Position /></el-icon></el-button>
        </div>
      </div>
  </el-main>
</template>

<script>
// marked 用于将 markdown 格式文本转为 html
import * as marked from "marked";
// highlight 用于代码高亮
import hljs from "highlight.js";
import qs from "qs";

// 设置 marked 的选项及配置
marked.setOptions({
  // 自定义高亮代码块的渲染
  // lang 用于指定代码块的语言
  highlight: function (code) {
    return hljs.highlightAuto(code).value;
  }
});

export default {
  name: "GPTWindow2",
  data() {
    return {
      // 提问
      message: "",
      items: [
        {user: "bot", message: "欢迎使用 ChatGPT", html: ""}
      ],
      generating: false,
      usePublicApi: true,
      conversationId: ""
    }
  },

  methods: {
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
    onSendMessage() {
      console.log("message: ",this.message);

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

      let this_ = this;

      const data = {usePublicApi: this_.usePublicApi,prompt: this_.message,conversationId: this_.conversationId};
      const requestData = qs.stringify(data);

      console.log(requestData);

      let sse = new EventSource(`http://localhost:8081/api/v1/chatgpt/chat?`+requestData);

      this.generating = true;

      this_.items.push({user: 'user', message: this_.message});
      this_.message = "";

      sse.addEventListener("open",(function () {
        console.log('open');
        this_.generating = true;
        this_.items.push({user: 'bot', message: ''});
      }));

      sse.addEventListener("message",function (event) {
        if(event.data === "[DONE]") {
          sse.close();
          this_.generating = false;
          return;
        }
        let answer = JSON.parse(event.data).content;
        console.log(answer);

        let last = this_.items[this_.items.length - 1];
        last.message += answer;
        let words = last.message.split('');
        let html = words.join('');
        last.html = marked.marked(html);
        document.querySelectorAll('code').forEach((block) => {
          if (!block.classList.contains('hljs')) {
            block.classList.add('hljs');
          }
        });
      });

      sse.addEventListener("error",function (event) {
        console.log("error: "+event.data);
        this_.generating = false;
        sse.close();
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

</style>