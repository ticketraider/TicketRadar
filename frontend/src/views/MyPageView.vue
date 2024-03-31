<template>
  <main style="background-color:#EEEAF1; display: flex; justify-content: center">
    <div
        style="background-color: #D4CBDC; padding: 30px; margin: 30px auto; width: 80%; border-radius: 20px">
      <div class="mypage">
        <div style="display:flex;">

          <aside>
            <div style="display:grid;">
              <button type="button"
                      style="background-color: #392365; border-color: #392365; margin: auto 10px 10px 10px"
                      @click="goTo('userinfo')" class="btn btn-primary">유저 정보
              </button>
              <button type="button"
                      style="background-color: #392365; border-color: #392365; margin: auto 10px 10px 10px"
                      @click="goTo('tickets')" class="btn btn-primary">티켓 목록
              </button>
              <button type="button"
                      style="background-color: #392365; border-color: #392365; margin: auto 10px 10px 10px"
                      @click="goTo('reviews')" class="btn btn-primary">리뷰 목록
              </button>
              <button type="button"
                      style="background-color: #392365; border-color: #392365; margin: auto 10px 10px 10px"
                      @click="goTo('likes')" class="btn btn-primary">좋아요 목록
              </button>
            </div>
          </aside>

          <div style="background-color: #392365; height: 1190px; width: 20px; border-radius: 5px;">
          </div>

          <section style="padding-left: 10px">
            <router-view></router-view>
          </section>

        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import {useRouter} from 'vue-router';

const router = useRouter();

function goTo(path) {
  router.push({path: `/my-page/${path}`});
}
</script>


<style scoped>

</style>