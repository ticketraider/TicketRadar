<template>
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
          <input type="number" v-model="eventId" placeholder="이벤트 ID" class="form-control" id="exampleInputPassword1">
        </div>
        <div style="text-align: right;">
          <button @click="deleteEvent" class="btn btn-primary"
                  style="background-color: #392365; border-color: #392365; margin-left: 15px;">입력
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue';

const eventId = ref(null);

const deleteEvent = async () => {
  try {
    await axios.delete(`http://localhost:8080/events/${eventId.value}`);
    console.log('이벤트가 성공적으로 삭제되었습니다!');
  } catch (error) {
    console.error('이벤트 삭제 중 오류 발생:', error);
  }
};
</script>

<style scoped>

</style>