<%@ page import="org.bitmex.controller.servlet.ActiveBots" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="static org.bitmex.controller.servlet.SendFormServlet.extractField" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <meta charset="UTF-8" />
  <title>User Info</title>
</head>
<body>
<%
    int number = Integer.parseInt(request.getParameter("number"));
    HttpSession currentSession = request.getSession();

    ActiveBots activeBots = extractField(currentSession);
    String key = activeBots.getBotApiKey(number);
    PrintWriter out1 = new PrintWriter(response.getWriter());
    out1.write(key);
    out1.close();
%>
</body>
</html>
