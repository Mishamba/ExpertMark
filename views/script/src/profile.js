const $ = require("jquery")

$(document).ready(function() {

    let urlVars = getUrlVars()
    let username = urlVars["username"]
    let ausername = getCookie("username")

    /*$.ajax({
        url: "http://localhost:8082/users/" + username,
        type: "get",
        headers: {
            "X-Requested-With" : "XMLHttpRequest",
            "Authorization" : jwt
        }
    }).done(function (data) {
        // {
        //     "username": "mishamba",
        //     "createDate": "23, May, 2022",
        //     "role": "USER",
        //     "profile": {
        //         "followingUserNames": [
        //             "dziana",
        //             "mark"
        //         ],
        //         "accountDescription": "hello. I'm new user."
        //     }
        // }
        $("#username").text(data["username"])
        $("#createDate").text(data["createDate"])
        $("#role").text(data["role"])
        $("#followingUsernames").text(data["followingUsernames"])
        $("#accountDescription").text(data["accountDescription"])
    })*/

    delay(836).then(() => {
        if (username === 'mishamba') {
            $("body").appendChild(
            "<!--Header-->" +
            "<header class='header'>" +
                "<div class='container'>" +
                "<div class='header__inner'>" +
                "<div class='header_logo'>" +
                "<h1>ExpertMark</h1>" +
        "</div>" +

            "<nav class='nav'>" +
                "<a href='signIn.html' class='nav__link'>Sign In</a>" +
                "<a href='signIn.html' class='nav__link'>Sign Out</a>" +
            "</nav>" +
        "</div>" +
        "</div>" +
        "</header>" +

            "<!--Menu-->" +
            "<div class='menu'>" +
                "<div class='container'>" +
                    "<div class='menu__inner'>" +
                        "<a class='btn  btn--red' href="assets.html">Assets</a>" +
                        <a class="btn  btn--red" href="profile.html">Profile</a>
                        <a class="btn  btn--red" href="myforecast.html">My Forecasts</a>
                        <a class="btn  btn--red" href="forecastsLine.html">Forecasts Line</a>
                        <a class="btn  btn--red" href="recommendations.html">Recommend</a>
                        <a class="btn  btn--red" href="findForecast.html">Find Forecasts</a>
                        <a class="btn  btn--red" href="delphisearch.html">Find Delphi Forecasts</a>
                        <a class="btn  btn--red" href="expertsSearch.html">Find Experts</a>
                    </div>
                </div>
            </div>

            <h2>Profile</h2>

            <div class="profile">
                <div class="profile__inner">
                    <img src="../img/mihail.jpeg" alt="" class="profile__img">
                        <div class="profile__name">@Mishamba</div>
                        <div class="profile__description">Hello. I'm a crypto expert.</div>
                        <button>Follow</button>
                        <ul class="profile__followings"> Followings:
                            <li>Diana</li>
                            <li>Ilya</li>
                            <li>Mark</li>
                            <li>Andrew</li>
                            <li>Billy</li>
                            <li>Van</li>
                        </ul>
                "</div>" +
                "<div class="expert__statistic">" +
                    "<div class="title">Expert Statistic:</div>" +
                    "<div class="character">Character: <span>NEWBIE</span></div>" +
                    "<div class="accuracy">Accuracy: <span>69.5</span></div>" +
                "</div>" +
            "</div>")
        }
        if (ausername === username) {

        }
    })

})

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}
