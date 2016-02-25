module.exports = {
  entry: './src/main.js',
  output: {
    filename: '[name].bundle.js', // Will output App_wp_bundle.js
    path: __dirname + '/javascripts', // Save to Rails Asset Pipeline
  },

  module: {
        loaders: [ { test: /\.js$/, exclude: /node_modules/, loader: "babel-loader" } ]
  },
  resolve: {
    extensions: ['', '.js']
  }


}
