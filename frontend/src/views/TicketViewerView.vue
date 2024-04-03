<template>
  <div>
    <h1>티켓 뷰어</h1>
    <!-- TicketViewer 컴포넌트 사용 -->
    <TicketViewer />
  </div>
</template>

<script>
import TicketViewer from '@/components/trash/TicketViewerBack.vue';

export default {
  components: {
    TicketViewer,
  },
};
</script>