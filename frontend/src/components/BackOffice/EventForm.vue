<template>
  <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto">
    <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
      <h2>이벤트</h2>
    </div>
    <div class="accordion" id="event">
<!--      이벤트 생성-->
      <EventCreateForm/>
<!--      이벤트 수정-->
      <EventUpdateForm/>
<!--      이벤트 삭제-->
      <EventDeleteForm/>
    </div>

  </form>
</template>
<script setup>
import EventCreateForm from '@/components/BackOffice/EventCreateForm.vue';
import EventUpdateForm from '@/components/BackOffice/EventUpdateForm.vue';
import EventDeleteForm from '@/components/BackOffice/EventDeleteForm.vue';
</script>