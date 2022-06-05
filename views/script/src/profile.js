import $ from "jquery"
import getUrlVars from "views/script/util/requestUtils.js"

$(document).ready(function() {
    let urlVars = getUrlVars()

    let username = urlVars["username"]

    $.ajax({
        url: "http://localhost:8082"
    })
})