<!DOCTYPE html>
<template>

  <html lang="ko">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket Radar</title>
  </head>
  <body>
  <header>
    <HeaderView/>
  </header>

  <main>
    <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
      <CardView/>
      <CardView/>
      <CardView/>
      <CardView/>
      <CardView/>
    </div>
    <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
      <CardView/>
      <CardView/>
      <CardView/>
      <CardView/>
      <CardView/>
    </div>
    <router-view />
  </main>

  <footer>
    <p>© 2024 티켓팅 웹사이트 티켓 레이더. 팀 코더스 게이트.</p>
  </footer>
  </body>
  </html>
</template>
<style>
@import "../css/styles/TestStyle.css";
</style>
<script>
import CardView from "@/components/CardView.vue";
import HeaderView from "@/components/HeaderView.vue";
export default {
  name: 'App',

  components: {

    HeaderView,
    CardView
  },

  data: () => ({
    //
  }),
}
</script>