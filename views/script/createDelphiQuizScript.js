import $ from "jquery";
import getJWTFromCookie  from "views/script/util/security"

$("#send_delphi_quiz_button").click(function() {
    let title = $("#title").val()
    let assetName = $("#asset_name").val()
    let description = $("#description").val()
    let discussionTimeInDays = $("#discussion_time_in_days").val()
    let expertUsernamesNotParsed = $("#expert_usernames").val()

    let expertUsernamesList = expertUsernamesNotParsed.split(",\s")
    let expertUsernamesJson = []
    expertUsernamesList.forEach(username => expertUsernamesJson.push(username))

    let delphiQuizJson = {
        "title" : title,
        "assetName" : assetName,
        "description" : description,
        "discussionTimeInDays" : discussionTimeInDays,
        "expertUsernames" : expertUsernamesJson
    }

    let jwtKey = getJWTFromCookie()

    $.ajax({
        url: "http://localhost:8084/delphiQuiz/create",
        type: "post",
        data: {"forecast": delphiQuizJson},
        headers: {
            "Content-Type": "application/json",
            "Authorization": jwtKey
        },
        dataStructure: "json"
    }).done(function (data) {
        console.log("Created forecast, got response: ", data)
    })
})
