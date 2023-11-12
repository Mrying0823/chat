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
                        文章
                        <router-link to="/articles">
                          <el-button color="#5194da" style="margin: 5px 0 10px 0;" round plain>Explore</el-button>
                        </router-link>
                      </div>
                      <div class="card-text">
                        分类查看文章，发表知识总结，分享您的思考
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
                        提供 ChatGPT 接口，通过 Token 或 OpenAI Api-Key 访问，学习与工作的好帮手
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
                        刷题
                        <router-link to="/problems">
                          <el-button color="#5194da" style="margin: 5px 0 10px 0;" round plain>Explore</el-button>
                        </router-link>
                      </div>
                      <div class="card-text">
                        速通高频面试题，记录回答音频，ChatGPT 辅助解答，完善您的答案
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
</style>