let script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.6.0.min.js';
document.getElementsByTagName('head')[0].appendChild(script);

function sendForecast()
{
    // getting head element. then we will get all data using it
    let forecastDataElement = document.getElementById("forecast_data")

    //getting all required info
    let assetName = forecastDataElement.getElementById("asset_name").value
    let targetDate = forecastDataElement.getElementById("target_date").value
    let methodType = forecastDataElement.getElementById("method_type").value
    let methodDataElement = forecastDataElement.getElementById("forecast_method_data").value

    // creating json body
    let methodDataJson = {"methodData" : getMethodDataJsonByMethodType(methodType, methodDataElement)}
    let forecastJSON = {"assetName" : assetName, "targetDate" : targetDate, "methodType" : methodType, "methodData": methodDataJson}

    //getting jwt key from cookie
    let jwtKey = getJWTFromCookie()

    $("send_forecast_button").click(function() {
        $.ajax({
            url: "http://localhost:8081/forecasts/create",
            type: "post",
            data: {"forecast" : forecastJSON},
            headers: {
                "Content-Type" : "application/json",
                "Authorization" : jwtKey
            },
            dataStructure: "json"
        }).done(function (data) {
            console.log("Created forecast, got response: ", data)
        })
    })
}

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
    let optimisticMark = methodDataElement.getElementById("optimisticMark").value
    let pessimisticMark = methodDataElement.getElementById("pessimisticMark").value

    // creating json
    return {
        methodType : "TWO_MARKS_FORECAST",
        "optimisticMark" : optimisticMark,
        "pessimisticMark" : pessimisticMark
    }
}

function getMethodDataForThreeMarksForecast(methodDataElement) {
    let optimisticMark = methodDataElement.getElementById("optimisticMark").value
    let middleMark = methodDataElement.getElementById("middleMark").value
    let pessimisticMark = methodDataElement.getElementById("pessimisticMark").value

    // creating json
    return {
        methodType : "THREE_MARKS_FORECAST",
        "optimisticMark" : optimisticMark,
        "middleMark" : middleMark,
        "pessimisticMark" : pessimisticMark}
}

function getMethodDataForThreeMarksWithChancesForecast(methodDataElement) {
    let optimisticMark = methodDataElement.getElementById("optimisticMark").value
    let optimisticMarkChance = methodDataElement.getElementById("optimisticMarkChance").value
    let realisticMark = methodDataElement.getElementById("realisticMark").value
    let realisticMarkChance = methodDataElement.getElementById("realisticMarkChance").value
    let pessimisticMark = methodDataElement.getElementById("pessimisticMark").value
    let pessimisticMarkChance = methodDataElement.getElementById("pessimisticMarkChance").value

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
