<template>
  <div class="event-detail">
    <EventCard :event="event" />
    <h1>리뷰</h1>
    <ReviewList :event="event" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import EventCard from "@/components/EventCardBack.vue";
import ReviewList from "@/components/ReviewListBack.vue";
import { useRoute } from "vue-router";

const event = ref(null);
const route = useRoute();

const fetchEventDetail = async () => {
  const eventId = Number(route.params.eventId);
  try {
    const response = await axios.get(`http://localhost:8080/events/${eventId}`);
    event.value = response.data;
  } catch (error) {
    console.error('이벤트 상세 정보를 불러오는 동안 오류가 발생했습니다:', error);
  }
};

onMounted(() => {
  fetchEventDetail();
});
</script>

<style scoped>
/* 스타일은 필요에 따라 추가 */
</style>