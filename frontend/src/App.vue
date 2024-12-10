<template>
  <div>
    <Aplayer :songs="songs" />
    <!-- 你可以在这里添加更多的UI元素来展示音乐列表 -->
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, toRefs} from 'vue'
import ajax from './Network/index'
import Aplayer from './views/Aplayer.vue'

// 创建响应式对象来存储音乐列表
const state = reactive({
  songs: [] as Array<{ name: string; artist: string; url: string; cover: string; lrc: string }>
})





// 创建获取音乐列表的请求方法
const fetchMusicList = async () => {

  try {
    // 调用 ajax 请求获取音乐列表
    const response = await ajax({
      url: '/api/music/list',
      method: 'get',
      params: {}
    });
    console.log(response)
    state.songs = response;// 更新响应式数据
    console.log(state.songs);
  } catch (error) {
      // 其他错误处理
      console.error('Error fetching music list:', error);
  }
}

// 在组件挂载时调用请求方法
onMounted(() => {
  fetchMusicList()
})

// 返回响应式数据和方法
const { songs } = toRefs(state)
</script>

<style scoped>
/* 添加你自己的样式 */
</style>
