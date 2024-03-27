<!--<template>-->
<!--  <div class="event-list">-->
<!--    <h1></h1>-->
<!--    <div class="event-grid">-->
<!--      <v-card v-for="event in eventList" :key="event.id" class="event-card">-->
<!--        &lt;!&ndash; 이벤트 정보를 표시하는 부분 &ndash;&gt;-->
<!--        <v-card class="mx-auto" style="width: 300px; background-color: white">-->
<!--          <v-img height="400px"  :src="event.posterImage"-->
<!--                 cover></v-img>-->
<!--          <v-card-title>-->
<!--            {{ event.title }}-->
<!--          </v-card-title>-->
<!--          <v-card-subtitle>-->
<!--            {{ event.eventInfo }}-->
<!--          </v-card-subtitle>-->
<!--          <v-card-actions>-->
<!--            <v-btn-->
<!--                @click="reserve(event.id)"-->
<!--                color=#0a0925-->
<!--                variant="text">-->
<!--              예매하러 가기-->
<!--            </v-btn>-->
<!--            <v-spacer></v-spacer>-->
<!--          </v-card-actions>-->
<!--        </v-card>-->
<!--      </v-card>-->
<!--    </div>-->
<!--    &lt;!&ndash; 페이지 네이션 &ndash;&gt;-->
<!--    <div style="width: 100%; margin: 10px">-->
<!--      <div class="pagination" style="margin-left: 680px ">-->
<!--        <v-btn @click="fetchEvents(currentPage - 1)" :disabled="currentPage === 0"-->
<!--               style="background-color: #0a0925; color: white;">-->
<!--          이전-->
<!--        </v-btn>-->
<!--        <v-btn @click="fetchEvents(currentPage + 1)" :disabled="currentPage === totalPages - 1"-->
<!--        style="margin-left: 20px; background-color: #0a0925; color: white;">-->
<!--          다음-->
<!--        </v-btn>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import { ref, onMounted } from 'vue';-->
<!--import axios from 'axios';-->
<!--import {useRoute, useRouter} from 'vue-router';-->

<!--const eventList = ref([]);-->
<!--const currentPage = ref(0); // 현재 페이지 (0부터 시작)-->
<!--const pageSize = 5; // 한 페이지당 보여줄 이벤트 수-->
<!--let totalPages = ref(0); // 총 페이지 수-->
<!--const router = useRouter();-->
<!--const route = useRoute();-->
<!--const selectedCategory = route.query.category;-->

<!--const fetchEvents = async (page) => {-->
<!--  try {-->
<!--    const response = await axios.get('http://localhost:8080/events', {-->
<!--      params: {-->
<!--        page: page,-->
<!--        size: pageSize,-->
<!--        category: selectedCategory-->
<!--      },-->
<!--    });-->
<!--    eventList.value = response.data.content;-->
<!--    totalPages.value = response.data.totalPages;-->
<!--    currentPage.value = page;-->
<!--  } catch (error) {-->
<!--    console.error('이벤트 목록을 불러오는 동안 오류가 발생했습니다:', error);-->
<!--  }-->
<!--};-->

<!--onMounted(() => {-->
<!--  fetchEvents(currentPage.value);-->
<!--});-->

<!--const reserve = (eventId) => {-->
<!--  // 예매 페이지로 이동하고 현재 이벤트의 ID 전달-->
<!--  router.push({ name: 'EventDetail', params: { eventId: Number(eventId) } });-->
<!--};-->
<!--</script>-->

<!--<style scoped>-->
<!--.event-list {-->
<!--  padding: 20px;-->
<!--}-->

<!--.event-grid {-->
<!--  display: grid;-->
<!--  grid-template-columns: repeat(5, 1fr);-->
<!--  gap: 20px;-->
<!--}-->

<!--.event-card {-->
<!--  width: 100%;-->
<!--}-->

<!--.pagination {-->
<!--  margin-top: 20px;-->
<!--}-->

<!--.pagination v-btn {-->
<!--  margin-right: 5px;-->
<!--}-->
<!--</style>-->