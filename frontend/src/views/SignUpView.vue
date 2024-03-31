<template>
  <main style="height: 750px; background-color: #EEEAF1; display: flex; justify-content: center; align-items: center">
    <div style="background-color: #0B0722; border-radius: 12px; width: 40%; padding: 35px; display: flex; flex-direction: column; align-items: center; justify-content: center;">
      <div style="display: flex; justify-content: center">
        <img :src="require('@/assets/ticketRadar.png')" style="width: 400px">
      </div>
      <form @submit.prevent="submitForm" style="color: white; width: 100%">
        <div class="mb-3">
          <label for="email" class="form-label">이메일</label>
          <input type="email" placeholder="이메일을 입력해주세요" class="form-control" id="email" v-model="email">
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input type="password" placeholder="비밀번호를 입력해주세요" class="form-control" id="password" v-model="password">
        </div>
        <div class="mb-3">
          <label for="nickname" class="form-label">닉네임</label>
          <input type="text" placeholder="닉네임을 입력해주세요" class="form-control" id="nickname" v-model="nickname">
        </div>
        <div style="width: 100%">
          <button type="submit" class="btn btn-primary" style="width: 100%; background-color: #392365; border-color: #392365;">회원 가입</button>
        </div>
      </form>
    </div>
  </main>
</template>

<script>
import axios from 'axios';
import {useRouter} from "vue-router";
import router from "@/router/router"; useRouter;

export default {
  data() {
    return {
      email: '',
      password: '',
      nickname: ''
    };
  },
  methods: {
    async submitForm() {
      try {
        const response = await axios.post('http://localhost:8080/members/signUp', {
          email: this.email,
          password: this.password,
          nickname: this.nickname
        });
        console.log(response.data);
        // 여기서 적절한 응답 처리를 수행합니다.

        await router.push('/event-list');
        location.reload();// 이벤트 목록 페이지로 이동

      } catch (error) {
        console.error(error);
        // 에러 처리 로직을 추가합니다.
      }
    }
  }
}
</script>

<style scoped>

</style>