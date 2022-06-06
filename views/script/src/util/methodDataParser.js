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
