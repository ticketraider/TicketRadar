<!DOCTYPE html>
<template>
  <header>
    <HeaderViewVer/>
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
import HeaderViewVer from "@/components/HeaderComponent.vue";

export default {
  name: 'App',

  components: {
    HeaderViewVer
  },

  data: () => ({
    //
  }),
}
</script>