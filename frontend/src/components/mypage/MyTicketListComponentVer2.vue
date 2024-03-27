<template>
  <div>
    <div
        style="background-color: #392365; width: 980px; text-align: center; border-radius: 5px; margin-bottom: 20px; color: white">
      <h2>내 티켓 목록 버전투</h2>
    </div>

    <div v-if="loading">로딩 중...</div>
    <div v-else>

      <div v-if="tickets.length === 0" style="color: white; text-align: center;">
        <h4>티켓이 없습니다.</h4>
      </div>

      <div v-else>

        <div v-for="ticket in tickets" :key="ticket.id" class="ticket-card">

          <div style="width: 900px; background-color: white; padding: 10px; margin-bottom: 10px; border-radius: 10px">
            <div style="margin-bottom: 15px">
              <h5>{{ ticket.eventName }}</h5>
              <div style="display: flex">
                <h6> 구매자 : {{ ticket.memberNickname }} </h6> <h6 style="margin-left: 10px"> 날짜: {{ ticket.date }} </h6>
              </div>
              <v-divider></v-divider>
              <div>
                <div>좌석 : {{ ticket.grade }}-{{ ticket.seatNo }}</div>
                <div>가격 : {{ ticket.price }} 원</div>
                <div>장소 : {{ ticket.place }}(장소의 주소)</div>
              </div>
              <v-divider></v-divider>
              <div style="width: 100%; height: 35px; display: flex">
                <h6 style="margin-right: 600px;">티켓 상태: {{ ticket.ticketStatus }} </h6>
                <a style="background-color: #392365; margin-left: 15px; border-color: #392365;" class="btn btn-primary">예매
                  취소하기</a>
              </div>
            </div>
          </div>

        </div>
        <div style="width: 100%; margin: 10px; height: 100px">
          <div class="pagination" style="margin-left: 680px">
            <v-btn
                style="background-color: #0a0925; color: white;"
            >
              이전
            </v-btn>
            <v-btn
                style="margin-left: 20px; background-color: #0a0925; color: white;"
            >
              다음
            </v-btn>
          </div>
        </div>



      </div>
    </div>
    <ul>

    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      loading: false,
      tickets: [],
      page: 0, // 페이지 번호, 0부터 시작
      size: 4, // 페이지당 아이템 수
    };
  },
  created() {
    this.fetchTickets();
  },
  methods: {
    async fetchTickets() {
      this.loading = true;
      try {
        const token = localStorage.getItem('token'); // JWT 토큰
        console.log(token)
        const response = await axios.get(`http://localhost:8080/tickets/ticket-list/user`, {
          params: {
            page: this.page,
            size: this.size
          },
          headers: {
            Authorization: `Bearer ${token}`
          },
        });
        this.tickets = response.data.content; // API 응답에 따라 조정
        // 페이지 정보 업데이트 등의 추가 작업이 필요할 수 있습니다.
      } catch (error) {
        console.error("티켓 정보를 가져오는데 실패했습니다.", error);
        // 오류 처리 로직
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>




<style scoped>

</style>