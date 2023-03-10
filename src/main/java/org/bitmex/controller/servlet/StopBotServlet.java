package org.bitmex.controller.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.bitmex.controller.servlet.SendFormServlet.extractField;

@WebServlet("/stopBotServlet")
public class StopBotServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(SendFormServlet.class.getName());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        int activeBotId = Integer.parseInt(request.getParameter("delete"));

        HttpSession currentSession = request.getSession();
        ActiveBots activeBots = extractField(currentSession);
        activeBots.getActiveBots().get(activeBotId).turnOff();
        activeBots.getActiveBots().remove(activeBotId);
        currentSession.setAttribute("activeBots", activeBots);
        log.info("Остановлен бот {}(работают {})", activeBotId, activeBots.getActiveBots().size());
    }
}
