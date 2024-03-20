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
    <div style="display: flex">
      <div class="event">
        <div style="width: 100%; height: 60px; background-color: #473baa; border-radius: 10px; margin: auto auto 10px auto; text-align: center">
          <h2 style="color: white">뮤지컬 오페라의 유령</h2>
        </div>
        <div class="이미지포함이벤트설명" style="display: flex; margin: auto auto 10px auto;">
          <div class="img">
          </div>
          <!--          공연 정보를 담는 디브-->
          <div
              style="width: 800px; height: 600px; background-color: #473baa; border-radius: 10px; margin: auto auto auto 10px;">
            <!--            안에 들어가는 정보들-->
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h5></h5>
              </div>
              <div class="공연정보2">
                <h5></h5>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h5>장소</h5>
              </div>
              <div class="공연정보2">
                <h5>블루스퀘어 신한카드홀</h5>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h5>기간</h5>
              </div>
              <div class="공연정보2">
                <h5>2024-03-29 ~ 2024-05-19</h5>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h5>정보</h5>
              </div>
              <div class="공연정보2">
                <h5>창작 초연의 놀라운 완성도를 보여준 뮤지컬!</h5>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h5>평점</h5>
              </div>
              <div class="공연정보2">
                <h5>4.0</h5>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h5>가격</h5>
              </div>
              <div class="공연정보2">
                <h5>R 석 - 160,000</h5>
                <h5>S 석 - 110,000</h5>
                <h5>A 석 - 80,000</h5>
              </div>
            </div>
          </div>
        </div>
        <div style="; width: 100%; height: 80px; text-align: right">
          <button type="button" style="width: 110px; height: 56px; background-color: #473baa; border-radius: 20px; ">
            <h5 style="color: white">좋아요</h5>
          </button>
        </div>
        <!--        여기 아래로는 리뷰들-->
        <div class="리뷰를담은디브">
          <div style="height: 60px; background-color: #473baa; border-radius: 10px">
            <div style="display: flex;">
              <h5 style="margin: 17px 10px auto auto; color: white;">리뷰 갯수 :</h5>
              <h5 style="margin: 17px auto auto 10px; color: white;">5</h5>
            </div>
          </div>
<!--리뷰 추가될때마다 1개씩 추가-->
          <ReviewCard/>
          <ReviewCard/>
          <ReviewCard/>
          <ReviewCard/>
          <ReviewCard/>
          <!--리뷰 페이지네이션-->
          <div class="text-center">
            <v-container>
              <v-row justify="center">
                <v-col cols="8">
                  <v-container class="max-width">
                    <v-pagination
                        v-model="page"
                        :length="2"
                        class="my-4"
                    ></v-pagination>
                  </v-container>
                </v-col>
              </v-row>
            </v-container>
          </div>

        </div>
      </div>
      <!--   예매시 날짜 선택창과 예매버튼-->

      <TicketingViewVer3/>

    </div>
  </main>
  </body>
  </html>
</template>
<style>
@import "../css/styles/TestStyle.css";

.리뷰를담은디브 {
  width: 100%;
}

.공연정보담고있는디브 {
  height: 60px;
  display: flex;
}

.공연정보1 {
  color: white;
  width: 160px;
  height: 100%;
  text-align: right;
}

.공연정보2 {
  color: white;
  width: 530px;
  height: 100%;
  margin-left: 40px
}

.event {
  background-color: #7980aa;
  border-radius: 20px;
  height: 2000px;
  width: 1200px;
  padding: 50px;
  margin: 100px;
}

.img {
  background-position: center;
  background-size: cover;
  background-image: url("../assets/SampleImg.webp");
  border-radius: 10px;
  width: 400px;
  height: 600px;
}
</style>
<script>
import ReviewCard from "@/components/ReviewCard.vue";
import TicketingViewVer3 from "@/components/TicketingViewVer3.vue";
export default {
  name: 'App',

  components: {
    ReviewCard,
    TicketingViewVer3
  },

  data: () => ({
    selectedItem: null,
    seat : [1,2,3,4]
  }),
}
</script>