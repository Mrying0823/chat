<template>
  <div class="sidebar">
    <!-- 新会话按钮 -->
    <el-button class="create-btn" @click="createConversation" size="large">新建会话</el-button>

    <!-- 会话列表 -->
    <el-scrollbar class="conversation-list">
      <ul class="conversation-ul">
        <li v-for="conversation in conversationList" :key="conversation.conversationId">
          <div class="conversation" v-html="conversation.conversationName" @click="selectConversation(conversation.conversationId)" :class="{'conversation-active':selectedConversationIndex === conversation.conversationId}"></div>
        </li>
      </ul>
    </el-scrollbar>
  </div>
</template>

<script>
import {doPost, toGet} from "@/axios/httpRequest";

export default {
  name: "ChatGPTSidebar",

  props: ["conversationId"],

  watch: {
    conversationId: {
      handler(newMessage) {
        // 执行额外的操作，例如转换文本，然后将结果保存在 otherVariable 中
        if(!this.conversationList[0].conversationId) {
          this.conversationList[0].conversationId = newMessage;
          this.selectConversation(newMessage);
        }
      },
      immediate: true, // 这个选项可以在组件初始化时立即触发观察者
    }
  },

  data() {
    return {
      conversationList: [{
        conversationId: "",
        userId: "",
        conversationType: 0,
        createTime: 0,
        conversationName: "",
        firstMessage: ""
      }],
      messageList: [{
        "messageId": "",
        "messageDirection": 0,
        "conversationId": "",
        "content": "",
        "createTime": 0
      }],
      selectedConversationIndex: null,
    };
  },
  methods: {
    selectConversation(index) {
      this.selectedConversationIndex = index;

      this.$emit("tran-conversationId",index);

      if(index) {
        doPost("/v1/chatgpt/getMessages",{conversationId: index}).then(response => {
          if(response && response.data.code === 200) {
            this.messageList = response.data.list;

            this.$emit("tran-messageList",this.messageList);
          }else {
            this.$message({
              type: "error",
              center: true,
              message: response.data.msg
            });
          }
        });
      }else {
        // 新建会话
        this.$emit("tran-messageList",[]);
      }
    },
    createConversation(event) {
      // 使按钮失焦
      let target = event.target
      if (target.nodeName === 'I') {
        target = event.target.parentNode
      }
      target.blur()

      // 遍历会话列表，查找是否已经存在 conversationId 为空的会话
      let conversationExists = false;

      for (let i = 0; i < this.conversationList.length; i++) {
        if (this.conversationList[i].conversationId === "") {
          conversationExists = true;
          this.$message({
            type: "warning",
            center: true,
            message: "不可重复创建空会话"
          });
        }
      }

      // 如果不存在，将新建的会话添加到列表中
      if (!conversationExists) {
        // 创建新会话并添加到 conversations 数组
        const newConversation = {
          conversationId: "",
          userId: "",
          conversationType: 0,
          createTime: 0,
          conversationName: "新建会话",
          firstMessage: ""
        };

        // 使用unshift方法将新会话添加到列表的首位
        this.conversationList.unshift(newConversation);

        // 自动选择新会话
        this.selectConversation("");

        this.$message({
          type: "info",
          center: true,
          message: "请开始对话吧！"
        });
      }
    },
  },
  mounted() {
    toGet("/v1/chatgpt/getConversation").then(response => {
      if(response && response.data.code === 200) {
        this.conversationList = response.data.list;

        // 在页面加载完成后，设置 selectedConversationIndex 为第一项的 conversationId
        this.selectConversation(this.conversationList[0].conversationId);
      }else {
        this.$message({
          type: "info",
          center: true,
          message: response.data.msg
        });
      }
    });
  }
}
</script>

<style scoped>
</style>