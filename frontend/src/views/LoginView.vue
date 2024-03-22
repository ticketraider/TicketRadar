<template>
  <main style="height: 750px">
    <div
        style="background-color: #aa98ba; padding: 30px; margin: 30px auto; height: 600px; width: 60%; border-radius: 20px">

      <form style="color: white; width: 500px; height: 400px; margin: 140px auto auto auto">
        <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
          <h2>로그인</h2>
        </div>
        <div class="mb-3">
          <label for="exampleInputEmail1" class="form-label">이메일</label>
          <input type="email" placeholder="이메일을 입력해주세요" class="form-control" id="exampleInputEmail1"
                 aria-describedby="emailHelp" v-model="email">
        </div>
        <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">비밀번호</label>
          <input type="password" placeholder="비밀번호를 입력해주세요" class="form-control" id="exampleInputPassword1"
                 v-model="password">
        </div>
        <div style="text-align: right">
          <button class="btn btn-primary" style="background-color: #263e5e; border-color: #263e5e"
                  @click="signUp">회원 가입
          </button>
          <button type="button" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;"
                  @click="signIn">로그인
          </button>
          <button type="button" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;"
                  @click="displayToken">토큰 출력
          </button>
          <button type="button" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;"
                  @click="logOut">로그아웃
          </button>
        </div>
      </form>
    </div>
  </main>
</template>

<script setup>
import {useRouter} from "vue-router";
import axios from 'axios';

const router = useRouter();
let email = ''; // 사용자 입력을 저장할 변수
let password = ''; // 사용자 입력을 저장할 변수

const signUp = () => {
  router.push({path: "/sign-up"})
}

const signIn = async () => {
  try {
    console.log(email, password); // 입력된 값 출력
    const response = await axios.post('http://localhost:8080/members/login', {
      email: email,
      password: password
    });

    // 로그인 성공 시 처리
    console.log("응답 : ", response.data);
    console.log(response.data);
    const token = response.data.token; // 토큰 추출
    localStorage.setItem('token', token); // 토큰 로컬 스토리지에 저장
    await router.push('/eventList');
    location.reload();// 이벤트 목록 페이지로 이동

  } catch (error) {
    // 로그인 실패 시 처리
    console.error(error);
  }
}
const logOut = async () => {
  console.log("로그아웃전")
  displayToken()
  localStorage.removeItem('token');
  console.log("로그아웃후")
  displayToken()
}


const displayToken = () => {
  for (let i = 0; i < localStorage.length; i++) {
    const key = localStorage.key(i);
    const value = localStorage.getItem(key);
    console.log(`Key: ${key}, Value: ${value}`);
  }
}

</script>

<style scoped>
</style>