const $ = require("jquery")

$("#send_forecast_button").click(function() {
    //getting all required info
    let assetName = $("#asset_name").val()
    let targetDate = $("#target_date").val()
    let methodType = $("#method_type").val()
    let methodDataElement = $("#forecast_method_data").val()

    // creating json body
    let methodDataJson = {"methodData": getMethodDataJsonByMethodType(methodType, methodDataElement)}
    let forecastJSON = {
        "assetName": assetName,
        "targetDate": targetDate,
        "methodType": methodType,
        "methodData": methodDataJson
    }

    //getting jwt key from cookie
    let jwtKey = getJWTFromCookie()

    $.ajax({
        url: "http://localhost:8081/forecasts/create",
        type: "post",
        data: {"forecast": forecastJSON},
        headers: {
            "Content-Type": "application/json",
            "Authorization": jwtKey
        },
        dataStructure: "json"
    }).done(function (data) {
        console.log("Created forecast, got response: ", data)
    })
})
