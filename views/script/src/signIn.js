const $ = require("jquery")

$("#sign_in_button").click(function() {
    let username = $("#username").val();
    let password = $("#password").val();

    let credentials = {"username": username, "password": password}

    console.log(credentials)

    $.ajax({
        url: "http://localhost:8085/authorize",
        type: "post",
        data: JSON.stringify(credentials),
        headers: {
            "X-Requested-With": "XMLHttpRequest",
            "Content-Type": "application/json"
        },
        dataStructure: "json"
    }).done(function(data) {
        console.log(data)
        if (data.status === 403 || data.status === 500) {
            $(".container").append("<p>Not Authorized</p>")
        } else {
            console.log(data)
            document.cookie = "userToken=" + data
            $(location).attr("href", "http://localhost:63342/ExpertMark/views/profile.html?username=" + username)
        }
    })
})