const $ = require("jquery")

//getting
$(document).ready(function() {
    let urlVars = getUrlVars()
    let jwt = getJwtToken()

    // Output example
    //
    //  {
    //     _id: 'BTC nearest future',
    //     quizSteps: [
    //       {
    //         stepNumber: 1,
    //         marks: [
    //           {
    //             methodType: 'TWO_MARKS_FORECAST',
    //             methodData: { pessimisticMark: 10, optimisticMark: 15 },
    //             ownerUsername: 'mark'
    //           },
    //           {
    //             methodType: 'TWO_MARKS_FORECAST',
    //             methodData: { pessimisticMark: 10, optimisticMark: 15 },
    //             ownerUsername: 'mishamba'
    //           }
    //         ],
    //         anotherTourRequired: null,
    //         medianMark: null,
    //         justificationFinishDate: '02, June, 2022'
    //       }
    //     ],
    //     assetName: 'BTC',
    //     description: 'we are trying to make forecast for BTC nearest future',
    //     discussionTimeInDays: 5,
    //     expertsUsernames: [ 'mishamba', 'dziana', 'mark' ],
    //     createDate: '28, May, 2022',
    //     discussionEndDate: '28, May, 2022',
    //     discussion: [
    //       {
    //         postDate: '01:29 28, May, 2022',
    //         message: 'trash point of view'
    //       },
    //       {
    //         postDate: '01:29 28, May, 2022',
    //         message: 'trash point of view'
    //       }
    //     ],
    //     firstTourQuartileRation: null
    //   },
    $.ajax({
        url: "http://localhost:8084/delphiQuiz/" + urlVars["delphiQuizId"],
        type: "get",
        contentType: "application/json",
        headers: {
            "Authentication" : jwt
        },
        processData: true,
        dataType: "json",
        success: function(data) {
            $(".delphi__title").text("Title: " + data["title"])
            $(".delphi__asset").text("Asset name: " + data["assetName"])
            $(".delphi__description").text("Description: " + data["description"])
            $(".delphi__date").text("Create Date: " + data["createDate"])
            $(".delphi__experts").text("Experts: " + data["expertUsernames"])

            data["discussion"].forEach(message => {
                $(".delphi__discussion").append($.get("./template/delphiQuizMessage_template.html", function(template) {
                    $.tmpl(template.html(), message)
                }, "html"))
                //if this wouldn't work use this
                // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function
            })

            data["quizSteps"].forEach(quizStep => {
                let marks = $("<div class='marks'></div>")

                quizStep["mark"].forEach(mark => {
                    marks.appendChild($.get("./template/methodData_template.html", function(template) {
                        $.tmpl(template.html(), mark)
                    }, "html"))
                    //if this wouldn't work use this
                    // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function
                })

                $("#quizzes").append(
                    $.get("./template/delphiQuizStep_template.html", function(template) {
                        $.tmpl(template.html(), quizStep)
                    }, "html").
                    find(".marks").appendChild(marks))
                //if this wouldn't work use this
                // https://stackoverflow.com/questions/35263196/jquery-templates-pass-html-parameter-into-a-function
            })

            let username = getCookie("username")
            let isExpert = false
            data["expertUsernames"].forEach(expertUsername => {
                if (expertUsername === username) {
                    isExpert = true
                }
            })

            if (isExpert) {
                createMethodDataEditor()
                createMessageEditor()
            }
        },
        error: function(data) {
            console.log("can\'t get data")
            console.log(data.items)
        }
    });
})
