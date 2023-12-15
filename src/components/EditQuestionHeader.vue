<template>
  <div class="left-action-box">
    <div class="title-time-box">
      <div class="title">
        <span class="text">{{ question }}</span>
      </div>
      <div class="time">最近更新：{{ new Date() }}</div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const question = router.currentRoute.value.query.question;
</script>

<style scoped>
.left-action-box {
  display: flex;
  border-bottom: 1px solid #f1f1f1;
  user-select: none;
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

</style>