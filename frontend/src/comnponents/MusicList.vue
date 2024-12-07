<template>
  <v-card>
    <v-card-title>音乐列表</v-card-title>
    <v-card-text>
      <v-list>
        <v-list-item v-for="item in music" :key="item.key">
          <v-list-item-content>
            <v-list-item-title>{{ item.key }}</v-list-item-title>
            <v-list-item-subtitle>大小: {{ item.size }} 字节</v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn icon @click="playMusic(item.url)">
              <v-icon>mdi-play</v-icon>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card-text>
  </v-card>
</template>

<script>
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
    },
    playMusic(url) {
      const audio = new Audio(url)
      audio.play()
    }
  }
}
</script>