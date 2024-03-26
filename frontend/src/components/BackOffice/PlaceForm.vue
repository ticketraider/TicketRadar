<template>
  <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto" > <!-- @submit.prevent="createCategory"-->
    <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
      <h2>장소</h2>
    </div>

    <div class="accordion" id="accordionPlace">
      <!-- 카테고리 생성 -->
      <PlaceCreateForm/>
      <!-- 카테고리 수정 -->
      <PlaceUpdateForm/>
      <!-- 카테고리 삭제 -->
      <PlaceEventDeleteForm/>
    </div>
  </form>
</template>

<script setup>
import PlaceCreateForm from '@/components/BackOffice/PlaceCreateForm.vue';
import PlaceUpdateForm from '@/components/BackOffice/PlaceUpdateForm.vue';
import PlaceEventDeleteForm from '@/components/BackOffice/PlaceDeleteForm.vue';
</script>