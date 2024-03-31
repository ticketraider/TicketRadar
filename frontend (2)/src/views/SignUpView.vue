<template>
  <main style="height: 750px">
    <div style="background-color: #aa98ba; padding: 30px; margin: 30px auto; height: 600px; width: 60%; border-radius: 20px">

      <form @submit.prevent="submitForm" style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto">
        <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
          <h2>회원 가입</h2>
        </div>
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
        <div style="text-align: right;">
          <button type="submit" class="btn btn-primary" style="background-color: #392365; border-color: #392365; margin-left: 15px;">회원 가입</button>
        </div>
      </form>
    </div>
  </main>
</template>

<script>
import axios from 'axios';

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