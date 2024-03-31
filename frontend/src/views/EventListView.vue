<!DOCTYPE html>
<template>

  <html lang="ko">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket Radar</title>
  </head>
  <body>
  <main style="height: 1000px">
    <div style=" width: 100%; padding: 100px;">
      <div style="width: 1600px; padding:10px; display: flex; margin: 0 auto;">
        <EventList type=""/>
      </div>
    </div>
  </main>
  </body>
  </html>
</template>
<style>
@import "../css/styles/TestStyle.css";
</style>
<script>
import EventList from "@/components/EventListComponent.vue";
export default {
  name: 'App',

  components: {
    EventList,
  },

  data: () => ({
    //
  }),
}
</script>