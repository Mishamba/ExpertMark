const $ = require("jquery");
$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8083/forecasts",
        type: "get",
        headers: {
            "X-Requested-With": "XMLHttpRequest",
            "Authorization" : getJWTFromCookie()
        },
        dataStructure: "json"
    }).done(function(data) {
        // data format
        //
        // [
        //     {
        //         "_id": "6292207a82c01712e95a565f",
        //         "methodType": "TWO_MARKS_FORECAST",
        //         "methodData": {
        //             "pessimisticMark": 25000.0,
        //             "optimisticMark": 30000.0
        //         },
        //         "assetName": "BTC",
        //         "ownerUsername": "someone",
        //         "createDate": "28, May, 2022",
        //         "targetDate": "28, May, 2022",
        //         "accuracy": null
        //     }
        // ]
        data.forEach(forecast => {
            $.get("./template/forecast_template.html", function(template) {
                $("#forecasts").appendChild($.tmpl(template.html(), forecast))
            })
            //if this wouldn't work use this
            // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function
        })
    })
})
