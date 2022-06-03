let script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.6.0.min.js';
document.getElementsByTagName('head')[0].appendChild(script);

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

function getMethodDataJsonByMethodType(methodType, methodDataElement) {
    switch (methodType) {
        case "TWO_MARKS_FORECAST" :
            return getMethodDataForTwoMarksForecast(methodDataElement);
        case "THREE_MARKS_FORECAST" :
            return getMethodDataForThreeMarksForecast(methodDataElement);
        case "THREE_MARKS_WITH_CHANCES_FORECAST" :
            return getMethodDataForThreeMarksWithChancesForecast(methodDataElement);
    }
}

function getMethodDataForTwoMarksForecast(methodDataElement) {
    let optimisticMark = methodDataElement.find("#optimistic_mark").val()
    let pessimisticMark = methodDataElement.find("#pessimistic_mark").val()

    // creating json
    return {
        methodType : "TWO_MARKS_FORECAST",
        "optimisticMark" : optimisticMark,
        "pessimisticMark" : pessimisticMark
    }
}

function getMethodDataForThreeMarksForecast(methodDataElement) {
    let optimisticMark = methodDataElement.find("#optimistic_mark").val()
    let realisticMark = methodDataElement.find("#realistic_mark").val()
    let pessimisticMark = methodDataElement.find("#pessimistic_mark").val()

    // creating json
    return {
        methodType : "THREE_MARKS_FORECAST",
        "optimisticMark" : optimisticMark,
        "middleMark" : realisticMark,
        "pessimisticMark" : pessimisticMark}
}

function getMethodDataForThreeMarksWithChancesForecast(methodDataElement) {
    let optimisticMark = methodDataElement.find("#optimistic_mark").value
    let optimisticMarkChance = methodDataElement.find("#optimistic_mark_chance").value
    let realisticMark = methodDataElement.find("#realistic_mark").value
    let realisticMarkChance = methodDataElement.find("#realistic_mark_chance").value
    let pessimisticMark = methodDataElement.find("#pessimistic_mark").value
    let pessimisticMarkChance = methodDataElement.find("#pessimistic_mark_chance").value

    // creating json
    return {
        methodType : "THREE_MARKS_FORECAST",
        "optimisticMark" : optimisticMark,
        "optimisticMarkChance" : optimisticMarkChance,
        "realisticMark" : realisticMark,
        "realisticMarkChance" : realisticMarkChance,
        "pessimisticMark" : pessimisticMark,
        "pessimisticMarkChance" : pessimisticMarkChance
    }
}

function getJWTFromCookie() {
    return getCookie("userToken")
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
