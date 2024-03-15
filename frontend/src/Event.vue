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
    <div>
      <div class="event" >
        <div style="width: 100%; height: 80px; background-color: white">
          <h1 style="">공연 제목</h1>
        </div>
        <div class = "이미지포함이벤트설명" style="display: flex">
          <div class="testImg">
          </div>
          <div>

          </div>
          <div style="background-color: white; width: 150px; height: 600px; padding: 20px">
            <h3>장소</h3>
            <h3>공연기간</h3>
            <h3>공연정보</h3>
            <h3>가격</h3>
          </div>
          <div style="background-color: beige; width: 400px; height: 600px; padding: 20px">
            <h3>장소</h3>
            <h3>공연기간</h3>
            <h3>공연정보</h3>
            <h3>가격</h3>
          </div>
        </div>
      </div>
    </div>
  </main>
  </body>
  </html>
</template>
<style>
@import "./css/styles/TestStyle.css";
.event {
  background-color: darkorange;
  height: 2000px;
  width: 1300px;
  padding:50px;
  margin: 100px;
}
.testImg {
  background-position: center;
  background-size: cover;
  background-image: url("../src/assets/SampleImg.webp");
  border-radius: 10px;
  width: 400px;
  height: 600px;
}
</style>
<script>
import HeaderView from "@/views/HeaderView.vue";
export default {
  name: 'App',

  components: {

    HeaderView,
  },

  data: () => ({
    //
  }),
}
</script>