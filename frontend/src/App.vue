<template>
  <v-app>
    <v-main>
      <ContactView/>
      <HelloWorld/>
    </v-main>
  </v-app>
</template>

<script>
import ContactView from "@/views/ContactView.vue";
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',

  components: {
    HelloWorld,
    ContactView
  },

  data: () => ({
    //
  }),
}
</script>
