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
<!--          공연 정보를 담는 디브-->
          <div style="background-color: aqua; width: 800px; height: 600px">
<!--            안에 들어가는 정보들-->
            <div style="height: 60px;" >
              <div style="width: 30%; height: 100%; background-color: white; text-align: right">
                <h3>장소</h3>
              </div>
              <div style="width: 70%; height: 100%; background-color: black; text-align: left; margin-left: 200px">
                <h3>들어갈 장소 적기</h3>
              </div>
            </div>
            <div style="height: 60px; background-color: white;">
              <div style="width: 30%; height: 100%; background-color: aquamarine; text-align: right">
                <h3>기간</h3>
              </div>
            </div>
            <div style="height: 60px;">
              <div style="width: 30%; height: 100%; background-color: white; text-align: right">
                <h3>정보</h3>
              </div>
            </div>
            <div style="height: 60px; background-color: white;">
              <div style="width: 30%; height: 100%; background-color: aquamarine; text-align: right">
                <h3>가격</h3>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  </body>
  </html>
</template>
<style>
@import "../css/styles/TestStyle.css";
.event {
  background-color: darkorange;
  height: 2000px;
  width: 1200px;
  padding:50px;
  margin: 100px;
}
.testImg {
  background-position: center;
  background-size: cover;
  background-image: url("../assets/SampleImg.webp");
  border-radius: 10px;
  width: 400px;
  height: 600px;
}
</style>
<script>
import HeaderView from "@/components/HeaderView.vue";
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