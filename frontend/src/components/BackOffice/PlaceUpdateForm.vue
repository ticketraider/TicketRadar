<template>
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#updatePlace"
              aria-expanded="false" aria-controls="updatePlace">
        장소 수정
      </button>
    </h2>
    <div id="updatePlace" class="accordion-collapse collapse" data-bs-parent="#accordionPlace">
      <div class="accordion-body">

        <div class="mb-3">
          <label for="categoryId" class="form-label">장소 ID</label>
          <input v-model="Place.updateId"  type="number" class="form-control" id="exampleInputEmail1"
                 aria-describedby="emailHelp">
        </div>
        <div class="mb-3">
          <label for="categoryTitle" class="form-label">장소 이름</label>
          <input v-model="Place.updateTitle" type="text" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
          <label for="categoryTitle" class="form-label">주소</label>
          <input v-model="Place.address" type="text" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
          <label for="categoryTitle" class="form-label">R석 갯수</label>
          <input v-model="Place.seatR" type="number" class="form-control" id="placeTitle">
        </div>
        <div class="mb-3">
          <label for="categoryTitle" class="form-label">S석 갯수</label>
          <input v-model="Place.seatS" type="number" class="form-control" id="placeTitle">
        </div>
        <div class="mb-3">
          <label for="categoryTitle" class="form-label">A석 갯수</label>
          <input v-model="Place.seatA" type="number" class="form-control" id="placeTitle">
        </div>
        <div style="text-align: right;">
          <button @click="updatePlace" type="button" class="btn btn-primary" style="background-color: #392365; border-color: #392365; margin-left: 15px;"> 입력 </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref } from 'vue';

// 로컬 스토리지에서 JWT 토큰 가져오기
const token = localStorage.getItem('token');

// 새 카테고리를 나타내는 데이터
const Place = ref({
  updateId : '',
  updateTitle : '',
  address : '',
  seatR : 0,
  seatS : 0,
  seatA : 0,
});

const updatePlace = () => {
  const placeId = parseInt(Place.value.updateId)

  // 서버로 POST 요청 보내기
  axios.put(`http://localhost:8080/place/${placeId}`,
      {
        name: Place.value.updateTitle,
        address : Place.value.address,
        seatR :parseInt(Place.value.seatR),
        seatS :parseInt(Place.value.seatS),
        seatA :parseInt(Place.value.seatA),
      },
      {
        headers: {
          Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
        }
      })
      .then(response => {
        // 성공적으로 생성되었을 때의 처리 (예: 페이지 리로드)
        console.log('장소 수정 성공:', response.data);
        window.location.reload();
      })
      .catch(error => {
        console.error('장소 수정 오류:', error);
        // 실패 시에도 현재 헤더에 포함된 토큰 확인
        console.log('현재 헤더:', error.config.headers);
        // 오류 발생 시 사용자에게 알림
        alert('장소 수정 실패했습니다.');
      });
};
</script>

<style scoped>

</style>