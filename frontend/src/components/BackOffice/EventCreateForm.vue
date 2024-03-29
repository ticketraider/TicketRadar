<template>
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#createEvent"
              aria-expanded="true" aria-controls="createEvent">
        이벤트 생성
      </button>
    </h2>
    <div id="createEvent" class="accordion-collapse collapse" data-bs-parent="#event">
      <div class="accordion-body">
        <div class="mb-3">
          <input type="text" v-model="eventRequest.categoryId" placeholder="카테고리 ID" class="form-control" id="categoryIdInput"
                 style="margin-bottom: 15px" aria-describedby="categoryIdHelp">
          <div class="input-group mb-3">
            <label class="input-group-text" for="imgForCreate">이미지</label>
            <input type="file" class="form-control" id="imgForCreate">
          </div>
          <div style="text-align: right;">
            <button @click="uploadImage" class="btn btn-primary" type="button"
                    style="background-color: #392365; border-color: #392365; margin-left: 15px;"> 업로드
            </button>
          </div>
          <input type="text" v-model="eventRequest.title" placeholder="타이틀" class="form-control" id="titleInput"
                 style="margin-bottom: 15px" aria-describedby="titleHelp">
          <textarea v-model="eventRequest.eventInfo" placeholder="이벤트 정보" class="form-control" id="eventInfoInput"
                    style="margin-bottom: 15px" aria-describedby="eventInfoHelp"></textarea>
          <input type="date" v-model="eventRequest.startDate" placeholder="시작 날짜 (예시: 2024-03-21)" class="form-control" id="startDateInput"
                 style="margin-bottom: 15px" aria-describedby="startDateHelp">
          <input type="date" v-model="eventRequest.endDate" placeholder="끝나는 날짜 (예시: 2024-03-21)" class="form-control" id="endDateInput"
                 style="margin-bottom: 15px" aria-describedby="endDateHelp">
          <input type="text" v-model="eventRequest.place" placeholder="장소" class="form-control" id="placeInput"
                 style="margin-bottom: 15px" aria-describedby="placeHelp">
          <input type="number" v-model="eventRequest.seatRPrice" placeholder="R시트 가격" class="form-control" id="seatRPriceInput"
                 style="margin-bottom: 15px" aria-describedby="seatRPriceHelp">
          <input type="number" v-model="eventRequest.seatSPrice" placeholder="S시트 가격" class="form-control" id="seatSPriceInput"
                 style="margin-bottom: 15px" aria-describedby="seatSPriceHelp">
          <input type="number" v-model="eventRequest.seatAPrice" placeholder="A시트 가격" class="form-control" id="seatAPriceInput"
                 style="margin-bottom: 15px" aria-describedby="seatAPriceHelp">
        </div>
        <div style="text-align: right;">
          <button @click="createEvent" class="btn btn-primary" type="button"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

// 로컬 스토리지에서 JWT 토큰 가져오기
const token = localStorage.getItem('token');


export default {
  data() {
    return {
      eventRequest: {
        categoryId: null,
        title: '',
        eventInfo: '',
        startDate: '',
        endDate: '',
        place: '',
        seatRPrice: null,
        seatSPrice: null,
        seatAPrice: null,
        posterImage : ''
      },
      imgUrl:''
    };
  },
  methods: {
    async uploadImage() {
      // 파일 인풋에서 선택한 파일 가져오기
      console.log( document.getElementById('imgForCreate') )
      let imageFile = document.getElementById('imgForCreate').files[0];
      console.log(`ImageFile : ${imageFile}`)
      // 파일이 선택되지 않았을 경우 처리
      if (!imageFile) {
        alert('이미지를 선택하세요.');
        return;
      }

      try {
        let formData = new FormData();
        formData.append('file', imageFile);
        console.log(`formData : ${formData.values}`)
        let response = await axios.post('http://localhost:8080/events/imgUpload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });

        // 서버에서 반환하는 응답 처리
        alert(`이미지 업로드 성공: ${response.data}`);
        this.imgUrl = response.data;
        alert(`이미지 업로드 성공: ${this.imgUrl}`);
      } catch (error) {
        alert(`이미지 업로드 실패: ${error}`);
      }
    },


    async createEvent() {
      try {
        console.log(this.eventRequest)

        this.eventRequest.posterImage = this.imgUrl

        // Axios를 사용하여 API에 POST 요청을 보냅니다.
        await axios.post('http://localhost:8080/events', this.eventRequest, {
          headers: {
            Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
          },
        });

        console.log('이벤트가 성공적으로 생성되었습니다!');
      } catch (error) {
        console.error('이벤트 생성 중 오류 발생:', error);
      }
    }
  }
};
</script>