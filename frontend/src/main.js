import {createApp} from 'vue'
import App from './App.vue'

import vuetify from './plugins/vuetify'

import { router } from './router/router'

// import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
// import 'bootstrap/dist/css/bootstrap.css'
// import 'bootstrap/dist/css/bootstrap.min.css'
// import 'bootstrap-vue/dist/bootstrap-vue.css'

import {loadFonts} from './plugins/webfontloader'

loadFonts()

createApp(App)
    .use(router)
    .use(vuetify)
    .mount('#app')
