<template>
  <el-container class="chat-container">
    <el-aside class="chat-aside">
      <ChatGPTSidebar></ChatGPTSidebar>
    </el-aside>
      <el-container class="chat-container-message">
        <el-main>
          <el-scrollbar>
            <ul>
              <li v-for="(item,i) in items" :key="i"
                  :class="item.user==='bot'? 'chat-reply':'chat-question'">
                <div :class="item.user==='bot'? 'reply-info':'question-info'">
                  <div v-html="item.html? item.html : item.message || ''" :class="item.user==='bot'? 'reply-info-content':'question-info-content'"></div>
                </div>
              </li>
            </ul>
          </el-scrollbar>
        </el-main>
        <el-footer class="chat-footer">
          <div class="chat-input borderNone">
            <el-input class="message-input" type="textarea" v-model="message" @keydown="handleKeyDown" placeholder="请输入您的消息..." :autosize="{ minRows: 1, maxRows: 2}"></el-input>
            <el-button type="text" @click="onSendMessage" :disabled="generating" :loading="generating"><el-icon v-show="!generating"><Position /></el-icon></el-button>
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
  components: {ChatGPTSidebar},
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
    onSendMessage() {
      let this_ = this;

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

      this.generating = true;

      this_.items.push({user: 'user', message: this_.message});

      this.onGetMessage();
    },
    onGetMessage() {
      let this_ = this;

      const data = {usePublicApi: this_.usePublicApi,prompt: this_.message,conversationId: "6644696e5fb74a6b9607695ecced315c"};
      const requestData = qs.stringify(data);

      console.log(requestData);

      let sse = new EventSource(`http://localhost:8081/api/v1/chatgpt/chat?`+requestData,{withCredentials: true});

      // this_.message = "";

      sse.addEventListener("open",(function () {
        console.log('open');
        this_.generating = true;
        this_.message = "";
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
        sse.close();

        this_.$message({
          type: "error",
          center: true,
          message: "服务繁忙，请稍后重试"
        });

        this_.generating = true;

        setTimeout(() => {
          this_.generating = false;
        },10000);
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
.chat-container {
  width: 100%; /* 设置容器宽度为100% */
  padding: 20px; /* 设置容器内边距，可以根据需要进行调整 */
  box-sizing: border-box; /* 设置盒模型为border-box，确保内边距和边框不会增加容器的宽度 */
  height: 97vh;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
  font-family: "Source Code Pro Light", sans-serif;
  background: whitesmoke;
}

.chat-container-message {
  box-sizing: border-box;
  height: 100%;
  width: 100%;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
}

.chat-aside {
  padding-right: 20px;
  width: auto;
  height: 100%;
  box-sizing: border-box;
}

.chat-img{
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.chat-question {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.question-info {
  width: 90%;
  margin-left: 10px;
  text-align: right;
}

.question-info-content {
  max-width: 70%;
  padding: 10px;
  font-size: 14px;
  float: right;
  margin-right: 10px;
  position: relative;
  margin-top: 8px;
  background: #A3C3F6;
  text-align: left;
}

.question-info-content::after {
  position: absolute;
  right: -8px;
  top: 8px;
  content: '';
  border-left: 10px solid #A3C3F6;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}

.chat-reply {
  display: flex;
  margin-bottom: 20px;
}

.reply-info {
  margin-left: 10px;
  text-align: left;
}

.reply-time {
  font-size: 12px;
  color: rgba(51, 51, 51, 0.8);
  height: 20px;
  line-height: 20px;
  margin: -5px 0 0;
}

.reply-info-content {
  padding: 10px;
  font-size: 14px;
  background: #fff;
  position: relative;
  margin-top: 8px;
}

.reply-info-content::before {
  position: absolute;
  left: -8px;
  top: 8px;
  content: '';
  border-right: 10px solid #FFF;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}

.chat-input {
  background: whitesmoke;
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
}

.message-input {
  flex: 1;
}

.borderNone >>>.el-textarea__inner {
  border: 0;
  resize: none;
  box-shadow: 0 0 0 0;
}
</style>