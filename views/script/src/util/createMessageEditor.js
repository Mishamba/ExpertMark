const $ = require("jquery");

function createMessageEditor() {
    $.get("./template/methodDataEditor_template.html", function (template) {
        $("delphi__discussion").appendChild($(template.html()))
    }, "html")

    let jwt = getJWTFromCookie()

    $("#message_send_button").click(function() {
        $.ajax({
            url: "http://localhost:8081/forecasts/create",
            type: "post",
            data: {
                "message": $("message_body_editor").val()
            },
            headers: {
                "Content-Type": "application/json",
                "Authorization": jwt
            },
            dataStructure: "json"
        })
    })
    //if this wouldn't work use this
    // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function
}