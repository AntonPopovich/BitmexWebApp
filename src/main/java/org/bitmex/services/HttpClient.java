package org.example.framework.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.example.framework.model.constants.URL.URL;
import org.example.framework.model.constants.Verb;
import org.example.framework.model.order.Order;
import org.json.JSONObject;

public class HttpClient {
    private String apiKey;
    private String apiSecret;

    public HttpClient(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public HttpResponse<String> sendOrder(Order order, URL url) {

//        URL url = new URLBuilder() // генерация url
//                .protocol(UtilURL.PROTOCOL)
//                .net(UtilURL.TESTNET)
//                .baseUrl(UtilURL.BASE_URL)
//                .apiPath(UtilURL.API_PATH)
//                .resourcePath(ResourceURL.ORDER)
//                .build();

        String orderJsonStr = new JSONObject(order).toString();
        Signature sign = new Signature(url, Verb.POST.toString(), orderJsonStr, apiSecret);
        String signature = sign.getSignature();
        String expires = sign.getExpires();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(orderJsonStr))
                .header("api-signature", signature)
                .header("api-expires", expires)
                .header("api-key", apiKey)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "application/json")
                .uri(URI.create(url.toString()))
                .build();

        HttpResponse<String> response;

        try {
            response = java.net.http.HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public HttpResponse<String> getMarketPrice(URL url) {
        Signature sign = new Signature(url, Verb.GET.toString(), "", apiSecret);
        String signature = sign.getSignature();
        String expires = sign.getExpires();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("api-signature", signature)
                .header("api-expires", expires)
                .header("api-key", apiKey)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "application/json")
                .uri(URI.create(url.toString()))
                .build();

        HttpResponse<String> response;

        try {
            response = java.net.http.HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public boolean authenticate(URL url) {

        Signature sign = new Signature(url, Verb.GET.toString(), "", apiSecret);
        String signature = sign.getSignature();
        String expires = sign.getExpires();


        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("api-signature", signature)
                .header("api-expires", expires)
                .header("api-key", apiKey)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "application/json")
                .uri(URI.create(url.toString()))
                .build();

        HttpResponse<String> response;

        try {
            response = java.net.http.HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.statusCode() == 200;
    }
}
