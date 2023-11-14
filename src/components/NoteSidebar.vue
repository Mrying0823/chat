<template>
  <div :class="this.$store.getters.getDarkMode ? 'night-mode-sidebar' : 'sidebar'">
    <!-- 新键笔记专题按钮 -->
    <el-button :class="this.$store.getters.getDarkMode ? 'night-mode-category-create-btn': 'note-create-btn'" @click="createNoteCategory" size="large">
      <el-icon><Plus /></el-icon>
      新建笔记专题
    </el-button>

    <!-- 新建笔记专题列表，参照 chatgpt 样式，直接使用 -->
    <el-scrollbar class="conversation-list">
      <ul class="conversation-ul">
        <li v-for="category in noteCategoryList" :key="category.categoryId">
          <div
              @click="selectCategoryNote(category.categoryId)"
              :class="{
                'conversation-active': selectedNoteCategoryIndex === category.categoryId && !this.$store.getters.getDarkMode,
                'night-mode-conversation-active': selectedNoteCategoryIndex === category.categoryId && this.$store.getters.getDarkMode,
                'conversation': !this.$store.getters.getDarkMode,
                'night-mode-conversation': this.$store.getters.getDarkMode
              }"
          >
            <el-icon class="icon-chat-round" v-if="!editConfirm || !deleteConfirm"><Collection /></el-icon>
            <el-icon class="icon-chat-round" v-else-if="editConfirm && selectedNoteCategoryIndex === category.categoryId"><EditPen /></el-icon>
            <el-icon class="icon-chat-round" v-else-if="deleteConfirm && selectedNoteCategoryIndex === category.categoryId"><Delete /></el-icon>
            <span class="span-conversation-name-length" v-html="category.categoryTitle"></span>
            <!-- 会话名称修改输入框 -->
            <el-input class="input-edit-name" :class="{'anti-transparent': this.$store.getters.getDarkMode}" v-if="selectedNoteCategoryIndex === category.categoryId" v-show="editConfirm" v-model="editedTitle"></el-input>
            <!-- 编辑图标 -->
            <el-icon class="icon-edit-pen" v-if="selectedNoteCategoryIndex === category.categoryId" @click.stop="editClick(category.categoryTitle)" v-show="!deleteConfirm && !editConfirm"><EditPen /></el-icon>
            <!-- 保存修改 -->
            <el-icon class="icon-edit-pen" v-if="selectedNoteCategoryIndex === category.categoryId" v-show="editConfirm" @click.stop="editNoteCategoryName(category.categoryId,category.categoryTitle)"><Check /></el-icon>
            <!-- 删除图标 -->
            <el-icon class="icon-edit-pen" v-if="selectedNoteCategoryIndex === category.categoryId" v-show="deleteConfirm" @click.stop="deleteNoteCategory(category.categoryId)"><Check /></el-icon>
            <!-- 确认删除 -->
            <el-icon class="icon-delete" v-if="selectedNoteCategoryIndex === category.categoryId" v-show="!deleteConfirm && !editConfirm" @click.stop="deleteClick"><Delete /></el-icon>
            <!-- 取消操作 -->
            <el-icon class="icon-delete" v-if="selectedNoteCategoryIndex === category.categoryId" v-show="deleteConfirm || editConfirm" @click.stop="repeatClick"><Close /></el-icon>
          </div>
        </li>
      </ul>
    </el-scrollbar>
    <el-drawer
        v-model="openDrawer"
        :with-header="false"
        direction="rtl"
        size="30%"
        style="position: absolute;height: 92%;top: 8%;font-family: 'Source Code Pro Light', sans-serif;font-size: 14px"
    >
      <el-icon v-show="!creating" class="icon-circle-plus" @click="createNewNote"><CirclePlus /></el-icon>
      <ul class="nav-ul">
        <li v-for="(item,i) in noteList" :key="i" class="nav-li">
          <div @click.prevent="openNote(item)"><el-icon class="icon-note-title"><Notebook /></el-icon>{{item.noteTitle}}<el-divider/></div>
        </li>
      </ul>
    </el-drawer>
  </div>
</template>

<script>

import {doPost, toGet} from "@/axios/httpRequest";

export default {
  name: "NoteSidebar",
  // props: ["conversationId"],

/*  watch: {
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

          // 使用 unshift 方法将新会话添加到列表的首位
          this.conversationList.unshift(newConversation);

          this.selectConversation(this.conversationList[0].conversationId);
        }
      },
      // 这个选项可以在组件初始化时立即触发观察者
      immediate: true,
    }
  },*/

  data() {
    return {
      // 抽屉里的笔记
      noteList: [{
        noteId: "",
        noteTitle: "测试 1",
        noteContent: "",
        createTime: "",
        userId: "",
        categoryId: ""
      }],
      // 打开抽屉
      openDrawer: false,
      // 笔记专题修改后的名称
      editedTitle: "",
      editConfirm: false,
      deleteConfirm: false,
      noteCategoryList: [{
        categoryId: "",
        userId: "",
        createTime: 0,
        categoryTitle: "新建笔记专题",
      }],
/*      messageList: [{
        "messageId": "",
        "messageDirection": 0,
        "conversationId": "",
        "content": "",
        "createTime": 0
      }],*/
      selectedNoteCategoryIndex: "",
    };
  },
  methods: {
    // 修改笔记专题名称
    editNoteCategoryName(index,currentName) {
      let this_ = this;

      if(currentName === this_.editedTitle) {
        this_.repeatClick();
        return;
      }

      if(this_.editedTitle && index) {
        doPost("/note/editNoteCategoryTitle",{categoryId: index, categoryTitle: this_.editedTitle}).then(response => {
          if(response && response.data.code === 200) {
            this_.noteCategoryList.forEach(function (category) {
              if(category.categoryId === index) {
                category.categoryTitle = this_.editedTitle;
              }
            })
          }
        });
      }else {
        this_.repeatClick();
        this_.$message({
          type: "error",
          center: true,
          message: "笔记专题名称不为空"
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
    editClick(currentTitle) {
      // 获取当前会话名称
      this.editedTitle = currentTitle;
      this.editConfirm = true;
    },
    // 删除笔记专题
    deleteNoteCategory(index) {
      doPost("/note/deleteNoteCategory",{categoryId: index}).then(response => {
        if(response && response.data.code === 200) {

          // 从 conversationList 中过滤掉对应的会话
          this.noteCategoryList = this.noteCategoryList.filter(category => category.categoryId !== index);

          this.selectCategoryNote("");
        }
      });
    },
    // 点击删除图标
    deleteClick() {
      this.deleteConfirm = true;
    },
    // 切换会话
    selectCategoryNote(index) {

      if(this.selectedNoteCategoryIndex !== index) {
        // 自动切换笔记专题重置图标
        this.deleteConfirm = false;

        this.editConfirm = false;
      }

      this.selectedNoteCategoryIndex = index;

      if(!this.editConfirm && !this.deleteConfirm) {
        // 打开抽屉
        this.openDrawer = true;
      }

      // this.$emit("tran-conversationId",index);

/*      if(index === this.lastSelectedConversation.selectedConversationIndex && this.lastSelectedConversation.messageList) {
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
      }*/

/*      if(index) {

        doPost("/chatgpt/getMessages", {conversationId: index}).then(response => {
          if (response && response.data.code === 200) {
            this.messageList = response.data.list;
            // 在组件中触发 mutation 来更新数据
            this.$store.commit('updateLastSelectedConversation', {selectedConversationIndex: index,messageList: this.messageList});
            this.$emit("tran-messageList", this.messageList);
          }
        });
      }*/
    },
    createNoteCategory(event) {
      // 使按钮失焦
      let target = event.target
      if (target.nodeName === 'I') {
        target = event.target.parentNode
      }
      target.blur()

      doPost("/note/createNoteCategory").then(response => {
        if(response && response.data.code === 200) {
          const newNoteCategory = {
            categoryId: response.data.noteCategoryId,
            userId: "",
            createTime: 0,
            categoryTitle: "新建笔记专题",
          }

          // 使用 unshift 方法将新会话添加到列表的首位
          this.noteCategoryList.unshift(newNoteCategory);

          this.selectCategoryNote(this.noteCategoryList[0].categoryId);
        }
      });
    },
  },
/*  computed: {
    lastSelectedNoteCategory() {
      return this.$store.getters.getLastSelectedNoteCategory;
    }
  },*/
  mounted() {

    // this.selectConversation(this.$store.getters.getLastSelectedConversation.selectedConversationIndex);

    // 启动计时器
    // this.$store.dispatch('startSessionExpiryTimer');

    toGet("/note/getNoteCategory").then(response => {
      if(response && response.data.code === 200) {
        this.noteCategoryList = response.data.list;
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
.note-create-btn {
  width: 13vw;
  color: white;
  background-color: transparent;
  border-color: white;
  font-family: "Source Code Pro Light", sans-serif;
  padding-right: 50%;
}

.note-create-btn .el-icon {
  margin-right: 10%;
  margin-bottom: 3%;
  font-size: 14px;
}

.note-create-btn:hover,
.note-create-btn:active{
  /* 鼠标悬停状态的按钮背景颜色和文本颜色 */
  background-color: #1ab2b2;
  color: white;
  border-color: white;
}

.night-mode-category-create-btn {
  width: 13vw;
  color: white;
  background-color: transparent;
  border-color: white;
  font-family: "Source Code Pro Light", sans-serif;
  padding-right: 50%;
}

.night-mode-category-create-btn .el-icon {
  margin-right: 10%;
  margin-bottom: 3%;
  font-size: 14px;
}

.night-mode-category-create-btn:hover,
.night-mode-category-create-btn:active{
  /* 鼠标悬停状态的按钮背景颜色和文本颜色 */
  background-color: #324B4B;
  color: white;
  border-color: white;
}

.icon-note-title {
  vertical-align: middle; /* 垂直居中对齐 */
  bottom: 0.35vh;
  margin-right: 2%; /* 调整图标与文本之间的间距 */
  font-size: 14px;
}

.icon-circle-plus {
  color: #cecece;
  cursor: pointer;
  font-size: 16px;
}

.icon-circle-plus:hover{
  color: black;
}
</style>