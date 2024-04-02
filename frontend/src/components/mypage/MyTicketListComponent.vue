<template>
  <div>
    <div class="header">
      <h2>내 티켓 목록</h2>
    </div>

    <div v-if="loading" class="loading">로딩 중...</div>
    <div v-else class="ticket-container">

      <div v-if="tickets.length === 0" class="no-tickets">
        <h4>티켓이 없습니다.</h4>
      </div>

      <div v-else>
        <div v-for="ticket in tickets" :key="ticket.id" class="ticket-card">
          <div class="card-content">
            <button
                @mouseover="highlightEvent = ticket.id"
                @mouseleave="highlightEvent = null"
                @click="navigateToEventDetail(ticket.eventId)"
                :class="{ highlight: highlightEvent === ticket.id }"
                class="event-name"
            >
              {{ ticket.eventName }}
            </button>
            <div class="info">
              <div class="info-row">
                <h6>구매자: {{ ticket.memberNickname }}</h6>
                <h6 style="margin-left: auto;">날짜: {{ ticket.date }}</h6>
              </div>
              <div class="info-row">
                <div>좌석: {{ ticket.grade }}-{{ ticket.seatNo }}</div>
              </div>
              <div class="info-row">
                <div>가격: {{ ticket.price }} 원</div>
              </div>
              <div class="info-row">
                <div>장소: {{ ticket.place }} ({{ ticket.address }})</div>
              </div>
              <hr>
              <div class="info-row" style="display: flex; justify-content: space-between; align-items: center;">
                <h6 style="margin: 0;">티켓 상태: {{ ticket.ticketStatus }}</h6>
                <div>
                  <button  class="cancel-btn" @click="makePayment(ticket.id)" style="margin-right: 20px;">결제하기</button>
                  <button  class="cancel-btn" @click="cancelTicket(ticket.id)">예매 취소하기</button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="pagination">
          <button @click="prevPage" :disabled="page === 0">이전</button>
          <div class="current-page">현재 페이지: {{ page + 1 }}</div>
          <button @click="nextPage" :disabled="page === totalPages - 1">다음</button>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
import axios from 'axios';
import {router} from "@/router/router";

export default {
  data() {
    return {
      highlightEvent: null,
      loading: false,
      tickets: [],
      page: 0, // 페이지 번호, 0부터 시작
      size: 4, // 페이지당 아이템 수
      totalPages: 0, // 전체 페이지 수, API 응답에서 설정
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
        this.totalPages = response.data.totalPages; // 전체 페이지 수 업데이트
        console.log(response)
      } catch (error) {
        console.error("티켓 정보를 가져오는데 실패했습니다.", error);
      } finally {
        this.loading = false;
      }
    },

    nextPage() {
      if (this.page < this.totalPages - 1) {
        this.page++;
        this.fetchTickets();
      }
    },

    prevPage() {
      if (this.page > 0) {
        this.page--;
        this.fetchTickets();
      }
    },

    async cancelTicket(ticketId) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`http://localhost:8080/tickets/cancel/${ticketId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          },
        });
        // 요청 성공 후 티켓 목록 갱신
        this.fetchTickets();
        alert('티켓이 성공적으로 취소되었습니다.');
      } catch (error) {
        console.error("티켓 취소에 실패했습니다.", error);
        alert('티켓 취소에 실패했습니다.');
      }
    },

    navigateToEventDetail(eventId) {
      // 여기서는 Vue Router를 사용하여 이벤트 상세 페이지로 이동한다고 가정
      // 실제 경로는 프로젝트의 라우팅 구조에 따라 달라질 수 있습니다.
      router.push({name: 'EventDetail', params: {eventId: Number(eventId)}});
    },

    async makePayment(ticketId) {
      try {
        const token = localStorage.getItem('token');

        await axios.post(
            'http://localhost:8080/tickets/makePayment',
            null,
            {
              params: {
                ticketId: ticketId
              },
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        alert('티켓이 성공적으로 결제되었습니다.');
        // 요청 성공 후 티켓 목록 갱신
        this.fetchTickets();
      } catch (error) {
        alert('티켓 결제에 실패했습니다.');
      }
    }
  },
}
</script>

<style scoped>
.highlight {
  border-radius: 5px;
  background-color: gray; /* 하이라이트 색상, 필요에 따라 변경 */
  cursor: pointer;
}
</style>