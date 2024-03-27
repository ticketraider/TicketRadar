<template>
  <div>
    <main>
      <div class="top5">
        <h1>인기 검색어 순 TOP.5</h1>
        <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
          <EventList :type="'popularity'"/>
        </div>
      </div>
      <div class="top5">
        <h1>평점 순 TOP.5</h1>
        <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
          <EventList :type="'rating'"/>
        </div>
      </div>
      <div class="top5">
        <h1>좋아요 순 TOP.5</h1>
        <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
          <EventList :type="'likes'"/>
        </div>
      </div>
      <div class="top5">
        <h1>오픈예정일 순 TOP.5</h1>
        <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
          <EventList :type="''"/>
        </div>
      </div>
    </main>
    <footer>
      <p>© 2024 티켓팅 웹사이트 티켓 레이더. 팀 코더스 게이트.</p>
    </footer>
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
  height: 700px;
  width: 100%;
  padding: 100px;
}
</style>