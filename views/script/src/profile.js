const $ = require("jquery")

$(document).ready(function() {

    let urlVars = getUrlVars()
    let username = urlVars["username"]
    let ausername = getCookie("username")

    /*$.ajax({
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
    })*/

    delay(836).then(() => {
        if (ausername !== username) {
            if (username === "Diana") {
                $('.profile__img').attr('src', function (index, attr) {
                    return attr.replace('trash', 'img');
                });
                $(".profile__name").text("@Diana")
                $(".profile__description").text("Etherium expert. Bitcoin going down")
                $("#followEditButton").text("Follow")
                $("#followEditButton").click(function () {
                    delay(200).then(() => {
                        if ($("#followEditButton").text() !== "Unfollow") {
                            $("#followEditButton").text("Unfollow")
                        } else {
                            $("#followEditButton").text("Follow")
                        }
                    })
                })
                $(".profile__followings").html(" Followings: <li>Alice</li><li>Bruce</li>")
                $(".accuracy").html(" Accuracy: <span>75.8</span>")
            }
        } else {
            $('.profile__img').attr('src', function (index, attr) {
                return attr.replace('trash', 'mishamba');
            });
            $(".profile__name").text("@Mishamba")
            $(".profile__description").text("Hello. I'm crypto expert and know everything about it.")
            $("#followEditButton").text("Edit")
            $(".profile__followings").html(" Followings: <li>Billy</li><li>Van</li><li>Ban</li>")
            $(".accuracy").html(" Accuracy: <span>76.35666</span>")
        }
    })

})

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}
