<!DOCTYPE html>
<template>
  <header>
    <HeaderView/>
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
import HeaderView from "@/components/HeaderView.vue";

export default {
  name: 'App',

  components: {
    HeaderView
  },

  data: () => ({
    //
  }),
}
</script>