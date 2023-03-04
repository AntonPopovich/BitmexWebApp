package org.example.framework.model;

import org.example.framework.model.Bot;

public class ConcurrentBot {
    private Bot bot;
    private Thread botThread;

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
