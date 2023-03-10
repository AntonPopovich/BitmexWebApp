package org.bitmex.controller.servlet;

import org.bitmex.model.Bot;
import org.bitmex.model.ConcurrentBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/sendFormServlet")
public class SendFormServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(SendFormServlet.class.getName());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String apiKey = request.getParameter("akey");
        String apiSecret = request.getParameter("asecret");
        Double step = Double.valueOf(request.getParameter("stp"));
        short level = Short.parseShort(request.getParameter("lvl"));
        Double coefficient = Double.valueOf(request.getParameter("coef"));

        Bot bot = new Bot.Builder()
                .addApiKey(apiKey)
                .addApiSecret(apiSecret)
                .addStep(step)
                .addLevel(level)
                .addCoefficient(coefficient)
                .build();

        if (bot.initializeStock()) {
            Thread concBot = new Thread(bot);
            ConcurrentBot concurrentBot = new ConcurrentBot(bot, concBot);

            HttpSession currentSession = request.getSession();
            ActiveBots activeBots = extractField(currentSession);
            int curId = activeBots.getActiveBots().size();
            activeBots.getActiveBots().put(curId, concurrentBot);
            currentSession.setAttribute("activeBots", activeBots);

            concurrentBot.turnOn();
            log.info("Запуск потока с данными: {}, {}, {}, {}, {}", apiKey, apiSecret, step, level, coefficient);
            response.getWriter().write("true");
        } else response.getWriter().write("false");
    }

    public static ActiveBots extractField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute("activeBots");
        if (ActiveBots.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, try one more time" + fieldAttribute.getClass());
        }
        return (ActiveBots) fieldAttribute;
    }
}
