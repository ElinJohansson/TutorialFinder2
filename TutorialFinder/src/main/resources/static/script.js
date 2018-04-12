$("input[type=\"checkbox\"]").on("click", function (e) {
    //get all boxes that are checked
    var checkedBoxes = $("input[type=\"checkbox\"]:checked");
    var language = "";
    var format = "";
    for (var i = 0; i < checkedBoxes.length; i++) {
        if ($(checkedBoxes[i]).hasClass("languageCheckbox")) {
            language += $(checkedBoxes[i]).val() + ",";
        }
        if ($(checkedBoxes[i]).hasClass("formatCheckbox")) {
            format += $(checkedBoxes[i]).val() + ",";
        }
    }

    //ajaxanrop till controllern
    console.log("value: " + language);
    $.ajax({
        type: "GET",
        error: function () {
            console.log("error retrieving the data");
            render([]);
        },
        data: {
            language: language,
            format: format
        },
        url: "/filterOnLanguage", //which is mapped to its partner function on our controller class
        success: function (result) {
            console.log("successfully fetched ", result)
            render(result); //to update the result on the web page
        }
    });


});


//Renders the tutorialList which is retrieved using the ajax request, the returned list is actually
//an array which is why we're using a regular for loop
function render(tutorialList) {
    $("#returnedLanguages").html("");
    for (var i = 0; i < tutorialList.length; i++) {
        $("#returnedLanguages").append("<li>" +
            "<h4>Title:</h4>" + tutorialList[i].title +
            "<h4>Average Rating:</h4>\n" + tutorialList[i].avgRating +
            "<h4>URL:</h4>\n" + "<a href=\"http://" + tutorialList[i].url + "\" target=\"_blank\">" + tutorialList[i].url + "</a>" +
            "<h4>Year added:</h4>\n" + tutorialList[i].creationDate.year +
            "<h4>Description:</h4>\n" + tutorialList[i].descr +
            "<h4>Format:</h4>\n" + tutorialList[i].format +
            "<h4>Language:</h4>" + tutorialList[i].language +
            "</li>");

    }
}

// <a href="http://www.google.se" target="_blank"> klicka här </a>




