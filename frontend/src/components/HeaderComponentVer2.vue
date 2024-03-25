<script setup>

import { computed } from 'vue';
// import jwtDecode from 'jwt-decode';
import { useRouter } from 'vue-router';
import {jwtDecode} from "jwt-decode";

const router = useRouter();

// 가정: 로그인 상태와 토큰은 VueX, Pinia, 또는 localStorage 같은 곳에 저장되어 있다고 가정
// 이 예시에서는 단순화를 위해 localStorage를 사용합니다.
const token = localStorage.getItem('token');

const isLoggedIn = computed(() => !!token);
const userRole = computed(() => {
  if (!token) return null;
  const decoded = jwtDecode(token);
  return decoded.role // 'role'은 토큰 내에 담겨있는 사용자 역할을 나타내는 속성입니다.
});
const isAdmin = computed(() => userRole.value === 'ADMIN');

function login() {
  router.push({path: "/login"}); // 'login'은 로그인 페이지의 라우터 이름입니다.
}

function register() {
  router.push({ path: "/sign-up" }); // 'register'는 회원가입 페이지의 라우터 이름입니다.
}

function logout() {
  // 로그아웃 로직 실행, 예: 토큰 삭제
  localStorage.removeItem('token');
  location.reload(); // 간단하게 페이지를 새로고침하여 상태를 초기화합니다.
}

function goToMyPage() {
  router.push({ path: "/my-page" }); // 'mypage'는 마이페이지의 라우터 이름입니다.
}
function goEventList() {
  router.push({ path: '/event-list' });
}

function goToAdminMode() {
  router.push({ path: '/admin' }); // 'admin'은 관리자모드의 라우터 이름입니다.
}
const goHome = () => {
  router.push({path: "/"})
}
</script>

<template>
  <div style="display: flex;">
    <button>
      <h1 style="font-size: 60px; font-weight: bold; color: #aa98ba" @click="goHome()">Ticket Radar</h1>
    </button>
    <v-spacer></v-spacer>
    <div style="height: 50px; width: 600px; margin-right: 10px">
      <div class="input-group" >
        <button class="btn btn-outline-secondary dropdown-toggle" style="color:white;" type="button" data-bs-toggle="dropdown"
                aria-expanded="false">검색 기준
        </button>
        <ul class="dropdown-menu">
          <li><a class="dropdown-item" href="#">제목</a></li>
          <li><a class="dropdown-item" href="#">지역</a></li>
        </ul>
        <input type="text" class="form-control" aria-label="Text input with 2 dropdown buttons">
        <button class="btn btn-outline-secondary dropdown-toggle" style="color:white;" type="button" data-bs-toggle="dropdown"
                aria-expanded="false">정렬 기준
        </button>
        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" href="#">좋아요 순</a></li>
          <li><a class="dropdown-item" href="#">리뷰 순</a></li>
        </ul>
      </div>
    </div>

    <nav>
      <!-- 로그인되지 않았을 때 보여질 버튼들 -->
      <button v-if="!isLoggedIn" class="btn btn-outline-light me-2" @click="login">로그인</button>
      <button v-if="!isLoggedIn" class="btn btn-outline-info" @click="register">회원가입</button>
      <!-- 로그인되었을 때 보여질 버튼들 -->
      <button v-if="isLoggedIn" class="btn btn-outline-light me-2" @click="logout">로그아웃</button>
      <button v-if="isLoggedIn" class="btn btn-outline-info" @click="goToMyPage">마이페이지</button>
      <!-- 관리자 역할일 때 보여질 버튼 -->
      <button v-if="isAdmin" class="btn btn-danger" style="margin-left: 8px" @click="goToAdminMode">관리자모드</button>
    </nav>


  </div>
  <nav>
    <ul>
      <li><a href="#" class="category" @click="goEventList">뮤지컬</a></li>
      <li><a href="#" class="category" @click="goEventList">콘서트</a></li>
      <li><a href="#" class="category" @click="goEventList">스포츠</a></li>
      <li><a href="#" class="category" @click="goEventList">전시/행사</a></li>
      <li><a href="#" class="category" @click="goEventList">클래식/무용</a></li>
      <li><a href="#" class="category" @click="goEventList">아동/가족</a></li>
      <li><a href="#" class="category" @click="goEventList">연극</a></li>
    </ul>
  </nav>
</template>

<style scoped>
.category {
  color: #53a7c7;
  font-weight: bold;
}
</style>