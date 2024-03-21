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
        <div
            style="width: 100%; height: 60px; background-color: #473baa; border-radius: 10px; margin: auto auto 10px auto; text-align: center">
          <h1 style="color: white">뮤지컬 오페라의 유령</h1>
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
                <h3></h3>
              </div>
              <div class="공연정보2">
                <h3></h3>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h3>장소</h3>
              </div>
              <div class="공연정보2">
                <h3>블루스퀘어 신한카드홀</h3>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h3>기간</h3>
              </div>
              <div class="공연정보2">
                <h3>2024-03-29 ~ 2024-05-19</h3>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h3>정보</h3>
              </div>
              <div class="공연정보2">
                <h3>창작 초연의 놀라운 완성도를 보여준 뮤지컬!</h3>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h3>평점</h3>
              </div>
              <div class="공연정보2">
                <h3>4.0</h3>
              </div>
            </div>
            <div class="공연정보담고있는디브">
              <div class="공연정보1">
                <h3>가격</h3>
              </div>
              <div class="공연정보2">
                <h3>R 석 - 160,000</h3>
                <h3>S 석 - 110,000</h3>
                <h3>A 석 - 80,000</h3>
              </div>
            </div>
          </div>
        </div>
        <div style="; width: 100%; height: 80px; text-align: right">
          <button type="button" style="width: 110px; height: 56px; background-color: #473baa; border-radius: 20px; ">
            <h3 style="color: white">좋아요</h3>
          </button>
        </div>
        <!--        여기 아래로는 리뷰들-->
        <div class="리뷰를담은디브">
          <div style="height: 60px; background-color: #473baa; border-radius: 10px">
            <div style="display: flex;">
              <h3 style="margin: 17px 10px auto auto; color: white;">리뷰 갯수 :</h3>
              <h3 style="margin: 17px auto auto 10px; color: white;">5</h3>
            </div>
          </div>

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
      <div style="margin: 150px 10px auto 1px; background-color: #7980aa; border-radius: 5px">
        <v-container>
          <v-row justify="space-around">
            <v-date-picker
                v-model="date"
                :allowed-dates="allowedDates"
                max="2024-05-19"
                min="2024-03-29"
            ></v-date-picker>
          </v-row>
        </v-container>
        <v-container style="background-color: white; width: 80%; border-radius: 10px;">
          <v-select
              v-model="selectedItem"
              :items="seat"
              label="예매할 좌석 갯수"
          ></v-select>
          <v-select
              v-model="selectedItem"
              :items="grade"
              label="좌석 등급"
          ></v-select>
          <v-select
              v-model="selectedItem"
              :items="seatNumber"
              label="좌석 번호"
          ></v-select>
        </v-container>
        <button type="button" style="width: 100%; height: 50px; background-color: #473baa; border-radius: 10px; ">
          <h2 style="color: white">예매하기</h2>
        </button>
      </div>
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
export default {
  name: 'App',

  components: {
    ReviewCard,
  },

  data: () => ({
    selectedItem: null,
    seat : [1,2,3,4],
    grade: ['R', 'S', 'A'],
    seatNumber: [
        1,
        2,
        3,
        4,
        5
    ]
  }),
}
</script>