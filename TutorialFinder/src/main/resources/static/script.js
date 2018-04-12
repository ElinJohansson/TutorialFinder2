$("input[type=\"checkbox\"]").on("click", function (e) {
    //get all boxes that are checked
   var checkedBoxes = $("input[type=\"checkbox\"]:checked");
   var language = "";
   var format = "";
   for(var i = 0; i < checkedBoxes.length; i++){
        if($(checkedBoxes[i]).hasClass("languageCheckbox")){
            language+=$(checkedBoxes[i]).val()+",";
        }
        if($(checkedBoxes[i]).hasClass("formatCheckbox")){
            format+=$(checkedBoxes[i]).val()+",";
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
            "<p>Title:</p>" + tutorialList[i].title +
            "<p>Average Rating:</p>\n" + tutorialList[i].avgRating +
            "<p>URL:</p>\n" + tutorialList[i].url +
            "<p>Year added:</p>\n" + tutorialList[i].creationDate.year +
            "<p>Description:</p>\n" + tutorialList[i].descr +
            "<p>Format:</p>\n" + tutorialList[i].format +
            "<p>Language:</p>" + tutorialList[i].language +
            "<form method=\"post\" action=\"addRating\">\n" +
            "    <select name=\"rating\">\n" +
            "    <option value=\"1\">1</option>\n" +
            "    <option value=\"2\">2</option>\n" +
            "    <option value=\"3\">3</option>\n" +
            "    <option value=\"4\">4</option>\n" +
            "    <option value=\"5\">5</option>\n" +
            "    </select>\n" +
            "    <input type=\"submit\" id=\"addRating\" value=\"Rate Tutorial\"/>\n" +
            "    </form></li>");
    }
}






