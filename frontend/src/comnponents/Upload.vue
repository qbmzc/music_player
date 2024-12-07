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
        // 获取预签名URL
        const response = await axios.get('/api/upload/presigned-url', {
          params: {
            fileName: this.file.name,
            fileType: this.file.type
          }
        })
        const { url } = response.data

        // 上传文件到S3
        await axios.put(url, this.file, {
          headers: {
            'Content-Type': this.file.type
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