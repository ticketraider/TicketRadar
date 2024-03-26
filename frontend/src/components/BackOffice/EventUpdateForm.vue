<script setup>

</script>

<template>
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
              data-bs-target="#updateEvent" aria-expanded="false" aria-controls="updateEvent">
        이벤트 수정
      </button>
    </h2>
    <div id="updateEvent" class="accordion-collapse collapse" data-bs-parent="#event">
      <div class="accordion-body">

        <div class="mb-3">
          <input type="email" placeholder="이벤트 ID" class="form-control" id="exampleInputEmail1"
                 aria-describedby="emailHelp">
        </div>
        <div class="mb-3">
          <input type="email" placeholder="카테고리 ID" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
          <div class="input-group mb-3">
            <label class="input-group-text" for="inputGroupFile01">이미지</label>
            <input type="file" class="form-control" id="inputGroupFile01">
          </div>
          <input type="email" placeholder="타이틀" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
          <input type="email" placeholder="이벤트 정보" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
          <input type="email" placeholder="시작 날짜 (예시: 2024-03-21)" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
          <input type="email" placeholder="끝나는 날짜 (예시: 2024-03-21)" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
          <input type="email" placeholder="장소" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
          <input type="email" placeholder="R시트 가격" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
          <input type="email" placeholder="S시트 가격" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
          <input type="email" placeholder="A시트 가격" class="form-control" id="exampleInputEmail1"
                 style="margin-bottom: 15px" aria-describedby="emailHelp">
        </div>
        <div style="text-align: right;">
          <button type="submit" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
          </button>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped>

</style>