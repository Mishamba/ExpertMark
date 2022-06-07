const $ = require("jquery");

function createMethodDataEditor() {
    $.get("./template/methodDataEditor_template.html", function(template) {
        $("delphi__marks").appendChild($(template.html()))
    }, "html")
    //if this wouldn't work use this
    // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function

    let urlVars = getUrlVars()
    let jwt = getJWTFromCookie()

    $("#method_type").change(function() {
        let selectedMethodType = ("#method_type").find(":selected").text()

        printNewFieldsOnSelectorChange(selectedMethodType)
    })

    $(".create__forecast").click(function() {
        let methodType = $("#method_type").find(":selected").text()
        let methodData = getMethodData(methodType)

        $.ajax({
            url: "http://localhost:8084/delphiQuiz/" + urlVars["delphiQuizId"] + "/post/mark",
            type: "post",
            contentType: "application/json",
            data: {
                "methodType" : methodType,
                "methodData" : methodData
            },
            headers: {
                "Authentication" : jwt
            },
            processData: true,
            dataType: "json",
        })
    })
}

function getMethodData(methodType) {
    let data = {}

    switch (methodType) {
        case "TWO_MARKS_FORECAST" :
            data = {
                "pessimisticMark" : $("#pessimisticMark").text(),
                "optimisticMark" : $("#optimisticMark").text()
            }
            break
        case "THREE_MARKS_FORECAST" :
            data = {
                "pessimisticMark" : $("#pessimisticMark").text(),
                "realisticMark" : $("#realisticMark").text(),
                "optimisticMark" : $("#optimisticMark").text()
            }
            break
        case "THREE_MARKS_WITH_CHANCES_FORECAST" :
            data = {
                "pessimisticMark" : $("#pessimisticMark").text(),
                "pessimisticChance" : $("#pessimisticChance").text(),
                "realisticMark" : $("#realisticMark").text(),
                "realisticChance" : $("#realisticChance").text(),
                "optimisticMark" : $("#optimisticMark").text(),
                "optimisticChance" : $("#optimisticChance").text()
            }
            break
    }

    return data
}

function printNewFieldsOnSelectorChange(methodType) {
    switch (methodType) {
        case "TWO_MARKS_FORECAST" :
            printTemplate("./template/twoMarksEditor_template.html")
            break
        case "THREE_MARKS_FORECAST" :
            printTemplate("./template/threeMarksEditor_template.html")
            break
        case "THREE_MARKS_WITH_CHANCES_FORECAST" :
            printTemplate("./template/threeMarksWithChancesEditor_template.html")
            break
    }
}

function printTemplate(templatePath) {
    $.get(templatePath, function(template) {
        let forecastMethodData = $("#forecast_method_data")
        forecastMethodData.empty()
        forecastMethodData.appendChild(template.html())
    })
    //if this wouldn't work use this
    // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function
}