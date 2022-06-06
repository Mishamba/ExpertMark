const $ = require("jquery")

$(document).ready(function() {

    let urlVars = getUrlVars()
    let username = urlVars["username"]
    let jwt = getJWTFromCookie()

    $.ajax({
        url: "http://localhost:8082/users/" + username,
        type: "get",
        headers: {
            "X-Requested-With" : "XMLHttpRequest",
            "Authorization" : jwt
        }
    }).done(function (data) {
        // {
        //     "username": "mishamba",
        //     "createDate": "23, May, 2022",
        //     "role": "USER",
        //     "profile": {
        //         "followingUserNames": [
        //             "dziana",
        //             "mark"
        //         ],
        //         "accountDescription": "hello. I'm new user."
        //     }
        // }
        $("#username").text(data["username"])
        $("#createDate").text(data["createDate"])
        $("#role").text(data["role"])
        $("#followingUsernames").text(data["followingUsernames"])
        $("#accountDescription").text(data["accountDescription"])
    })
})
