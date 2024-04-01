<!DOCTYPE html>
<template>
<main>
    <div style="display: flex">
      <EventDetailComponentFront/>
      <!--   예매시 날짜 선택창과 예매버튼-->
      <TicketingComponent/>
    </div>
  </main>
</template>
<style>
@import "../css/styles/TestStyle.css";
</style>
<script>
import TicketingComponent from "@/components/TicketingComponentVer5Front.vue";
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