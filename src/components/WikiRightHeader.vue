<template>
  <el-row>
    <el-col :span="24">
      <div class="left-action-box">
        <div class="collapse-box">
          <el-button @click="turnLeftCollapse" v-if="storeDisplay.showMenu" text :icon="ElIconFold" class="fold-btn"></el-button>
          <el-button @click="turnLeftCollapse" v-else text :icon="ElIconExpand" class="fold-btn"></el-button>
        </div>
        <div v-if="storePage.pageInfo.id" class="title-time-box">
          <div class="title">
            <span class="text">{{ storePage.pageInfo.label }}</span>
          </div>
          <div class="time">最近更新：{{ new Date() }}</div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script setup>
import {
  Fold as ElIconFold,
  Expand as ElIconExpand,


} from '@element-plus/icons-vue'
import {ref, defineEmits} from 'vue';
import {useRouter} from "vue-router";
import { ElMessageBox, ElMessage } from 'element-plus'
import {useStoreDisplay} from "@/store/wikiDisplay";
import {useStorePageData} from "@/store/pageData";

let router = useRouter();
let storePage = useStorePageData();
let storeDisplay = useStoreDisplay();
// eslint-disable-next-line no-unused-vars
let storeUser = null;
let storeSpace = null;
// eslint-disable-next-line no-unused-vars
const emit = defineEmits(['collapse']);

let turnLeftCollapse = () => {
  storeDisplay.showMenu = !storeDisplay.showMenu;
  setTimeout(() => {
    if (storeDisplay.showMenu) {
      storeDisplay.rightAsideWidth = 301;
    } else {
      storeDisplay.rightAsideWidth = 1;
    }
  }, 100);
};

// eslint-disable-next-line no-unused-vars
const editWiki = () => {
  // 锁定页面并进入编辑页面
  storePage.pageIsUnlock = false;
  // eslint-disable-next-line no-unused-vars
  let param = {pageId: storePage.pageInfo.id};
/*  pageApi.pageLock(param).then(() => {
    router.push({path: '/page/edit', query: {pageId: storePage.pageInfo.id}});
  });*/
}
// eslint-disable-next-line no-unused-vars
const showCommentWiki = () => {
  storeDisplay.commentShow = !storeDisplay.commentShow;
}
let pageAuthDialogVisible = ref(false);
// eslint-disable-next-line no-unused-vars
const editWikiAuth = () => {
  pageAuthDialogVisible.value = true;
}
// eslint-disable-next-line no-unused-vars
const showOpenPage = () => {
  if (storeSpace.spaceInfo.openDoc !== 1) {
    ElMessage.warning('该空间未开放，无法查看开放文档地址');
  } else {
    let routeUrl = router.resolve({
      path: '/page/share/view',
      query: {pageId: storePage.pageInfo.id, space: storeSpace.spaceInfo.uuid}
    });
    window.open(routeUrl.href, '_blank');
  }
}
// eslint-disable-next-line no-unused-vars
const deleteWikiPage = () => {
  ElMessageBox.confirm('确定要删除此页面及其所有子页面吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
/*    pageApi.pageDelete({pageId: storePage.pageInfo.id}).then(() => {
      pageApi.pageList({spaceId: storeSpace.chooseSpaceId}).then((json) => {
        storePage.wikiPageList = json.data || []
      }).then(()=>{
        router.push({path: '/home', query: {spaceId: storePage.pageInfo.spaceId}});
      })
    });*/
  }).catch((e) => {
    console.log(e)
  });
}
// 下载为Word
let downloadFormRef = ref();
let downloadFormParam = ref({url: 'zyplayer-doc-wiki/page/download', param: {}});
// eslint-disable-next-line no-unused-vars
const exportWord = () => {
  downloadFormParam.value.param = {pageId: storePage.pageInfo.id};
  setTimeout(() => downloadFormRef.value.submit(), 0);
}
// 手机扫码
let mobileScanDialogVisible = ref(false);
// eslint-disable-next-line no-unused-vars
const showMobileView = () => {
  if (storeSpace.spaceInfo.openDoc !== 1) {
    ElMessage.warning('该空间未开放，无法查看开放文档地址');
  } else {
    mobileScanDialogVisible.value = true;
  }
}
// eslint-disable-next-line no-unused-vars
const userSignOut = () => {
  // eslint-disable-next-line no-undef
  userApi.userLogout().then(() => {
    location.reload();
  });
}
let aboutDialogVisible = ref(false);
// eslint-disable-next-line no-unused-vars
const showAbout = () => {
  aboutDialogVisible.value = true;
}
// eslint-disable-next-line no-unused-vars
const showConsole = () => {
  window.open(import.meta.env.VITE_APP_BASE_API, '_blank')
}
</script>

<style scoped>
.left-action-box {
  display: flex;
  border-bottom: 1px solid #f1f1f1;
  user-select: none;
}

.left-action-box .collapse-box {
  line-height: 64px;
}

.left-action-box .collapse-box .fold-btn {
  font-size: 18px;
  padding: 4px 10px;
}

.left-action-box .title-time-box {
  flex: 1;
  padding: 5px 0 6px 10px;
  overflow: hidden;
}

.left-action-box .title-time-box .title {
  overflow: hidden;
  white-space: nowrap;
  -o-text-overflow: ellipsis;
  text-overflow: ellipsis;
  line-height: 28px;
}

.left-action-box .title-time-box .title .text {
  vertical-align: middle;
}

.left-action-box .title-time-box .time {
  font-size: 12px;
  overflow: hidden;
  white-space: nowrap;
  -o-text-overflow: ellipsis;
  text-overflow: ellipsis;
}

.left-action-box .title-setting-box .setting-title {
  font-size: 18px;
  line-height: 60px;
  padding-left: 10px;
}

.left-action-box .title-setting-box .setting-title .text {
  display: inline-block;
}

.left-action-box .title-time-box .title .i-icon {
  vertical-align: middle;
}

.left-action-box .title-time-box .title .i-icon svg {
  vertical-align: unset;
}

.left-action-box .title-setting-box .setting-title .tips-icon {
  vertical-align: 0.1em;
  margin-left: 6px;
}

.header-action-box {
  display: inline-block;
  line-height: 60px;
}

.header-action-box .disabled-btn-box {
  margin-right: 10px;
}

.header-action-box .hover-button {
  border: 0;
  color: #888;
}

.header-action-box .hover-button .i-icon svg {
  vertical-align: middle;
}

.header-action-box .hover-button:focus {
  color: #888;
  background: #fff;
}

.header-action-box .hover-button:hover {
  color: #888;
  background: #eaeaea;
}

.header-action-user-dropdown {
  width: 120px;
}

.header-action-more-dropdown {
  width: 140px;
}

.header-action-more-dropdown .delete {
  color: #f00;
}

.header-action-more-dropdown .delete.disabled {
  cursor: not-allowed;
  color: var(--el-text-color-disabled);
}

.header-action-more-dropdown .cant-hover {
  cursor: default;
}

.header-action-more-dropdown .cant-hover:hover {
  background: #fff;
}
</style>
