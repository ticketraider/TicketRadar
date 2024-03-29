<!--<template>-->
<!--  <div class="container">-->
<!--    <h1 class="title">티켓 정보 조회</h1>-->
<!--    <div class="form-group">-->
<!--      <label for="ticketIdInput">티켓 ID:</label>-->
<!--      <input type="text" id="ticketIdInput" v-model="ticketId" class="input">-->
<!--      <button @click="fetchTicket" class="button">티켓 가져오기</button>-->
<!--    </div>-->

<!--    &lt;!&ndash; 티켓 정보 표시 &ndash;&gt;-->
<!--    <div v-if="ticket" class="ticket-info">-->
<!--      <h2>티켓 정보</h2>-->
<!--      <p>티켓 번호: {{ ticket.id }}</p>-->
<!--      <p>좌석 번호: {{ ticket.seatNo }}</p>-->
<!--      <p>가격: {{ ticket.price }}</p>-->
<!--      <p>등급: {{ ticket.grade }}</p>-->
<!--      <p>날짜: {{ ticket.date }}</p>-->
<!--      <p>장소: {{ ticket.place }}</p>-->
<!--      <p>티켓 상태: {{ ticket.ticketStatus }}</p>-->
<!--      <p>이벤트 이름: {{ ticket.eventName }}</p>-->
<!--      <p>회원 닉네임: {{ ticket.memberNickname }}</p>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import { ref } from 'vue';-->
<!--import axios from 'axios';-->

<!--// TicketResponse 클래스와 유사한 구조를 가지는 티켓 데이터 객체 정의-->
<!--const ticket = ref({-->
<!--  id: null,-->
<!--  seatNo: null,-->
<!--  price: null,-->
<!--  grade: null,-->
<!--  date: null,-->
<!--  place: '',-->
<!--  ticketStatus: null,-->
<!--  eventName: '',-->
<!--  memberNickname: '',-->
<!--});-->

<!--const ticketId = ref('');-->
<!--const fetchTicket = async () => {-->
<!--  try {-->
<!--    // HTTP 요청을 통해 티켓 데이터 가져오기-->
<!--    const response = await axios.get(`http://localhost:8080/tickets/${ticketId.value}`);-->
<!--    // 가져온 데이터를 ticket 객체에 할당-->
<!--    Object.assign(ticket.value, response.data);-->
<!--  } catch (error) {-->
<!--    console.error('티켓을 불러오는 동안 오류가 발생했습니다:', error);-->
<!--  }-->
<!--};-->
<!--</script>-->

<!--<style>-->
<!--.container {-->
<!--  max-width: 600px;-->
<!--  margin: 0 auto;-->
<!--  padding: 20px;-->
<!--  background-color: #f0f7ff; /* 연한 파란색 */-->
<!--  border-radius: 8px;-->
<!--}-->

<!--.title {-->
<!--  color: #1a73e8; /* Google 블루 */-->
<!--}-->

<!--.form-group {-->
<!--  margin-bottom: 20px;-->
<!--}-->

<!--.label {-->
<!--  font-weight: bold;-->
<!--}-->

<!--.input {-->
<!--  width: calc(100% - 120px);-->
<!--  padding: 8px;-->
<!--  margin-right: 10px;-->
<!--  border: 1px solid #1a73e8; /* Google 블루 */-->
<!--  border-radius: 4px;-->
<!--}-->

<!--.button {-->
<!--  padding: 8px 20px;-->
<!--  background-color: #1a73e8; /* Google 블루 */-->
<!--  color: #fff;-->
<!--  border: none;-->
<!--  border-radius: 4px;-->
<!--  cursor: pointer;-->
<!--}-->

<!--.ticket-info {-->
<!--  margin-top: 20px;-->
<!--  padding: 20px;-->
<!--  background-color: #d6e4ff; /* 연한 파란색 */-->
<!--  border-radius: 8px;-->
<!--}-->
<!--</style>-->