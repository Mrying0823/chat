<template>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-chat-container': 'chat-container'">
    <el-scrollbar>
      <el-aside width="300px" :style="{ width: storeDisplay.rightAsideWidth + 'px' }" v-show="storeDisplay.showMenu">
        <div class="logo">{{ nowSpaceShow.name }}</div>
        <div style="padding: 10px; box-sizing: border-box; user-select: none">
          <el-input v-model="searchKeywords" @keyup="searchByKeywords" placeholder="搜索文档" style="padding-right: 5px"/>
          <div class="wiki-page-tree-box">
            <el-tree
                ref="wikiPageTreeRef"
                :current-node-key="storePage.pageInfo.id"
                :data="wikiPageList"
                :default-expanded-keys="wikiPageExpandedKeys"
                :expand-on-click-node="true"
                :filter-node-method="filterPageNode"
                :props="defaultProps"
                accordion
                highlight-current
                node-key="id"
                @node-click="handleNodeClick"
            >
              <template #default="{ data }">
                <div class="page-tree-node">
                  <el-tooltip :content="data.label" placement="top-start" :show-after="1000">
                  <span class="label">
                    <el-icon>
                      <el-icon-folder v-if="data.isFolder"/>
                      <el-icon-document v-else/>
                    </el-icon>
                    <span class="text">{{ data.label }}</span>
                  </span>
                  </el-tooltip>
                </div>
              </template>
            </el-tree>
          </div>
          <!--请保留申明-->
<!--          <div class="build-info">本文档使用<a target="_blank" href="https://gitee.com/zyplayer/zyplayer-doc"> zyplayer-doc </a>构建</div>-->
        </div>
      </el-aside>
    </el-scrollbar>
    <RightResize v-show="storeDisplay.showMenu" v-model:value="storeDisplay.rightAsideWidth" @change="rightAsideWidthChange"></RightResize>
  </el-container>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useRouter, useRoute} from "vue-router";
import {
  Folder as ElIconFolder,
  Document as ElIconDocument
} from '@element-plus/icons-vue'
import RightResize from "@/components/RightResize"
import {useStoreDisplay} from "@/store/wikiDisplay";
import {useStorePageData} from "@/store/pageData";

let storePage = useStorePageData();

// eslint-disable-next-line no-unused-vars
let leftCollapse = ref(true);

let defaultProps = ref({children: 'children', label: 'name'});

// 空间搜索相关
let spaceUuid = ref('');
// eslint-disable-next-line no-unused-vars
let nowPageId = ref('');

let nowSpaceShow = ref({name: "学习文档"});
// 搜索的输入内容
let searchKeywords = ref('');
// 页面展示相关
let wikiPageList = ref(storePage.pageCategory);

let wikiPageExpandedKeys = ref([]);
// eslint-disable-next-line no-unused-vars
let rightAsideWidth = ref(300);

let route = useRoute();
// eslint-disable-next-line no-unused-vars
let router = useRouter();

onMounted(() => {
  spaceUuid.value = route.query.space || ''
  /*  getSpaceInfo()
    doGetPageList(null)*/
});

const filterPageNode = (value, data) => {
  if (!value || !data.label) return true
  // issues:I2CG72 忽略大小写
  let name = data.label.toLowerCase()
  return name.indexOf(value.toLowerCase()) !== -1
}

const handleNodeClick = (data) => {
  if (storePage.pageInfo.id === data.id || data.isFolder) {
    return;
  }
  // nowPageId.value = data.id
  storePage.pageInfo = data;

  // 重置页面大小
  storePage.pageWidth = 80;
/*  router.push({
    path: '/page/share/view',
    query: {pageId: data.id, space: spaceUuid.value}
  })*/
}

let wikiPageTreeRef = ref();

const searchByKeywords = () => {
  wikiPageTreeRef.value.filter(searchKeywords.value)
}

let storeDisplay = useStoreDisplay();

const rightAsideWidthChange = (width) =>{
  storeDisplay.rightAsideWidth = width
  storeDisplay.commentShow =width
}
/*const doGetPageList = () => {
  pageApi.openPageList({space: spaceUuid.value}).then((json) => {
    wikiPageList.value = json.data || []
    nowPageId.value = ''
  })
}
const getSpaceInfo = () => {
  pageApi.openSpaceInfo({space: spaceUuid.value}).then((json) => {
    nowSpaceShow.value = json.data
  })
}*/
</script>

<style scoped>
html,
body,

pre {
  margin: 0;
  white-space: pre-wrap;
  font-size: 14px;
  font-family: "Source Code Pro Light",sans-serif;
}

.el-menu {
  box-sizing: border-box;
  border-right: 0;
  margin-right: 3px;
}

.el-header {
  color: #333;
  line-height: 40px;
  text-align: right;
  height: 40px !important;
}

.doc-body-box {
  overflow-x: hidden;
  overflow-y: auto;
  width: 100%;
  padding: 10px;
  border-left: 1px solid #f1f1f1;
  box-sizing: border-box;
}

.el-tree {
  margin-right: 3px;
}

.logo {
  border-bottom: 1px solid #f1f1f1;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  padding: 5px 10px 5px 5px;
  height: 40px;
  line-height: 30px;
  font-size: 25px;
  text-align: center;
  user-select: none;
}

.icon-collapse {
  float: left;
  font-size: 25px;
  color: #aaa;
  cursor: pointer;
  position: fixed;
}

.icon-collapse:hover {
  color: #ccc;
}

.comment-box .head {
  float: left;
  border-radius: 50%;
  margin-right: 10px;
  width: 45px;
  height: 45px;
  line-height: 45px;
  text-align: center;
  color: #fff;
}

.build-info {
  position: fixed;
  bottom: 0;
  left: 0;
  text-align: center;
  padding: 5px 0;
  color: #aaa;
  font-size: 12px;
  width: 240px;
  user-select: none;
}

.build-info a {
  color: #4183c4;
  cursor: pointer;
  text-decoration: none;
}

.wiki-page-tree-box {
  overflow-y: auto;
  overflow-x: hidden;
  padding-top: 5px;
  padding-bottom: 30px;
  user-select: none;
}

.wiki-page-tree-box .el-tree-node__content {
  height: 35px;
  position: relative;
}

.wiki-page-tree-box .el-tree-node__content .page-tree-node {
  width: 100%;
}

.wiki-page-tree-box .el-tree-node__content .page-tree-node .label .el-icon {
  vertical-align: middle;
}

.wiki-page-tree-box .el-tree-node__content .page-tree-node .label .text {
  margin-left: 5px;
  vertical-align: middle;
  max-width: calc(100% - 40px);
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.wiki-page-tree-box .el-tree-node__content .page-tree-node .rename-input {
  width: 90%;
}

.wiki-page-tree-box .el-tree-node__content .page-tree-node .page-action-box {
  position: absolute;
  right: 0;
  top: 0;
  height: 35px;
  line-height: 35px;
  border-radius: 4px;
  display: none;
}

.wiki-page-tree-box .el-tree-node__content .page-tree-node .page-action-box .page-action-dropdown-btn {
  padding: 0 8px;
  height: 35px;
  margin-top: -1px;
}

.wiki-page-tree-box .el-tree-node__content .page-tree-node .page-action-box .el-button + .el-button {
  margin-left: 0;
}

.wiki-page-tree-box .el-tree-node__content .page-tree-node .page-action-box.renaming {
  display: none !important;
}

.wiki-page-tree-box .el-tree-node__content:hover .page-tree-node .page-action-box {
  display: block;
}
</style>
