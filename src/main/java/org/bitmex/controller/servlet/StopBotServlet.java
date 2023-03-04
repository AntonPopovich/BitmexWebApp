package org.bitmex.controller.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.bitmex.controller.servlet.LogicServlet.extractField;

@WebServlet("/stopBotServlet")
public class StopBotServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    HttpSession currentSession = request.getSession();
        System.out.println(request.getParameter("stop"));
        int index = Integer.parseInt(request.getParameter("stop"));
    // Получаем объект игрового поля из сессии
    ActiveBots activeBots = extractField(currentSession);
    activeBots.getActiveBots().get(index).turnOff();

    response.sendRedirect("/index.jsp");

    }
}
