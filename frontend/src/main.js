import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import {loadFonts} from './plugins/webfontloader'
import VueAPlayer from 'vue3-aplayer';

loadFonts()

createApp(App)
  .use(router) .use(vuetify)
    .component('aplayer',VueAPlayer)
  .mount('#app')
