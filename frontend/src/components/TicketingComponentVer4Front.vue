<!--<script setup>-->
<!--import {ref, computed, onMounted} from 'vue';-->
<!--import axios from 'axios';-->
<!--import {useRoute} from 'vue-router';-->

<!--const event = ref(null);-->
<!--let seats = ref([]);-->

<!--let date = ref(new Date(null));-->
<!--const selectedSeats = ref([]);-->
<!--const route = useRoute();-->
<!--// let bookedSeatsIds = ref([]);-->
<!--let bookedSeatsIds = [`R1`, `S2`, `A3`]-->


<!--const fetchEventDetail = async () => {-->
<!--  const eventId = Number(route.params.eventId);-->
<!--  try {-->
<!--    const response = await axios.get(`http://localhost:8080/events/${eventId}`);-->
<!--    event.value = response.data;-->
<!--    console.log(seats.value); // 변경: seats.value를 로깅-->

<!--    date = ref(new Date(event.value.startDate));-->
<!--  } catch (error) {-->
<!--    console.error('이벤트 상세 정보를 불러오는 동안 오류가 발생했습니다:', error);-->
<!--  }-->
<!--};-->

<!--const fetchBookedTicket = async () => {-->
<!--  const eventId = Number(route.params.eventId);-->
<!--  // Date 객체에서 yyyy-MM-dd 형식의 문자열로 변환-->
<!--  const formattedDate = date.value.toISOString().split('T')[0];-->
<!--  try {-->
<!--    const response = await axios.get(`http://localhost:8080/tickets/ticket-list/${eventId}`, {-->
<!--      params: {-->
<!--        date: formattedDate // 변환된 날짜 문자열을 사용-->
<!--      }-->
<!--    });-->
<!--    bookedSeatsIds.value = Array.isArray(response.data) ? response.data : [response.data];-->
<!--    bookedSeatsIds = bookedSeatsIds.value[0].bookedTicketList-->
<!--    console.log("bookedSeatsIds.value의 타입:", typeof bookedSeatsIds.value[0].bookedTicketList);-->
<!--    console.log("bookedSeatsIds.value는 배열인가?", Array.isArray(bookedSeatsIds.value[0].bookedTicketList));-->
<!--    console.log("bookedSeatsIds의 타입:", typeof bookedSeatsIds);-->
<!--    console.log("bookedSeatsIds는 배열인가?", Array.isArray(bookedSeatsIds));-->
<!--    console.log(bookedSeatsIds.value[0].bookedTicketList);-->
<!--    console.log(bookedSeatsIds);-->
<!--  } catch (error) {-->
<!--    console.error('티켓 정보를 불러오는 동안 오류가 발생했습니다:', error);-->
<!--  }-->
<!--};-->


<!--onMounted(async () => {-->
<!--  // 이벤트 정보를 가져올 때까지 기다림-->
<!--  await fetchEventDetail();-->
<!--  await fetchBookedTicket();-->
<!--  seats.value = generateSeats();-->
<!--});-->

<!--function generateSeats() {-->

<!--  // event.value.availableSeats 객체에서 각 좌석 등급별 최대 좌석 수를 가져옴-->
<!--  const grades = {-->
<!--    R: {price: event.value.price.seatRPrice, count: event.value.place.seatR},-->
<!--    S: {price: event.value.price.seatSPrice, count: event.value.place.seatS},-->
<!--    A: {price: event.value.price.seatAPrice, count: event.value.place.seatA},-->
<!--  };-->
<!--  let seatsArray = [];-->
<!--  let id = 1;-->
<!--  // 예시로 사용된 예약된 좌석 ID들-->
<!--  for (const [grade, info] of Object.entries(grades)) {-->
<!--    for (let i = 1; i <= info.count; i++) {-->
<!--      seatsArray.push({-->
<!--        id: id,-->
<!--        number: i,-->
<!--        grade,-->
<!--        price: info.price,-->
<!--        selected: false,-->
<!--        booked: bookedSeatsIds.includes(`${grade}${i}`),-->
<!--      });-->
<!--      id++;-->
<!--    }-->
<!--  }-->
<!--  return seatsArray;-->
<!--}-->

<!--function selectSeat(id) {-->
<!--  const seat = seats.value.find(seat => seat.id === id);-->
<!--  if (!seat.booked && !seat.selected && selectedSeats.value.length < 4) {-->
<!--    seat.selected = true;-->
<!--    selectedSeats.value.push(seat);-->
<!--  } else if (seat.selected) {-->
<!--    seat.selected = false;-->
<!--    selectedSeats.value = selectedSeats.value.filter(s => s.id !== id);-->
<!--  }-->
<!--}-->

<!--const totalPrice = computed(() => {-->
<!--  return selectedSeats.value.reduce((acc, seat) => acc + seat.price, 0);-->
<!--});-->

<!--const submitTicketReservation = async () => {-->
<!--  const reservationDetails = {-->
<!--    eventId: event.value.id,-->
<!--    date: date.value.toJSON(),-->
<!--    seatList: selectedSeats.value.map(seat => ({-->
<!--      ticketGrade: seat.grade,-->
<!--      seatNumber: seat.number,-->
<!--    })),-->
<!--  };-->
<!--  const token = localStorage.getItem('token');-->

<!--  try {-->
<!--    await axios.post(`http://localhost:8080/tickets/create`, reservationDetails-->
<!--        ,-->
<!--        {-->
<!--          headers: {-->
<!--            Authorization: `Bearer ${token}` // JWT 토큰을 포함한 Authorization 헤더 설정-->
<!--          }-->
<!--        });-->
<!--    // 예매 성공 처리-->
<!--    alert('예매가 성공적으로 완료되었습니다.');-->
<!--    window.location.reload()-->
<!--  } catch (error) {-->
<!--    // 예매 실패 처리-->
<!--    console.error('예매 실패:', error);-->
<!--    alert('예매에 실패하였습니다.');-->
<!--  }-->

<!--};-->

<!--function formatPrice(price) {-->
<!--  return `${price.toLocaleString()}원`;-->
<!--}-->

<!--</script>-->

<!--<template>-->
<!--  <div style="margin: 150px 10px auto 1px; background-color: #aa98ba; border-radius: 5px" v-if="event">-->
<!--    <v-container>-->
<!--      <v-row justify="space-around">-->
<!--        <v-date-picker-->
<!--            v-model="date"-->
<!--            :allowed-dates="allowedDates"-->
<!--            :max="event.endDate"-->
<!--            :min="event.startDate"-->
<!--        ></v-date-picker>-->
<!--      </v-row>-->
<!--    </v-container>-->

<!--    &lt;!&ndash; Button trigger modal &ndash;&gt;-->
<!--    <button type="button"-->
<!--            style="width: 100%; height: 50px; background-color: #392365; border-radius: 10px;"-->
<!--            data-bs-toggle="modal" data-bs-target="#staticBackdrop">-->
<!--      <h5 style="color: white" @click=fetchBookedTicket()>예매하기</h5>-->
<!--    </button>-->


<!--    &lt;!&ndash; Modal &ndash;&gt;-->
<!--    <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"-->
<!--         aria-labelledby="staticBackdropLabel" aria-hidden="true">-->
<!--      <div class="modal-dialog">-->
<!--        <div class="modal-content">-->
<!--          <div class="modal-header" style="background-color: #392365">-->
<!--            <h1 class="modal-title fs-5" id="staticBackdropLabel" style="color:white;">티켓 예매창</h1>-->
<!--            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>-->
<!--          </div>-->
<!--          &lt;!&ndash;          모달 바디&ndash;&gt;-->
<!--          <div class="modal-body" style="background-color: #aa98ba">-->


<!--            <v-stepper :items="['Step 1', 'Step 2', 'Step 3']">-->
<!--              &lt;!&ndash;           티켓 상세정보 확인용&ndash;&gt;-->
<!--              <template v-slot:[`item.1`]>-->
<!--                <h3 class="text-h6">공연 상세정보</h3>-->
<!--                <br>-->
<!--                <v-sheet border>-->
<!--                  <v-table>-->
<!--                    <thead>-->
<!--                    <tr>-->
<!--                      <th>이름</th>-->
<!--                      <th class="text-end"></th>-->
<!--                      <th class="text-end">{{ event.title }}</th>-->
<!--                    </tr>-->
<!--                    </thead>-->

<!--                    <tbody>-->
<!--                    <tr>-->
<!--                      <th>날짜</th>-->
<!--                      <th class="text-end"></th>-->
<!--                      <th class="text-end">{{ date.toLocaleDateString() }}</th>-->
<!--                    </tr>-->

<!--                    <tr>-->
<!--                      <th>장소</th>-->
<!--                      <th class="text-end"></th>-->
<!--                      <th class="text-end">{{ event.place.name }}</th>-->
<!--                    </tr>-->
<!--                    </tbody>-->
<!--                  </v-table>-->
<!--                </v-sheet>-->
<!--              </template>-->

<!--              &lt;!&ndash;              좌석 등급 선택&ndash;&gt;-->
<!--              <template v-slot:[`item.2`]>-->
<!--                <h4>예매하실 좌석을 선택해주세요.</h4>-->

<!--                <div class="theater">-->
<!--                  <div class="screen">SCREEN</div>-->
<!--                  <div class="seats">-->
<!--                    <div-->
<!--                        v-for="seat in seats"-->
<!--                        :key="seat.id"-->
<!--                        :class="['seat', seat.grade, { selected: seat.selected, booked: seat.booked }]"-->
<!--                        @click="selectSeat(seat.id)"-->
<!--                    >-->
<!--                      {{ seat.grade }}-->
<!--                    </div>-->
<!--                  </div>-->
<!--                  <div class="summary" style="margin-left: 10px;">-->
<!--                    <h2>선택된 좌석</h2>-->
<!--                    <ul>-->
<!--                      <li v-for="seat in selectedSeats" :key="seat.id">-->
<!--                        {{ seat.grade }}{{ seat.number }} - {{ formatPrice(seat.price) }}-->
<!--                      </li>-->
<!--                    </ul>-->
<!--                    <p>최종 가격: {{ formatPrice(totalPrice) }}</p>-->
<!--                  </div>-->
<!--                </div>-->

<!--              </template>-->

<!--              &lt;!&ndash;              좌석 번호 선택&ndash;&gt;-->
<!--              <template v-slot:[`item.3`]>-->

<!--                <h3 class="text-h6">티켓 정보 확인</h3>-->

<!--                <br>-->

<!--                <v-sheet border>-->
<!--                  <v-table>-->
<!--                    <thead>-->
<!--                    <tr>-->
<!--                      <th>이름</th>-->
<!--                      <th class="text-end"></th>-->
<!--                      <th class="text-end">{{ event.title }}</th>-->
<!--                    </tr>-->
<!--                    </thead>-->

<!--                    <tbody>-->
<!--                    <tr>-->
<!--                      <th>날짜</th>-->
<!--                      <th class="text-end"></th>-->
<!--                      <th class="text-end">{{ date.toLocaleDateString() }}</th>-->
<!--                    </tr>-->

<!--                    <tr>-->
<!--                      <th>장소</th>-->
<!--                      <th class="text-end"></th>-->
<!--                      <th class="text-end">{{ event.place.name }}</th>-->
<!--                    </tr>-->

<!--                    <tr>-->
<!--                      <th>좌석 ({{ selectedSeats.length }}매)</th>-->
<!--                      <th class="text-end"></th>-->
<!--                      <th class="text-end" v-for="seat in selectedSeats" :key="seat.id" style="display: flow">-->
<!--                        {{ seat.grade }}{{ seat.number }}-{{ formatPrice(seat.price) }}-->
<!--                      </th>-->
<!--                    </tr>-->
<!--                    <tr>-->
<!--                      <th>가격</th>-->
<!--                      <th class="text-end"></th>-->
<!--                      <th class="text-end">{{ formatPrice(totalPrice) }}</th>-->
<!--                    </tr>-->
<!--                    </tbody>-->
<!--                  </v-table>-->
<!--                </v-sheet>-->
<!--                <div>-->
<!--                  <button type="button" class="btn btn-primary" @click="submitTicketReservation">예매하기</button>-->
<!--                </div>-->
<!--              </template>-->
<!--            </v-stepper>-->


<!--          </div>-->
<!--          <div class="modal-footer" style="background-color: #392365">-->
<!--            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->


<!--  </div>-->
<!--</template>-->

<!--<style scoped>-->
<!--.screen {-->
<!--  width: 100%;-->
<!--  text-align: center;-->
<!--  padding: 5px;-->
<!--  margin-bottom: 20px;-->
<!--  color: #fff;-->
<!--  background-color: #666;-->
<!--  border-radius: 4px;-->
<!--}-->


<!--.seats {-->
<!--  display: flex;-->
<!--  flex-wrap: wrap;-->
<!--  max-width: 414px;-->
<!--  padding: 10px;-->
<!--  border-radius: 8px;-->
<!--  background-color: #fafafa;-->
<!--}-->

<!--.seat {-->
<!--  width: 20px;-->
<!--  height: 20px;-->
<!--  margin: 3px;-->
<!--  background-color: #f3f3f3;-->
<!--  cursor: pointer;-->
<!--  border-radius: 5px;-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  align-items: center;-->
<!--  font-size: 15px;-->
<!--  font-weight: bold;-->
<!--  transition: background-color 0.2s ease;-->
<!--}-->

<!--.seat:hover {-->
<!--  background-color: #ddd;-->
<!--}-->

<!--.seat.selected {-->
<!--  background-color: #ffca28;-->
<!--}-->

<!--.seat.booked {-->
<!--  background-color: #ff5722;-->
<!--  cursor: not-allowed;-->
<!--}-->

<!--.R {-->
<!--  background-color: #ff9999;-->
<!--}-->

<!--.S {-->
<!--  background-color: #9999ff;-->
<!--}-->

<!--.A {-->
<!--  background-color: #99ff99;-->
<!--}-->

<!--.info {-->
<!--  display: flex;-->
<!--  margin-top: 20px;-->
<!--}-->

<!--.selected-seats {-->
<!--  display: flow;-->
<!--  font-weight: bold;-->
<!--}-->
<!--</style>-->