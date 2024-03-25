
<template>
  <main>
    <div
        style="background-color: #aa98ba; padding: 30px; margin: 30px auto 100px auto; min-height: 700px; height: 1520px; width: 60%; border-radius: 20px">

      <!--      카테고리-->
      <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto">
        <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
          <h2>카테고리</h2>
        </div>

        <div class="accordion" id="accordionExample">
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
                  <input type="email" class="form-control" id="exampleInputEmail1"
                         aria-describedby="emailHelp">
                </div>
                <div style="text-align: right;">
                  <button type="submit" class="btn btn-primary"
                          style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
                  </button>
                </div>

              </div>
            </div>
          </div>
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
                  <input type="email" class="form-control" id="exampleInputEmail1"
                         aria-describedby="emailHelp">
                </div>
                <div class="mb-3">
                  <label for="exampleInputPassword1" class="form-label">타이틀</label>
                  <input type="password" class="form-control" id="exampleInputPassword1">
                </div>
                <div style="text-align: right;">
                  <button type="submit" class="btn btn-primary"
                          style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
                  </button>
                </div>

              </div>
            </div>
          </div>
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
                  <button type="submit" class="btn btn-primary"
                          style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
                  </button>
                </div>

              </div>
            </div>
          </div>
        </div>

      </form>

      <!--      이벤트-->
      <form style="color: white; width: 500px; height: 400px; margin: 100px auto auto auto">
        <div style="background-color: #392365; text-align: center; border-radius: 5px; margin-bottom: 20px">
          <h2>이벤트</h2>
        </div>


        <div class="accordion" id="event">
          <div class="accordion-item">
            <h2 class="accordion-header">
              <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#createEvent"
                      aria-expanded="true" aria-controls="createEvent">
                이벤트 생성
              </button>
            </h2>
            <div id="createEvent" class="accordion-collapse collapse show" data-bs-parent="#event">
              <div class="accordion-body">

                <!--                상세정보 넣는 칸-->
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
          <div class="accordion-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                      data-bs-target="#deleteEvent" aria-expanded="false" aria-controls="deleteEvent">
                이벤트 삭제
              </button>
            </h2>
            <div id="deleteEvent" class="accordion-collapse collapse" data-bs-parent="#event">
              <div class="accordion-body">

                <div class="mb-3">
                  <input type="password" placeholder="이벤트 ID" class="form-control" id="exampleInputPassword1">
                </div>
                <div style="text-align: right;">
                  <button type="submit" class="btn btn-primary"
                          style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
                  </button>
                </div>

              </div>
            </div>
          </div>
        </div>

      </form>
    </div>

  </main>
</template>

<script setup>

</script>


<style scoped>

</style>