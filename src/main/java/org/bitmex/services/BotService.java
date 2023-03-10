package org.bitmex.services;

import org.bitmex.model.Bot;
import org.bitmex.model.ConcurrentBot;
import org.bitmex.model.Stock;
import org.bitmex.model.constants.Symbol;
import org.bitmex.model.constants.URL.WsTopic;
import org.bitmex.services.webSocket.WebSocketRunner;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class BotService { //Только для проверки backend

  public static void main(String[] args) throws DeploymentException, URISyntaxException, IOException, InterruptedException {
//    String apiKey = "";
//    String apiSecret = "";
//    Double coefficient = 100.0;
//    Short level = 2;
//    Double step = 50.0;
//
//    Bot bot = new Bot.Builder()
//            .addApiKey(apiKey)
//            .addApiSecret(apiSecret)
//            .addCoefficient(coefficient)
//            .addLevel(level)
//            .addStep(step)
//            .build();
//
//    Thread concBot = new Thread(bot);
//    ConcurrentBot concurrentBot = new ConcurrentBot(bot, concBot);
//
//
//    Stock stock = new Stock(apiKey, apiSecret, Symbol.XBTUSD);
//    WebSocketRunner wsrUpOrders = new WebSocketRunner(apiKey, apiSecret, WsTopic.ORDER, markPriceLive, stock.getWebSocketUri());
//    Thread thread = new Thread(wsrUpOrders);
//    thread.start();
//    Thread.sleep(6000);
//    wsrUpOrders.setRunning(false);
//    thread.interrupt();

//    bot.start();
//    bot.stop();


//    stock.sendOrder(OrderSide.BUY, 23183.5, coefficient);
//    System.out.println(stock.getMarketPrice());


    //WebSocket
//    WebSocketRunner wsrUpOrders = new WebSocketRunner(apiKey, apiSecret, WsTopic.ORDER, updatedOrdersInfo, stock.getWebSocketUri());
////    wsr.runWS();
//      Thread thread = new Thread(wsrUpOrders);
//      thread.start();
  }
}