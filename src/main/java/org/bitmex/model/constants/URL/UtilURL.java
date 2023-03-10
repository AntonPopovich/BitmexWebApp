package org.bitmex.model.constants.URL;

public class UtilURL {
  public static final String PROTOCOL = "https://";
  public static final String TESTNET = "testnet.";
  public static final String REALNET = "www.";
  public static final String BASE_URL = "bitmex.com";
  public static final String API_PATH = "/api/v1";
  public static final String WEBSOCKET = "wss://";
  public static final String REALTIME = "/realtime";
  public static String getTestnetWebSocketURI() {
    return WEBSOCKET+TESTNET+BASE_URL+REALTIME;
  }
}
