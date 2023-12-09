<template>
  <div style="padding-top: 8px;user-select: none">
    <el-row>
      <el-col :span="4">
        <el-space direction="vertical" alignment="normal">
          <span>专题</span>
          <el-select
              v-model="subject"
              placeholder="请选择专题"
          >
            <el-option
                v-for="item in subjects"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                style="font-family: 'Source Code Pro Light',sans-serif"
            />
          </el-select>
        </el-space>
      </el-col>
      <el-col :span="4">
        <el-space direction="vertical" alignment="normal">
          <span>题目</span>
          <el-autocomplete
              v-model="question"
              :fetch-suggestions="querySearch"
              :trigger-on-focus="false"
              clearable
              debounce="500"
              placeholder="请输入关键词"
              @select="handleSelect"
              style="width: 225px"
          />
        </el-space>
      </el-col>
      <el-col :span="4">
        <el-space direction="vertical" alignment="normal">
          <span>难度</span>
          <el-select
              v-model="difficulty"
              placeholder="请选择难度"
          >
            <el-option
                v-for="item in difficultyList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-space>
      </el-col>
      <el-col :span="4" style="padding-top: 29px">
        <el-button
            size="default"
            type="plain"
            @click="searchQuestion"
        >
          查询
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {ref} from "vue";

const difficulty = ref();

const difficultyList = [
  {
    value: '简单',
    label: '简单',
  },
  {
    value: '普通',
    label: '普通',
  },
  {
    value: '困难',
    label: '困难',
  },
  {
    value: '专家',
    label: '专家',
  }
]

const question = ref();

const querySearch = (queryString, cb) => {
  const results = queryString
      ? loadAll().filter(createFilter(queryString))
      : loadAll();
  // call callback function to return suggestions
  cb(results);
};

const createFilter = (queryString) => {
  return (restaurant) => {
    return restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0;
  };
};

const loadAll = () => [
  { value: 'vue', link: 'https://github.com/vuejs/vue' },
  { value: 'element', link: 'https://github.com/ElemeFE/element' },
  { value: 'cooking', link: 'https://github.com/ElemeFE/cooking' },
  { value: 'mint-ui', link: 'https://github.com/ElemeFE/mint-ui' },
  { value: 'vuex', link: 'https://github.com/vuejs/vuex' },
  { value: 'vue-router', link: 'https://github.com/vuejs/vue-router' },
  { value: 'babel', link: 'https://github.com/babel/babel' },
];

const handleSelect = (item) => {
  console.log(item);
};

const subject = ref();

const subjects = [
  {
    value: 'JAVA 基础',
    label: 'JAVA 基础',
  },
  {
    value: 'JAVA 异常',
    label: 'JAVA 异常',
  },
  {
    value: 'JAVA 集合',
    label: 'JAVA 集合',
  },
  {
    value: 'JAVA 线程',
    label: 'JAVA 线程',
  },
  {
    value: 'JAVA 文件',
    label: 'JAVA 文件',
  },
]
</script>

<style scoped>

</style>