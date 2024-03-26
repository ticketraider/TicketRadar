<template>
  <main style="height: 1000px">
    <div
        style="background-color: #aa98ba; padding: 30px; margin: 30px auto; height: 800px; width: 67%; border-radius: 20px">
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
                      @click="goTo('tickets')" class="btn btn-primary">내 티켓 목록
              </button>
            </div>
          </aside>

          <div style="background-color: #392365; height: 740px; width: 20px; border-radius: 5px;">
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