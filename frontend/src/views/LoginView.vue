<template>
  <main style="height: 810px; background-color: #EEEAF1; display: flex; justify-content: center; align-items: center">
    <div
        style="background-color: #0B0722; border-radius: 12px; width: 40%; display: flex; flex-direction: column; align-items: center; justify-content: center;">
      <form style="color: white;">
        <div style="display: flex; justify-content: center">
          <img :src="require('@/assets/ticketRadar.png')" style="width: 400px">
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
        <div>
          <div style="margin-top: 10px; width: 100%">
            <button type="button" class="btn btn-primary"
                    style="font-weight:bold; background-color: #392365; border-color: #392365; width: 100%; height: 42px;"
                    @click="signIn">로그인
            </button>
            <button style="width: 100%; color: #CABED4; height: 42px; margin-top: 30px;"
                    @click="signUp">❗아직 회원이 아니신가요?
            </button>
          </div>
          <div style="display: flex; width: 100%; justify-content: center">
            <button style="margin-top: 10px; margin-left: 20px;" @click="kakaoSocialSignIn">
              <v-img style="height: 50px; width: 240px;" :src="require('@/assets/kakao_login_large_narrow.png')" cover></v-img>
            </button>
            <button style="margin-top: 10px" @click="googleSocialSignIn">
              <v-img style="height: 50px; width: 240px;" :src="require('@/assets/web_neutral_sq_SU@2x.png')" cover></v-img>
            </button>
          </div>



          <!--          <button type="button" class="btn btn-primary"-->
          <!--                  style="background-color: #392365; border-color: #392365; margin-left: 15px;"-->
          <!--                  @click="displayToken">토큰 출력-->
          <!--          </button>-->
          <!--          <button type="button" class="btn btn-primary"-->
          <!--                  style="background-color: #392365; border-color: #392365; margin-left: 15px;"-->
          <!--                  @click="logOut">로그아웃-->
          <!--          </button>-->
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
    const token = response.data.token; // 토큰 추출
    localStorage.setItem('token', token); // 토큰 로컬 스토리지에 저장

    // 이후에는 토큰을 사용하여 요청을 보낼 때마다 헤더에 포함하여 전송
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

    await router.back()
    location.reload()
    // await router.push('/event-list');
    // location.reload();// 이벤트 목록 페이지로 이동

  } catch (error) {
    // 로그인 실패 시 처리
    console.error(error);
    alert("로그인에 실패하였습니다.");
  }
}
const kakaoSocialSignIn = async () => {
  window.open("http://localhost:8080/oauth2/login/kakao", '', 'width=400,height=600')
  const token = document.cookie.replace(/(?:^|.*;\s*)token\s*=\s*([^;]*).*$|^.*$/, "$1");
  localStorage.setItem('token', token); // 로컬 스토리지에 토큰 저장
  document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  console.log("소셜 로그인 함수가 호출되었습니다.");
}
const googleSocialSignIn = async () => {
  window.open("http://localhost:8080/oauth2/login/google", '', 'width=400,height=600')
  const token = document.cookie.replace(/(?:^|.*;\s*)token\s*=\s*([^;]*).*$|^.*$/, "$1");
  localStorage.setItem('token', token); // 로컬 스토리지에 토큰 저장
  document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  console.log("소셜 로그인 함수가 호출되었습니다.");
}
// const logOut = async () => {
//   if (localStorage.getItem('token')) {
//     localStorage.removeItem('token');
//     alert("로그아웃 되었습니다.");
//   } else {
//     alert("로그인 되어있지 않습니다.");
//   }
// }


// const displayToken = () => {
//   for (let i = 0; i < localStorage.length; i++) {
//     const key = localStorage.key(i);
//     const value = localStorage.getItem(key);
//     console.log(`Key: ${key}, Value: ${value}`);
//     console.log('요청 헤더:', axios.defaults.headers);
//   }
// }

</script>

<style scoped>
</style>