<template>
  <main>
    <div class="container-fluid">
      <div class="row">
        <div class="col">
          <!-- 카테고리 -->
          <CategoryForm/>
        </div>
        <div class="col">
          <!-- 장소 -->
          <PlaceForm/>
        </div>
        <div class="col">
          <!-- 이벤트 -->
          <EventForm/>
        </div>
      </div>
    </div>
  </main>
</template>


<script setup>
import CategoryForm from '@/components/BackOffice/CategoryForm.vue';
import EventForm from '@/components/BackOffice/EventForm.vue';
import PlaceForm from '@/components/BackOffice/PlaceForm.vue';
</script>


<style scoped>

</style>