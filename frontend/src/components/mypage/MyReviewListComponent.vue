<template>
  <div>
    <div
        style="background-color: #392365; width: 980px; text-align: center; border-radius: 5px; margin-bottom: 20px; color: white">
      <h2>내 리뷰 모아보기</h2>
    </div>

    <div v-if="loading">로딩 중...</div>
    <div v-else style="display: flex; justify-content: center">

      <div v-if="reviews.length === 0" style="color: white; text-align: center;">
        <h4>리뷰가 없습니다.</h4>
      </div>

      <div v-else>

        <div v-for="review in reviews" :key="review.id" class="review-card">
          <div
              style="width: 900px; background-color: white; padding: 10px; margin-bottom: 10px; border-radius: 10px; height: 250px">
            <div>
              <button
                  @mouseover="highlightEvent = review.id"
                  @mouseleave="highlightEvent = null"
                  @click="navigateToEventDetail(review.eventId)"
                  :class="{ highlight: highlightEvent === review.id }"
              >
                <h5>{{ review.eventTitle }}</h5>
              </button>
              <v-divider></v-divider>
              <v-row>
                <v-col>
                  <v-card class="review-card" style="height: 150px">
                    <div style="display: flex;">
                      <v-card-title>{{ review.title }}</v-card-title>
                      <div style="margin-top: 13px">
                        <v-card-subtitle>{{ displayRating(review.rating) }}</v-card-subtitle>
                      </div>
                      <v-card-text>{{ review.nickname }}</v-card-text>
                      <div style="margin-left: 50px">
                        <v-card-text>{{ review.modifiedAt }}</v-card-text>
                      </div>
                    </div>
                    <v-card-text>{{ review.content }}</v-card-text>

                    <div style="width: 100%; height: 35px; display: flex; justify-content: right">
<!--                      <a style="background-color: #392365; margin-left: 15px; border-color: #392365;" class="btn btn-primary"-->
<!--                         @click="updateReview(review.id)">리뷰 수정하기</a>-->

                      <!-- Button trigger modal -->
                      <button style="background-color: #392365; margin-left: 15px; border-color: #392365;"
                              type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                        리뷰 수정하기
                      </button>

                      <a style="background-color: #392365; margin-left: 15px; border-color: #392365;" class="btn btn-primary"
                         @click="deleteReview(review.id)">리뷰 삭제하기</a>
                    </div>
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
                          <input type="text" placeholder="리뷰 제목" v-model="updateReviewTitle" class="form-control" id="exampleInputEmail1"
                                 aria-describedby="emailHelp">
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


            </div>
          </div>

        </div>
        <div style="width: 100%; margin: 10px; height: 100px">
          <div class="pagination" style="margin-left: 580px">
            <v-btn
                style="background-color: #0a0925; color: white;"
                @click="prevPage"
            >
              이전
            </v-btn>
            <div>
              <h5 style="font-weight: bold; color: white; margin-left: 15px">현재 페이지 : {{ page + 1 }}</h5>
            </div>
            <v-btn
                style="margin-left: 20px; background-color: #0a0925; color: white;"
                @click="nextPage"
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