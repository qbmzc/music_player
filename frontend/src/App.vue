<template>
  <Aplayer :songs='songs' />
  <!-- 参数说明 -->
  <!-- direction方向参数 -->
</template>
<script setup lang="ts">
import { reactive,toRefs } from 'vue'
import Aplayer from './views/Aplayer.vue';
let state = reactive({
  songs: [
    {
      name: '渡凉关',
      artist: '叶里',
      url: 'https://tm-fs-test.s3.amazonaws.com/music/%E5%B9%BB%E4%B9%8B%E5%A2%83.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAQK6N4RH7UIVJGR3X/20241209/us-west-2/s3/aws4_request&X-Amz-Date=20241209T095954Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=0ffe1f2adefd80dc709e34dcf065e97a6b7c4bed15a5d9f09d43f3e6c7aa8dd5',
      cover: 'https://p1.music.126.net/K0-IPcIQ9QFvA0jXTBqoWQ==/109951163636756693.jpg?param=300y300', // prettier-ignore
      lrc: 'https://cdn.moefe.org/music/lrc/thing.lrc',
    },
    {
      name: '万千花蕊慈母悲哀',
      artist: '泠鸢yousa',
      url: 'https://music.163.com/song/media/outer/url?id=1903991886.mp3 ',
      cover: 'https://p1.music.126.net/K0-IPcIQ9QFvA0jXTBqoWQ==/109951163636756693.jpg?param=300y300', // prettier-ignore
      lrc: 'https://cdn.moefe.org/music/lrc/kyoukiranbu.lrc',
    }
  ]
})
let { songs } = toRefs(state)
</script>
<style scoped>

</style>
