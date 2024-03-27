<!DOCTYPE html>
<template>
  <header>
    <HeaderViewVer2/>
  </header>
  <body>
  <router-view/>
  </body>
  <footer>
    <p>© 2024 티켓팅 웹사이트 티켓 레이더. 팀 코더스 게이트.</p>
  </footer>
</template>
<style>
@import "./css/styles/TestStyle.css";
</style>
<script>
import HeaderViewVer2 from "@/components/HeaderComponentVer3.vue";

export default {
  name: 'App',

  components: {
    HeaderViewVer2
  },

  data: () => ({
    //
  }),
}
</script>