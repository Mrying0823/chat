<template>
  <NavBar/>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-chat-container': 'chat-container'" style="overflow: hidden">
    <el-header>
      <SearchBar/>
    </el-header>
    <el-main>
      <QuestionList/>
    </el-main>
  </el-container>
</template>

<script>
import NavBar from "@/components/NavBar";
import QuestionList from "@/components/QuestionList";
import SearchBar from "@/components/SearchBar";
export default {
  name: "QuestionListView",
  components: {SearchBar, QuestionList, NavBar}
}
</script>

<style scoped>
</style>