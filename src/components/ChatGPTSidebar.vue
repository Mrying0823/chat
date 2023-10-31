<template>
  <div class="sidebar">
    <!-- 新会话按钮 -->
    <el-button class="create-btn" @click="createConversation" size="large">
        <el-icon><Plus /></el-icon>
        新建会话
    </el-button>

    <!-- 会话列表 -->
    <el-scrollbar class="conversation-list">
      <ul class="conversation-ul">
        <li v-for="conversation in conversationList" :key="conversation.conversationId">
          <div class="conversation" @click="selectConversation(conversation.conversationId)" :class="{'conversation-active':selectedConversationIndex === conversation.conversationId}">
            <el-icon class="icon-chat-round" v-if="!editConfirm || !deleteConfirm"><ChatRound /></el-icon>
            <el-icon class="icon-chat-round" v-else-if="editConfirm && selectedConversationIndex === conversation.conversationId"><EditPen /></el-icon>
            <el-icon class="icon-chat-round" v-else-if="deleteConfirm && selectedConversationIndex === conversation.conversationId"><Delete /></el-icon>
            <span v-html="conversation.conversationName"></span>
            <el-input class="input-edit-name" v-if="selectedConversationIndex === conversation.conversationId" v-show="editConfirm"></el-input>
            <el-icon class="icon-edit-pen" v-if="selectedConversationIndex === conversation.conversationId" @click.stop="editClick" v-show="!deleteConfirm && !editConfirm"><EditPen /></el-icon>
            <el-icon class="icon-edit-pen" v-if="selectedConversationIndex === conversation.conversationId" v-show="deleteConfirm" @click.stop="deleteConversation(conversation.conversationId)"><Check /></el-icon>
            <el-icon class="icon-edit-pen" v-if="selectedConversationIndex === conversation.conversationId" v-show="editConfirm" @click.stop="editConversationName(conversation.conversationId)"><Check /></el-icon>
            <el-icon class="icon-delete" v-if="selectedConversationIndex === conversation.conversationId" v-show="!deleteConfirm && !editConfirm" @click.stop="deleteClick"><Delete /></el-icon>
            <el-icon class="icon-delete" v-if="selectedConversationIndex === conversation.conversationId" v-show="deleteConfirm || editConfirm" @click.stop="repeatClick"><Close /></el-icon>
          </div>
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

        // 服务器端传来了新的 conversationId
        if(this.selectedConversationIndex !== newMessage) {

          this.newConversation.conversationId = newMessage;

          // 使用unshift方法将新会话添加到列表的首位
          this.conversationList.unshift(this.newConversation);

          this.selectConversation(this.conversationList[0].conversationId);
        }
      },
      // 这个选项可以在组件初始化时立即触发观察者
      immediate: true,
    }
  },

  data() {
    return {
      editConfirm: false,
      deleteConfirm: false,
      newConversation: {
        conversationId: "",
        userId: "",
        conversationType: 0,
        createTime: 0,
        conversationName: "新建会话",
        firstMessage: ""
      },
      conversationList: [{
        conversationId: "",
        userId: "",
        conversationType: 0,
        createTime: 0,
        conversationName: "新建会话",
        firstMessage: ""
      }],
      messageList: [{
        "messageId": "",
        "messageDirection": 0,
        "conversationId": "",
        "content": "",
        "createTime": 0
      }],
      selectedConversationIndex: "",
    };
  },
  methods: {
    // 取消编辑或删除操作
    repeatClick() {
      if(this.editConfirm) {
        this.editConfirm = !this.editConfirm;
      }

      if(this.deleteConfirm) {
        this.deleteConfirm = !this.deleteConfirm;
      }
    },
    editClick() {
      this.editConfirm = true;
    },
    deleteConversation(index) {
      doPost("/v1/chatgpt/deleteConversation",{conversationId: index}).then(response => {
        if(response && response.data.code === 200) {

          // 从 conversationList 中过滤掉对应的会话
          this.conversationList = this.conversationList.filter(conversation => conversation.conversationId !== index);

          this.selectConversation("");
        }
      });
    },
    deleteClick() {
      this.deleteConfirm = true;
    },
    selectConversation(index) {

      this.selectedConversationIndex = index;

      this.$emit("tran-conversationId",index);

      if(index) {
        doPost("/v1/chatgpt/getMessages",{conversationId: index}).then(response => {
          if(response && response.data.code === 200) {
            this.messageList = response.data.list;

            this.$emit("tran-messageList",this.messageList);
          }
        });
      }else {
        // 新建会话
        this.$emit("tran-messageList",[]);
      }

      // 切换会话重置删除图标
      this.deleteConfirm = false;
    },
    createConversation(event) {
      // 使按钮失焦
      let target = event.target
      if (target.nodeName === 'I') {
        target = event.target.parentNode
      }
      target.blur()

      // 清屏
      this.selectConversation("");
    },
  },
  mounted() {
    toGet("/v1/chatgpt/getConversation").then(response => {
      if(response && response.data.code === 200) {
        this.conversationList = response.data.list;

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