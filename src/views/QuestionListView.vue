<template>
  <NavBar/>
  <LoadingBar v-show="loadingDisplay.display"></LoadingBar>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-chat-container': 'chat-container'" style="overflow: hidden">
    <el-header>
      <SearchBar/>
    </el-header>
    <el-main>
      <QuestionList/>
    </el-main>
  </el-container>
</template>

<script setup>
import NavBar from "@/components/NavBar";
import QuestionList from "@/components/QuestionList";
import SearchBar from "@/components/SearchBar";
import LoadingBar from "@/components/LoadingBar"
import {useLoadingDisplay} from "@/store/loadingDisplay";

const loadingDisplay = useLoadingDisplay();

</script>

<style scoped>
</style>