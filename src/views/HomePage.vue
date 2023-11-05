<template>
  <NavBar></NavBar>
  <el-container class="home-container">
    <el-main>
      <div id="particles-js"></div>
      <div class="container">
        <el-row>
          <!-- 左边区域，网站介绍-->
          <div class="el-col-16">
            <el-carousel height="70vh" :interval="5000" class="left" >
              <el-carousel-item>
                <el-card shadow="hover" body-style="" class="card">
                  <div class="el-row">
                    <div class="el-col-16">
                      <img class="card-image"  src="" alt="hi">
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
                        提供ChatGPT接口，通过Token或OpenAI Api-Key访问，学习与工作的好帮手
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-carousel-item>
              <el-carousel-item>
                <el-card shadow="hover" body-style="" class="card">
                  <div class="el-row">
                    <div class="el-col-16">
                      <img class="card-image"  src="" alt="hi">
                    </div>
                    <div class="el-col-8 card-intro">
                      <div class="card-title">
                        刷题
                        <router-link to="/problems">
                          <el-button color="#5194da" style="margin: 5px 0 10px 0;" round plain>Explore</el-button>
                        </router-link>
                      </div>
                      <div class="card-text">
                        速通高频面试题，记录回答音频，ChatGPT辅助解答，完善您的答案
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
.home-container {
  width: 100%; /* 设置容器宽度为100% */
  box-sizing: border-box; /* 设置盒模型为border-box，确保内边距和边框不会增加容器的宽度 */
  height: 100vh;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
  font-family: "Source Code Pro Light", sans-serif;
  position: fixed;
  background-color: #00C9CD;
}

#particles-js {
  position: absolute;
  height: 100%;
  width: 100%;
}

.container {
  font-family: "Source Code Pro Light",sans-serif;
  background-color: #00C9CD;
  background-repeat: no-repeat;
  padding-left: 5%;
  padding-right: 8%;
  box-sizing: border-box;
}

.left {
  height: 68vh;
  margin: 12vh 0 10vh 0;
}

.intro {
  padding-left: 10%;
  margin: auto;
}

h1 {
  color: rgba(246, 247, 248, 0.82);
  font-size: 60px;
  margin: 0;
}

h3 {
  color: rgba(246, 247, 248, 0.82);
  font-size: 28px;
  margin: 0;
}

.card {
  height: 70vh;
  background-color: rgba(255, 255, 255, 0.58);
  border: none;
}

.card-image {
  height: 70vh;
}


.card-title {
  margin: 25vh 20px 10px 20px;
  color: rgba(75, 98, 147, 0.6);
  font-size: xx-large;
  font-weight: bold;
}
</style>