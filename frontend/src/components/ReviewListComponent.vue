<template>
  <form style=" width: 100%; height: 180px; margin: 50px auto auto auto">
    <div style="display: flex">
      <div class="mb-3">
        <input type="text" placeholder="리뷰 제목" v-model="reviewTitle" class="form-control" id="exampleInputEmail1"
               aria-describedby="emailHelp">
      </div>
      <div style="margin-bottom: 10px">
        <v-rating
            v-model="rating"
            class="ma-2"
            density="comfortable"
        ></v-rating>
      </div>

    </div>
    <div class="mb-3">
      <input type="text" placeholder="리뷰 내용" v-model="reviewContent" class="form-control" id="exampleInputPassword1">
    </div>
    <div style="text-align: right;">
      <button type="button" class="btn btn-primary"
              style="background-color: #392365; border-color: #392365; margin-left: 15px;"
              @click="checkLoginStatus"
      >리뷰 남기기
      </button>
    </div>
  </form>

  <div class="review-list">
      <v-row>
        <v-col v-for="review in reviewList" :key="review.id" cols="12">
          <v-card class="review-card" style="width: 100%; height: 150px">
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
          </v-card>
        </v-col>
      </v-row>
    </div>
    <!-- 페이지 네이션 -->
    <div style="width: 100%; margin-top: 10px; display: flex; justify-content: flex-end">
      <div class="pagination">
        <v-btn @click="fetchReviews(currentPage - 1)" :disabled="currentPage === 0"
               style="background-color: #0a0925; color: white;">
          이전
        </v-btn>
        <v-btn @click="fetchReviews(currentPage + 1)" :disabled="currentPage === totalPages - 1"
               style="margin-left: 20px; background-color: #0a0925; color: white;">
          다음
        </v-btn>
      </div>
    </div>

</template>

<script>
import {ref, onMounted} from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router';
import router from "@/router/router";

export default {
  data() {
    return {
      rating: 3,
      page: 1,
      reviewTitle: '', // 리뷰 제목을 위한 데이터 프로퍼티
      reviewContent: '', // 리뷰 내용을 위한 데이터 프로퍼티
    }
  },
  setup() {
    const reviewList = ref([]);
    const currentPage = ref(0);
    const totalPages = ref(0);

    const router = useRouter();
    const eventId = router.currentRoute.value.params.eventId;

    const fetchReviews = async (page) => {
      try {
        const response = await axios.get('http://localhost:8080/reviews', {
          params: {
            page: page,
            eventId: eventId,
          },
        });
        reviewList.value = response.data.content;
        totalPages.value = response.data.totalPages;
        currentPage.value = page;
      } catch (error) {
        console.error('리뷰 목록을 불러오는 동안 오류가 발생했습니다:', error);
      }
    };

    const displayRating = (rating) => {
      return '⭐'.repeat(rating);
    };

    onMounted(() => {
      fetchReviews(currentPage.value);
    });

    return {eventId, reviewList, currentPage, totalPages, fetchReviews, displayRating};
  },
  methods: {
    async submitReview() {
      try {
        const reviewDetail = {
          title: this.reviewTitle,
          content: this.reviewContent,
          rating: this.rating,
          eventId: this.eventId//여기에 이벤트ID 받아와야됨
        };
        console.log(reviewDetail)
        const token = localStorage.getItem('token');

        await axios.post('http://localhost:8080/reviews/create', reviewDetail
            ,
            {
              headers: {
                Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
              }
            });

        alert('리뷰가 성공적으로 생성되었습니다.');

        this.reviewTitle = ''; // 입력 필드 초기화
        this.reviewContent = ''; // 입력 필드 초기화
        window.location.reload()// 리뷰 목록을 다시 불러옵니다.

      } catch (error) {
        if (error.response.status === 500) {
          alert("리뷰를 남기기 전에 티켓을 먼저 예매해주세요")
        } else {
          alert("알수없는 에러가 발생 했습니다.")
        }
      }
    },
    checkLoginStatus() {
      const token = localStorage.getItem('token');
      if (!token) {
        alert("로그인이 필요한 기능입니다.");
        router.push({path: "/login"})
        // 로그인이 필요한 상황이므로, 로그인 페이지로 리다이렉트하는 로직도 추가할 수 있습니다.
        // 예: router.push('/login');
      }else {
        this.submitReview()
      }
    },
  },
}
</script>

<style scoped>
.review-list {
}

.review-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.review-card {

  background-color : #EEEAF1;
  width: 100%;
}

.pagination {
  margin-top: 20px;
}

.pagination v-btn {
  margin-right: 5px;
}
</style>