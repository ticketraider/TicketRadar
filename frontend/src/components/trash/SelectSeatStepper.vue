<script>
export default {
  name: 'App',

  components: {},

  data: () => ({
    step: 1,
    numbers: [1, 2, 3, 4], // 예시를 위한 숫자 배열
    choices: [],
  }),
  methods: {
    selectNumber(number) {
      this.choices = Array.from({length: number}, (_, i) => i + 1);
      this.step = 2;
    },
    confirmChoices() {
      // 사용자가 선택을 확인했을 때의 로직을 여기에 작성합니다.
      alert(`Choices confirmed!`);
    }
  }
}
</script>

<template>

<!--  <div id="app">-->
<!--    <div v-if="step === 1">-->
<!--      <h2>Step 1: Choose a number</h2>-->
<!--      <button v-for="number in numbers" :key="number" @click="selectNumber(number)">-->
<!--        {{ number }}-->
<!--      </button>-->
<!--    </div>-->

<!--    <div v-if="step === 2">-->
<!--      <h2>Step 2: Make your choices</h2>-->
<!--      <div v-for="n in choices" :key="n">-->
<!--        <p>Choice {{ n }}: <input type="text" placeholder="Type here..."></p>-->
<!--      </div>-->
<!--      <button @click="step = 1">Go Back</button>-->
<!--      <button @click="confirmChoices">Confirm</button>-->
<!--    </div>-->
<!--  </div>-->

  <v-stepper :items="['Step 1', 'Step 2', 'Step 3']">
    <!--              좌석 갯수 선택-->
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
      <h5 style="margin-top: 20px">예매하실 좌석 개수를 선택해주세요.</h5>

      <select v-model="numberOfSeats">
        <option disabled value="">Please select one</option>
        <option v-for="n in numbers" :key="n" :value="n">{{ n }}</option>
      </select>

      <select class="form-select form-select-lg mb-3" aria-label="Large select example">
        <option selected>개수</option>
        <option v-for="n in numbers" :key="n" :value="n">{{ n }}</option>
      </select>

    </template>

    <!--              좌석 등급 선택-->
    <template v-slot:[`item.2`]>
      <h4>예매하실 좌석의 등급을 선택해주세요.</h4>

      <br>
      <select class="form-select form-select-lg mb-3" aria-label="Large select example">
        <option selected>등급</option>
        <option value="R">R</option>
        <option value="S">S</option>
        <option value="A">aA</option>
      </select>


      <h3>좌석 번호를 선택해주세요</h3>
      <select class="form-select form-select-lg mb-3" aria-label="Large select example">
        <option selected>번호</option>
        <option value="R">R</option>
        <option value="S">S</option>
        <option value="A">aA</option>
      </select>
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

</template>

<style scoped>

</style>