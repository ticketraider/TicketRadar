<template>
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#createEvent"
              aria-expanded="true" aria-controls="createEvent">
        이벤트 생성
      </button>
    </h2>
    <div id="createEvent" class="accordion-collapse collapse show" data-bs-parent="#event">
      <div class="accordion-body">
        <div class="mb-3">
          <input type="text" v-model="eventRequest.categoryId" placeholder="카테고리 ID" class="form-control" id="categoryIdInput"
                 style="margin-bottom: 15px" aria-describedby="categoryIdHelp">
          <div class="input-group mb-3">
            <label class="input-group-text" for="inputGroupFile01">이미지</label>
            <input type="file" class="form-control" id="imageInput" @change="onFileChange">
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

export default {
  data() {
    return {
      eventRequest: {
        categoryId: 0,
        title: '',
        eventInfo: '',
        startDate: '',
        endDate: '',
        place: '',
        seatRPrice: 0,
        seatSPrice: 0,
        seatAPrice: 0
      },
      file: null // For file upload
    };
  },
  methods: {
    async createEvent(event) {

      try {
        const file = event.target.file[0]
        console.log(file)
        const formData = new FormData();
        formData.append('eventRequest', JSON.stringify(this.eventRequest));
        formData.append('file', this.file);

        console.log(formData)

        const token = localStorage.getItem('token');

        await axios.post('http://localhost:8080/events', {

        }, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
          }
        });

        // 성공 처리
        console.log('이벤트가 성공적으로 생성되었습니다!');
      } catch (error) {
        // 오류 처리
        console.error('이벤트 생성 중 오류 발생:', error);
      }
    },

  }
};
</script>