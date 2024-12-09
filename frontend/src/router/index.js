import Upload from '@/components/Upload.vue'
import MusicList from '@/components/MusicList.vue'
import Player from '@/components/Play.vue'
import {createRouter, createWebHistory} from 'vue-router'

const routes=  [
    { path: '/', component: Upload },
    {path: '/music', component: MusicList},
    {path: '/player', component: Player}
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

export default router