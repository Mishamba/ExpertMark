const $ = require("jquery")
$(document).ready(function() {
    if (getCookie("role") === "ADMIN") {
        $.get("./template/roleEditor_template.html", function (template) {
            $("editor_body").appendChild($(template.html()))
        }, "html")
    }

    $("#create_account_button").click(function () {
        $.ajax({
            url: "http://localhost:8082/user",
            type: "post",
            data: {
                "username": $("#username").val(),
                "password": $("#password").val(),
                "role": $("#role").val(),
                "profile": {
                    "accountDescription": $("#account_description").val()
                }
            },
            headers: {
                "Content-Type": "application/json",
                "Authorization": getJWTFromCookie()
            },
            dataStructure: "json"
        }).done(function(data) {
            $.ajax({
                url: "http://localhost:8085/authorize",
                type: "post",
                data: {
                    "username" : $("#username").val(),
                    "password" : $("#password").val()
                },
                headers: {
                    "X-Requested-With": "XMLHttpRequest",
                    "Content-Type": "application/json"
                },
                dataStructure: "json"
            }).done(function(data) {
                    console.log(data)
                    document.cookie = "userToken=" + data['jwt']
                    document.cookie = "username=" + data['username']
                    document.cookie = "role=" + data['role']
                    $(location).attr("href", "http://localhost:63342/ExpertMark/views/pages/profile.html?username=" + username)
            })
        })
    })
})
