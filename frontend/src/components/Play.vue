<template>
  <div id="app">
    <aplayer :music="music" />
  </div>
</template>

<script>
// import axios from 'axios';
//
// export default defineComponent({
//   name: 'App',
//   components: {
//     APlayer
//   },
//   setup() {
//     const musicList = ref([]);
//
//     onMounted(async () => {
//       try {
//         const response = await axios.get('/api/music/list');
//         musicList.value = response.data.map(item => ({
//           title: item.title,
//           artist: item.artist,
//           src: item.url,
//           pic: item.pic
//         }));
//       } catch (error) {
//         console.error('获取播放列表失败:', error);
//       }
//     });
//
//     return {
//       musicList
//     };
//   }
// });

import axios from 'axios'

export default {
  data() {
    return {
      music: []
    }
  },
  created() {
    this.fetchMusicList()
  },
  methods: {
    async fetchMusicList() {
      try {
        const response = await axios.get('/api/music/list')
        this.music = response.data
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>
