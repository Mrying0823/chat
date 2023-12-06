<template>
  <PDF
      :src="pageData"
      ref="pdfViewer"
      progressColor="#f0f0f0"
      @wheel="handleWheel"
      :showPageTooltip="isShow"
      :showBackToTopBtn="isShow"
      :page="pageNo"
      :pdfWidth="storePage.pageWidth+'%'"
      :disableStream=true
      :disableAutoFetch=true
      :disableRange=true
      @on-page-change="handlePageChange"
      @on-pdf-init="handlePdfInit"
      @on-scroll="handleOnScroll"
  >
    <template v-slot:pageTooltip>
      <div style="width: 6%">
        <el-progress
            :text-inside="true"
            :stroke-width="20"
            :percentage="100"
            color="#324B4B"
            indeterminate
            duration="5"
        >
          <span>{{ currentPage }} / {{ totalPages }}</span>
        </el-progress>
      </div>
    </template>
    <!-- eslint-disable vue/no-unused-vars -->
    <template v-slot:backToTopBtn="{ scrollOffset }">
      <el-backtop visibility-height="0" :bottom="30" :right="30" @click="backToTop"/>
    </template>
  </PDF>
</template>

<script setup>
import PDF from "pdf-vue3";
import {useStorePageData} from "@/store/pageData";
import {onMounted, onUnmounted, ref} from "vue";
import * as pdfjs from 'pdfjs-dist/build/pdf';
import {PDFDocument} from 'pdf-lib';
import {useLoadingDisplay} from "@/store/loadingDisplay";
import {ElMessage} from "element-plus";

let storePage = useStorePageData();
let storeDisplay = useLoadingDisplay();
let loadingDisplay = useLoadingDisplay();

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

// 当前页码
let currentPage = ref(1);

let pageNo = ref(1);

const emits = defineEmits(['msg']);

// 页面改变
const handlePageChange = (newPage) => {
  // 在数组中找到对应 id 的对象
  let pageToUpdate = null;

  if(pageInfo) {
    pageToUpdate = findPageById(pageInfo.id,storePage.pageCategory);
  }

  // 如果找到了对应 id 的对象，则更新它的 page 字段
  if (pageToUpdate) {
    if(pageBeginNo > 1) {
      pageToUpdate.page = pageBeginNo+newPage-1;
      //  pdf-vue3 从 1 开始计数
      pageNo.value = newPage;
      currentPage.value = pageBeginNo+newPage-1;
      localStorage.setItem("pageCategory",JSON.stringify(storePage.pageCategory));
    }else {
      pageToUpdate.page = newPage;
      pageNo.value = newPage;
      currentPage.value = newPage;
      localStorage.setItem("pageCategory",JSON.stringify(storePage.pageCategory));
    }

    if(loadedPages === newPage) {
      storeDisplay.loadMoreOrHistory = true
      console.log("storeDisplay.loadMoreOrHistory",storeDisplay.loadMoreOrHistory);
      // emits('msg',{pdfKey: new Date().getTime()});
    }
  }
};

// 已加载的总页码
let loadedPages;

// 跳转至上一次阅读的页面
const handlePdfInit = (pdf) => {
  setTimeout(() => {
    loadingDisplay.display = false;
  },3000);

  loadedPages = pdf.numPages;

  if(!storeDisplay.loadMoreOrHistory) {
    pageNo.value = loadedPages;
  }
};

// 分页加载
let pageData = ref();

function range(start, end) {
  let length = end - start + 1;
  return Array.from({ length }, (_, i) => start + i - 1);
}

async function extractPdfPage(arrayBuff) {
  const pdfSrcDoc = await PDFDocument.load(arrayBuff);
  const pdfNewDoc = await PDFDocument.create();
  const pages = await pdfNewDoc.copyPages(pdfSrcDoc, range(pageBeginNo, pageBeginNo+10));
  pages.forEach((page) => pdfNewDoc.addPage(page));
  return await pdfNewDoc.save();
}

let totalPages = ref(1);

const loadPages = async () => {
  try {
    const pdfDocument = await pdfjs.getDocument(storePage.pageInfo.pageSrc).promise;

    // 获取总页码
    totalPages.value = pdfDocument.numPages;

    pdfDocument.getData().then((pdf) => {
      extractPdfPage(pdf).then((newPdf) => {
        pageData.value = newPdf;
      });
    });
  } catch (error) {
    console.error('Error loading pages:', error);

    setTimeout(() => {
      ElMessage({
        message: '服务繁忙，请稍后重试',
        type: 'warning',
        center: true
      });

      loadingDisplay.display = false;
    }, 3000);
  }
};

let isShow = ref(false);

// 初始化计时器
let timer;

async function extractHistoryPdfPage(arrayBuff) {
  const pdfSrcDoc = await PDFDocument.load(arrayBuff);
  const pdfNewDoc = await PDFDocument.create();
  const pages = await pdfNewDoc.copyPages(pdfSrcDoc, range(pageBeginNo-10, pageBeginNo));
  pages.forEach((page) => pdfNewDoc.addPage(page));
  return await pdfNewDoc.save();
}

const loadHistoryPages = async () => {
  try {
    const pdfDocument = await pdfjs.getDocument(storePage.pageInfo.pageSrc).promise;

    // 获取总页码
    totalPages.value = pdfDocument.numPages;

    pdfDocument.getData().then((pdf) => {
      extractHistoryPdfPage(pdf).then((newPdf) => {
        pageData.value = newPdf;
      });
    });
  } catch (error) {
    console.error('Error loading pages:', error);

    setTimeout(() => {
      ElMessage({
        message: '服务繁忙，请稍后重试',
        type: 'warning',
        center: true
      });

      loadingDisplay.display = false;
    }, 3000);
  }
};

const handleOnScroll = (scrollOffset) => {
  // 设置 isShow 为 true
  isShow.value = true;

  // 重置定时器
  clearTimeout(timer);

  // 设置 isShow 为 true
  isShow.value = true;

  // 在3秒后将 isShow 设置为 false
  timer = setTimeout(() => {
    isShow.value = false;
  }, 3000);

  // eslint-disable-next-line no-empty
  if(scrollOffset === 0) {
    storeDisplay.loadMoreOrHistory = false;
    emits('msg',{pdfKey: new Date().getTime()});
  }
}

// 返回页码顶部
const backToTop = () => {
  pageNo.value = 1;
}

// eslint-disable-next-line no-unused-vars
let pageBeginNo;

onMounted(() => {
  // 初始化 PDF.js
  pdfjs.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.0.269/pdf.worker.mjs';

  // 获取记录页码
  const pageCategory = JSON.parse(localStorage.getItem("pageCategory"));
  const page = findPageById(pageInfo.id,pageCategory);

  pageBeginNo = page.page;
  currentPage.value = pageBeginNo;

  if(storeDisplay.loadMoreOrHistory) {
    loadPages();
  }else {
    loadHistoryPages();
  }
});

let pdfViewer = ref();

// 在组件销毁时清理资源
onUnmounted(() => {
  // 在这里进行清理操作，释放资源
});
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
