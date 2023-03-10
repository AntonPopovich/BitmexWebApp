package org.bitmex.services.webSocket;

import org.bitmex.model.constants.URL.WsTopic;
import org.bitmex.services.Signature;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class WebSocketRunner implements Runnable{
    String apiKey;
    String apiSecret;
    String subscribe;
    List<String> writeData;
    boolean running = true;
    WebSocket webSocket;
    public WebSocketRunner(String apiKey, String apiSecret, WsTopic topic, List<String> writeData, String uri) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.subscribe = topic.getSubscription();
        this.writeData = writeData;
        this.webSocket = new WebSocket(uri);
    }

    private void authorize() throws DeploymentException, URISyntaxException, IOException {
        webSocket.connect();
        Signature sg = new Signature(apiSecret);
        String auth = "{\"op\": \"authKeyExpires\", \"args\": [\"" + apiKey + "\", " + sg.getExpires() + ", \""
                + sg.getSignature() + "\"]}";
        webSocket.send_message(auth);
    }
    @Override
    public void run() {
        try {
            authorize();
            Thread.sleep(100);
            webSocket.send_message(subscribe);

            StringBuilder stringBuilder = new StringBuilder();

            while (running) {
                if (webSocket.getSession().isOpen()) {
                    StringBuilder sb;
                    sb = webSocket.getOutput();
                    if (stringBuilder.compareTo(sb) != 0) {
                        stringBuilder.replace(0, sb.length(), sb.toString());
                        writeData.add(stringBuilder.toString());
                    }
                } else {
                    authorize();
                    Thread.sleep(100);
                    webSocket.send_message(subscribe);
                }
            }
        } catch (URISyntaxException | IOException | DeploymentException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}