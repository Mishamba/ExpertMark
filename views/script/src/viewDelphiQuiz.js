const $ = require("jquery")

//getting
$(document).ready(function() {
    let urlVars = getUrlVars()

    let delphiQuizId = urlVars["delphiQuizId"]
    let jwt = getJwtToken()

    $.ajax({
        url: "http://localhost:8084/delphiQuiz/" + delphiQuizId,
        type: "get",
        contentType: "application/json",
        headers: {
            "Authentication" : jwt
        },
        processData: true,
        dataType: "json",
        success: function(data) {
            let dataJson = JSON.parse(data)
            let delphiQuiz = $("<div class=\"delphi__inner\"></div>")
            $(delphiQuiz).appendChild($("<div class=\"delphi__title\">Title: Bitcoin future</div>"))
            $(delphiQuiz).appendChild($("<img src=\"img/" + dataJson.get("assetName") +".webp\" alt=\"\" class=\"delphi__img\">Title: " + dataJson.get("title") + "</img>"))
            $(delphiQuiz).appendChild($("<div class=\"delphi__asset\">Asset name: " + dataJson.get("assetName") +"</div>"))
            $(delphiQuiz).appendChild($("<div class=\"delphi__description\">Description: " + dataJson.get("description") +"</div>"))
            $(delphiQuiz).appendChild($("<div class=\"delphi__date\">Create Date: " + dataJson.get("createDate") +"</div>"))
            $(delphiQuiz).appendChild($("<div class=\"delphi__experts\">Experts: " + dataJson.get("experts") +"</div>"))

            //TODO complete chat

            //TODO complete expertMarks

            $(".box").append(delphiQuiz)
            dataJson.get("createDate");
        },
        error: function(data) {
            console.log("can\'\t get data")
            console.log(data.items)
        }
    });
})
