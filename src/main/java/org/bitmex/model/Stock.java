package org.example.framework.model;

import org.apache.http.client.utils.URIBuilder;
import org.example.framework.services.HttpClient;
import org.example.framework.model.constants.OrderSide;
import org.example.framework.model.constants.Symbol;
import org.example.framework.model.constants.URL.ResourceURL;
import org.example.framework.model.constants.URL.URL;
import org.example.framework.model.constants.URL.URLBuilder;
import org.example.framework.model.constants.URL.UtilURL;
import org.example.framework.model.order.LimitOrder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class Stock {
    private String apiKey;
    private String apiSecret;
    private Symbol pair;
    private URL marketPriceUrl;
    private URL sendOrderUrl;
    private URL authUrl;
    private String webSocketUri = UtilURL.getTestnetWebSocketURI();

    public Stock(String apiKey, String apiSecret, Symbol pair) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.pair = pair;
        marketPriceUrl = createMarketPriceUrl();
        sendOrderUrl = createSendUrl();
        authUrl = createAuthUrl();
    }

    public boolean authenticate() {
        HttpClient client = new HttpClient(apiKey, apiSecret);
        boolean response = client.authenticate(authUrl);
        return response;
    }

    public Double getMarketPrice() {
        HttpClient client = new HttpClient(apiKey, apiSecret);
        HttpResponse<String> response = client.getMarketPrice(marketPriceUrl);
        try {
            JSONArray jsonArray = new JSONArray(response.body());
            JSONObject param = jsonArray.getJSONObject(0);
            String price = param.getString("markPrice");
            return Double.parseDouble(price);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<Double> getMarketPriceLive(List<String> markPriceLive) {
        Optional<Double> mPrice = Optional.ofNullable(null);
        try {
            if (markPriceLive.size() > 0) {
                String lastElement = markPriceLive.get(markPriceLive.size() - 1);
                markPriceLive.clear();
                JSONObject jsObjSocketData = new JSONObject(lastElement);
                JSONArray jsonArray = jsObjSocketData.getJSONArray("data");
                JSONObject param = jsonArray.getJSONObject(0);
                String price = param.getString("markPrice");
                mPrice = Optional.of(Double.parseDouble(price));
            }
        } catch (JSONException ignored) {}
        return mPrice;
    }

    public HttpResponse<String> sendOrder(OrderSide side, Double price, Double qty) {
        LimitOrder order = new LimitOrder(pair, side, price, qty);
        HttpClient client = new HttpClient(apiKey, apiSecret);

        HttpResponse<String> response = client.sendOrder(order, sendOrderUrl);
        System.out.println(response.body());
        return response;
    }

    public String getOrderId(HttpResponse<String> response) {
        try {
        JSONObject object = new JSONObject(response.body());
        String price = object.getString("orderID");
        return price;
        } catch (JSONException js) {
            throw new RuntimeException(js + " Сервер отдал некорректный JSON файл");
        }
    }
    private URL createMarketPriceUrl() {
        URI uri = null;
        try {
            uri = new URIBuilder()
                    .addParameter("filter", "{\"symbol\": \"XBTUSD\"}")
                    .addParameter("columns", "[\"markPrice\"]")
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        URL url = new URLBuilder() // генерация url
                .protocol(UtilURL.PROTOCOL)
                .net(UtilURL.TESTNET)
                .baseUrl(UtilURL.BASE_URL)
                .apiPath(UtilURL.API_PATH)
                .resourcePath(ResourceURL.POSITION + uri)
                .build();
        return url;
    }

    private  URL createSendUrl() {
        URL url = new URLBuilder() // генерация url
                .protocol(UtilURL.PROTOCOL)
                .net(UtilURL.TESTNET)
                .baseUrl(UtilURL.BASE_URL)
                .apiPath(UtilURL.API_PATH)
                .resourcePath(ResourceURL.ORDER)
                .build();
        return url;
    }

    private  URL createAuthUrl() {
        String ordersInfoParam = "{\"open\": \"true\"}"; //To get open orders only, send {"open": true} in the filter param.
        String encodeParam = "?filter=" + URLEncoder.encode(ordersInfoParam, StandardCharsets.UTF_8);

        URL url = new URLBuilder() // генерация url
                .protocol(UtilURL.PROTOCOL)
                .net(UtilURL.TESTNET)
                .baseUrl(UtilURL.BASE_URL)
                .apiPath(UtilURL.API_PATH)
                .resourcePath(ResourceURL.ORDER + encodeParam)
                .build();
        return url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getWebSocketUri() {
        return webSocketUri;
    }

}
