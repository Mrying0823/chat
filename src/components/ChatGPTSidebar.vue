<template>
  <div :class="this.$store.getters.getDarkMode ? 'night-mode-sidebar' : 'sidebar'">
    <!-- 新会话按钮 -->
    <el-button :class="this.$store.getters.getDarkMode ? 'night-mode-create-btn': 'create-btn'" @click="createConversation" size="large">
        <el-icon><Plus /></el-icon>
        新建会话
    </el-button>

    <!-- 会话列表 -->
    <el-scrollbar class="conversation-list">
      <ul class="conversation-ul">
        <li v-for="conversation in conversationList" :key="conversation.conversationId">
          <div
              @click="selectConversation(conversation.conversationId)"
              :class="{
                'conversation-active': selectedConversationIndex === conversation.conversationId && !this.$store.getters.getDarkMode,
                'night-mode-conversation-active': selectedConversationIndex === conversation.conversationId && this.$store.getters.getDarkMode,
                'conversation': !this.$store.getters.getDarkMode,
                'night-mode-conversation': this.$store.getters.getDarkMode
              }"
          >
            <el-icon class="icon-chat-round" v-if="!editConfirm || !deleteConfirm"><ChatRound /></el-icon>
            <el-icon class="icon-chat-round" v-else-if="editConfirm && selectedConversationIndex === conversation.conversationId"><EditPen /></el-icon>
            <el-icon class="icon-chat-round" v-else-if="deleteConfirm && selectedConversationIndex === conversation.conversationId"><Delete /></el-icon>
            <span class="span-conversation-name-length" v-html="conversation.conversationName"></span>
            <!-- 会话名称修改输入框 -->
            <el-input class="input-edit-name" :class="{'anti-transparent': this.$store.getters.getDarkMode}" v-if="selectedConversationIndex === conversation.conversationId" v-show="editConfirm" v-model="editedName"></el-input>
            <!-- 编辑图标 -->
            <el-icon class="icon-edit-pen" v-if="selectedConversationIndex === conversation.conversationId" @click.stop="editClick(conversation.conversationName)" v-show="!deleteConfirm && !editConfirm"><EditPen /></el-icon>
            <!-- 保存修改 -->
            <el-icon class="icon-edit-pen" v-if="selectedConversationIndex === conversation.conversationId" v-show="editConfirm" @click.stop="editConversationName(conversation.conversationId,conversation.conversationName)"><Check /></el-icon>
            <!-- 删除图标 -->
            <el-icon class="icon-edit-pen" v-if="selectedConversationIndex === conversation.conversationId" v-show="deleteConfirm" @click.stop="deleteConversation(conversation.conversationId)"><Check /></el-icon>
            <!-- 确认删除 -->
            <el-icon class="icon-delete" v-if="selectedConversationIndex === conversation.conversationId" v-show="!deleteConfirm && !editConfirm" @click.stop="deleteClick"><Delete /></el-icon>
            <!-- 取消操作 -->
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

          const newConversation = {
            conversationId: "",
            userId: "",
            conversationType: 0,
            createTime: 0,
            conversationName: "新建会话",
            firstMessage: ""
          }

          newConversation.conversationId = newMessage;

          // 使用unshift方法将新会话添加到列表的首位
          this.conversationList.unshift(newConversation);

          this.selectConversation(this.conversationList[0].conversationId);
        }
      },
      // 这个选项可以在组件初始化时立即触发观察者
      immediate: true,
    }
  },

  data() {
    return {
      // 会话修改后的名称
      editedName: "",
      editConfirm: false,
      deleteConfirm: false,
      conversationList: [],
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
    // 修改会话名称
    editConversationName(index,currentName) {
      let this_ = this;

      if(currentName === this_.editedName) {
        this_.repeatClick();
        return;
      }

      if(this_.editedName && index) {
        doPost("/chatgpt/editConversationName",{conversationId:index,conversationName: this_.editedName}).then(response => {
          if(response && response.data.code === 200) {
            this_.conversationList.forEach(function (conversation) {
              if(conversation.conversationId === index) {
                conversation.conversationName = this_.editedName;
              }
            })
          }
        });
      }else {
        this_.repeatClick();
        this_.$message({
          type: "error",
          center: true,
          message: "会话名称不为空"
        })
      }
      this_.repeatClick();
    },
    // 取消编辑或删除操作
    repeatClick() {
      if(this.editConfirm) {
        this.editConfirm = !this.editConfirm;
      }

      if(this.deleteConfirm) {
        this.deleteConfirm = !this.deleteConfirm;
      }
    },
    // 点击编辑图标
    editClick(currentName) {
      // 获取当前会话名称
      this.editedName = currentName;
      this.editConfirm = true;
    },
    // 删除会话
    deleteConversation(index) {
      doPost("/chatgpt/deleteConversation",{conversationId: index}).then(response => {
        if(response && response.data.code === 200) {

          // 从 conversationList 中过滤掉对应的会话
          this.conversationList = this.conversationList.filter(conversation => conversation.conversationId !== index);

          this.selectConversation("");
        }
      });
    },
    // 点击删除图标
    deleteClick() {
      this.deleteConfirm = true;
    },
    // 切换会话
    selectConversation(index) {

      if(this.selectedConversationIndex !== index) {
        // 自动切换笔记专题重置图标
        this.deleteConfirm = false;

        this.editConfirm = false;
      }

      this.selectedConversationIndex = index;

      this.$emit("tran-conversationId",index);

      console.log("running",this.$store.getters.getLastSelectedConversation.selectedConversationIndex);

      if(index === this.lastSelectedConversation.selectedConversationIndex && this.lastSelectedConversation.messageList) {
        this.$emit("tran-messageList",this.lastSelectedConversation.messageList);
      }else if(index) {

        doPost("/chatgpt/getMessages", {conversationId: index}).then(response => {
          if (response && response.data.code === 200) {
            this.messageList = response.data.list;
            // 在组件中触发 mutation 来更新数据
            this.$store.commit('updateLastSelectedConversation', {selectedConversationIndex: index,messageList: this.messageList});
            this.$emit("tran-messageList", this.messageList);
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

      // 清屏
      this.selectConversation("");
    },
  },
  computed: {
    lastSelectedConversation() {
      return this.$store.getters.getLastSelectedConversation;
    }
  },
  mounted() {

    this.selectConversation(this.$store.getters.getLastSelectedConversation.selectedConversationIndex);

    // 启动计时器
    this.$store.dispatch('startSessionExpiryTimer');

    toGet("/chatgpt/getConversation").then(response => {
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