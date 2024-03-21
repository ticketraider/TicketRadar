<template>
  <div class="review-list">
    <h1>리뷰 목록</h1>
    <div class="review-grid">
      <v-card v-for="review in reviewList" :key="review.id" class="review-card">
        <v-card-title>{{ review.title }}</v-card-title>
        <v-card-subtitle>{{ review.rating }}</v-card-subtitle>
        <v-card-text>{{ review.content }}</v-card-text>
      </v-card>
    </div>
    <!-- 페이지 네이션 -->
    <div class="pagination">
      <v-btn @click="fetchReviews(currentPage - 1)" :disabled="currentPage === 0">
        이전 페이지
      </v-btn>
      <span>현재 페이지: {{ currentPage + 1 }}</span>
      <v-btn @click="fetchReviews(currentPage + 1)" :disabled="currentPage === totalPages - 1">
        다음 페이지
      </v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const reviewList = ref([]);
const currentPage = ref(0);
let totalPages = ref(0);

const router = useRouter();
const eventId = router.currentRoute.value.params.eventId; // EventView.vue에서 받아온 eventId

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

onMounted(() => {
  fetchReviews(currentPage.value);
});
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