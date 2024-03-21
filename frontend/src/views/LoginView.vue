<script setup>
import {useRouter} from "vue-router";

const router = useRouter();
const signUp = () => {
  router.push({path: "/sign_up"})
}
</script>

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
                 aria-describedby="emailHelp">
        </div>
        <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">비밀번호</label>
          <input type="password" placeholder="비밀번호를 입력해주세요" class="form-control" id="exampleInputPassword1">
        </div>
        <div style="text-align: right">
          <button class="btn btn-primary" style="background-color: #263e5e; border-color: #263e5e"
                  @click="signUp">회원 가입
          </button>
          <button type="submit" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;">로그인
          </button>
        </div>
      </form>
    </div>

  </main>


</template>

<style scoped>
</style>