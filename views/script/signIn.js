let script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.6.0.min.js';
document.getElementsByTagName('head')[0].appendChild(script);

$("#sign_in_button").click(function() {
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
        }
    })
})