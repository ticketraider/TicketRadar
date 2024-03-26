<template>
 <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto" > <!-- @submit.prevent="createCategory"-->
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
              <input v-model="newCategory.createTitle" type="text" class="form-control" id="categoryTitle">
            </div>
            <div style="text-align: right;">
              <button @click="createCategory" type="button" class="btn btn-primary" style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 카테고리 수정 -->
      <div class="accordion-item">
        <h2 class="accordion-header">
          <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                  data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
            카테고리 수정
          </button>
        </h2>
        <div id="collapseTwo" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
          <div class="accordion-body">

            <div class="mb-3">
              <label for="categoryId" class="form-label">카테고리 ID</label>
              <input v-model="newCategory.updateId"  type="number" class="form-control" id="exampleInputEmail1"
                     aria-describedby="emailHelp">
            </div>
            <div class="mb-3">
              <label for="categoryTitle" class="form-label">타이틀</label>
              <input v-model="newCategory.updateTitle" type="text" class="form-control" id="exampleInputPassword1">
            </div>
            <div style="text-align: right;">
              <button @click="updateCategory" type="button" class="btn btn-primary" style="background-color: #392365; border-color: #392365; margin-left: 15px;"> 입력 </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 카테고리 삭제 -->
      <div class="accordion-item">
        <h2 class="accordion-header">
          <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                  data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
            카테고리 삭제
          </button>
        </h2>
        <div id="collapseThree" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
          <div class="accordion-body">

            <div class="mb-3">
              <label for="exampleInputPassword1" class="form-label">카테고리 ID</label>
              <input v-model="newCategory.deleteId" type="number" class="form-control" id="exampleInputPassword1">
            </div>
            <div style="text-align: right;">
              <button @click="deleteCategory" type="button" class="btn btn-primary"
                      style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
              </button>
            </div>

          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

// 로컬 스토리지에서 JWT 토큰 가져오기
const token = localStorage.getItem('token');

// 새 카테고리를 나타내는 데이터
const newCategory = ref({
  createTitle : '',
  updateId : '',
  updateTitle : '',
  deleteId : '',
});

// 카테고리를 생성하는 함수
const createCategory = () => {

  // 서버로 POST 요청 보내기
  axios.post('http://localhost:8080/categories',
      {
        title: newCategory.value.createTitle
      },
      {
    headers: {
      Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
    }
  })
      .then(response => {
        console.log('카테고리 생성 성공:', response.data);
        // 성공적으로 생성되었을 때의 처리 (예: 페이지 리로드)
        window.location.reload();
      })
      .catch(error => {
        console.error('카테고리 생성 오류:', error);
        // 실패 시에도 현재 헤더에 포함된 토큰 확인
        console.log('현재 헤더:', error.config.headers);
        // 오류 발생 시 사용자에게 알림
        alert('카테고리 생성에 실패했습니다.');
      });
};

const updateCategory = () => {
  const categoryId = parseInt(newCategory.value.updateId)

  // 서버로 POST 요청 보내기
  axios.put(`http://localhost:8080/categories/${categoryId}`,
      {
        title: newCategory.value.updateTitle
      },
      {
        headers: {
          Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
        }
      })
      .then(response => {
        // 성공적으로 생성되었을 때의 처리 (예: 페이지 리로드)
        console.log('카테고리 생성 성공:', response.data);
        window.location.reload();
      })
      .catch(error => {
        console.error('카테고리 생성 오류:', error);
        // 실패 시에도 현재 헤더에 포함된 토큰 확인
        console.log('현재 헤더:', error.config.headers);
        // 오류 발생 시 사용자에게 알림
        alert('카테고리 생성에 실패했습니다.');
      });
};

const deleteCategory = () => {
  const categoryId = parseInt(newCategory.value.deleteId)

  // 서버로 POST 요청 보내기
  axios.delete(`http://localhost:8080/categories/${categoryId}`, {
        headers: {
          Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
        }
      })
      .then(response => {
        console.log('카테고리 삭제 성공:', response.data);
        // 성공적으로 생성되었을 때의 처리 (예: 페이지 리로드)
        alert('카테고리 삭제에 성공했습니다.');
        window.location.reload();
      })
      .catch(error => {
        console.error('카테고리 삭제 오류:', error);
        // 실패 시에도 현재 헤더에 포함된 토큰 확인
        console.log('현재 헤더:', error.config.headers);
        // 오류 발생 시 사용자에게 알림
        alert('카테고리 삭제에 실패했습니다.');
      });
};

</script>

<style scoped>
</style>