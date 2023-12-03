<template>
  <PDF
      :src="storePage.pageInfo.pageSrc"
      ref="pdfViewer"
      progressColor="#f0f0f0"
      @wheel="handleWheel"
      :page="pageNo"
      :pdfWidth="storePage.pageWidth+'%'"
      :disableStream=true
      :disableAutoFetch=true
      :disableRange=true
      @on-page-change="handlePageChange"
      @on-pdf-init="handlePdfInit"
  />
</template>

<script setup>
import PDF from "pdf-vue3";
import {useStorePageData} from "@/store/pageData";
import {ref} from "vue";

let storePage = useStorePageData();

// ctrl + 滚轮实现缩放
// eslint-disable-next-line no-unused-vars
const handleWheel = (event) => {
  if (event.ctrlKey) {
    // 如果按下了Ctrl键
    // 阻止浏览器的默认缩放行为
    event.preventDefault();

    // 计算缩放比例，可以根据实际需求进行调整
    const scaleDelta = 0.02;
    const delta = event.deltaY > 0 ? -scaleDelta : scaleDelta;

    // 设置新的文档宽度，确保在合适的区间内
    storePage.pageWidth = Math.max(50, Math.min(100, storePage.pageWidth + delta * storePage.pageWidth));
  }
};

// 递归查找具有特定 id 值的对象
function findPageById(id, items) {
  for (const item of items) {
    if (item.id === id) {
      return item; // 找到匹配的对象
    }

    if (item.children && item.children.length > 0) {
      const foundInChildren = findPageById(id, item.children);
      if (foundInChildren) {
        return foundInChildren; // 在子级中找到匹配的对象
      }
    }
  }

  return null; // 没有找到匹配的对象
}

let pageInfo = storePage.pageInfo;

// 页面改变
const handlePageChange = (newPage) => {
  // 在数组中找到对应 id 的对象
  let pageToUpdate = null;

  if(pageInfo) {
    pageToUpdate = findPageById(pageInfo.id,storePage.pageCategory);
  }

  // 如果找到了对应 id 的对象，则更新它的 page 字段
  if (pageToUpdate) {
    pageToUpdate.page = newPage;
    localStorage.setItem("pageCategory",JSON.stringify(storePage.pageCategory));
  }
};

let pageNo = ref(1);

// 跳转至上一次阅读的页面
const handlePdfInit = () => {
  const pageCategory = JSON.parse(localStorage.getItem("pageCategory"));
  if(pageCategory) {
    const page = findPageById(pageInfo.id,pageCategory);
    pageNo.value = page.page;
  }
};
</script>

<style>
/* 设置滚动条的轨道（整个滚动条的背景）样式 */
::-webkit-scrollbar {
  width: 6px; /* 设置滚动条的宽度 */
}

/* 设置滚动条的滑块（拖动滚动条时的小方块）样式 */
::-webkit-scrollbar-thumb {
  background-color: #eeeeef;
  border-radius: 6px; /* 圆角边框 */
}

/* 鼠标悬停在滚动条上时的滑块样式 */
::-webkit-scrollbar-thumb:hover {
  background-color: #e5e4e4;
}
</style>
