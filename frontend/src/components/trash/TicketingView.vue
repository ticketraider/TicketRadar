<script>
import {computed, reactive} from "vue";

export default {
  name: 'App',

  components: {
  },
  setup() {
    const rows = 6;
    const cols = 13;
    const seats = reactive(Array.from({ length: rows * cols }, () => ({ selected: false })));

    const selectSeat = (index) => {
      seats[index].selected = !seats[index].selected;
    };

    const selectedSeats = computed(() =>
        seats
            .map((seat, index) => seat.selected ? index : null)
            .filter(index => index !== null)
    );

    return { seats, selectSeat, selectedSeats };
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
                <h3 class="text-h6">티켓 상세정보</h3>

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
                  <div class="cinema-seats">
                    <div
                        v-for="(seat, index) in seats"
                        :key="index"
                        :class="{ seat: true, selected: seat.selected }"
                        @click="selectSeat(index)">
                    </div>
                  </div>
                  <div class="cinema-seats">
                    <div
                        v-for="(seat, index) in seats"
                        :key="index"
                        :class="{ seat: true, selected: seat.selected }"
                        @click="selectSeat(index)">
                    </div>
                  </div>
                  <div class="cinema-seats">
                    <div
                        v-for="(seat, index) in seats"
                        :key="index"
                        :class="{ seat: true, selected: seat.selected }"
                        @click="selectSeat(index)">
                    </div>
                  </div>

                  <div class="info">
                    Selected Seats:
                    <span class="selected-seats" v-for="seat in selectedSeats" :key="seat">Seat {{ seat + 1 }}, </span>
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
                      <th>Description</th>
                      <th class="text-end">Quantity</th>
                      <th class="text-end">Price</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr v-for="(product, index) in products" :key="index">
                      <td v-text="product.name"></td>
                      <td class="text-end" v-text="product.quantity"></td>
                      <td class="text-end" v-text="product.quantity * product.price"></td>
                    </tr>

                    <tr>
                      <td>Shipping</td>
                      <td></td>
                      <td class="text-end" v-text="shipping"></td>
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
.cinema-seats {
  display: flex;
  flex-wrap: wrap;
  max-width: 414px;
  border: 2px solid #ddd;
  padding: 10px;
  border-radius: 8px;
  background-color: #fafafa;
}

.seat {
  width: 20px;
  height: 20px;
  margin: 5px;
  background-color: #bbb;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.seat:hover {
  background-color: #999;
}

.selected {
  background-color: #4CAF50;
}

.selected:hover {
  background-color: #45a049;
}

.info {
  text-align: center;
  margin-top: 20px;
}

.selected-seats {
  font-weight: bold;
  color: #4CAF50;
}
</style>