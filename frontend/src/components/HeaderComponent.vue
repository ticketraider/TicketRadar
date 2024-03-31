<script setup>
import {computed, ref} from 'vue';
import { useRouter } from 'vue-router';
import {jwtDecode} from "jwt-decode";

const router = useRouter();
const searchText = ref(''); // 검색어
const searchCriterion = ref(''); // 선택된 검색 기준의 실제 값 (예: 'title', 'location')
const sortingCriterion = ref(''); // 선택된 정렬 기준의 실제 값 (예: 'likes', 'reviews')
const searchCriterionDisplay = ref('검색 기준'); // 화면에 표시될 검색 기준 텍스트
const sortingCriterionDisplay = ref('정렬 기준'); // 화면에 표시될 정렬 기준 텍스트


// 드롭다운에서 항목을 선택했을 때 호출할 메서드
function updateSearchCriterion(value, displayText) {
  searchCriterion.value = value;
  searchCriterionDisplay.value = displayText;
}

function updateSortingCriterion(value, displayText) {
  sortingCriterion.value = value;
  sortingCriterionDisplay.value = displayText;
}

// 검색 실행 메서드
function executeSearch() {
  router.push({
    path: '/event-list',
    query: {
      search: searchText.value,
      criterion: searchCriterion.value,
      sort: sortingCriterion.value,
    }
  });
}

// 엔터 키가 눌렸을 때 검색을 실행하도록 하는 메서드
function onEnterPress(event) {
  if (event.key === "Enter") {
    executeSearch();
  }
}
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
function goEventList(categoryName) {
  router.push({
    path: '/event-list',
    query: { category: categoryName }
  });
}


function goToAdminMode() {
  router.push({ path: '/admin' }); // 'admin'은 관리자모드의 라우터 이름입니다.
}
const goHome = () => {
  router.push({path: "/"})
}
</script>

<template>
  <div style="display: flex; flex-direction: column">
    <div style="display: flex">
    <button>
      <h1 style="font-size: 30px; font-weight: bold; background: linear-gradient(to right, #53a7c7, #392365); -webkit-background-clip: text; background-clip: text; color: transparent;" @click="goHome()"><img :src="require('@/assets/ticketRadar_logo.png')" style="vertical-align: middle; width: 50px; margin-right: 10px">Ticket Radar</h1>
    </button>
    </div>
  <div style="display: flex; flex-direction: row; justify-content: space-between; align-items: baseline">
    <nav>
      <ul>
        <li><a href="#" class="category" @click="goEventList('뮤지컬')">뮤지컬</a></li>
        <li><a href="#" class="category" @click="goEventList('콘서트')">콘서트</a></li>
        <li><a href="#" class="category" @click="goEventList('스포츠')">스포츠</a></li>
        <li><a href="#" class="category" @click="goEventList('전시/행사')">전시/행사</a></li>
        <li><a href="#" class="category" @click="goEventList('클래식/무용')">클래식/무용</a></li>
        <li><a href="#" class="category" @click="goEventList('아동/가족')">아동/가족</a></li>
        <li><a href="#" class="category" @click="goEventList('연극')">연극</a></li>
      </ul>
    </nav>
    <div style="display: flex; gap: 30px">
    <div style="height: 38px; width: 500px; display: flex; gap: 10px">
      <div class="input-group">
        <button class="btn btn-outline-secondary dropdown-toggle" style="color:darkgray; width: 100px" type="button" data-bs-toggle="dropdown" aria-expanded="false">
          {{ searchCriterionDisplay }}
        </button>
        <ul class="dropdown-menu">
          <li><a class="dropdown-item" href="#" @click="updateSearchCriterion('title', '제목')">제목</a></li>
          <li><a class="dropdown-item" href="#" @click="updateSearchCriterion('location', '지역')">지역</a></li>
        </ul>
        <input type="text" placeholder="검색어를 입력하세요." v-model="searchText" @keypress="onEnterPress" class="form-control" style="color:darkgray" aria-label="Text input with 2 dropdown buttons">
        <button class="btn btn-outline-secondary dropdown-toggle" style="color:darkgray; width: 100px" type="button" data-bs-toggle="dropdown" aria-expanded="false">
          {{ sortingCriterionDisplay }}
        </button>
        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" href="#" @click="updateSortingCriterion('likes', '좋아요 순')">좋아요 순</a></li>
          <li><a class="dropdown-item" href="#" @click="updateSortingCriterion('reviews', '리뷰 순')">리뷰 순</a></li>
          <li><a class="dropdown-item" href="#" @click="updateSortingCriterion('rating', '평점 순')">평점 순</a></li>
        </ul>
      </div>
      <button type="button" class="btn btn-primary" @click="executeSearch" style="width: 120px">검색하기</button>
    </div>

    <nav>
      <!-- 로그인되지 않았을 때 보여질 버튼들 -->
      <button v-if="!isLoggedIn" class="btn btn-outline-secondary me-2" @click="login">로그인</button>
      <button v-if="!isLoggedIn" class="btn btn-outline-secondary" @click="register">회원가입</button>
      <!-- 로그인되었을 때 보여질 버튼들 -->
      <button v-if="isLoggedIn" class="btn btn-outline-light me-2" @click="logout">로그아웃</button>
      <button v-if="isLoggedIn" class="btn btn-outline-info" @click="goToMyPage">마이페이지</button>
      <!-- 관리자 역할일 때 보여질 버튼 -->
      <button v-if="isAdmin" class="btn btn-danger" style="margin-left: 8px" @click="goToAdminMode">관리자모드</button>
    </nav>
    </div>
  </div>
  </div>

</template>

<style scoped>
.category {
  color: gray;
}
</style>