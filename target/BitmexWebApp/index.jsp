<html>
<head>
    <title>BitmexWebApp</title>
    <link href="static/main.css" rel="stylesheet" type="text/css">
    <%-- объявление ниже JSP Standard Tag Library – JSTL --%>
    <%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> --%>  <%-- комментарий в jsp --%>
</head>
<body>
<h1>Welcome to Bitmex Bot!<br> (testnet)</h1>
<p> testnet </p>
<%--    <form action="/action_page.php">--%>
<form name="botDataForm" method="post", action="sendFormServlet">
    <label for="akey">API Key:</label><br>
    <input type="text" id="akey" name="akey" value="123"><br>
    <label for="asecret">API Secret:</label><br>
    <input type="text" id="asecret" name="asecret" value="123456789"><br>
    <label for="stp">Step:</label><br>
    <input type="text" id="stp" name="stp" value="200"><br>
    <label for="lvl">Level:</label><br>
    <input type="text" id="lvl" name="lvl" value="3"><br>
    <label for="coef">Coefficient:</label><br>
    <input type="text" id="coef" name="coef" value="100"><br><br>
    <input type="submit" value="Submit">
</form>
<%--<table>--%>
<%--    <tr> <td>API_KEY</td> </tr>--%>
<%--    <tr> <td>API_SECRET</td> </tr>--%>
<%--    <tr> <td>STEP</td> </tr>--%>
<%--    <tr> <td>LEVEL</td> </tr>--%>
<%--    <tr> <td>COEFFICIENT</td> </tr>--%>
<%--</table>--%>
</body>
</html>
