<template>
  <NavBar></NavBar>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-chat-container': 'chat-container'">
    <el-aside v-show="storeDisplay.showMenu" :style="{ width: storeDisplay.rightAsideWidth + 'px' }">
      <WikiLeftSidebar></WikiLeftSidebar>
    </el-aside>
    <el-container>
      <el-header>
        <WikiRightHeader></WikiRightHeader>
      </el-header>
      <el-main style="user-select: none">
        <suspense>
          <WikiPdf :key="pdfKey"></WikiPdf>
        </suspense>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import WikiLeftSidebar from "@/components/WikiLeftSidebar";
import NavBar from "@/components/NavBar";
import WikiRightHeader from "@/components/WikiRightHeader";
import {useStoreDisplay} from "@/store/wikiDisplay";
import {defineAsyncComponent, onMounted, ref, watchEffect} from "vue";
import {useStorePageData} from "@/store/pageData";

let storeDisplay = useStoreDisplay();
let storePage = useStorePageData();

const WikiPdf = defineAsyncComponent(() => import('@/components/WikiPdf'));

const pdfKey = ref(new Date().getTime());

const reloadPdf = () => {
  // 更新 key，触发组件的销毁和重新加载
  pdfKey.value = new Date().getTime()
};

onMounted(() => {
  watchEffect(() => {
    reloadPdf();
    console.log(storePage.pageInfo.pageSrc);
  },{ deep: true });
});
</script>

<style scoped>

</style>
