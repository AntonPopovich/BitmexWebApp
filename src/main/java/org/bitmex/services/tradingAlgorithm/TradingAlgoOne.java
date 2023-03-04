package org.example.framework.services.tradingAlgorithm;

import org.example.framework.services.webSocket.WebSocketRunner;
import org.example.framework.model.Stock;
import org.example.framework.model.constants.OrderSide;
import org.example.framework.model.constants.URL.WsTopic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradingAlgoOne implements TradingAlgorithm, Runnable{
    private String apiKey;
    private String apiSecret;
    private Double step;
    private short level;
    private Double coefficient;
    private Stock stock;
    private Double baseMarkPrice;
    protected List<String> updatedOrdersInfo = new CopyOnWriteArrayList<String>();
    protected List<String> markPriceLive = new CopyOnWriteArrayList<String>();

    public TradingAlgoOne(Double step, short level, Double coefficient, Stock stock) {
        this.step = step;
        this.level = level;
        this.coefficient = coefficient;
        this.stock = stock;
        apiKey = stock.getApiKey();
        apiSecret =stock.getApiSecret();
    }

    public void startAlgo() {
        baseMarkPrice = stock.getMarketPrice();
        WebSocketRunner wsrMarkPrice = new WebSocketRunner(apiKey, apiSecret, WsTopic.POSITION, markPriceLive, stock.getWebSocketUri());
        Thread threadMark = new Thread(wsrMarkPrice);
        threadMark.start();
        boolean wsOrderPermission = true;


        int completedLevels = 0;
        double orderPrice = Math.round(baseMarkPrice - (step * (completedLevels + 1)));

        while (completedLevels < level) {
            if (!markPriceLive.isEmpty()) {
                Optional<Double> value = stock.getMarketPriceLive(markPriceLive);
                if (value.isPresent()) {
                    double mPrice = value.get();
                    System.out.print("mPrice " + mPrice);
                    System.out.println("  orderPrice: " + orderPrice);
                    if (mPrice <= orderPrice) {
                        int numberOfOrders = (int) ((orderPrice - mPrice) / step);
                        for (int i = 0; i <= numberOfOrders; i++) {
                            stock.sendOrder(OrderSide.BUY, orderPrice, coefficient);
                            orderPrice = orderPrice - (step);
                        }
                        completedLevels = completedLevels + (numberOfOrders + 1);
                        if (wsOrderPermission) {
                            // запустить поток на ордера
                                WebSocketRunner wsrUpOrders = new WebSocketRunner(apiKey, apiSecret, WsTopic.ORDER, updatedOrdersInfo, stock.getWebSocketUri());
                                Thread thread = new Thread(wsrUpOrders);
                                thread.start();

                                TradingAlgoOne counterOrders = new TradingAlgoOne(step, level, coefficient, stock);
                                Thread threadCounterOrders = new Thread(counterOrders);
                                threadCounterOrders.start();
                            System.out.println("запустили поток на ордера");
                            wsOrderPermission = false;
                        }
                    }
                }
            }
        }
        wsrMarkPrice.stop();
        threadMark.interrupt();

            // else передать сообщение на сервлет о невалидности ключей

//        WebSocketRunner wsr = new WebSocketRunner(stock.getApiKey(), stock.getApiSecret());
//        wsr.run();
//
//        while (true) {
//           if (updatedOrdersInfo.size() > 0) {
//               String id;
//               String price;
//               String side;
//               try {
//                   JSONObject jsObjSocketData = new JSONObject(updatedOrdersInfo.get(0));
//                   JSONArray jsonArray = jsObjSocketData.getJSONArray("data");
//                   JSONObject param = jsonArray.getJSONObject(0);
//                   id = param.getString("orderID");
//                   price = param.getString("price");
//                   side = param.getString("side");
//               } catch (JSONException js) {
//                       throw new RuntimeException(js + " В список updatedOrdersInfo попала строка которую нельзя преобразовать в JSON файл");
//                   }
//               if (side.equalsIgnoreCase("buy")) {
//                   stock.sendOrder(pair, OrderSide.SELL, Double.parseDouble(price) + step, coefficient);
//               } else {
//                   stock.sendOrder(pair, OrderSide.BUY, Double.parseDouble(price) - step, coefficient);
//               }
//               updatedOrdersInfo.remove(0);
//           }
//        }
    }

    @Override
    public void turnOff() {

    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!updatedOrdersInfo.isEmpty()) {
                    String lastElement = updatedOrdersInfo.get(updatedOrdersInfo.size() - 1);
                    updatedOrdersInfo.clear();
                    JSONObject jsObjSocketData = new JSONObject(lastElement);
                    JSONArray jsonArray = jsObjSocketData.getJSONArray("data");
                    JSONObject param = jsonArray.getJSONObject(0);
                    String ordStatus = param.getString("ordStatus");
                    String price = param.getString("price");
                    double numericPrice = Double.parseDouble(price);
                    String side = param.getString("side");
//            System.out.println(ordStatus + " " + price + " " + side);
                    if (ordStatus.equals("Filled")) {
                        OrderSide inversedSide;
                        if (side.equalsIgnoreCase("Buy")) {
                            inversedSide = OrderSide.SELL;
                            numericPrice = numericPrice + step;
                        } else {
                            inversedSide = OrderSide.BUY;
                            numericPrice = numericPrice - step;
                        }
                        stock.sendOrder(inversedSide, numericPrice, coefficient);
                        System.out.println("Выставлен контрордер: ");
                    }
                }
            } catch (JSONException ignored) {}
        }
    }

    // получить currentPrice (Http-запрос)
    // разместить ордеры (Post) в количестве level (Http-запрос) (нет проверки на баланс)
    // Get-запрос на каждый orderId и положить в список List

    //запуск WebSocket, подписка на "order" // Live updates on your orders
    //доставать из них orderId, price, side
    //делать Post-запрос на продажу/покупку
}


//            while (completedLevels < level) {
//                try {
//                    if (!markPriceLive.isEmpty()) {
//                        String lastElement = markPriceLive.get(markPriceLive.size() - 1);
//                        JSONObject jsObjSocketData = new JSONObject(lastElement);
//                        JSONArray jsonArray = jsObjSocketData.getJSONArray("data");
//                        JSONObject param = jsonArray.getJSONObject(0);
//                        String price = param.getString("markPrice");
//                        double mPrice = Double.parseDouble(price);
//                        System.out.print(mPrice + " markPrice   ");
//                        markPriceLive.clear();
//
//                            System.out.print(mPrice + " markPrice   ");
//                        System.out.println(orderPrice + " orderPrice");
//                        if (mPrice <= orderPrice) {
//                            int numberOfOrders = (int) ((orderPrice - mPrice) / step);
//                            System.out.println("numberOfOrders: " + numberOfOrders);
//                            for (int i = 0; i <= numberOfOrders; i++) {
//                                System.out.println("цикл - " + i);
//                                stock.sendOrder(OrderSide.BUY, orderPrice, coefficient);
//                                orderPrice = orderPrice - (step);
//                            }
//                            completedLevels = completedLevels + (numberOfOrders + 1);
//                            System.out.println("completedLevels: " + completedLevels);
//                            if (wsOrderPermission) {
//                                // запустить поток на ордера
//                                System.out.println("запустили поток на ордера");
//                                wsOrderPermission = false;
//                            }
//                        }
//                    }
//                    }
//                } catch (JSONException ignored) {}
//            }
//        wsrMarkPrice.stop();
//        threadMark.interrupt();