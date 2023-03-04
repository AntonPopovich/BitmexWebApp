package org.example.framework.model.constants.URL;

public enum WsTopic {
    ORDER("{\"op\": \"subscribe\", \"args\": [\"order\"]}"),
    POSITION("{\"op\": \"subscribe\", \"args\": [\"position\"]}"),
    EXECUTION("{\"op\": \"subscribe\", \"args\": [\"execution\"]}");
    private String subscription;
    WsTopic(String subscription) {
        this.subscription = subscription;
    }
    public String getSubscription() {
        return subscription;
    }
}
