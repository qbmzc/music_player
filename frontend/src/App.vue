<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title>音乐播放器</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text to="/">上传</v-btn>
      <v-btn text to="/music">音乐列表</v-btn>
    </v-app-bar>
    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: 'App'
}
</script>