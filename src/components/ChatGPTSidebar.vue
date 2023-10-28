<template>
  <div class="sidebar">
    <!-- 侧边栏标题 -->
    <div class="sidebar-title">ChatGPT 会话</div>

    <!-- 会话列表 -->
    <div class="conversation-list">
      <div v-for="(conversation, index) in conversations" :key="index" @click="selectConversation(index)">
        {{ conversation.title }}
      </div>
    </div>

    <!-- 新会话按钮 -->
    <el-button @click="createConversation">新会话</el-button>
  </div>
</template>

<script>
export default {
  name: "ChatGPTSidebar"
}
</script>

<style scoped>
.sidebar {
  width: 200px;
  background: #f0f0f0;
  padding: 10px;
}

.sidebar-title {
  font-weight: bold;
}

.conversation-list {
  margin-top: 10px;
}

.conversation-list div {
  cursor: pointer;
  margin: 5px 0;
  padding: 5px;
  background: #ddd;
  border: 1px solid #999;
  border-radius: 5px;
  text-align: center;
}


</style>