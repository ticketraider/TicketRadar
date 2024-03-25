<template>
  <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto">
    <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
      <h2>카테고리</h2>
    </div>

    <div class="accordion" id="accordionExample">
      <!-- 카테고리 생성 -->
      <div class="accordion-item">
        <h2 class="accordion-header">
          <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne"
                  aria-expanded="false" aria-controls="collapseOne">
            카테고리 생성
          </button>
        </h2>
        <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
          <div class="accordion-body">
            <div class="mb-3">
              <label for="exampleInputEmail1" class="form-label">타이틀</label>
              <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
            </div>
            <div style="text-align: right;">
              <button type="submit" class="btn btn-primary" style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 카테고리 수정 -->
      <div class="accordion-item">
        <h2 class="accordion-header">
          <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                  data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
            카테고리 수정
          </button>
        </h2>
        <div id="collapseTwo" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
          <div class="accordion-body">
            <div class="mb-3">
              <label for="exampleInputEmail1" class="form-label">카테고리 ID</label>
              <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
            </div>
            <div class="mb-3">
              <label for="exampleInputPassword1" class="form-label">타이틀</label>
              <input type="password" class="form-control" id="exampleInputPassword1">
            </div>
            <div style="text-align: right;">
              <button type="submit" class="btn btn-primary" style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 카테고리 삭제 -->
      <div class="accordion-item">
        <h2 class="accordion-header">
          <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                  data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
            카테고리 삭제
          </button>
        </h2>
        <div id="collapseThree" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
          <div class="accordion-body">
            <div class="mb-3">
              <label for="exampleInputPassword1" class="form-label">카테고리 ID</label>
              <input type="password" class="form-control" id="exampleInputPassword1">
            </div>
            <div style="text-align: right;">
              <button type="submit" class="btn btn-primary" style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<script setup>
</script>

<style scoped>
</style>