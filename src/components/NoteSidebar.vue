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
        style="position: absolute;height: 92%;top: 8%;font-family: 'Source Code Pro Light', sans-serif;font-size: 14px;color: white"
        :style="{background: this.$store.getters.getDarkMode ? '#18392F' : '#00AAAD'}"
    >
      <el-icon v-show="!creating" class="icon-circle-plus" @click="createNewNote"><CirclePlus /></el-icon>
      <ul class="nav-ul">
        <li v-for="(note,i) in noteList" :key="i" class="nav-li">
          <div
              @click.prevent="openNote(note.noteId,note.noteContent)"
              :class="{
                'conversation-active': selectedNoteIndex === note.noteId && !this.$store.getters.getDarkMode,
                'night-mode-conversation-active': selectedNoteIndex === note.noteId && this.$store.getters.getDarkMode,
                'conversation': !this.$store.getters.getDarkMode,
                'night-mode-conversation': this.$store.getters.getDarkMode
              }"
          >
            <el-icon class="icon-note-title">
              <Notebook />
            </el-icon>
            <span class="span-noteTitle-length">{{note.noteTitle}}</span>
            <!-- 会话名称修改输入框 -->
            <el-input class="note-edit-title" :class="{'anti-transparent': this.$store.getters.getDarkMode}" v-if="selectedNoteIndex === note.noteId" v-show="editNoteConfirm" v-model="editedNoteTitle" @click.stop></el-input>
            <!-- 编辑图标 -->
            <el-icon class="note-edit-pen" v-if="selectedNoteIndex === note.noteId" @click.stop="editClickForNote(note.noteTitle)" v-show="!deleteNoteConfirm && !editNoteConfirm"><EditPen /></el-icon>
            <!-- 保存修改 -->
            <el-icon class="note-edit-pen" v-if="selectedNoteIndex === note.noteId" v-show="editNoteConfirm" @click.stop="editNoteTitle(note.noteId,note.noteTitle)"><Check /></el-icon>
            <!-- 删除图标 -->
            <el-icon class="note-edit-pen" v-if="selectedNoteIndex === note.noteId" v-show="deleteNoteConfirm" @click.stop="deleteNote(note.noteId)"><Check /></el-icon>
            <!-- 确认删除 -->
            <el-icon class="note-delete" v-if="selectedNoteIndex === note.noteId" v-show="!deleteNoteConfirm && !editNoteConfirm" @click.stop="deleteClickForNote"><Delete /></el-icon>
            <!-- 取消操作 -->
            <el-icon class="note-delete" v-if="selectedNoteIndex === note.noteId" v-show="deleteNoteConfirm || editNoteConfirm" @click.stop="repeatClickForNote"><Close /></el-icon>
          </div>
          <el-divider/>
        </li>
      </ul>
    </el-drawer>
  </div>
</template>

<script>

import {doPost, toGet} from "@/axios/httpRequest";

export default {
  name: "NoteSidebar",

  data() {
    return {
      // 笔记标题修改后的名称
      editedNoteTitle: "",
      editNoteConfirm: false,
      deleteNoteConfirm: false,
      // 创建笔记中
      creating: false,
      // 笔记 Id
      selectedNoteIndex: "",
      // 抽屉里的笔记
      noteList: [],
      // 打开抽屉
      openDrawer: false,
      // 笔记专题修改后的名称
      editedTitle: "",
      editConfirm: false,
      deleteConfirm: false,
      noteCategoryList: [],
      selectedNoteCategoryIndex: "",
    };
  },
  methods: {
    // eslint-disable-next-line no-unused-vars
    openNote(noteId,noteContent) {
      this.selectNote(noteId);

      this.$store.commit("updateLastSelectedNote", {noteId: noteId, noteContent: noteContent});

      this.openDrawer = false;
    },
    // 取消编辑或删除操作
    repeatClickForNote() {
      if(this.editNoteConfirm) {
        this.editNoteConfirm = !this.editNoteConfirm;
      }

      if(this.deleteNoteConfirm) {
        this.deleteNoteConfirm = !this.deleteNoteConfirm;
      }
    },
    // 点击删除图标
    deleteClickForNote() {
      this.deleteNoteConfirm = true;
    },
    // 点击编辑图标
    editClickForNote(currentTitle) {
      // 获取当前笔记专题名称
      this.editedNoteTitle = currentTitle;
      this.editNoteConfirm = true;
    },
    // 修改笔记标题
    editNoteTitle(index,currentTitle) {
      let this_ = this;

      if(currentTitle === this_.editedNoteTitle) {
        this_.repeatClickForNote();
        return;
      }

      if(this_.editedNoteTitle && index) {
        doPost("/note/editNoteTitle",{noteId: index, noteTitle: this_.editedNoteTitle}).then(response => {
          if(response && response.data.code === 200) {
            this_.noteList.forEach(function (note) {
              if(note.noteId === index) {
                note.noteTitle = this_.editedNoteTitle;
              }
            })
          }
        });
      }else {
        this_.repeatClickForNote();
        this_.$message({
          type: "error",
          center: true,
          message: "笔记标题不为空"
        })
      }
      this_.repeatClickForNote();
    },
    // 删除笔记
    deleteNote(index) {
      doPost("/note/deleteNote",{noteId: index}).then(response => {
        if(response && response.data.code === 200) {

          // 从 noteList 中过滤掉对应的笔记
          this.noteList = this.noteList.filter(note => note.noteId !== index);
        }
      });
    },
    // 获取笔记专题下的笔记列表
    getNoteListByCategoryId() {
      toGet("/note/getNote",{categoryId: this.selectedNoteCategoryIndex}).then(response => {
        if(response && response.data.code === 200) {
          this.noteList = response.data.list;
        }else {
          this.$message({
            type: "info",
            center: true,
            message: response.data.msg
          });
        }
      });
    },
    // 切换笔记
    selectNote(index) {

      if (this.selectedNoteIndex !== index) {
        // 自动切换笔记重置图标
        this.deleteNoteConfirm = false;

        this.editNoteConfirm = false;
      }

      this.selectedNoteIndex = index;
    },
    // 创建新的笔记
    createNewNote() {
      this.creating = true;
      doPost("/note/createNewNote", {categoryId: this.selectedNoteCategoryIndex}).then(response => {
        if(response && response.data.code === 200) {
          const newNote = {
            noteId: response.data.noteId,
            noteTitle: "新建笔记",
            noteContent: "",
            createTime: "",
            userId: "",
            categoryId: this.selectedNoteCategoryIndex
          }

          // 使用 unshift 方法将新笔记添加到列表的首位
          this.noteList.unshift(newNote);

          this.selectNote(this.noteList[0].noteId);

          this.creating = false;
        }
      });
    },

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
      // 获取当前笔记专题名称
      this.editedTitle = currentTitle;
      this.editConfirm = true;
    },
    // 删除笔记专题
    deleteNoteCategory(index) {
      doPost("/note/deleteNoteCategory",{categoryId: index}).then(response => {
        if(response && response.data.code === 200) {

          // 从 noteCategoryList 中过滤掉对应的会话
          this.noteCategoryList = this.noteCategoryList.filter(category => category.categoryId !== index);
        }
      });
    },
    // 点击删除图标
    deleteClick() {
      this.deleteConfirm = true;
    },
    // 切换笔记专题
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

        this.getNoteListByCategoryId();
      }
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
  mounted() {
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
  top: 0.1vh;
  margin-right: 2%; /* 调整图标与文本之间的间距 */
  font-size: 14px;
}

.icon-circle-plus {
  color: #cecece;
  cursor: pointer;
  font-size: 16px;
  left: 0.5vw;
}

.icon-circle-plus:hover{
  color: black;
}

.note-edit-title {
  position: fixed;
  width: 21vw;
  height: auto;
  left: 73.2vw;
}

.note-edit-pen {
  vertical-align: middle; /* 垂直居中对齐 */
  position: fixed;
  right: 4vw;
  font-size: 16px;
  color: #cecece;
}

.note-edit-pen:hover,
.note-edit-pen:active {
  color: white;
}

.note-delete {
  vertical-align: middle; /* 垂直居中对齐 */
  position: fixed;
  right: 2vw;
  font-size: 16px;
  color: #cecece;
}

.note-delete:hover,
.note-delete:active {
  color: white;
}

.span-noteTitle-length {
  width: 21vw; /* 适应你的容器宽度 */
  overflow: hidden; /* 隐藏溢出的内容 */
  white-space: nowrap; /* 防止文本换行 */
  text-overflow: ellipsis; /* 用省略号表示溢出内容 */
}
</style>