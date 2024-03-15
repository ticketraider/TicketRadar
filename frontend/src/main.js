import {createApp} from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import { router } from './router/router'
// import VueRouter from 'vue-router'
import {loadFonts} from './plugins/webfontloader'

loadFonts()

createApp(App)
    .use(router)
    .use(vuetify)
    .mount('#app')
