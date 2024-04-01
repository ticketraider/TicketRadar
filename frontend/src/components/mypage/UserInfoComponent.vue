<template>
  <div>
    <div
        style="background-color: #392365; width: 980px; text-align: center; border-radius: 5px; margin-bottom: 20px; color: white">
      <h2>유저 정보</h2>
    </div>
    <ul>
      <div v-if="!isPasswordVerified" style="display: flex; justify-content: center; width: 100%">
        <div class="mb-3" style="width: 400px;">
          <input type="password" v-model="currentPassword" placeholder="현재 비밀번호를 입력해주세요" class="form-control"
                 id="exampleInputPassword1" @keyup.enter="verifyCurrentPassword">
        </div>
        <button type="button" class="btn btn-primary"
                style="background-color: #392365; border-color: #392365; margin-left: 15px; height: 40px;" @click="verifyCurrentPassword">
          비밀번호 확인
        </button>
      </div>

      <form v-if="isPasswordVerified" style="color: white; width: 910px; height: 400px; justify-content: center">



        <label for="exampleInputEmail1" class="form-label">유저 정보 변경</label>
        <div class="mb-3">
          <p>현재 닉네임: {{ currentUserNickname }}</p>
          <p>현재 이메일: {{ currentUserEmail }}</p>
        </div>
        <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">비밀번호</label>
          <input type="password" v-model="password" placeholder="비밀번호를 입력해주세요" class="form-control"
                 id="exampleInputPassword1">
        </div>
        <div class="mb-3">
          <label for="exampleInputEmail1" class="form-label">닉네임</label>
          <input type="text" v-model="nickname" placeholder="닉네임을 입력해주세요" class="form-control" id="exampleInputEmail1"
                 aria-describedby="emailHelp">
        </div>
        <div style="text-align: right">
          <button type="button" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;" @click="updateUserInfo">
            변경
          </button>
        </div>
        <div style="text-align: end; margin-top: 200px">
          <button type="button" class="btn btn-danger" @click="deleteAccount">계정 탈퇴
          </button>
        </div>
      </form>
    </ul>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import axios from 'axios';
import {jwtDecode} from "jwt-decode";

// 상태 관리
const password = ref("");
const nickname = ref("");
const currentPassword = ref("");
const currentUserNickname = ref("");
const currentUserEmail = ref("");// 유저 현재 닉네임
const isPasswordVerified = ref(false); // 비밀번호가 확인되었는지 나타내는 상태
const apiUrl = 'http://localhost:8080';
const token = localStorage.getItem('token');

// 현재 비밀번호 확인 후 유저 정보 업데이트 폼 표시
async function verifyCurrentPassword() {
  try {
    console.log(token)
    console.log(currentPassword.value)
    const response = await axios.post(`${apiUrl}/members/verify-password`, {}, {
      params: {
        currentPassword: currentPassword.value
      },
      headers: {
        Authorization: `Bearer ${token}`
      },
    });
    await fetchUserInfo();
    if (response.data.success) {
      isPasswordVerified.value = true;
    // 비밀번호 확인 성공 시, 업데이트 폼 표시
    } else {
      alert('비밀번호가 일치하지 않습니다.');
    }
  } catch (error) {
    console.error('비밀번호 확인 중 오류가 발생했습니다.', error);
    alert('비밀번호가 일치하지 않습니다.');
  }
}

// 유저 정보 업데이트
async function updateUserInfo() {
  if (password.value.length < 6) {
    alert('비밀번호는 최소 6자 이상이어야 합니다.');
    return; // 조건에 맞지 않으면 함수 종료
  }
  try {
    await axios.put(`${apiUrl}/members/update`, {
      nickname: nickname.value,
      password: password.value,
    }, {
      headers: {
        Authorization: `Bearer ${token}`
      },
    });

    alert('유저 정보가 성공적으로 업데이트 되었습니다.');
    window.location.reload()
    // 업데이트 성공 후 필요한 처리 추가 (예: 페이지 새로고침, 리다이렉트 등)

  } catch (error) {
    console.error('유저 정보 업데이트 중 오류가 발생했습니다.', error);
    alert('유저 정보 업데이트 중 문제가 발생했습니다.');
  }
}
async function fetchUserInfo() {

  const decodedToken = jwtDecode(token);
  const memberId = decodedToken.sub;
  console.log(token)

  try {
    const response = await axios.get(`${apiUrl}/members/${memberId}`);
    currentUserNickname.value = response.data.nickname; // 조회된 유저 닉네임 저장
    currentUserEmail.value = response.data.email; // 조회된 유저 이메일 저장
  } catch (error) {
    console.error('유저 정보 조회 중 오류가 발생했습니다.', error);
  }
}

// 계정 삭제 로직
async function deleteAccount() {
  // 사용자 확인 절차 추가 권장 (예: "정말로 계정을 삭제하시겠습니까?" 대화상자 표시)
  const confirmation = confirm('정말로 계정을 삭제하시겠습니까?');
  if (!confirmation) {
    return;
  }

  try {
    await axios.delete(`${apiUrl}/members/unregister`, {
      headers: {
        Authorization: `Bearer ${token}`
      }, // DELETE 요청 시 본문을 보내려면 'data' 속성 사용
    });


    alert('계정이 성공적으로 삭제되었습니다.');
    window.location.reload()
    // 계정 삭제 성공 후 필요한 처리 추가 (예: 로그아웃, 홈 페이지로 리다이렉트)
  } catch (error) {
    console.error('계정 삭제 중 오류가 발생했습니다.', error);
    alert('계정 삭제 중 문제가 발생했습니다.');
  }
}
</script>


<style scoped>

</style>