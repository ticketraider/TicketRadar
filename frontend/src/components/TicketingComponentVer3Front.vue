<script setup>
import {ref, onMounted} from 'vue';
import axios from 'axios';
import {useRoute} from "vue-router";

const event = ref(null);
const route = useRoute(); // useRoute()를 사용하여 현재 라우팅 정보를 가져옴

const fetchEventDetail = async () => {
  const eventId = Number(route.params.eventId); // 이벤트 ID를 Long으로 변환
  try {
    const response = await axios.get(`http://localhost:8080/events/${eventId}`);
    event.value = response.data;
  } catch (error) {
    console.error('이벤트 상세 정보를 불러오는 동안 오류가 발생했습니다:', error);
  }
};

onMounted(() => {
  const eventId = route.params.eventId; // 라우팅 정보에서 eventId를 가져옴
  fetchEventDetail(eventId); // 받아온 이벤트 ID 값을 사용하여 이벤트 상세 정보 호출
});
</script>
<script>
export default {
  data() {
    return {
      date: new Date('2024-03-29'),
      seats: this.generateSeats(),
      selectedSeats: [],
    };
  },
  computed: {
    totalPrice() {
      return this.selectedSeats.reduce((acc, seat) => acc + seat.price, 0);
    },
  },
  methods: {
    generateSeats() {
      // 각 등급별 가격과 개수 설정
      const grades = {
        R: { price: 100000, count: 30 }, // R 등급은 5개의 좌석
        S: { price: 80000, count: 60 }, // S 등급은 10개의 좌석
        A: { price: 60000, count: 45 }, // A 등급은 15개의 좌석
      };
      const seats = [];
      let id = 1;

      // 예약된 좌석의 ID 목록
      const bookedSeatsIds = [2, 5, 8, 45];

      for (const [grade, info] of Object.entries(grades)) {
        for (let i = 1; i <= info.count; i++) {
          seats.push({
            id: id,
            number: i,
            grade,
            price: info.price,
            selected: false,
            booked: bookedSeatsIds.includes(id),
          });
          id++;
        }
      }

      return seats;
    },


    selectSeat(id) {
      const seat = this.seats.find(seat => seat.id === id);
      if (!seat.booked && !seat.selected && this.selectedSeats.length < 4) {
        seat.selected = true;
        this.selectedSeats.push(seat);
      } else if (seat.selected) {
        seat.selected = false;
        this.selectedSeats = this.selectedSeats.filter(s => s.id !== id);
      }
    },
    formatPrice(price) {
      return `${price.toLocaleString()}원`;
    },
  },
};

</script>

<template >
  <div style="margin: 150px 10px auto 1px; background-color: #aa98ba; border-radius: 5px" v-if="event">
      <v-container>
        <v-row justify="space-around">
          <v-date-picker
              v-model="date"
              :allowed-dates="allowedDates"
              :max="event.endDate"
              :min="event.startDate"
          ></v-date-picker>
        </v-row>
      </v-container>

    <!-- Button trigger modal -->
    <button type="button"
            style="width: 100%; height: 50px; background-color: #392365; border-radius: 10px;"
            data-bs-toggle="modal" data-bs-target="#staticBackdrop">
      <h5 style="color: white">예매하기</h5>
    </button>


    <!-- Modal -->
    <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
         aria-labelledby="staticBackdropLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header" style="background-color: #392365">
            <h1 class="modal-title fs-5" id="staticBackdropLabel" style="color:white;">티켓 예매창</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <!--          모달 바디-->
          <div class="modal-body" style="background-color: #aa98ba">


            <v-stepper :items="['Step 1', 'Step 2', 'Step 3']">
              <!--           티켓 상세정보 확인용-->
              <template v-slot:[`item.1`]>
                <h3 class="text-h6">공연 상세정보</h3>
                <br>
                <v-sheet border>
                  <v-table>
                    <thead>
                    <tr>
                      <th>이름</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{event.title}}</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                      <th>날짜</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{date.toLocaleDateString()}}</th>
                    </tr>

                    <tr>
                      <th>장소</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{ event.place }}</th>
                    </tr>
                    </tbody>
                  </v-table>
                </v-sheet>
              </template>

              <!--              좌석 등급 선택-->
              <template v-slot:[`item.2`]>
                <h4>예매하실 좌석을 선택해주세요.</h4>

                <div class="theater">
                  <div class="screen">SCREEN</div>
                  <div class="seats">
                    <div
                        v-for="seat in seats"
                        :key="seat.id"
                        :class="['seat', seat.grade, { selected: seat.selected, booked: seat.booked }]"
                        @click="selectSeat(seat.id)"
                    >
                      {{ seat.grade }}
                    </div>
                  </div>
                  <div class="summary" style="margin-left: 10px;">
                    <h2>선택된 좌석</h2>
                    <ul>
                      <li v-for="seat in selectedSeats" :key="seat.id">
                        {{ seat.grade }}{{ seat.number }} - {{ formatPrice(seat.price) }}
                      </li>
                    </ul>
                    <p>최종 가격: {{ formatPrice(totalPrice) }}</p>
                  </div>
                </div>

              </template>

              <!--              좌석 번호 선택-->
              <template v-slot:[`item.3`]>

                <h3 class="text-h6">티켓 정보 확인</h3>

                <br>

                <v-sheet border>
                  <v-table>
                    <thead>
                    <tr>
                      <th>이름</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{ event.title }}</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                      <th>날짜</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{date.toLocaleDateString()}}</th>
                    </tr>

                    <tr>
                      <th>장소</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{ event.place }}</th>
                    </tr>

                    <tr>
                      <th>좌석 ({{selectedSeats.length}}매)</th>
                      <th class="text-end"></th>
                      <th class="text-end" v-for="seat in selectedSeats" :key="seat.id" style="display: flow">
                        {{ seat.grade }}{{ seat.number }}-{{ formatPrice(seat.price) }}</th>
                    </tr>
                    <tr>
                      <th>가격</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{ formatPrice(totalPrice) }}</th>
                    </tr>
                    </tbody>
                  </v-table>
                </v-sheet>
                <div>
                  <button type="button" class="btn btn-primary">예매하기</button>
                </div>
              </template>
            </v-stepper>


          </div>
          <div class="modal-footer" style="background-color: #392365">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<style scoped>
.screen {
  width: 100%;
  text-align: center;
  padding: 5px;
  margin-bottom: 20px;
  color: #fff;
  background-color: #666;
  border-radius: 4px;
}


.seats {
  display: flex;
  flex-wrap: wrap;
  max-width: 414px;
  padding: 10px;
  border-radius: 8px;
  background-color: #fafafa;
}

.seat {
  width: 20px;
  height: 20px;
  margin: 3px;
  background-color: #f3f3f3;
  cursor: pointer;
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
  font-weight: bold;
  transition: background-color 0.2s ease;
}

.seat:hover {
  background-color: #ddd;
}

.seat.selected {
  background-color: #ffca28;
}

.seat.booked {
  background-color: #ff5722;
  cursor: not-allowed;
}

.R {
  background-color: #ff9999;
}

.S {
  background-color: #9999ff;
}

.A {
  background-color: #99ff99;
}

.info {
  display: flex;
  margin-top: 20px;
}

.selected-seats {
  display: flow;
  font-weight: bold;
}
</style>