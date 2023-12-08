<template>
  <NavBar></NavBar>
  <LoadingBar v-show="loadingDisplay.display"></LoadingBar>
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
          <router-view :key="pdfKey" v-if="isShow" @msg="loadMorePage"/>
        </suspense>
<!--        <suspense>
          <WikiPdf :key="pdfKey" v-if="isShow" @msg="loadMorePage"></WikiPdf>
        </suspense>-->
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import WikiLeftSidebar from "@/components/WikiLeftSidebar";
import NavBar from "@/components/NavBar";
import WikiRightHeader from "@/components/WikiRightHeader";
import {useStoreDisplay} from "@/store/wikiDisplay";
// eslint-disable-next-line no-unused-vars
import {defineAsyncComponent, onMounted, ref, watchEffect} from "vue";
import {useStorePageData} from "@/store/pageData";
import LoadingBar from "@/components/LoadingBar";
import {useLoadingDisplay} from "@/store/loadingDisplay";

let storeDisplay = useStoreDisplay();
let storePage = useStorePageData();
let loadingDisplay = useLoadingDisplay();

// eslint-disable-next-line no-unused-vars
const WikiPdf = defineAsyncComponent(() => import('@/components/WikiPdf'));

let pdfKey = ref(new Date().getTime());

const reloadPdf = () => {
  // 更新 key，触发组件的销毁和重新加载
  pdfKey.value = new Date().getTime();
  loadingDisplay.display = true;
  isShow.value = true;
};

// eslint-disable-next-line no-unused-vars
const loadMorePage = (msg) => {
  pdfKey.value = msg.pdfKey;
  loadingDisplay.display = true;
}

let isShow = ref(false);

onMounted(() => {
  watchEffect(() => {
    if(storePage.pageInfo.pageSrc) {
      isShow.value = false;
      reloadPdf();
    }
  });
});
</script>

<style scoped>
</style>
