<template>
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
            @click="checkLoginStatus" >
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


            <v-stepper v-model="currentStep" :items="['Step 1', 'Step 2', 'Step 3']">
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
                      <th class="text-end">{{ event.title }}</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                      <th>날짜</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{ date.toLocaleDateString() }}</th>
                    </tr>

                    <tr>
                      <th>장소</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{ event.place.name }}</th>
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
                      <th class="text-end">{{ date.toLocaleDateString() }}</th>
                    </tr>

                    <tr>
                      <th>장소</th>
                      <th class="text-end"></th>
                      <th class="text-end">{{ event.place.name }}</th>
                    </tr>

                    <tr>
                      <th>좌석 ({{ selectedSeats.length }}매)</th>
                      <th class="text-end"></th>
                      <th class="text-end" v-for="seat in selectedSeats" :key="seat.id" style="display: flow">
                        {{ seat.grade }}{{ seat.number }}-{{ formatPrice(seat.price) }}
                      </th>
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
                  <button type="button" class="btn btn-primary" @click="reservationFinished">예매하기</button>
                </div>
              </template>
            </v-stepper>
          </div>
          <div class="modal-footer" style="background-color: #392365">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" @click="resetModal">닫기</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue';
import axios from 'axios';
import {useRoute} from 'vue-router';
import router from "@/router/router";
import * as Bootstrap from "bootstrap";

const event = ref(null);
let seats = ref([]);
let date = ref(new Date());
const selectedSeats = ref([]);
const route = useRoute();
let bookedSeatsIds = [`R1`, `S2`, `A3`]

const currentStep = ref(1);
function resetModal() {
  currentStep.value = 1;
  selectedSeats.value = [];
  // 모달이 닫힐 때 선택된 좌석 초기화
}
const checkLoginStatus = () => {
  const token = localStorage.getItem('token');
  if (!token) {
    alert("로그인이 필요한 기능입니다.");
    router.push({path: "/login"})
    // 로그인이 필요한 상황이므로, 로그인 페이지로 리다이렉트하는 로직도 추가할 수 있습니다.
    // 예: router.push('/login');
  }else {
    fetchBookedTicket()
    openModal()
  }
}

const openModal = () => {
  const myModal = new Bootstrap.Modal(document.getElementById('staticBackdrop'), {
    keyboard: false
  });
  myModal.show();
}

// fetchBookedTicket 함수는 이벤트 상세 정보 로드 로직에서 호출되어야 합니다.
const fetchEventDetail = async () => {
  const eventId = Number(route.params.eventId);
  try {
    const response = await axios.get(`http://localhost:8080/events/${eventId}`);
    event.value = response.data;
    // 이벤트 정보가 로드되면 해당 이벤트의 날짜로 date ref를 업데이트합니다.
    date.value = new Date(event.value.startDate);
    await fetchBookedTicket(); // 여기서 fetchBookedTicket 호출
  } catch (error) {
    console.error('이벤트 상세 정보를 불러오는 동안 오류가 발생했습니다:', error);
  }
};

const fetchBookedTicket = async () => {
  const eventId = Number(route.params.eventId);
  // date.value를 복사하여 사용
  const selectedDate = new Date(date.value);
  // 필요한 경우, selectedDate를 조작
  selectedDate.setHours(selectedDate.getHours()+9);
  const formattedDate = selectedDate.toISOString().split('T')[0];
  console.log(formattedDate);
  console.log(date.value); // 현재 상태 확인을 위해 로그 출력


  try {
    const response = await axios.get(`http://localhost:8080/tickets/ticket-list/${eventId}`, {
      params: {date: formattedDate}
    });
    bookedSeatsIds.value = Array.isArray(response.data) ? response.data : [response.data];
    // 데이터 구조에 따라 적절히 수정
    bookedSeatsIds = bookedSeatsIds.value[0].bookedTicketList;
    seats.value = generateSeats();
    // bookedSeatsIds가 갱신된 후 좌석을 다시 생성
  } catch (error) {
    console.error('티켓 정보를 불러오는 동안 오류가 발생했습니다:', error);
  }
};


onMounted(async () => {
  // onMounted에서는 초기 이벤트 정보만 가져옵니다. 좌석 정보는 예매하기 버튼을 클릭할 때 로드합니다.
  await fetchEventDetail();
});

function generateSeats() {

  // event.value.availableSeats 객체에서 각 좌석 등급별 최대 좌석 수를 가져옴
  const grades = {
    R: {price: event.value.price.seatRPrice, count: event.value.place.seatR},
    S: {price: event.value.price.seatSPrice, count: event.value.place.seatS},
    A: {price: event.value.price.seatAPrice, count: event.value.place.seatA},
  };
  let seatsArray = [];
  let id = 1;
  // 예시로 사용된 예약된 좌석 ID들
  for (const [grade, info] of Object.entries(grades)) {
    for (let i = 1; i <= info.count; i++) {
      seatsArray.push({
        id: id,
        number: i,
        grade,
        price: info.price,
        selected: false,
        booked: bookedSeatsIds.includes(`${grade}${i}`),
      });
      id++;
    }
  }
  return seatsArray;
}

function selectSeat(id) {
  const seat = seats.value.find(seat => seat.id === id);
  if (!seat.booked && !seat.selected && selectedSeats.value.length < 4) {
    seat.selected = true;
    selectedSeats.value.push(seat);
  } else if (seat.selected) {
    seat.selected = false;
    selectedSeats.value = selectedSeats.value.filter(s => s.id !== id);
  }
}

const totalPrice = computed(() => {
  return selectedSeats.value.reduce((acc, seat) => acc + seat.price, 0);
});

function reservationFinished() {
  // 현재 스텝이 2이고 선택된 좌석이 없을 경우
  if (selectedSeats.value.length === 0) {
    alert("좌석을 하나 이상 선택해주세요."); // 함수를 여기서 종료하여 스텝 변경 중단
  } else {
    submitTicketReservation()
  }
}
const submitTicketReservation = async () => {
  const reservationDetails = {
    eventId: event.value.id,
    date: date.value.toJSON(),
    seatList: selectedSeats.value.map(seat => ({
      ticketGrade: seat.grade,
      seatNumber: seat.number,
    })),
  };
  const token = localStorage.getItem('token');

  try {
    await axios.post(`http://localhost:8080/tickets/create`, reservationDetails
        ,
        {
          headers: {
            Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정
          }
        });
    // 예매 성공 처리
    alert('예매가 성공적으로 완료되었습니다.');
    window.location.reload()
  } catch (error) {
    // 예매 실패 처리
    console.error('예매 실패:', error);
    alert('예매에 실패하였습니다.');
    resetModal()
    await fetchBookedTicket()
  }

};

function formatPrice(price) {
  return `${price.toLocaleString()}원`;
}

</script>

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