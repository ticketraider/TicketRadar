<template>
  <div>
    <main style="display: flex; flex-direction: column; gap: 30px; align-items: center">
      <div style="width: 90%; display: flex; flex-direction: column; margin-top: 15px;">
        <h3>✨인기 검색어 순 TOP.5</h3>
        <div style="display: flex; flex-wrap: wrap; gap: 20px">
          <EventList :type="'popularity'"/>
        </div>
      </div>
      <div style="width: 90%; display: flex; flex-direction: column">
        <h3>👍평점 순 TOP.5</h3>
        <div style="display: flex; flex-wrap: wrap; gap: 20px">
          <EventList :type="'rating'"/>
        </div>
      </div>
      <div style="width: 90%; display: flex; flex-direction: column">
        <h3>✏️리뷰 순 TOP.5</h3>
        <div style="display: flex; flex-wrap: wrap; gap: 20px">
          <EventList :type="'reviews'"/>
        </div>
      </div>
      <div style="width: 90%; display: flex; flex-direction: column">
        <h3>❤️좋아요 순 TOP.5</h3>
        <div style="display: flex; flex-wrap: wrap; gap: 20px">
          <EventList :type="'likes'"/>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import EventList from "@/components/EventListComponent.vue";

const isEventListLoaded = ref(false);

onMounted(() => {
  // setTimeout을 사용하여 2초 후에 내부 컴포넌트 로딩이 완료되었다고 가정
  setTimeout(() => {
    // 내부 컴포넌트 로딩이 완료되면 isEventListLoaded 값을 true로 변경
    isEventListLoaded.value = true;
  }, 2000); // 2초 후에 로딩이 완료되었다고 가정
});
</script>


<style scoped>
@import "../css/styles/TestStyle.css";

.top5 {
  display: flex;
  width: 90%;
  justify-content: center;
}
</style>