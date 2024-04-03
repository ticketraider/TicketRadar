<script setup xmlns:background-color="http://www.w3.org/1999/xhtml">

import ReviewList from "@/components/ReviewListComponent.vue";
import {ref, onMounted} from 'vue';
import axios from 'axios';
import {useRoute} from "vue-router";
import router from "@/router/router";

const event = ref(null);
const route = useRoute(); // useRoute()를 사용하여 현재 라우팅 정보를 가져옴

const checkLoginStatus = () => {
  const token = localStorage.getItem('token');
  if (!token) {
    alert("로그인이 필요한 기능입니다.");
    router.push({path: "/login"})
    // 로그인이 필요한 상황이므로, 로그인 페이지로 리다이렉트하는 로직도 추가할 수 있습니다.
    // 예: router.push('/login');
  } else {
    likeEvent()
    // 로그인 상태이므로, 필요한 로직을 수행합니다.
  }
}
const fetchEventDetail = async () => {
  const eventId = Number(route.params.eventId); // 이벤트 ID를 Long으로 변환
  try {
    const response = await axios.get(`http://localhost:8080/events/${eventId}`);
    event.value = response.data;
  } catch (error) {
    console.error('이벤트 상세 정보를 불러오는 동안 오류가 발생했습니다:', error);
  }
};
const likeEvent = async () => {
  const eventId = event.value.id; // 또는 Number(route.params.eventId) 로부터 이벤트 ID를 가져올 수 있습니다.
  const token = localStorage.getItem('token'); // 실제로는 사용자 인증 토큰을 여기에 할당합니다.

  try {
    await axios.post(`http://localhost:8080/likes?eventId=${eventId}`, {}, {
      headers: {
        Authorization: `Bearer ${token}` // 인증 토큰을 Bearer 토큰으로 사용
      }
    });
    console.log('좋아요 성공!');
    window.location.reload()
    // 필요하다면 여기에서 좋아요 카운트를 업데이트하는 로직을 추가할 수 있습니다.
  } catch (error) {
    console.error('좋아요 처리 중 오류가 발생했습니다:', error);
  }
};

onMounted(() => {
  const eventId = route.params.eventId; // 라우팅 정보에서 eventId를 가져옴
  fetchEventDetail(eventId); // 받아온 이벤트 ID 값을 사용하여 이벤트 상세 정보 호출
});
</script>

<template>
  <div class="event-detail" v-if="event">
    <div style="width: 100%;  text-align: center; margin-bottom: 50px;">
      <h2 style="color: #392365; font-weight: bold">{{ event.title }}</h2>
    </div>
    <div class="이미지포함이벤트설명" style="display: flex; margin: auto auto 10px auto;">
      <div>
      <v-img class="img" :src="event.posterImage" cover alt="Event Poster">

      </v-img>
      </div>
      <!-- 공연 정보를 담는 디브-->
      <div style="width: 100%; height: 500px; margin-left:10px; background-color: #EEEAF1; border-radius: 10px; justify-content: center; align-items: center">
        <div style="text-align: right;">
          <button @click="checkLoginStatus" type="button" class="btn btn-light"
                  style="background-color: #ffffff; border-radius: 20px; margin-top:30px; margin-right:30px">
            <h5>좋아요 ❤️ {{ event.likeCount }} </h5>
          </button>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5 style = "font-weight: bold; ">장소</h5>
          </div>
          <div class="공연정보2">
            <h5>{{ event.place.name }}</h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5 style = "font-weight: bold; ">기간</h5>
          </div>
          <div class="공연정보2">
            <h5>{{ event.startDate }} ~ {{ event.endDate }}</h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5 style = "font-weight: bold; ">정보</h5>
          </div>
          <div class="공연정보2">
            <h5>{{ event.eventInfo }}</h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5 style = "font-weight: bold; ">평점</h5>
          </div>
          <div class="공연정보2">
            <h5>{{ event.averageRating }}</h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5 style = "font-weight: bold; ">가격</h5>
          </div>
          <div class="공연정보2">
            <h5>R 석 - {{ event.price.seatRPrice }} 원 </h5>
            <h5>S 석 - {{ event.price.seatSPrice }} 원</h5>
            <h5>A 석 - {{ event.price.seatAPrice }} 원</h5>
          </div>
        </div>

        </div>
    </div>


    <div class="리뷰를담은디브" style="margin-top: 50px;">
      <div style="height: 1px; width: 100%; background-color: #ffffff; border-radius: 10px; border: 1px solid #ccc;"></div>
      <div style="display: flex;">
        <h4 style="margin-top: 30px; color: #5d3d9c;"> ✏️ {{ event.reviewCount }}</h4>
        <h5 style="margin-top: 30px; margin-left: 5px; color: #1f1f1f;"> 건의 리뷰가 있습니다! </h5>
      </div>
      <ReviewList/>
    </div>
  </div>
</template>

<style scoped>
@import "../css/styles/TestStyle.css";

.리뷰를담은디브 {
  width: 100%;
}

.공연정보담고있는디브 {
  height: 60px;
  display: flex;
}

.공연정보1 {
  color: #5d3d9c;
  width: 85px;
  height: 100%;
  text-align: right;
  font-weight: bold;
}

.공연정보2 {
  color: #0c0126;
  width: 530px;
  height: 100%;
  margin-left: 40px
}

.event-detail {
  box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.2);
  background-color : white;
  border-radius: 20px;
  width: 70%;
  padding: 50px;
}

.img {
  border-radius: 10px;
  width: 300px;
  height: 500px;
}
</style>