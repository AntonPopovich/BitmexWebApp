package org.example.framework.services;

import org.example.framework.services.webSocket.WebSocketRunner;
import org.example.framework.model.Bot;
import org.example.framework.model.ConcurrentBot;
import org.example.framework.model.Stock;
import org.example.framework.model.constants.Symbol;
import org.example.framework.model.constants.URL.WsTopic;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class BotService {
    public static List<String> updatedOrdersInfo = new CopyOnWriteArrayList<>();
  protected static List<String> markPriceLive = new CopyOnWriteArrayList<String>();
  public static void main(String[] args) throws DeploymentException, URISyntaxException, IOException, InterruptedException {
    String apiKey = "qSMU1A_dtkZPaHpJk1GM7ulA";
    String apiSecret = "gydFxkSa6-vYL-TnhELzul8AhtrXA3CFmyJHPcNPXFfG3KQ5";
    Double coefficient = 100.0;
    Short level = 2;
    Double step = 50.0;

    Bot bot = new Bot.Builder()
            .addApiKey(apiKey)
            .addApiSecret(apiSecret)
            .addCoefficient(coefficient)
            .addLevel(level)
            .addStep(step)
            .build();

    Thread concBot = new Thread(bot);
    ConcurrentBot concurrentBot = new ConcurrentBot(bot, concBot);


    Stock stock = new Stock(apiKey, apiSecret, Symbol.XBTUSD);
    WebSocketRunner wsrUpOrders = new WebSocketRunner(apiKey, apiSecret, WsTopic.ORDER, markPriceLive, stock.getWebSocketUri());
    Thread thread = new Thread(wsrUpOrders);
    thread.start();
    Thread.sleep(6000);
    wsrUpOrders.setRunning(false);
    thread.interrupt();

//    bot.start();
    bot.stop();


//    stock.sendOrder(OrderSide.BUY, 23183.5, coefficient);
//    System.out.println(stock.getMarketPrice());


    //WebSocket
//    WebSocketRunner wsrUpOrders = new WebSocketRunner(apiKey, apiSecret, WsTopic.ORDER, updatedOrdersInfo, stock.getWebSocketUri());
////    wsr.runWS();
//      Thread thread = new Thread(wsrUpOrders);
//      thread.start();


    ////////////////////////////////////////////////////////////////
//      while (true) {
//        try {
//          if (!updatedOrdersInfo.isEmpty()) {
//            String lastElement = updatedOrdersInfo.get(updatedOrdersInfo.size() - 1);
//            updatedOrdersInfo.clear();
//            JSONObject jsObjSocketData = new JSONObject(lastElement);
//            JSONArray jsonArray = jsObjSocketData.getJSONArray("data");
//            JSONObject param = jsonArray.getJSONObject(0);
//            String ordStatus = param.getString("ordStatus");
//            String price = param.getString("price");
//            double numericPrice = Double.parseDouble(price);
//            String side = param.getString("side");
////            System.out.println(ordStatus + " " + price + " " + side);
//            if (ordStatus.equals("Filled")) {
//              OrderSide inversedSide;
//              if (side.equalsIgnoreCase("Buy")) {
//                inversedSide = OrderSide.SELL;
//                numericPrice = numericPrice + step;
//              } else {
//                inversedSide = OrderSide.BUY;
//                numericPrice = numericPrice - step;
//              }
//              stock.sendOrder(inversedSide, numericPrice, coefficient);
//            }
//          }
//        } catch (JSONException ignored) {}
//      }
      ////////////////////////////////////////////////////////////////


//
//        while (true) {
//            if(!updatedOrdersInfo.isEmpty()) {
//                System.out.println("from update" + updatedOrdersInfo.get(0));
//                updatedOrdersInfo.remove(0);
////                updatedOrdersInfo.add("sad1");
////                Thread.sleep(5000);
////                wsr.stop();
////                thread.interrupt();
//            }
//        }
//    String id;
//               String price;
//               String side;
//String wrong = "{\"table\":\"position\",\"action\":\"partial\",\"keys\":[\"account\",\"symbol\"],\"types\":{\"account\":\"long\",\"symbol\":\"symbol\",\"currency\":\"symbol\",\"underlying\":\"symbol\",\"quoteCurrency\":\"symbol\",\"commission\":\"float\",\"initMarginReq\":\"float\",\"maintMarginReq\":\"float\",\"riskLimit\":\"long\",\"leverage\":\"float\",\"crossMargin\":\"boolean\",\"deleveragePercentile\":\"float\",\"rebalancedPnl\":\"long\",\"prevRealisedPnl\":\"long\",\"prevUnrealisedPnl\":\"long\",\"openingQty\":\"long\",\"openOrderBuyQty\":\"long\",\"openOrderBuyCost\":\"long\",\"openOrderBuyPremium\":\"long\",\"openOrderSellQty\":\"long\",\"openOrderSellCost\":\"long\",\"openOrderSellPremium\":\"long\",\"currentQty\":\"long\",\"currentCost\":\"long\",\"currentComm\":\"long\",\"realisedCost\":\"long\",\"unrealisedCost\":\"long\",\"grossOpenPremium\":\"long\",\"isOpen\":\"boolean\",\"markPrice\":\"float\",\"markValue\":\"long\",\"riskValue\":\"long\",\"homeNotional\":\"float\",\"foreignNotional\":\"float\",\"posState\":\"symbol\",\"posCost\":\"long\",\"posCross\":\"long\",\"posComm\":\"long\",\"posLoss\":\"long\",\"posMargin\":\"long\",\"posMaint\":\"long\",\"initMargin\":\"long\",\"maintMargin\":\"long\",\"realisedPnl\":\"long\",\"unrealisedPnl\":\"long\",\"unrealisedPnlPcnt\":\"float\",\"unrealisedRoePcnt\":\"float\",\"avgCostPrice\":\"float\",\"avgEntryPrice\":\"float\",\"breakEvenPrice\":\"float\",\"marginCallPrice\":\"float\",\"liquidationPrice\":\"float\",\"bankruptPrice\":\"float\",\"timestamp\":\"timestamp\"},\"filter\":{\"account\":406889},\"data\":[{\"account\":406889,\"symbol\":\"XBTUSD\",\"currency\":\"XBt\",\"underlying\":\"XBT\",\"quoteCurrency\":\"USD\",\"commission\":7.5E-4,\"initMarginReq\":0.01,\"maintMarginReq\":0.0035,\"riskLimit\":20000000000,\"leverage\":100.0,\"crossMargin\":true,\"deleveragePercentile\":0.6,\"rebalancedPnl\":427770,\"prevRealisedPnl\":0,\"prevUnrealisedPnl\":0,\"openingQty\":900,\"openOrderBuyQty\":0,\"openOrderBuyCost\":0,\"openOrderBuyPremium\":0,\"openOrderSellQty\":0,\"openOrderSellCost\":0,\"openOrderSellPremium\":0,\"currentQty\":900,\"currentCost\":-3809844,\"currentComm\":385,\"realisedCost\":0,\"unrealisedCost\":-3809844,\"grossOpenPremium\":0,\"isOpen\":true,\"markPrice\":23489.73,\"markValue\":-3831462,\"riskValue\":3831462,\"homeNotional\":0.03831462,\"foreignNotional\":-900.0,\"posCost\":-4325621,\"posCross\":88912,\"posComm\":3344,\"posLoss\":67294,\"posMargin\":68219,\"posMaint\":18917,\"initMargin\":0,\"maintMargin\":46601,\"realisedPnl\":-385,\"unrealisedPnl\":-21618,\"unrealisedPnlPcnt\":-0.0057,\"unrealisedRoePcnt\":-0.5674,\"avgCostPrice\":20806.2852,\"avgEntryPrice\":23623.0,\"breakEvenPrice\":21240.5,\"marginCallPrice\":17250.5,\"liquidationPrice\":17250.5,\"bankruptPrice\":17199.5,\"timestamp\":\"2023-02-28T17:36:07.359Z\"}]}";
//
//                    JSONObject jsObjSocketData = new JSONObject(wrong);
//                   JSONArray jsonArray = jsObjSocketData.getJSONArray("data");
//                   JSONObject param = jsonArray.getJSONObject(0);
//                   id = param.getString("orderID");
//                   price = param.getString("price");
//                   side = param.getString("side");

//{"table":"position","action":"update","data":[{"account":406889,"symbol":"XBTUSD","markPrice":23491.68,"markValue":-3831147,"riskValue":3831147,"homeNotional":0.03831147,"posCross":88597,"posMargin":67904,"unrealisedPnl":-21303,"unrealisedPnlPcnt":-0.0056,"unrealisedRoePcnt":-0.5592,"timestamp":"2023-02-28T17:36:11.859Z"}]}entile":"float","rebalancedPnl":"long","prevRealisedPnl":"long","prevUnrealisedPnl":"long","openingQty":"long","openOrderBuyQty":"long","openOrderBuyCost":"long","openOrderBuyPremium":"long","openOrderSellQty":"long","openOrderSellCost":"long","openOrderSellPremium":"long","currentQty":"long","currentCost":"long","currentComm":"long","realisedCost":"long","unrealisedCost":"long","grossOpenPremium":"long","isOpen":"boolean","markPrice":"float","markValue":"long","riskValue":"long","homeNotional":"float","foreignNotional":"float","posState":"symbol","posCost":"long","posCross":"long","posComm":"long","posLoss":"long","posMargin":"long","posMaint":"long","initMargin":"long","maintMargin":"long","realisedPnl":"long","unrealisedPnl":"long","unrealisedPnlPcnt":"float","unrealisedRoePcnt":"float","avgCostPrice":"float","avgEntryPrice":"float","breakEvenPrice":"float","marginCallPrice":"float","liquidationPrice":"float","bankruptPrice":"float","timestamp":"timestamp"},"filter":{"account":406889},"data":[{"account":406889,"symbol":"XBTUSD","currency":"XBt","underlying":"XBT","quoteCurrency":"USD","commission":7.5E-4,"initMarginReq":0.01,"maintMarginReq":0.0035,"riskLimit":20000000000,"leverage":100.0,"crossMargin":true,"deleveragePercentile":0.6,"rebalancedPnl":427770,"prevRealisedPnl":0,"prevUnrealisedPnl":0,"openingQty":900,"openOrderBuyQty":0,"openOrderBuyCost":0,"openOrderBuyPremium":0,"openOrderSellQty":0,"openOrderSellCost":0,"openOrderSellPremium":0,"currentQty":900,"currentCost":-3809844,"currentComm":385,"realisedCost":0,"unrealisedCost":-3809844,"grossOpenPremium":0,"isOpen":true,"markPrice":23489.73,"markValue":-3831462,"riskValue":3831462,"homeNotional":0.03831462,"foreignNotional":-900.0,"posCost":-4325621,"posCross":88912,"posComm":3344,"posLoss":67294,"posMargin":68219,"posMaint":18917,"initMargin":0,"maintMargin":46601,"realisedPnl":-385,"unrealisedPnl":-21618,"unrealisedPnlPcnt":-0.0057,"unrealisedRoePcnt":-0.5674,"avgCostPrice":20806.2852,"avgEntryPrice":23623.0,"breakEvenPrice":21240.5,"marginCallPrice":17250.5,"liquidationPrice":17250.5,"bankruptPrice":17199.5,"timestamp":"2023-02-28T17:36:07.359Z"}]}
  }
}

//ID:	qSMU1A_dtkZPaHpJk1GM7ulA
//Secret:	gydFxkSa6-vYL-TnhELzul8AhtrXA3CFmyJHPcNPXFfG3KQ5