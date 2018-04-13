$("input[type=\"checkbox\"]").on("click", renderTutorials);

var listOfVotedTitles = [];

//Renders the tutorialList which is retrieved using the ajax request, the returned list is an array
function render(tutorialList) {
    $(".instructions").remove();
    $("#returnedLanguages").html(""); //clears the resultarea

    //Loops through the respons from our ajax request
    for (var i = 0; i < tutorialList.length; i++) {

        //to the nearest integer
        var rating = Math.round(tutorialList[i].avgRating);
        var stars = [];
        //loop over all the stars and push checked stars
        for (var j = 0; j < rating; j++) {
            stars.push("<span class=\"fa fa-star checked\"></span>");

        }
        //loop over until 5 stars are in the stars array
        for (var k = rating ; k < 5; k++) {
            stars.push("<span class=\"fa fa-star\"></span>");
        }
        //create a whole string of all the stars in the array
        var allStars = "";
        for (var l = 0; l < stars.length; l++) {
            allStars += stars[l];
        }

        //checks what titles the user has already voted on in the current view
        var isDisabled = listOfVotedTitles.indexOf(tutorialList[i].title) > -1 ? "disabled" : "";

        $("#returnedLanguages").append("<li class=\"listcolor" + i % 2 + "\">" +
            "<h4 ><span class=\"title\">" + tutorialList[i].title + "</span></h4>" +
            "" + allStars +
            "<h4><span>" + tutorialList[i].descr + "</span></h4>\n" +
            "<h4><span> " + "<a href=\"" + tutorialList[i].url + "\" target=\"_blank\">" + tutorialList[i].url + "</a>" + "</span></h4>\n" +
            "<h4>Year added: <span>" + tutorialList[i].creationDate.year + "</span></h4>\n" +
            "    <select name=\"rating\">\n" +
            "    <option value=\"1\">1</option>\n" +
            "    <option value=\"2\">2</option>\n" +
            "    <option value=\"3\">3</option>\n" +
            "    <option value=\"4\">4</option>\n" +
            "    <option value=\"5\">5</option>\n" +
            "    </select>\n" +
            "    <button class=\"button\ " + isDisabled + "\" >Rate me</button>\n" + "</li>"
        );
    }
    $(".disabled").text("Rated!");

    $(".button").on("click", function (e) {
        var rating = $(this).prev().val();
        var tutorialTitle = $(this).closest("li").children(":first").children(":first").html();

        $.ajax({
            type: "POST",
            error: function () {
                //console.log("error sending the data");
                render([]);
            },
            data: {
                rating: rating,
                title: tutorialTitle
            },
            url: "/addRating", //which is mapped to its partner function on our controller class
            success: function (result) {
                //console.log("successfully inserted ", result)
                listOfVotedTitles.push(result);
                // render(result); //to update the result on the web page
            }
        });
        window.setTimeout(renderTutorials, 100);
    });
}

function renderTutorials() {
    //get all boxes that are checked
    var checkedBoxes = $("input[type=\"checkbox\"]:checked");
    var language = "";
    var format = "";
    var tag = "";
    for (var i = 0; i < checkedBoxes.length; i++) {
        if ($(checkedBoxes[i]).hasClass("languageCheckbox")) {
            language += $(checkedBoxes[i]).val() + ",";
        }
        if ($(checkedBoxes[i]).hasClass("formatCheckbox")) {
            format += $(checkedBoxes[i]).val() + ",";
        }
        if ($(checkedBoxes[i]).hasClass("tagCheckbox")) {
            tag += $(checkedBoxes[i]).val() + ",";
        }
    }

    $.ajax({
        type: "GET",
        error: function () {
           // console.log("error retrieving the data");
            render([]);
        },
        data: {
            language: language,
            format: format,
            tag: tag
        },
        url: "/filterOnLanguage", //which is mapped to its partner function on our controller class
        success: function (result) {
          //  console.log("successfully fetched ", result)
            render(result); //to update the result on the web page
        }
    });
}