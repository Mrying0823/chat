<template>
  <div class="nav darkcyan">
    <ul>
      <li><router-link :to="{path: '/'}"><i class="fa fa-home"></i></router-link></li>
      <li><router-link :to="{path: '/gpt/chatGPT'}"><i class="fa fa-comments-o"></i><span>ChatGPT </span></router-link></li>
    </ul>
  </div>

  <br>
  <br>
</template>

<script>
export default {
  name: "NavBar"
}
</script>

<style scoped>

</style>