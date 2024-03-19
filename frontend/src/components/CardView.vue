<template>
<v-card
    class="mx-auto"
    width="300"
>
<v-img
    height="200px"
    src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
    cover
></v-img>

<v-card-title>
  테스트공연 1
</v-card-title>

<v-card-subtitle>
  짤막한 설명
</v-card-subtitle>

<v-card-actions>
  <v-btn

      @click="join()"
      color="orange-lighten-2"
      variant="text">
    예매하러 가기
  </v-btn>

  <v-spacer></v-spacer>
</v-card-actions>
</v-card>
</template>
<script setup>
import { useRouter } from "vue-router";
const router = useRouter();
const join = () => {
  router.push({ path: "/move" });
};
</script>