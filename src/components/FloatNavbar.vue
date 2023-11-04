<template>
  <div
      class="callback float"
      @mousedown="down($event)"
      @touchstart="down($event)"
      @mousemove="move($event)"
      @touchmove="move($event)"
      @mouseup="end($event)"
      @touchend="end($event)"
      ref="fu"
      :class="{'callback-grab':isGrab}"
  >
    <el-popover
        trigger="click"
        placement="right"
        @click.prevent="click"
        :disabled="clickAllow"
        width="20%"
    >
      <template #reference>
        <el-icon><ChatLineRound /></el-icon>
      </template>
      <el-icon v-show="!generating" class="refresh-icon" @click="refreshQuestionList"><Refresh /></el-icon>
      <ul class="nav-ul">
        <li v-for="(item,i) in questionList" :key="i" class="nav-li">
          <div @click.prevent="sendQuestion(item)">{{item}}<el-divider/></div>
        </li>
      </ul>
      <div class="chat-input borderNone">
        <el-input class="message-input" type="textarea" v-model="userPrompt" @keydown="handleKeyDown" placeholder="您想要问哪方面的问题？" :autosize="{ minRows: 1, maxRows: 2}"></el-input>
        <el-button type="text" @click="onSendMessage" :disabled="generating" :loading="generating"><el-icon v-show="!generating"><Position /></el-icon></el-button>
      </div>
    </el-popover>
  </div>
</template>

<script>
import {doPost} from "@/axios/httpRequest";

export default {
  name: "FloatNavbar",
  data() {
    return {
      currentQuestion: "",
      // 问题列表生成
      generating: false,
      // 用户提示
      userPrompt: "",
      // 控制图标活跃
      isIconActive: true,
      questionList:[],
      isGrab:false,
      clickAllow: false,
      flags: false,
      // 切换图标
      isClosed: true,
      position: {
        x: 0,
        y: 0,
      },
      nx: "",
      ny: "",
      dx: "",
      dy: "",
      xPum: "",
      yPum: "",
    };
  },
  methods: {
    refreshQuestionList() {
      if(this.currentQuestion) {
        this.generating = true;
        doPost("/v1/chatgpt/refreshChatList",{prompt: this.currentQuestion}).then(response => {
          if(response && response.data.code === 200) {
            this.questionList = response.data.list;
            this.generating = false;
          }
        });
      }else {
        this.$message({
          type: "warning",
          center: true,
          message: "请先输入"
        });
      }
    },
    onSendMessage() {
      if(this.userPrompt) {
        this.currentQuestion = this.userPrompt;
        this.generating = true;
        doPost("/v1/chatgpt/chatList", {prompt: this.userPrompt}).then(response => {
          if (response && response.data.code === 200) {
            this.questionList = response.data.list;
            this.userPrompt = "";
            this.generating = false;
          }
        });
      }else {
        this.$message({
          type: "warning",
          center: true,
          message: "输入不为空"
        });
      }
    },
    handleKeyDown(event) {
      if (event.keyCode === 13 && !event.shiftKey) {
        event.preventDefault();
        this.onSendMessage();
      }
    },
    sendQuestion(question) {
      this.$emit("tran-question",question);
      console.log(question);
    },
    click() {
      if(!this.clickAllow) {
        return;
      }
      this.isIconActive = !this.isIconActive;
      this.isClosed = !this.isClosed;
    },
    down(event) {
      this.$refs.fu.focus();
      this.clickAllow = false;
      this.flags = true;
      var touch;
      if (event.touches) {
        touch = event.touches[0];
      } else {
        touch = event;
      }
      this.position.x = touch.clientX;
      this.position.y = touch.clientY;
      this.dx = this.$refs.fu.offsetLeft;
      this.dy = this.$refs.fu.offsetTop;
    },
    move(event) {
      if (this.flags) {
        requestAnimationFrame(() => {
          var touch;
          if (event.touches) {
            touch = event.touches[0];
          } else {
            touch = event;
          }
          this.nx = touch.clientX - this.position.x;
          this.ny = touch.clientY - this.position.y;
          this.xPum = this.dx + this.nx;
          this.yPum = this.dy + this.ny;
          let width = window.innerWidth - this.$refs.fu.offsetWidth; //屏幕宽度减去自身控件宽度
          let height = window.innerHeight - this.$refs.fu.offsetHeight; //屏幕高度减去自身控件高度
          this.xPum < 0 && (this.xPum = 0);
          this.yPum < 0 && (this.yPum = 0);
          this.xPum > width && (this.xPum = width);
          this.yPum > height && (this.yPum = height);
          this.$refs.fu.style.left = this.xPum + "px";
          this.$refs.fu.style.top = this.yPum + "px";
        });

        if(this.nx > 0 || this.ny > 0) {
          this.clickAllow = true;
          this.isGrab = true;
        }

        //阻止页面的滑动默认事件
        document.addEventListener(
            "touchmove",
            function () {
              event.preventDefault();
            },
            false
        );

        // 在mousemove事件中重新设置元素焦点
        this.$refs.fu.focus();
      }
    },
    //鼠标释放时候的函数
    end() {
      this.flags = false;
      this.isGrab = false;
    }
  }
};
</script>
<style scoped>
.borderNone >>>.el-textarea__inner {
  border: 0;
  resize: none;
  box-shadow: 0 0 0 0;
}
</style>

