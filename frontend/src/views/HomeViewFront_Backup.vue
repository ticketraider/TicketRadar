<!DOCTYPE html>
<template>

  <html lang="ko">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket Radar</title>
  </head>
  <body>
  <main>
    <div class="top5">
      <h1>평점 순 TOP.5</h1>
      <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
        <CardView/>
        <CardView/>
        <CardView/>
        <CardView/>
        <CardView/>
      </div>
    </div>
    <div class="top5">
      <h1>좋아요 순 TOP.5</h1>
      <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
        <CardView/>
        <CardView/>
        <CardView/>
        <CardView/>
        <CardView/>
      </div>
    </div>
    <div class="top5">
      <h1>오픈예정일 순 TOP.5</h1>
      <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
        <CardView/>
        <CardView/>
        <CardView/>
        <CardView/>
        <CardView/>
      </div>
    </div>
  </main>

  <footer>
    <p>© 2024 티켓팅 웹사이트 티켓 레이더. 팀 코더스 게이트.</p>
  </footer>
  </body>
  </html>
</template>
<style>
@import "../css/styles/TestStyle.css";

.top5 {
  height: 700px;
  width: 100%;
  padding: 100px;
}
</style>
<script>
import CardView from "@/components/trash/EventCardFront.vue";

export default {
  name: 'App',

  components: {
    CardView
  },

  data: () => ({
    //
  }),
}
</script>