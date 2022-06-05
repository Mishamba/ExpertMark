const path = require("path")

module.exports = {
    entry: './src/signIn.js',
    output: {
        path: path.resolve(".", 'bundle'),
        filename: 'signIn.js',
    },
};