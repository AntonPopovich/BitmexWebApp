package org.bitmex.services.tradingAlgorithm;

import org.bitmex.controller.servlet.SendFormServlet;
import org.bitmex.model.Stock;
import org.bitmex.model.constants.OrderSide;
import org.bitmex.model.constants.URL.WsTopic;
import org.bitmex.services.webSocket.WebSocketRunner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(SendFormServlet.class.getName());

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
        log.info("Сейчас marketPrice: {}", baseMarkPrice);
        while (updatedOrdersInfo.size() == 0) {}
        for (int x = 0; x < level; x++) {
            double orderPrice = Math.round(baseMarkPrice - (step * (x + 1)));
            HttpResponse<String> response = stock.sendOrder(OrderSide.BUY, orderPrice, coefficient);
            ordersId.add(stock.getOrderId(response));
            log.info("Выставлен ордер по цене: {}", orderPrice);
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
                    log.info("Информация по Web Socket (реконнект) статус ордера, цена, side: {}, {}, {}, {}", ordStatus, price, side, orderID);

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
                        log.info("Выставлен контрордер: {}", numericPrice);
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