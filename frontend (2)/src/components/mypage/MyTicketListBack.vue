<!--<template>-->
<!--  <div>-->
<!--    <div style="background-color: #392365; width: 980px; text-align: center; border-radius: 5px; margin-bottom: 20px; color: white">-->
<!--      <h2>내 티켓 목록</h2>-->
<!--    </div>-->
<!--    <div v-if="loading">로딩 중...</div>-->
<!--    <div v-else>-->

<!--      <h5 v-if="tickets.length === 0" style="color: white">티켓이 없습니다.</h5>-->

<!--      <div v-else>-->

<!--        <div v-for="ticket in tickets" :key="ticket.id" class="ticket-card">-->

<!--          <div style="width: 900px; background-color: white; padding: 10px; margin-bottom: 10px; border-radius: 10px">-->
<!--            <div style="margin-bottom: 15px">-->
<!--              <h5>{{ ticket.eventName }}</h5>-->
<!--              <div style="display: flex">-->
<!--                <h6> 구매자 : {{ ticket.memberNickname }} </h6> <h6 style="margin-left: 10px"> 날짜: {{ ticket.date }} </h6>-->
<!--              </div>-->
<!--              <v-divider></v-divider>-->
<!--              <div>-->
<!--                <div>좌석 : {{ ticket.grade }}-{{ ticket.seatNo }} </div>-->
<!--                <div>가격 : {{ ticket.price }} 원</div>-->
<!--                <div>장소 : {{ ticket.place }}(장소의 주소)</div>-->
<!--              </div>-->
<!--              <v-divider></v-divider>-->
<!--              <div style="width: 100%; height: 35px; display: flex">-->
<!--                <h6 style="margin-right: 600px;">티켓 상태: {{ ticket.ticketStatus }} </h6>-->
<!--                <a style="background-color: #392365; margin-left: 15px; border-color: #392365;" class="btn btn-primary">예매 취소하기</a>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->

<!--        </div>-->

<!--      </div>-->
<!--    </div>-->
<!--    <ul>-->

<!--      &lt;!&ndash; 티켓 나열&ndash;&gt;-->
<!--      &lt;!&ndash;      티켓 몇개 더 구현&ndash;&gt;-->
<!--      &lt;!&ndash;여기에 페이지네이션 구현?&ndash;&gt;-->
<!--    </ul>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import axios from 'axios';-->

<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      tickets: [],-->
<!--      loading: false-->
<!--    };-->
<!--  },-->
<!--  created() {-->
<!--    this.fetchTickets();-->
<!--  },-->
<!--  methods: {-->
<!--    async fetchTickets() {-->
<!--      this.loading = true;-->
<!--      try {-->
<!--        const token = this.getJwtToken(); // JWT 토큰을 가져옵니다.-->
<!--        console.error('jwtToken :', token);-->
<!--        const response = await axios.get('/api/getTicketListByUserId', {-->
<!--          headers: {-->
<!--            Authorization: `Bearer ${token}` // JWT 토큰을 헤더에 포함하여 요청합니다.-->
<!--          }-->
<!--        });-->
<!--        this.tickets = response.data.content; // API 응답에서 티켓 리스트를 가져와 저장합니다.-->
<!--      } catch (error) {-->
<!--        console.error('티켓 목록을 불러오는 동안 오류가 발생했습니다:', error);-->
<!--      } finally {-->
<!--        this.loading = false;-->
<!--      }-->
<!--    },-->
<!--    getJwtToken() {-->
<!--      return localStorage.getItem('jwt_token'); // 로컬 스토리지에서 JWT 토큰을 가져옵니다.-->
<!--    }-->
<!--  }-->
<!--};-->
<!--</script>-->


<!--<style scoped>-->

<!--</style>-->