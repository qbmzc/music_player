<template>
  <v-card>
    <v-card-title>上传音乐</v-card-title>
    <v-card-text>
      <v-file-input v-model="file" label="选择音乐文件" accept="audio/*"></v-file-input>
      <v-btn @click="uploadFile" color="primary">上传</v-btn>
    </v-card-text>
  </v-card>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      file: null
    }
  },
  methods: {
    async uploadFile() {
      if (!this.file) {
        alert('请先选择文件')
        return
      }
      try {
        const url = "/api/upload/file"
        // 上传文件到S3
        let formData = new FormData();
        formData.append("file", this.file)
        await axios.post(url, formData, {
          headers: {
            'Content-Type': "multipart/form-data"
          }
        })

        alert('上传成功')
      } catch (error) {
        console.error(error)
        alert('上传失败')
      }
    }
  }
}
</script>