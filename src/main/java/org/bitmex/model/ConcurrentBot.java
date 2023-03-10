package org.bitmex.model;

import org.bitmex.controller.servlet.SendFormServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcurrentBot {
    private Bot bot;
    private Thread botThread;
    private static final Logger log = LoggerFactory.getLogger(SendFormServlet.class.getName());

    public ConcurrentBot(Bot bot, Thread botThread) {
        this.bot = bot;
        this.botThread = botThread;
    }

    public void turnOn() {
        botThread.start();
    }

    public void turnOff() {
        bot.stop();
        botThread.interrupt();
        log.info("Бот остановлен. botThread.isAlive()/isInterrupted() - {}, {}", botThread.isAlive(), botThread.isInterrupted());
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public Thread getBotThread() {
        return botThread;
    }

    public void setBotThread(Thread botThread) {
        this.botThread = botThread;
    }
}
