<template>
  <div class="event-list">
    <div class="event-grid">
      <v-card v-for="event in eventList" :key="event.id" class="event-card">
        <v-card class="mx-auto" style="width: 300px; background-color: white">
          <v-img :src="event.posterImage" contain></v-img>
          <v-card-title>{{ event.title }}</v-card-title>
          <div style="display:flex; margin-bottom: 10px;">
            <v-card-subtitle>리뷰 {{ event.reviewCount }}</v-card-subtitle>
            <v-card-subtitle>좋아요 {{ event.likeCount }}</v-card-subtitle>
          </div>

          <v-card-subtitle>{{ event.eventInfo }}</v-card-subtitle>
          <v-card-actions>
            <v-btn @click="reserve(event.id)" color="#0a0925" variant="text">
              예매하러 가기
            </v-btn>
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-card>
    </div>
    <div style="width: 100%; margin: 10px">
      <div class="pagination" style="margin-left: 680px">
        <v-btn
            v-if="type === ''"
            @click="fetchEvents(currentPage - 1)"
            :disabled="currentPage === 0"
            style="background-color: #0a0925; color: white;"
        >
          이전
        </v-btn>
        <v-btn
            v-if="type === ''"
            @click="fetchEvents(currentPage + 1)"
            :disabled="currentPage === totalPages - 1"
            style="margin-left: 20px; background-color: #0a0925; color: white;"
        >
          다음
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, defineProps } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const eventList = ref([]);
const currentPage = ref(0);
const pageSize = 5;
let totalPages = ref(0);
const router = useRouter();
const route = useRoute();
const selectedCategory = ref("");
const searchKeyword = ref("");
const sortStatus = ref("");
const searchCriterion = ref("");


const props = defineProps({
  type: {
    type: String,
    required: false
  }
});

const fetchEvents = async (page = 0) => {

  selectedCategory.value = route.query.category;
  searchKeyword.value = route.query.search;
  sortStatus.value = route.query.sort;
  searchCriterion.value = route.query.criterion;


  try {
    let apiUrl = '';
    let request = '';

    // type에 따라 다른 API 호출
    if (props.type === 'likes' || props.type === 'reviews' || props.type === 'rating' || props.type === 'popularity') {
      apiUrl = 'http://localhost:8080/getCachedEventList'
      request = {
        params: {
          key: props.type,
        },
      }
    }
        // else if( props.type === 'likes' || props.type === 'reviews' || props.type === 'rating'|| props.type === 'popularity') {
        //   apiUrl = 'http://localhost:8080/events'
        //   request = {
        //     params: {
        //       page: page,
        //       size: pageSize,
        //       category: selectedCategory.value,
        //       keyword: searchKeyword.value,
        //       sortStatus: props.type,              //  좋아요, 리뷰,
        //       searchStatus: searchCriterion.value //  제목 or 장소
        //     },
        //   }
    // }
    else {
      apiUrl = 'http://localhost:8080/events'
      request = {
        params: {
          page: page,
          size: pageSize,
          category: selectedCategory.value,
          keyword: searchKeyword.value,
          sortStatus: sortStatus.value,       //  좋아요, 리뷰,
          searchStatus: searchCriterion.value //  제목 or 장소
        },
      }
      if (props.type === 'rating') {
        request.sortStatus = 'rating'
      } else if (props.type === 'likes') {
        request.sortStatus = 'likes'
      }
    }

    const response = await axios.get(apiUrl, request);
    console.log(`${props.type} Response : `, response)

    if (props.type === '') {
      eventList.value = response.data.content;
      totalPages.value = response.data.totalPages;
      currentPage.value = page;
    }

    else{
      eventList.value = response.data;
    }
    console.log(`type : ${props.type} : ${eventList.value[0]}`)
  } catch (error) {
    console.error('이벤트 목록을 불러오는 동안 오류가 발생했습니다:', error);
  }
};

// selectedCategory 변화 감지 및 해당 카테고리의 이벤트 목록 불러오기
watch(() => route.query, () => {
  fetchEvents(currentPage.value);
}, { deep: true });


onMounted(() => {
  fetchEvents(currentPage.value);
});

const reserve = (eventId) => {
  router.push({ name: 'EventDetail', params: { eventId: Number(eventId) } });
};
</script>

<style scoped>
.event-list {
  padding: 20px;
}

.event-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 35px;
}

.event-card {
  width: 100%;
}

.pagination {
  margin-top: 20px;
}

.pagination v-btn {
  margin-right: 5px;
}
</style>