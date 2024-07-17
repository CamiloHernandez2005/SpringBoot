const path = require('path');
const webpack = require('webpack'); // Importa webpack aquí

module.exports = {
  transpileDependencies: true,

  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'),
      },
    },
    plugins: [
      new webpack.DefinePlugin({
        '__VUE_PROD_HYDRATION_MISMATCH_DETAILS__': JSON.stringify(true),
        // Aquí puedes definir otros flags si es necesario
      }),
    ],
  },

  devServer: {
    // Configuraciones adicionales del servidor de desarrollo
  },
};
