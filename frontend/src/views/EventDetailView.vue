<!DOCTYPE html>
<template>
<main style ="background-color: #EEEAF1;" >
  <div style="display: flex; ">
    <EventDetailComponentFront/>
    <div style="width: 100px"></div> <!-- 100px 간격 -->
    <!-- 예매시 날짜 선택창과 예매버튼 -->
    <TicketingComponent/>
  </div>
  </main>
</template>
<style>
@import "../css/styles/TestStyle.css";
</style>
<script>
import TicketingComponent from "@/components/TicketingComponent.vue";
import EventDetailComponentFront from "@/components/EventDetailComponentFront.vue";
export default {
  name: 'App',

  components: {
    EventDetailComponentFront,
    TicketingComponent
  },

  data: () => ({
    selectedItem: null,
    seat : [1,2,3,4]
  }),
}
</script>