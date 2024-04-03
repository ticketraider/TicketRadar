<!DOCTYPE html>
<template>
  <header>
    <HeaderViewVer/>
  </header>
  <body>
  <router-view/>
  </body>
  <footer>
    <p>© 2024 티켓팅 웹사이트 티켓 레이더. 팀 코더스 게이트.</p>
  </footer>
</template>
<style>
@import "./css/styles/TestStyle.css";
  /* 추가적인 스타일링은 여기에 작성하세요 */

.header {
  background-color: #FFF;
  width: 980px;
  text-align: center;
  font-weight: bold;
  border-radius: 5px;
  margin-bottom: 20px;
  color: #392365;
}

.loading,
.no-tickets {
  text-align: center;
  color: #392365;
}

.ticket-container {
  display: flex;
  justify-content: center;
  color: #392365;
}

.ticket-card {
  width: 900px;
  margin-bottom: 20px;
}

.card-content {
  background-color: #eeeaf1;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.event-name {
  background-color: transparent;
  border: none;
  cursor: pointer;
  outline: none;
  color: #392365;
  font-size: 1.5rem;
  font-weight: bold;
}

.highlight {
  background-color: #f3f3f3;
}

.info {
  margin-top: 10px;
}

.info-row {
  display: flex;
  margin-bottom: 5px;
}

.cancel-btn {
  background-color: #392365;
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  margin-left: auto;
}

.pagination {
  width: 100%;
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.pagination button {
  background-color: #392365;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.current-page {
  margin: 0 20px;
  font-weight: bold;
}
</style>
<script>
import HeaderViewVer from "@/components/HeaderComponent.vue";

export default {
  name: 'App',

  components: {
    HeaderViewVer
  },

  data: () => ({
    //
  }),
}
</script>