const path = require("path")

/*module.exports = {
    entry: './src/forecastRecommendations.js',
    output: {
        path: path.resolve(".", 'bundle'),
        filename: 'forecastRecommendations.js',
    },
};*/
/*module.exports = {
    entry: './src/signIn.js',
    output: {
        path: path.resolve(".", 'bundle'),
        filename: 'singIn/signInBundle.js',
    },
};*/
module.exports = {
    entry: './src/profile.js',
    module: {
        rules: [
            {test: '/\.js/\$' , use: 'module-brfs.js'}
        ]
    },
    output: {
        path: path.resolve(".", 'bundle'),
        filename: 'profile/profileBundle.js',
    }
};
/*module.exports = {
    entry: './src/createDelphiQuiz.js',
    output: {
        path: path.resolve(".", 'bundle'),
        filename: 'createDelphiQuizBundle.js',
    }
};
module.exports = {
    entry: './src/createForecast.js',
    output: {
        path: path.resolve(".", 'bundle'),
        filename: 'createForecastBundle.js',
    }
};
module.exports = {
    entry: './src/assetForecasts.js',
    output: {
        path: path.resolve(".", 'bundle'),
        filename: 'assetForecastsBundle.js',
    }
};
module.exports = {
    entry: './src/viewDelphiQuiz.js',
    output: {
        path: path.resolve(".", 'bundle'),
        filename: 'viewDelphiQuizBundle.js',
    }
};
*/
