package org.example.framework.services.tradingAlgorithm;

import org.example.framework.services.webSocket.WebSocketRunner;
import org.example.framework.model.Stock;
import org.example.framework.model.constants.OrderSide;
import org.example.framework.model.constants.URL.WsTopic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradingAlgoTwo implements TradingAlgorithm {
    private final String apiKey;
    private final String apiSecret;
    private final Double step;
    private final short level;
    private final Double coefficient;
    private final Stock stock;
    protected List<String> updatedOrdersInfo = new CopyOnWriteArrayList<>();
    private List<String> ordersId = new ArrayList<>();
    private boolean isOff = false;

    public TradingAlgoTwo(Double step, short level, Double coefficient, Stock stock) {
        this.step = step;
        this.level = level;
        this.coefficient = coefficient;
        this.stock = stock;
        apiKey = stock.getApiKey();
        apiSecret =stock.getApiSecret();
    }

    public void startAlgo() {
        WebSocketRunner wsrUpOrders = new WebSocketRunner(apiKey, apiSecret, WsTopic.ORDER, updatedOrdersInfo, stock.getWebSocketUri());
        Thread threadWsr = new Thread(wsrUpOrders);
        threadWsr.start();

        Double baseMarkPrice = stock.getMarketPrice();
        System.out.println(baseMarkPrice);
        while (updatedOrdersInfo.size() == 0) {}
        for (int x = 0; x < level; x++) {
            double orderPrice = Math.round(baseMarkPrice - (step * (x + 1)));
            HttpResponse<String> response = stock.sendOrder(OrderSide.BUY, orderPrice, coefficient);
            ordersId.add(stock.getOrderId(response));
            System.out.println(orderPrice);
        }

        while (!isOff) {
            try {
                if (!updatedOrdersInfo.isEmpty()) {
                    String lastElement = updatedOrdersInfo.get(updatedOrdersInfo.size() - 1);
                    updatedOrdersInfo.remove(lastElement);
                    JSONObject jsObjSocketData = new JSONObject(lastElement);
                    JSONArray jsonArray = jsObjSocketData.getJSONArray("data");
                    JSONObject param = jsonArray.getJSONObject(0);
                    String orderID = param.getString("orderID");
                    String ordStatus = param.getString("ordStatus");
                    String price = param.getString("price");
                    double numericPrice = Double.parseDouble(price);
                    String side = param.getString("side");
            System.out.println(ordStatus + " " + price + " " + side);

                    if (ordStatus.equals("Filled") && ordersId.contains(orderID)) {
                        OrderSide inversedSide;
                        if (side.equalsIgnoreCase("Buy")) {
                            inversedSide = OrderSide.SELL;
                            numericPrice = numericPrice + step;
                        } else {
                            inversedSide = OrderSide.BUY;
                            numericPrice = numericPrice - step;
                        }
                        HttpResponse<String> response = stock.sendOrder(inversedSide, numericPrice, coefficient);
                        ordersId.add(stock.getOrderId(response));
                        System.out.println("Выставлен контрордер: " + numericPrice);
                    }
                }
            } catch (JSONException ignored) {}
        }
        if (isOff) {
            wsrUpOrders.setRunning(false);
            threadWsr.interrupt();
        }
    }

    public void turnOff() {
        this.isOff = true;
    }
}




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






    // получить currentPrice (Http-запрос)
    // разместить ордеры (Post) в количестве level (Http-запрос) (нет проверки на баланс)
    // Get-запрос на каждый orderId и положить в список List

    //запуск WebSocket, подписка на "order" // Live updates on your orders
    //доставать из них orderId, price, side
    //делать Post-запрос на продажу/покупку


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