<template>
  <form style="color: white; width: 90%; height: 180px; margin: 50px auto auto auto">
    <div style="display: flex">
      <div class="mb-3">
        <input type="email" placeholder="리뷰 제목" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
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
      <input type="password" placeholder="리뷰 내용" class="form-control" id="exampleInputPassword1">
    </div>
    <div style="text-align: right;">
      <button type="submit" class="btn btn-primary"
              style="background-color: #392365; border-color: #392365; margin-left: 15px;">리뷰 남기기
      </button>
    </div>

  </form>
  <div class="review-list">
    <div class="review-grid">
      <div>

      </div>
      <v-row>
        <v-col v-for="review in reviewList" :key="review.id" cols="12">
          <v-card class="review-card" style="width: 1050px; height: 150px">
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
    <div style="width: 100%; margin: 10px">
      <div class="pagination" style="margin-left: 447px ">
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
  </div>

  <div class="text-center">
    <v-container>
      <v-row justify="center">
        <v-col cols="8">
          <v-container class="max-width">
            <v-pagination
                v-model="page"
                :length="totalPages"
                class="my-4"
            ></v-pagination>
          </v-container>
        </v-col>
      </v-row>
    </v-container>
  </div>

</template>

<script>
import {ref, onMounted} from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router';

export default {
  data() {
    return {
      rating: 3,
      page: 1,
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

    return {reviewList, currentPage, totalPages, fetchReviews, displayRating};
  }
}
</script>

<style scoped>
.review-list {
  padding: 20px;
}

.review-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.review-card {
  width: 100%;
}

.pagination {
  margin-top: 20px;
}

.pagination v-btn {
  margin-right: 5px;
}
</style>