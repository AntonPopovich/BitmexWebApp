package org.bitmex.controller.servlet;

import java.util.ArrayList;
import java.util.List;

public class ActiveBots {
    private List<Thread> activeThreads = new ArrayList<Thread>();

    public ActiveBots() {
    }

    public List<Thread> getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(List<Thread> activeThreads) {
        this.activeThreads = activeThreads;
    }
}
