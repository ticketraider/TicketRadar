<template>
  <main>
    <div
        style="background-color: #aa98ba; padding: 30px; margin: 30px auto 100px auto; min-height: 700px; height: 1520px; width: 60%; border-radius: 20px">

      <!--      카테고리-->
      <CategoryForm/>

      <!--      이벤트-->
      <EventForm/>
    </div>

  </main>
</template>


<script setup>
import CategoryForm from '@/components/CategoryForm.vue';
import EventForm from '@/components/EventForm.vue';
</script>


<style scoped>

</style>