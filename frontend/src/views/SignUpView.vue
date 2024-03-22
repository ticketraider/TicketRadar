<script setup>

</script>

<template>
  <main style="height: 750px">
    <div style="background-color: #aa98ba; padding: 30px; margin: 30px auto; height: 600px; width: 60%; border-radius: 20px">

      <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto">
        <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
          <h2>회원 가입</h2>
        </div>
        <div class="mb-3">
          <label for="exampleInputEmail1" class="form-label">이메일</label>
          <input type="email" placeholder="이메일을 입력해주세요" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
        </div>
        <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">비밀번호</label>
          <input type="password" placeholder="비밀번호를 입력해주세요" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">닉네임</label>
          <input type="password" placeholder="닉네임을 입력해주세요" class="form-control" id="exampleInputPassword1">
        </div>
        <div style="text-align: right;">
          <button type="submit" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;">회원 가입
          </button>
        </div>

      </form>
    </div>

  </main>
</template>

<style scoped>

</style>