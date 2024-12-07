import Upload from '@/components/Upload.vue'
import MusicList from '@/components/MusicList.vue'
import {createRouter, createWebHistory} from 'vue-router'

const routes=  [
    { path: '/', component: Upload },
    { path: '/music', component: MusicList }
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

export default router