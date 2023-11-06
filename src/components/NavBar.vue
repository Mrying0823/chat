<template>
  <div class="nav darkcyan">
    <ul>
      <li><router-link :to="{path: '/'}"><i class="fa fa-home"></i></router-link></li>
      <li><router-link :to="{path: '/gpt/chatGPT'}"><i class="fa fa-comments-o"></i><span>ChatGPT </span></router-link></li>
      <li class="nav-login" v-show="!isAvatarShow"><router-link :to="{path: '/user/login'}"><i class="fa fa-user"></i><span>登录</span></router-link></li>
      <li class="nav-login" v-show="isAvatarShow">
        <router-link :to="{path: '/'}">
          <i class="fa fa-user"></i>
          <el-avatar class="nav-login-avatar" shape="square">
            {{ $store.getters.getUser.name }}
          </el-avatar>
        </router-link>
      </li>

    </ul>
  </div>

  <br>
  <br>
</template>

<script>
export default {
  name: "NavBar",
  data() {
    return {
      isAvatarShow: false
    }
  },
  mounted() {
    if(this.$store.getters.getUser.account) {
      this.isAvatarShow = false;
    }
  }
}
</script>

<style scoped>

</style>