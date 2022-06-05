import $ from "jquery"
import getUrlVars from "views/script/util/requestUtils.js"
import getJwtToken from "views/script/util/security.js"

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
            let delphiQuiz = $("div", {"class":"delphi__inner"})
            $(delphiQuiz).append($("<div class=\"delphi__title\">Title: Bitcoin future</div>"))
            $(delphiQuiz).append($("<img src=\"img/" + dataJson.get("assetName") +".webp\" alt=\"\" class=\"delphi__img\">Title: " + dataJson.get("title") + "</img>"))
            $(delphiQuiz).append($("<div class=\"delphi__asset\">Asset name: " + dataJson.get("assetName") +"</div>"))
            $(delphiQuiz).append($("<div class=\"delphi__description\">Description: " + dataJson.get("description") +"</div>"))
            $(delphiQuiz).append($("<div class=\"delphi__date\">Create Date: " + dataJson.get("createDate") +"</div>"))
            $(delphiQuiz).append($("<div class=\"delphi__experts\">Experts: " + dataJson.get("experts") +"</div>"))

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
