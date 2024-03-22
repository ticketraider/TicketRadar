<template>
  <div class="event-detail " v-if="event">
    <h1>이벤트 상세 정보</h1>
    <!-- 이벤트 정보를 표시하는 부분 -->
    <div class="event-info">
      <h2>{{ event.title }}</h2>
      <p>장소: {{ event.place }}</p>
      <p>기간: {{ event.startDate }} ~ {{ event.endDate }}</p>
      <p>정보: {{ event.eventInfo }}</p>
      <p>평점: {{ event.averageRating }}</p>
      <p>가격:</p>
      <ul>
        <li>
          R석 - {{ event.price.seatRPrice }}
        </li>
        <li>
          S석 - {{ event.price.seatSPrice }}
        </li>
        <li>
          A석 - {{ event.price.seatAPrice }}
        </li>
      </ul>
    </div>
    <!-- 예매 버튼 -->
    <button @click="reserveTicket" style="width: 150px; height: 50px; background-color: #473baa; border-radius: 10px; color: white;">
      예매하러 가기
    </button>
  </div>
  <div v-else>
    <p>Loading...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import {useRoute} from "vue-router";

const event = ref(null);
const route = useRoute(); // useRoute()를 사용하여 현재 라우팅 정보를 가져옴

const fetchEventDetail = async () => {
  const eventId = Number(route.params.eventId); // 이벤트 ID를 Long으로 변환
  try {
    const response = await axios.get(`http://localhost:8080/events/${eventId}`);
    event.value = response.data;
  } catch (error) {
    console.error('이벤트 상세 정보를 불러오는 동안 오류가 발생했습니다:', error);
  }
};

onMounted(() => {
  const eventId = route.params.eventId; // 라우팅 정보에서 eventId를 가져옴
  fetchEventDetail(eventId); // 받아온 이벤트 ID 값을 사용하여 이벤트 상세 정보 호출
});

const reserveTicket = () => {
  // 예매 처리 로직을 작성하세요
};
</script>

<style scoped>
.event-detail {
  padding: 20px;
}

.event-info {
  margin-bottom: 20px;
}

.event-info h2 {
  margin-bottom: 10px;
}

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
</style>