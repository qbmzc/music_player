<template>
  <APlayer :audio="audio" ref="aplayer" />
</template>
<script setup>
import axios from 'axios'
import { ref, onMounted } from "vue";
import APlayer from "@worstone/vue-aplayer";

const aplayer = ref(null);
// 设置 2 个或 2 个以上的歌曲信息

const audio = ref([]);

onMounted(async () => {
  try {
    // 加载歌曲信息
    const response = await axios.get('/api/music/list');
    const audios = response.data; // 假设服务器返回的数据直接是音频列表

    // 更新音频列表
    audio.value = audios;

    // 如果 APlayer 组件提供了 addList 方法，那么可以在这里使用
    // 但是通常情况下，你只需要设置 :audio 属性，Vue 会自动更新组件
    if (aplayer.value && aplayer.value.addList) {
      aplayer.value.addList(audios);
    }
  } catch (error) {
    console.error("Failed to load music list:", error);
    // 这里可以添加更多错误处理逻辑，比如显示错误消息给用户
  }
});

</script>
