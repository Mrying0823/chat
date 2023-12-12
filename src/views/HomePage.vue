<template>
  <NavBar></NavBar>
  <el-container :class="this.$store.getters.getDarkMode ? 'night-mode-home-container': 'home-container'">
    <el-main>
      <div id="particles-js"></div>
      <div :class="this.$store.getters.getDarkMode ? 'night-mode-container': 'container'">
        <el-row>
          <!-- 左边区域，网站介绍-->
          <div class="el-col-16">
            <el-carousel height="70vh" :interval="5000" class="left" >
              <el-carousel-item>
                <el-card shadow="hover" body-style="" class="card">
                  <div class="el-row">
                    <div class="el-col-16">
                      <img class="card-image"  src="../assets/img/作业.png" alt="hi">
                    </div>
                    <div class="el-col-8 card-intro">
                      <div class="card-title">
                        文档
                        <router-link to="/wiki/wikiView">
                          <el-button color="#5194da" style="margin: 5px 0 10px 0;" round plain>Explore</el-button>
                        </router-link>
                      </div>
                      <div class="card-text">
                        系统学习面向对象程序设计
                      </div>

                    </div>
                  </div>
                </el-card>
              </el-carousel-item>
              <el-carousel-item>
                <el-card shadow="hover" body-style="" class="card">
                  <div class="el-row">
                    <div class="el-col-16">
                      <img class="card-image" src="../assets/img/讨论.png" alt="hi">
                    </div>
                    <div class="el-col-8 card-intro">
                      <div class="card-title">
                        对话
                        <router-link to="/gpt/chatgpt">
                          <el-button color="#5194da" style="margin: 5px 0 10px 0;" round plain>Explore</el-button>
                        </router-link>
                      </div>
                      <div class="card-text">
                        对话 ChatGPT 辅助学习
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-carousel-item>
              <el-carousel-item>
                <el-card shadow="hover" body-style="" class="card">
                  <div class="el-row">
                    <div class="el-col-16">
                      <img class="card-image"  src="../assets/img/面试.png" alt="hi">
                    </div>
                    <div class="el-col-8 card-intro">
                      <div class="card-title">
                        问题
                        <router-link to="/question/questionList">
                          <el-button color="#5194da" style="margin: 5px 0 10px 0;" round plain>Explore</el-button>
                        </router-link>
                      </div>
                      <div class="card-text">
                        寻找问题的答案
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-carousel-item>
              <el-carousel-item>
                <el-card shadow="hover" body-style="" class="card">
                  <div class="el-row">
                    <div class="el-col-16">
                      <img class="card-image"  src="../assets/img/阅读.png" alt="hi">
                    </div>
                    <div class="el-col-8 card-intro">
                      <div class="card-title">
                        笔记
                        <router-link to="/note/userNote">
                          <el-button color="#5194da" style="margin: 5px 0 10px 0;" round plain>Explore</el-button>
                        </router-link>
                      </div>
                      <div class="card-text">
                        记录学习进度
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-carousel-item>
            </el-carousel>
          </div>
          <!-- 右边区域，网站名称介绍-->
          <div class="el-col-8 intro">
            <h1>Chat</h1>
            <h3> Talk to ChatGPT </h3>
          </div>
        </el-row>
      </div>
    </el-main>
  </el-container>
</template>


<script>
import NavBar from "@/components/NavBar";
import particlesJson from "@/assets/particles.json"

export default {
  name: "HomePage",
  components: {NavBar},
  mounted() {
    require('particles.js')
    // eslint-disable-next-line no-undef
    particlesJS('particles-js', particlesJson)
  }
}
</script>

<style scoped>
h1 {
  color: rgba(246, 247, 248, 0.82);
  font-size: 60px;
  margin: 0;
  user-select: none;
}

h3 {
  color: rgba(246, 247, 248, 0.82);
  font-size: 28px;
  margin: 0;
  user-select: none;
}
</style>