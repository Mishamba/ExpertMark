const $ = require("jquery");
$("#find_button").click(function() {
    let findBy = $("#find_by").find(":selected").text()
    let query = $("#search").val()

    let queryUrl

    if (findBy === "asset") {
        queryUrl = "http://localhost:8081/forecasts/assets/" + query
    } else {
        queryUrl = "http://localhost:8081/forecasts/user_owned/" + query
    }

    $.ajax({
        url: queryUrl,
        type: "get",
        headers: {
            "X-Requested-With": "XMLHttpRequest",
            "Authorization" : getJWTFromCookie()
        },
        dataStructure: "json"
    }).done(function() {
        data.forEach(forecast => {
            $.get("./template/forecast_template.html", function(template) {
                $("experts").appendChild($.tmpl(template.html(), forecast))
            })
            //if this wouldn't work use this
            // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function
        })
    })
})