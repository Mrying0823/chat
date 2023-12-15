<template>
  <NavBar/>
<!--  <LoadingBar v-show="loadingDisplay.display"></LoadingBar>-->
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-chat-container': 'chat-container'" style="overflow: hidden">
    <el-header>
      <SearchBar
          :subject-search="true"
          :keyword-search="true"
          :difficulty-search="true"
          :created-date-search="true"
          :updated-date-search="true"
          :admin="false"
      />
    </el-header>
    <el-main>
      <QuestionDetails/>
      <QuestionList
          :table-data="tableData"
          :total-rows="totalRows"
      />
    </el-main>
  </el-container>
</template>

<script setup>
import NavBar from "@/components/NavBar";
import QuestionList from "@/components/QuestionList";
import SearchBar from "@/components/SearchBar";
// import LoadingBar from "@/components/LoadingBar"
import QuestionDetails from "@/components/QuestionDetails";

</script>

<style scoped>

</style>