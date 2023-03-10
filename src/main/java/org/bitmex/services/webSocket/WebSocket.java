package org.bitmex.services.webSocket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocket extends Endpoint{

    private String url;
    private StringBuilder output;
    private Session session;

    public Session getSession() {
        return session;
    }

    public WebSocket(String url) {
        super();

        this.url = url;
        this.output = new StringBuilder();
    }

    public void send_message(String message) {
        session.getAsyncRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.session.setMaxIdleTimeout(-1L);

        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                output.replace(0, message.length(), message);
            }
        });
    }

    public StringBuilder getOutput() {
        return output;
    }

    public void connect() throws URISyntaxException, DeploymentException, IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setAsyncSendTimeout(-1L);

        ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                .configurator(new ClientEndpointConfig.Configurator())
                .build();
            container.connectToServer(this, config, new URI(url));
    }

    public void onError(Session session, Throwable throwable) {
        super.onError(session, throwable);
    }
}
