<template>
  <main style="background-color: #EEEAF1; display: flex; justify-content: center; align-items: flex-start; padding-top: 100px; height: 100vh;">
    <div style="background-color: #FFF; padding: 30px; border-radius: 20px; box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.2);">
      <div class="mypage">
        <div style="display: flex;">

          <aside style="flex: 1; padding-right: 20px;">
            <div style="display: flex; flex-direction: column; margin-top: 60px;">
              <button type="button"
                      style="background-color: #5d3d9c; border: none; padding: 10px 20px; margin-bottom: 10px; color: white; border-radius: 5px; cursor: pointer;"
                      @click="goTo('userinfo')">유저 정보
              </button>
              <button type="button"
                      style="background-color: #5d3d9c; border: none; padding: 10px 20px; margin-bottom: 10px; color: white; border-radius: 5px; cursor: pointer;"
                      @click="goTo('tickets')">티켓 목록
              </button>
              <button type="button"
                      style="background-color: #5d3d9c; border: none; padding: 10px 20px; margin-bottom: 10px; color: white; border-radius: 5px; cursor: pointer;"
                      @click="goTo('reviews')">리뷰 목록
              </button>
              <button type="button"
                      style="background-color: #5d3d9c; border: none; padding: 10px 20px; margin-bottom: 10px; color: white; border-radius: 5px; cursor: pointer;"
                      @click="goTo('likes')">좋아요 목록
              </button>
            </div>
          </aside>

          <div style="background-color: #392365; height: 100%; width: 2px;"></div>

          <section style="flex: 2; padding-left: 20px;">
            <router-view></router-view>
          </section>

        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

function goTo(path) {
  router.push({ path: `/my-page/${path}` });
}
</script>

<style>
button:hover {
  background-color: #6D5A8D;
}

</style>
