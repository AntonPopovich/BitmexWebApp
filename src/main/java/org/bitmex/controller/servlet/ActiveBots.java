package org.bitmex.controller.servlet;

import org.bitmex.model.ConcurrentBot;

import java.util.HashMap;
import java.util.Map;

public class ActiveBots {
    private Map<Integer, ConcurrentBot> activeBots = new HashMap<>();

    public ActiveBots() {
    }

    public Map<Integer, ConcurrentBot> getActiveBots() {
        return activeBots;
    }

    public String getBotApiKey(int index) {
        return getActiveBots().get(index).getBot().getApiKey();
    }
}
