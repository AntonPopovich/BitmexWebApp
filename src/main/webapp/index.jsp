<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>BitmexWebApp</title>
    <link href="static/main.css" rel="stylesheet" type="text/css">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <script src="https://code.jquery.com/jquery-1.10.2.js"
            type="text/javascript"></script>
</head>
<body>

<h1>Welcome to Bitmex Bot!<br> (testnet)</h1>
<div class="input_fields_wrap">
</div>
<br><br>
<div class="bot_form">

<form name="botDataForm" action="javascript:void(null);">
    <label for="akey">API Key:</label><br>
    <input type="text" id="akey" name="akey" value=""><br>
    <label for="asecret">API Secret:</label><br>
    <input type="text" id="asecret" name="asecret" value=""><br>
    <label for="stp">Step:</label><br>
    <input type="text" id="stp" name="stp" value=""><br>
    <label for="lvl">Level:</label><br>
    <input type="text" id="lvl" name="lvl" value=""><br>
    <label for="coef">Coefficient:</label><br>
    <input type="text" id="coef" name="coef" value=""><br>
    <input class="add_field_button" id="submit" type="submit" name="submsit" value="Submit">
</form>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script>
    function deleteF2() {
        var formData = {
            akey2: $("#akey2").val(),
            delete: $("#delete").val(),
        };

        $.ajax({
            type: "POST",
            url: "/stopBotServlet",
            data: formData,
            dataType: "json",
            encode: true,
            async: false,
        }).done(function (data) {
            console.log(data);
        });}

    var x = 0;
    var y = 0;
    var def = "";

    function loadString() {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function ajaxF() {

            if (this.readyState === 4 && this.status === 200) {
                def = this.responseText;
            }
        };
        xhttp.open("GET", "updateKey.jsp?number=" + y, false);
        xhttp.send();
        y++;
        return def;
    }

    $(document).ready(function () {
        var max_fields      = 5; //maximum input boxes allowed
        var wrapper   		= $(".input_fields_wrap");
        var x = 0;

        $("#submit").click(function sendF() {
            var formData = {
                akey: $("#akey").val(),
                asecret: $("#asecret").val(),
                stp: $("#stp").val(),
                lvl: $("#lvl").val(),
                coef: $("#coef").val(),
            };

            $.ajax({
                type: "POST",
                url: "/sendFormServlet",
                data: formData,
                dataType: "json",
                encode: true,
                async: false,
                success: function(response) {
                    var output = JSON.parse(response);
                    if (output == true) {
                            if(x < max_fields){
                                $(wrapper).append('<div class="form-center">' +
                                    '<form action="javascript:void(null);"> ' +
                                    '<td style="vertical-align:top">' + x + '</td><br><br>' +
                                    '<label for="akey2">API Key:</label><br>' +
                                    '<input disabled type="text" id="akey2" name="akey2" value='+ loadString() +'>' +
                                    '<label for="pair">Pair:</label><br>' +
                                    '<input disabled type="text" id="pair" name="pair" value="XBTUSD"><br>' +
                                    '<input hidden disabled type="text" id="delete" name="delete" value=' + x + '>' +
                                    '<input class="remove_field" type="submit" id="stop" name="stop" value="Stop">' +
                                    '</form></div>')
                            }
                            x++;
                    } else {
                        alert("InCorrect ApiKey or ApiSecret. Please try again.");
                    }
                }
            }).done(function (data) {
                console.log(data);
            });

        });

        $(wrapper).on("click", ".remove_field", function () {
            var formData = {
                delete:  $(this).prev().val(),
            };

            $.ajax({
                type: "POST",
                url: "/stopBotServlet",
                data: formData,
                dataType: "json",
                encode: true,
                async: false
            }).done(function (data) {
                console.log(data);
            });
            y--;
        });

        $(wrapper).on("click",".remove_field", function(){
            $(this).parent('form').remove(); x--;
        })
    });
</script>
</body>
</html>
