const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave:false,

  pluginOptions: {
    vuetify: {
			// https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
		}
  }
})

module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://192.168.84.6:8080',
        changeOrigin: true
      }
    }
  },

  pluginOptions: {
    vuetify: {}
  }
}
