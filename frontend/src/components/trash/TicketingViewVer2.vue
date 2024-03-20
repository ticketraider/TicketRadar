<script>
import {computed, reactive} from "vue";

export default {
  name: 'App',

  components: {},
  setup() {
    const cols = 15;
    // Sample bookings
    const bookedSeats = [3, 15, 16, 17];

    // Configurable grades
    const gradesConfig = {
      R: 3, // rows of R
      S: 5, // rows of S
      A: 4, // rows of A
    };

    // Generate seat map
    const seats = reactive([]);
    let seatIndex = 0;
    Object.entries(gradesConfig).forEach(([grade, rows]) => {
      for (let i = 0; i < rows * cols; i++) {
        seats.push({
          index: seatIndex,
          selected: false,
          booked: bookedSeats.includes(seatIndex),
          grade,
          visible: true,
        });
        seatIndex++;
      }
    });

    const selectSeat = (index) => {
      if (!seats[index].booked) {
        seats[index].selected = !seats[index].selected;
      }
    };

    const selectedSeats = computed(() =>
        seats
            .filter((seat) => seat.selected)
            .map((seat) => ({index: seat.index, grade: seat.grade}))
    );

    return {seats, selectSeat, selectedSeats};
  },

  data: () => ({
    selectedItem: null,
    seat: [1, 2, 3, 4],

    shipping: 0,
    step: 1,
    items: [
      'Review Order',
      'Select Shipping',
      'Submit',
    ],
    products: [
      {
        name: 'Product 1',
        price: 10,
        quantity: 2,
      },
      {
        name: 'Product 2',
        price: 15,
        quantity: 10,
      },
    ]
  }),

  computed: {
    subtotal() {
      return this.products.reduce((acc, product) => acc + product.quantity * product.price, 0)
    },
    total() {
      return this.subtotal + Number(this.shipping ?? 0)
    },
  },

}

</script>

<template>
  <div style="margin: 150px 10px auto 1px; background-color: #7980aa; border-radius: 5px">
    <v-container>
      <v-row justify="space-around">
        <v-date-picker
            v-model="date"
            :allowed-dates="allowedDates"
            max="2024-05-19"
            min="2024-03-29"
        ></v-date-picker>
      </v-row>
    </v-container>

    <!-- Button trigger modal -->
    <button type="button"
            style="width: 100%; height: 50px; background-color: #473baa; border-radius: 10px;"
            data-bs-toggle="modal" data-bs-target="#staticBackdrop">
      <h5 style="color: white">예매하기</h5>
    </button>


    <!-- Modal -->
    <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
         aria-labelledby="staticBackdropLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header" style="background-color: #473baa">
            <h1 class="modal-title fs-5" id="staticBackdropLabel">티켓 예매창</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <!--          모달 바디-->
          <div class="modal-body" style="background-color: #473baa">


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
                      <th class="text-end">오페라의 유령</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                      <th>날짜</th>
                      <th class="text-end"></th>
                      <th class="text-end">2024-03-29</th>
                    </tr>

                    <tr>
                      <th>장소</th>
                      <th class="text-end"></th>
                      <th class="text-end">블루스퀘어 신한카드홀</th>
                    </tr>
                    </tbody>
                  </v-table>
                </v-sheet>
              </template>

              <!--              좌석 등급 선택-->
              <template v-slot:[`item.2`]>
                <h4>예매하실 좌석을 선택해주세요.</h4>

                <div id="app">
                  <div class="screen">SCREEN</div>
                  <div class="cinema-seats">
                    <div
                        v-for="seat in seats"
                        :key="seat.index"
                        :class="['seat', seat.grade, { selected: seat.selected, booked: seat.booked }]"
                        @click="selectSeat(seat.index)"
                    >{{ seat.grade }}
                    </div>
                  </div>
                  <div class="info">
                    선택된 좌석:
                    <span class="selected-seats" v-for="seat in selectedSeats" :key="seat.index">
        {{ seat.grade }}-{{ seat.index + 1 }}번,
      </span>
                  </div>
                </div>

              </template>

              <!--              좌석 번호 선택-->
              <template v-slot:[`item.3`]>

                <h3 class="text-h6">Confirm</h3>

                <br>

                <v-sheet border>
                  <v-table>
                    <thead>
                    <tr>
                      <th>이름</th>
                      <th class="text-end"></th>
                      <th class="text-end">오페라의 유령</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                      <th>날짜</th>
                      <th class="text-end"></th>
                      <th class="text-end">2024-03-29</th>
                    </tr>

                    <tr>
                      <th>장소</th>
                      <th class="text-end"></th>
                      <th class="text-end">블루스퀘어 신한카드홀</th>
                    </tr>

                    <tr v-for="seat in selectedSeats" :key="seat.index">
                      <td v-text="selectedSeats[index]"></td>
                      <td class="text-end" v-text=""></td>
                      <td class="text-end" v-text="product.quantity * product.price"></td>
                    </tr>


                    <tr>
                      <th>Total</th>
                      <th></th>
                      <th class="text-end">
                        ${{ total }}
                      </th>
                    </tr>
                    </tbody>
                  </v-table>
                </v-sheet>

                <button type="button" class="btn btn-primary">예매하기</button>
              </template>
            </v-stepper>


          </div>
          <div class="modal-footer" style="background-color: #473baa">
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


.cinema-seats {
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