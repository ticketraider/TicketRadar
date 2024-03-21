<script setup>
import {useRouter} from "vue-router";

const router = useRouter();
const goHome = () => {
  router.push({path: "/"})
}
const signUp = () => {
  router.push({path: "/sign-up"});
};
const login = () => {
  router.push({path: "/login"});
};
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

    <div class="text-end">
      <button type="button" class="btn btn-outline-light me-2" @click="login">로그인</button>
      <button type="button" class="btn btn-outline-info" @click="signUp">회원 가입</button>
    </div>
  </div>
  <nav>
    <ul>
      <li><a href="#" class="category">뮤지컬</a></li>
      <li><a href="#" class="category">콘서트</a></li>
      <li><a href="#" class="category">스포츠</a></li>
      <li><a href="#" class="category">전시/행사</a></li>
      <li><a href="#" class="category">클래식/무용</a></li>
      <li><a href="#" class="category">아동/가족</a></li>
      <li><a href="#" class="category">연극</a></li>
    </ul>
  </nav>
</template>

<style scoped>
.category {
  color: #53a7c7;
  font-weight: bold;
}
</style>