const $ = require("jquery")

$("#sign_in_button").click(function() {
    let username = $("#username").val();
    let password = $("#password").val();

    let credentials = {"username": username, "password": password}

    console.log(credentials)
    /*

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
            document.cookie = "userToken=" + data['jwt']
            document.cookie = "username=" + data['username']
            document.cookie = "role=" + data['role']
            $(location).attr("href", "http://localhost:63342/ExpertMark/views/pages/profile.html?username=" + username)
        }
    })*/
    delay(438).then(() => {
            document.cookie = "username=" + username
            $(location).attr("href", "http://localhost:63342/ExpertMark/views/pages/profile.html?username=" + username)
        })
})

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}