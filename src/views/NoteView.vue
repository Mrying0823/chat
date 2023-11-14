<template>
  <NavBar></NavBar>
  <el-container class="note-container">
    <el-aside class="note-side">
      <NoteSidebar></NoteSidebar>
    </el-aside>
    <el-container>
      <el-header id="note-title">你好</el-header>
      <el-main id="note-body">
        <el-text>
          Hello World!
        </el-text>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import NavBar from "@/components/NavBar";
import * as marked from "marked";
import NoteSidebar from "@/components/NoteSidebar";

export default {
  name: "NoteView",
  components:{NoteSidebar, NavBar},
  data() {
    return {
    }
  },
  methods: {
    marked () {
      return marked(String(this.noteData.body))
    }
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
#data-shower{
  height: 100%;
}
#note-title{
  height: 5%;
  background-color: rgba(128, 128, 128, 0.062);
  border-radius: 1rem;
  margin: 2%;
  padding: 1% 0 1% 3%;
}
#note-body{
  margin: 2%;
  padding: 1% 5%;
  border-radius: 1rem;
  background-color: rgba(128, 128, 128, 0.034);
}

.note-side {
  width: auto;
  height: 100%;
  box-sizing: border-box;
}
</style>