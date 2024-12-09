## 基于Vue3开发的音乐播放器

## 基于二次开发

## 下载element plus ui组件库
```shell
 npm i element-plus -s
```
## 下载 字节图标库
```shell
 npm i @icon-park/vue-next -s 
```

# 该组件引入相应的歌曲 数组形式 

```javascript
let songs = [
    {
      name: '爱上是你的错11111111111111111111111',
      artist: 'Hanser',
      url: 'https://music.163.com/song/media/outer/url?id=1978036723.mp3',
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
```
 


## directtion='right'可以设置播放器相应的位置