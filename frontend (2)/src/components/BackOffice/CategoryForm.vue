<template>
 <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto" > <!-- @submit.prevent="createCategory"-->
    <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
      <h2>카테고리</h2>
    </div>

    <div class="accordion" id="accordionCategory">
      <!-- 카테고리 생성 -->
      <CategoryCreateForm/>
      <!-- 카테고리 수정 -->
      <CategoryUpdateForm/>
      <!-- 카테고리 삭제 -->
      <CategoryDeleteForm/>
    </div>
  </form>
</template>

<script setup>
import CategoryCreateForm from '@/components/BackOffice/CategoryCreateForm.vue';
import CategoryUpdateForm from '@/components/BackOffice/CategoryUpdateForm.vue';
import CategoryDeleteForm from '@/components/BackOffice/CategoryDeleteForm.vue';

</script>

<style scoped>
</style>