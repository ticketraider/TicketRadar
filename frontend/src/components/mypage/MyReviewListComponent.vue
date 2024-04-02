<template>
  <div>
    <div class="header" style="margin-bottom: 20px;">
      <h2>내 리뷰 모아보기</h2>
    </div>

    <div v-if="loading" class="loading" style="text-align: center;">로딩 중...</div>
    <div v-else style="display: flex; justify-content: center;">

      <div v-if="reviews.length === 0" class="no-tickets" style="text-align: center;">
        <h4>리뷰가 없습니다.</h4>
      </div>

      <div v-else>
        <div v-for="review in reviews" :key="review.id" class="ticket-card" style="width: 900px; margin-bottom: 20px;">
          <div class="card-content" style="background-color: #eeeaf1; padding: 20px; border-radius: 10px; overflow: hidden;">
            <h5 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
              <button
                  @mouseover="highlightEvent = review.id"
                  @mouseleave="highlightEvent = null"
                  @click="navigateToEventDetail(review.eventId)"
                  :class="{ highlight: highlightEvent === review.id }"
                  class="event-name"
                  style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;"
              >
                {{ review.eventTitle }}
              </button>
            </h5>
            <v-divider></v-divider>
            <div class="info" style="overflow: hidden;">
              <v-row>
                <v-col>
                  <v-card>
                    <v-card-title style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ review.title }}</v-card-title>
                    <v-card-subtitle>{{ displayRating(review.rating) }}</v-card-subtitle>
                    <!--                    <v-card-text style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ review.nickname }}</v-card-text>-->
                    <v-card-text style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ review.modifiedAt }}</v-card-text>
                    <v-card-text style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ review.content }}</v-card-text>
                  </v-card>
                </v-col>
              </v-row>

              <!-- Modal -->
              <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h1 class="modal-title fs-5" id="staticBackdropLabel">리뷰 수정하기</h1>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                      <div style="display: flex">
                        <div class="mb-3">
                          <input type="text" placeholder="리뷰 제목" v-model="updateReviewTitle" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
                        </div>
                        <div style="margin-bottom: 10px">
                          <v-rating
                              v-model="updateRating"
                              class="ma-2"
                              density="comfortable"
                          ></v-rating>
                        </div>
                      </div>
                      <div class="mb-3">
                        <input type="text" placeholder="리뷰 내용" v-model="updateReviewContent" class="form-control" id="exampleInputPassword1">
                      </div>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                      <button type="button" class="btn btn-primary" data-bs-dismiss="modal" @click="updateReview(review.id)">변경하기</button>
                    </div>
                  </div>
                </div>
              </div>

              <div style="display: flex; justify-content: flex-end; margin-top: 12px;">
                <button class="cancel-btn" style="background-color: #392365; margin-left: 15px;" data-bs-toggle="modal" data-bs-target="#staticBackdrop">리뷰 수정하기</button>
                <button class="cancel-btn" style="background-color: #392365; margin-left: 15px;" @click="deleteReview(review.id)">리뷰 삭제하기</button>
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
      updateReviewTitle: "",
      updateRating: "",
      updateReviewContent: "",
      highlightEvent: null,
      loading: false,
      reviews: [], // 변경된 부분
      page: 0, // 페이지 번호, 0부터 시작
      size: 4, // 페이지당 아이템 수
      totalPages: 0, // 전체 페이지 수, API 응답에서 설정
    };
  },
  created() {
    this.fetchReviews(); // 변경된 부분
  },
  methods: {
     displayRating(rating) {
      return '⭐'.repeat(rating);
    },
    async fetchReviews() { // 변경된 부분
      this.loading = true;
      try {
        const token = localStorage.getItem('token'); // JWT 토큰
        const response = await axios.get(`http://localhost:8080/reviews/members`, { // URL 변경 가정
          params: {
            page: this.page,
            size: this.size
          },
          headers: {
            Authorization: `Bearer ${token}`
          },
        });
        this.reviews = response.data.content; // API 응답에 따라 조정, 변경된 부분
        this.totalPages = response.data.totalPages; // 전체 페이지 수 업데이트
        console.log(response)
      } catch (error) {
        console.error("리뷰 정보를 가져오는데 실패했습니다.", error); // 메시지 변경
      } finally {
        this.loading = false;
      }
    },
    nextPage() {
      if (this.page < this.totalPages - 1) {
        this.page++;
        this.fetchReviews(); // 변경된 부분
      }
    },
    prevPage() {
      if (this.page > 0) {
        this.page--;
        this.fetchReviews(); // 변경된 부분
      }
    },
    async updateReview(reviewId) { // 메소드 변경 가정
       if (this.updateReviewTitle === ""|| this.updateReviewContent==="") {
         alert("리뷰를 정확히 작성해주세요")
         return
       }
      const updateReviewDetails = {
        title: this.updateReviewTitle,
        content: this.updateReviewContent,
        rating: this.updateRating,
      };
      try {
        await axios.put(`http://localhost:8080/reviews/update/${reviewId}`,  // URL 변경 가정
          updateReviewDetails
        );
        // 요청 성공 후 리뷰 목록 갱신
        this.fetchReviews(); // 변경된 부분
        alert('리뷰가 업데이트 되었습니다.'); // 메시지 변경
      } catch (error) {
        console.error("리뷰 업데이트에 실패했습니다.", error); // 메시지 변경
        alert('리뷰 업데이트에 실패했습니다.'); // 메시지 변경
      }
    },
    async deleteReview(reviewId) { // 메소드 변경 가정
      try {
        await axios.delete(`http://localhost:8080/reviews/delete/${reviewId}`);
        // 요청 성공 후 리뷰 목록 갱신
        this.fetchReviews(); // 변경된 부분
        alert('리뷰가 성공적으로 취소되었습니다.'); // 메시지 변경
      } catch (error) {
        console.error("리뷰 취소에 실패했습니다.", error); // 메시지 변경
        alert('리뷰 취소에 실패했습니다.'); // 메시지 변경
      }
    },
    navigateToEventDetail(eventId) {
      // 여기서는 Vue Router를 사용하여 이벤트 상세 페이지로 이동한다고 가정
      // 실제 경로는 프로젝트의 라우팅 구조에 따라 달라질 수 있습니다.
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