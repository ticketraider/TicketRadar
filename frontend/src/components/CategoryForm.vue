<template>
  <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto" @submit.prevent="createCategory">
    <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
      <h2>카테고리</h2>
    </div>

    <div class="accordion" id="accordionExample">
      <!-- 카테고리 생성 -->
      <div class="accordion-item">
        <h2 class="accordion-header">
          <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne"
                  aria-expanded="false" aria-controls="collapseOne">
            카테고리 생성
          </button>
        </h2>
        <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
          <div class="accordion-body">
            <div class="mb-3">
              <label for="categoryTitle" class="form-label">타이틀</label>
              <input v-model="newCategory.title" type="text" class="form-control" id="categoryTitle">
            </div>
            <div style="text-align: right;">
              <button type="submit" class="btn btn-primary" style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 카테고리 수정 -->


      <!-- 카테고리 삭제 -->

    </div>
  </form>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

// 새 카테고리를 나타내는 데이터
const newCategory = ref({
  title: ''
});

// 카테고리를 생성하는 함수
const createCategory = () => {
  // 서버에 보낼 데이터
  const requestData = {
    title: newCategory.value.title
  };

  // 서버로 POST 요청 보내기
  axios.post('http://localhost:8080/categories', requestData)
      .then(response => {
        console.log('카테고리 생성 성공:', response.data);
        // 성공적으로 생성되었을 때의 처리 (예: 페이지 리로드)
        window.location.reload();
      })
      .catch(error => {
        console.error('카테고리 생성 오류:', error);
        // 오류 발생 시 사용자에게 알림
        alert('카테고리 생성에 실패했습니다.');
      });
};
</script>

<style scoped>
</style>