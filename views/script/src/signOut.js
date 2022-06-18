const $ = require("jquery")

$("#sign_out_button").click(function() {
    document.cookie = 'username=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    document.cookie = 'role=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    document.cookie = 'userToken=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    $(location).attr("href", "http://localhost:63342/ExpertMark/views/pages/index.html")
})