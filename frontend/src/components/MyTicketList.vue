<template>
  <div class="ticket-list">
    <h1>티켓 목록</h1>
    <div v-if="loading">로딩 중...</div>
    <div v-else>
      <div v-if="tickets.length === 0">티켓이 없습니다.</div>
      <div v-else>
        <div v-for="ticket in tickets" :key="ticket.id" class="ticket-card">
          <div>티켓 번호: {{ ticket.id }}</div>
          <div>좌석 번호: {{ ticket.seatNo }}</div>
          <div>가격: {{ ticket.price }}원</div>
          <div>등급: {{ ticket.grade }}</div>
          <div>날짜: {{ ticket.date }}</div>
          <div>장소: {{ ticket.place }}</div>
          <div>티켓 상태: {{ ticket.ticketStatus }}</div>
          <div>이벤트 이름: {{ ticket.eventName }}</div>
          <div>구매자 닉네임: {{ ticket.memberNickname }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      tickets: [],
      loading: false
    };
  },
  created() {
    this.fetchTickets();
  },
  methods: {
    async fetchTickets() {
      this.loading = true;
      try {
        const token = this.getJwtToken(); // JWT 토큰을 가져옵니다.
        console.error('jwtToken :', token);
        const response = await axios.get('/api/getTicketListByUserId', {
          headers: {
            Authorization: `Bearer ${token}` // JWT 토큰을 헤더에 포함하여 요청합니다.
          }
        });
        this.tickets = response.data.content; // API 응답에서 티켓 리스트를 가져와 저장합니다.
      } catch (error) {
        console.error('티켓 목록을 불러오는 동안 오류가 발생했습니다:', error);
      } finally {
        this.loading = false;
      }
    },
    getJwtToken() {
      return localStorage.getItem('jwt_token'); // 로컬 스토리지에서 JWT 토큰을 가져옵니다.
    }
  }
};
</script>

<style scoped>
.ticket-list {
  margin-top: 20px;
}

.ticket-card {
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 10px;
}
</style>