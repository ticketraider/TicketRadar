<template>
  <div class="event-list">
    <div class="event-grid">
      <v-card v-for="event in eventList" :key="event.id" class="event-card">
        <v-card class="mx-auto" style="width: 300px; background-color: white">
          <v-img height="400px" :src="event.posterImage" cover></v-img>
          <v-card-title>{{ event.title }}</v-card-title>
          <v-card-subtitle>{{ event.eventInfo }}</v-card-subtitle>
          <v-card-actions>
            <v-btn @click="reserve(event.id)" color="#0a0925" variant="text">
              예매하러 가기
            </v-btn>
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-card>
    </div>
    <div style="width: 100%; margin: 10px">
      <div class="pagination" style="margin-left: 680px">
        <v-btn
            @click="fetchEvents(currentPage - 1)"
            :disabled="currentPage === 0"
            style="background-color: #0a0925; color: white;"
        >
          이전
        </v-btn>
        <v-btn
            @click="fetchEvents(currentPage + 1)"
            :disabled="currentPage === totalPages - 1"
            style="margin-left: 20px; background-color: #0a0925; color: white;"
        >
          다음
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const eventList = ref([]);
const currentPage = ref(0);
const pageSize = 5;
let totalPages = ref(0);
const router = useRouter();
const route = useRoute();
const selectedCategory = ref(route.query.category);

const fetchEvents = async (page = 0) => {
  try {
    const response = await axios.get('http://localhost:8080/events', {
      params: {
        page: page,
        size: pageSize,
        category: selectedCategory.value,
      },
    });
    eventList.value = response.data.content;
    totalPages.value = response.data.totalPages;
    currentPage.value = page;
  } catch (error) {
    console.error('이벤트 목록을 불러오는 동안 오류가 발생했습니다:', error);
  }
};

// selectedCategory 변화 감지 및 해당 카테고리의 이벤트 목록 불러오기
watch(() => route.query.category, (newCategory) => {
  selectedCategory.value = newCategory;
  fetchEvents(0); // 카테고리가 바뀌면 첫 페이지부터 이벤트를 불러옵니다.
});

onMounted(() => {
  fetchEvents(currentPage.value);
});

const reserve = (eventId) => {
  router.push({ name: 'EventDetail', params: { eventId: Number(eventId) } });
};
</script>

<style scoped>
.event-list {
  padding: 20px;
}

.event-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
}

.event-card {
  width: 100%;
}

.pagination {
  margin-top: 20px;
}

.pagination v-btn {
  margin-right: 5px;
}
</style>