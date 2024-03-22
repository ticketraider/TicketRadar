<!DOCTYPE html>
<template>

  <html lang="ko">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket Radar</title>
  </head>
  <body>
  <main>
    <div style="display: flex">
      <EventDetailComponentFront/>
      <!--   예매시 날짜 선택창과 예매버튼-->
      <TicketingComponentVer3/>

    </div>
  </main>
  </body>
  </html>
</template>
<style>
@import "../css/styles/TestStyle.css";
</style>
<script>
import TicketingComponentVer3 from "@/components/TicketingComponentVer3Front.vue";
import EventDetailComponentFront from "@/components/EventDetailComponentFront.vue";
export default {
  name: 'App',

  components: {
    EventDetailComponentFront,
    TicketingComponentVer3
  },

  data: () => ({
    selectedItem: null,
    seat : [1,2,3,4]
  }),
}
</script>