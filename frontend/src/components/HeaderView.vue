<script setup>

</script>

<template>
  <div style="display: flex;">
    <h1>Ticket Radar</h1>
    <v-spacer></v-spacer>
    <form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3" role="search">
      <input type="search" class="form-control form-control-white text-bg-white" placeholder="Search..." aria-label="Search">
    </form>
    <div class="text-end">
      <button type="button" class="btn btn-outline-light me-2">Login</button>
      <button type="button" class="btn btn-warning">Sign-up</button>
    </div>
  </div>
  <nav>
    <ul>
      <li><a href="#">뮤지컬</a></li>
      <li><a href="#">콘서트</a></li>
      <li><a href="#">스포츠</a></li>
      <li><a href="#">전시/행사</a></li>
      <li><a href="#">클래식/무용</a></li>
      <li><a href="#">아동/가족</a></li>
      <li><a href="#">연극</a></li>
    </ul>
  </nav>
</template>

<style scoped>

</style>