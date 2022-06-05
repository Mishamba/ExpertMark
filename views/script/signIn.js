import $ from "jquery"

$("#sign_in_button").onclick(function() {
    let username = $("username").val();
    let password = $("password").val();

    $.ajax({
        url: "http://localhost:8085/authorize",
        type: "post",
        data: {"username" : username, "password" : password},
        headers: {
            "Content-Type" : "application/json",
        },
        dataStructure: "json"
    }).done(function (data) {
        console.log(data)
        if (data.status === 403 || data.status === 500) {
            $(".container").append("<p>Not Authorized</p>")
        } else {
            document.cookie = "userToken:" + data
            $(location).attr("href", "http://localhost:8080/profile.html")
        }
    })
})