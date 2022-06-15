const $ = require("jquery");
$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8083/experts",
        type: "get",
        headers: {
            "X-Requested-With": "XMLHttpRequest",
            "Authorization" : getJWTFromCookie()
        },
        dataStructure: "json"
    }).done(function(data) {
        // data format
        //
        // [
        //     {
        //         "username": "mishamba",
        //         "createDate": "23, May, 2022",
        //         "role": "USER",
        //     },
        //     {
        //         "username": "mark",
        //         "createDate": "24, May, 2022",
        //     }
        data.forEach(expert => {
            $.get("./template/expertPresent_template.html", function(template) {
                $("#experts").appendChild($.tmpl(template.html(), expert))
            })
            //if this wouldn't work use this
            // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function
        })
    })
})