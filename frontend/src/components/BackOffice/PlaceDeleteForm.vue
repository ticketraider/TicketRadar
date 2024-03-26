<script setup>
import axios from "axios";
import { ref } from 'vue';

// 로컬 스토리지에서 JWT 토큰 가져오기
const token = localStorage.getItem('token');

const Category = ref({
  deleteId : '',
});

const deleteCategory = () => {
  const categoryId = parseInt(Category.value.deleteId)

  // 서버로 POST 요청 보내기
  axios.delete(`http://localhost:8080/place/${categoryId}`, {
    headers: {
      Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
    }
  })
      .then(response => {
        console.log('장소 삭제 성공:', response.data);
        // 성공적으로 생성되었을 때의 처리 (예: 페이지 리로드)
        alert('장소 삭제에 성공했습니다.');
        window.location.reload();
      })
      .catch(error => {
        console.error('장소 삭제 오류:', error);
        // 실패 시에도 현재 헤더에 포함된 토큰 확인
        console.log('현재 헤더:', error.config.headers);
        // 오류 발생 시 사용자에게 알림
        alert('장소 삭제에 실패했습니다.');
      });
};
</script>

<template>
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#deletePlace"
              aria-expanded="false" aria-controls="deletePlace">
        장소 삭제
      </button>
    </h2>
    <div id="deletePlace" class="accordion-collapse collapse" data-bs-parent="#accordionPlace">
      <div class="accordion-body">

        <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">카테고리 ID</label>
          <input v-model="Category.deleteId" type="number" class="form-control" id="exampleInputPassword1">
        </div>
        <div style="text-align: right;">
          <button @click="deleteCategory" type="button" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>