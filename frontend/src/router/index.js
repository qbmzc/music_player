import Vue from 'vue'
import VueRouter from 'vue-router'
import Upload from '@/components/Upload.vue'
import MusicList from '@/components/MusicList.vue'

Vue.use(VueRouter)

const routes = [
    { path: '/', component: Upload },
    { path: '/music', component: MusicList }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

export default router