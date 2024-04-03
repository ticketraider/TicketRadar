<template>
  <div>
    <div class="header" style="margin-bottom: 20px;">
      <h2>좋아요한 이벤트 모아보기</h2>
    </div>

    <div v-if="loading" class="loading" style="text-align: center;">로딩 중...</div>
    <div v-else style="display: flex; justify-content: center;">

      <div v-if="likes.length === 0" class="no-tickets" style="text-align: center;">
        <h4>목록이 없습니다.</h4>
      </div>

      <div v-else>
        <div v-for="like in likes" :key="like.id" class="ticket-card" style="width: 900px; margin-bottom: 20px;">
          <div class="card-content" style="background-color: white; padding: 10px; border-radius: 10px; height: 60px;">
            <div style="display:flex; align-items: center; justify-content: space-between;">
              <button
                  @mouseover="highlightEvent = like.id"
                  @mouseleave="highlightEvent = null"
                  @click="navigateToEventDetail(like.eventId)"
                  :class="{ highlight: highlightEvent === like.id }"
                  class="event-name"
              >
                <h5>{{ like.eventTitle }}</h5>
              </button>
              <div>
                <button class="cancel-btn" style="background-color: #392365; border-color: #392365;" @click="likeEvent(like.eventId)">좋아요 취소</button>
              </div>
            </div>
          </div>
        </div>
        <div class="pagination" style="width: 100%; margin-top: 20px; display: flex; justify-content: center;">
          <button @click="prevPage" :disabled="page === 0" class="btn btn-primary" style="background-color: #392365; margin-right: 20px;">이전</button>
          <div class="current-page" style="color: white; font-weight: bold;">현재 페이지: {{ page + 1 }}</div>
          <button @click="nextPage" :disabled="page === totalPages - 1" class="btn btn-primary" style="background-color: #392365; margin-left: 20px;">다음</button>
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
      likes: [], // 변경된 부분
      page: 0, // 페이지 번호, 0부터 시작
      size: 15, // 페이지당 아이템 수
      totalPages: 0, // 전체 페이지 수, API 응답에서 설정
    };
  },
  created() {
    this.fetchLikes(); // 변경된 부분
  },
  methods: {
    displayRating(rating) {
      return '⭐'.repeat(rating);
    },
    async fetchLikes() { // 변경된 부분
      this.loading = true;
      const token = localStorage.getItem('token'); // JWT 토큰
      try {
        const response = await axios.get(`http://localhost:8080/likes`, { // URL 변경 가정
          params: {
            page: this.page,
            size: this.size
          },
          headers: {
            Authorization: `Bearer ${token}`
          },
        });
        this.likes = response.data.content; // API 응답에 따라 조정, 변경된 부분
        this.totalPages = response.data.totalPages; // 전체 페이지 수 업데이트
        console.log(response)
      } catch (error) {
        console.error("좋아요 정보를 가져오는데 실패했습니다.", error); // 메시지 변경
      } finally {
        this.loading = false;
      }
    },
    nextPage() {
      if (this.page < this.totalPages - 1) {
        this.page++;
        this.fetchLikes(); // 변경된 부분
      }
    },
    prevPage() {
      if (this.page > 0) {
        this.page--;
        this.fetchLikes(); // 변경된 부분
      }
    },
    async likeEvent(eventId) {
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
    },
    navigateToEventDetail(eventId) {
      // 이벤트 상세 페이지로 이동
      router.push({name: 'EventDetail', params: {eventId: Number(eventId)}});
    },
  }
}
</script>



<style scoped>
.highlight {
  border-radius: 5px;
  background-color: gray; /* 하이라이트 색상, 필요에 따라 변경 */
  cursor: pointer;
}
</style>