<template>
  <NavBar/>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-note-container' : 'note-container'">
    <el-aside class="note-side">
      <NoteSidebar/>
    </el-aside>
    <NoteQuillEditor class="note-body"/>
  </el-container>
</template>

<script>
import NavBar from "@/components/NavBar";
import NoteSidebar from "@/components/NoteSidebar";
import NoteQuillEditor from "@/components/NoteQuillEditor";


export default {
  name: "NoteView",
  components:{NoteQuillEditor, NoteSidebar, NavBar},
  data() {
    return {
    }
  },
  methods: {
  }
}
</script>

<style scoped>
.note-container {
  width: 100%; /* 设置容器宽度为100% */
  box-sizing: border-box; /* 设置盒模型为border-box，确保内边距和边框不会增加容器的宽度 */
  height: 96vh;
  padding-top: 0.9%;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
  font-family: "Source Code Pro Light", sans-serif;
  background: #E6F4F1;
  position: fixed;
}

.night-mode-note-container {
  width: 100%; /* 设置容器宽度为100% */
  box-sizing: border-box; /* 设置盒模型为border-box，确保内边距和边框不会增加容器的宽度 */
  height: 96vh;
  padding-top: 0.9%;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
  font-family: "Source Code Pro Light", sans-serif;
  position: fixed;
  background-color: #18392F;
}

.note-body {
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
}

.note-side {
  width: auto;
  height: 100%;
  box-sizing: border-box;
}
</style>
