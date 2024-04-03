import {createApp} from 'vue'
import App from './App.vue'

import vuetify from './plugins/vuetify'

import { router } from './router/router'

import BootstrapVue3 from 'bootstrap-vue-3'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/css/bootstrap.min.css'

import {loadFonts} from './plugins/webfontloader'

loadFonts()

createApp(App)
    .use(BootstrapVue3)
    .use(router)
    .use(vuetify)
    .mount('#app');