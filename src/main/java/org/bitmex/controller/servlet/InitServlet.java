package org.bitmex.controller.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "InitServlet", value = "/start")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession(true);
        ActiveBots activeBots = new ActiveBots();
        currentSession.setAttribute("activeBots", activeBots);
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}