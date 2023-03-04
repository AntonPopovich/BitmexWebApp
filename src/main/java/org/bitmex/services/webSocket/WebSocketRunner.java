package org.example.framework.services.webSocket;

import org.example.framework.model.constants.URL.WsTopic;
import org.example.framework.services.Signature;

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
//            WebSocket webSocket = new WebSocket(UtilURL.getTestnetWebSocketURI());
//            webSocket.connect();

//        Signature sg = new Signature(apiSecret);
//        String auth = "{\"op\": \"authKeyExpires\", \"args\": [\"" + apiKey + "\", " + sg.getExpires() + ", \""
//                + sg.getSignature() + "\"]}";
////        String subscribe = "{\"op\": \"subscribe\", \"args\": [\"position\"]}"; //lastPrice
//            System.out.println(subscribe);
//        webSocket.send_message(auth);
        authorize();
        Thread.sleep(100);
        webSocket.send_message(subscribe);

        StringBuilder stringBuilder = new StringBuilder();
//        Thread.sleep(100);

        while (running) {
            if (webSocket.getSession().isOpen()) {
                StringBuilder sb;
                sb = webSocket.getOutput();
                if (stringBuilder.compareTo(sb) != 0) {
                    stringBuilder.replace(0, sb.length(), sb.toString());
                    writeData.add(stringBuilder.toString());
                    System.out.println(stringBuilder);
                }
            } else {
                authorize();
                Thread.sleep(100);
                webSocket.send_message(subscribe);
            }
        }

        } catch (URISyntaxException | IOException | DeploymentException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void stop() {
        setRunning(false);
    }
}
//{"table":"instrument","action":"update","data":[{"symbol":"XBTUSD","impactBidPrice":17006.1664,"impactMidPrice":17043,"impactAskPrice":17079.6235,"timestamp":"2022-12-12T19:56:25.000Z"}]}Z","indicativeSettlePrice":17043.72}]}listedSettle":"timestamp","relistInterval":"timespan","inverseLeg":"symbol","sellLeg":"symbol","buyLeg":"symbol","optionStrikePcnt":"float","optionStrikeRound":"float","optionStrikePrice":"float","optionMultiplier":"float","positionCurrency":"symbol","underlying":"symbol","quoteCurrency":"symbol","underlyingSymbol":"symbol","reference":"symbol","referenceSymbol":"symbol","calcInterval":"timespan","publishInterval":"timespan","publishTime":"timespan","maxOrderQty":"long","maxPrice":"float","lotSize":"long","tickSize":"float","multiplier":"long","settlCurrency":"symbol","underlyingToPositionMultiplier":"long","underlyingToSettleMultiplier":"long","quoteToSettleMultiplier":"long","isQuanto":"boolean","isInverse":"boolean","initMargin":"float","maintMargin":"float","riskLimit":"long","riskStep":"long","limit":"float","capped":"boolean","taxed":"boolean","deleverage":"boolean","makerFee":"float","takerFee":"float","settlementFee":"float","insuranceFee":"float","fundingBaseSymbol":"symbol","fundingQuoteSymbol":"symbol","fundingPremiumSymbol":"symbol","fundingTimestamp":"timestamp","fundingInterval":"timespan","fundingRate":"float","indicativeFundingRate":"float","rebalanceTimestamp":"timestamp","rebalanceInterval":"timespan","openingTimestamp":"timestamp","closingTimestamp":"timestamp","sessionInterval":"timespan","prevClosePrice":"float","limitDownPrice":"float","limitUpPrice":"float","bankruptLimitDownPrice":"float","bankruptLimitUpPrice":"float","prevTotalVolume":"long","totalVolume":"long","volume":"long","volume24h":"long","prevTotalTurnover":"long","totalTurnover":"long","turnover":"long","turnover24h":"long","homeNotional24h":"float","foreignNotional24h":"float","prevPrice24h":"float","vwap":"float","highPrice":"float","lowPrice":"float","lastPrice":"float","lastPriceProtected":"float","lastTickDirection":"symbol","lastChangePcnt":"float","bidPrice":"float","midPrice":"float","askPrice":"float","impactBidPrice":"float","impactMidPrice":"float","impactAskPrice":"float","hasLiquidity":"boolean","openInterest":"long","openValue":"long","fairMethod":"symbol","fairBasisRate":"float","fairBasis":"float","fairPrice":"float","markMethod":"symbol","markPrice":"float","indicativeTaxRate":"float","indicativeSettlePrice":"float","optionUnderlyingPrice":"float","settledPriceAdjustmentRate":"float","settledPrice":"float","timestamp":"timestamp"},"filter":{"symbol":"XBTUSD"},"data":[{"symbol":"XBTUSD","rootSymbol":"XBT","state":"Open","typ":"FFWCSX","listing":"2016-05-04T12:00:00.000Z","front":"2016-05-04T12:00:00.000Z","expiry":null,"settle":null,"listedSettle":null,"relistInterval":null,"inverseLeg":"","sellLeg":"","buyLeg":"","optionStrikePcnt":null,"optionStrikeRound":null,"optionStrikePrice":null,"optionMultiplier":null,"positionCurrency":"USD","underlying":"XBT","quoteCurrency":"USD","underlyingSymbol":"XBT=","reference":"BMEX","referenceSymbol":".BXBT","calcInterval":null,"publishInterval":null,"publishTime":null,"maxOrderQty":10000000,"maxPrice":1000000,"lotSize":100,"tickSize":0.5,"multiplier":-100000000,"settlCurrency":"XBt","underlyingToPositionMultiplier":null,"underlyingToSettleMultiplier":-100000000,"quoteToSettleMultiplier":null,"isQuanto":false,"isInverse":true,"initMargin":0.01,"maintMargin":0.0035,"riskLimit":20000000000,"riskStep":15000000000,"limit":null,"capped":false,"taxed":true,"deleverage":true,"makerFee":-0.0001,"takerFee":0.00075,"settlementFee":0,"insuranceFee":0,"fundingBaseSymbol":".XBTBON8H","fundingQuoteSymbol":".USDBON8H","fundingPremiumSymbol":".XBTUSDPI8H","fundingTimestamp":"2022-12-12T20:00:00.000Z","fundingInterval":"2000-01-01T08:00:00.000Z","fundingRate":0.0001,"indicativeFundingRate":0.0001,"rebalanceTimestamp":null,"rebalanceInterval":null,"openingTimestamp":"2022-12-12T19:00:00.000Z","closingTimestamp":"2022-12-12T20:00:00.000Z","sessionInterval":"2000-01-01T01:00:00.000Z","prevClosePrice":16989.68,"limitDownPrice":null,"limitUpPrice":null,"bankruptLimitDownPrice":null,"bankruptLimitUpPrice":null,"prevTotalVolume":148294448530,"totalVolume":148294450730,"volume":2200,"volume24h":250400,"prevTotalTurnover":1956883991985632,"totalTurnover":1956884004904391,"turnover":12918759,"turnover24h":1471596293,"homeNotional24h":14.71596293,"foreignNotional24h":250400,"prevPrice24h":17192,"vwap":17015.542,"highPrice":17259.5,"lowPrice":16869.5,"lastPrice":17027,"lastPriceProtected":17027,"lastTickDirection":"ZeroPlusTick","lastChangePcnt":-0.0096,"bidPrice":17032,"midPrice":17034.25,"askPrice":17036.5,"impactBidPrice":17006.1664,"impactMidPrice":17043,"impactAskPrice":17079.6235,"hasLiquidity":false,"openInterest":58217000,"openValue":341584172310,"fairMethod":"FundingRate","fairBasisRate":0.1095,"fairBasis":0.01,"fairPrice":17043.25,"markMethod":"FairPrice","markPrice":17043.25,"indicativeTaxRate":0,"indicativeSettlePrice":17043.24,"optionUnderlyingPrice":null,"settledPriceAdjustmentRate":null,"settledPrice":null,"timestamp":"2022-12-12T19:56:20.000Z"}]}

// {"success":true,"request":{"op":"authKeyExpires","args":["qSMU1A_dtkZPaHpJk1GM7ulA",1677709466,"f93443f278d9f7f2c3e9183f3815480567e25ddfe97f1eacd003c7d648a6c752"]}}false,"limit":{"remaining":179}}
//{"table":"order","action":"partial","keys":["orderID"],"types":{"orderID":"guid","clOrdID":"string","clOrdLinkID":"string","account":"long","symbol":"symbol","side":"symbol","orderQty":"long","price":"float","displayQty":"long","stopPx":"float","pegOffsetValue":"float","pegPriceType":"symbol","currency":"symbol","settlCurrency":"symbol","ordType":"symbol","timeInForce":"symbol","execInst":"symbol","contingencyType":"symbol","ordStatus":"symbol","triggered":"symbol","workingIndicator":"boolean","ordRejReason":"string","leavesQty":"long","cumQty":"long","avgPx":"float","text":"string","transactTime":"timestamp","timestamp":"timestamp"},"filter":{"account":406889},"data":[]}

//{"success":true,"request":{"op":"authKeyExpires","args":["qSMU1A_dtkZPaHpJk1GM7ulA",1677709526,"8a4a5b92ab8c647854d2afee57bb9feeeb8aceef34c2d9544677c383516706e2"]}}false,"limit":{"remaining":179}}ce":"float","displayQty":"long","stopPx":"float","pegOffsetValue":"float","pegPriceType":"symbol","currency":"symbol","settlCurrency":"symbol","ordType":"symbol","timeInForce":"symbol","execInst":"symbol","contingencyType":"symbol","ordStatus":"symbol","triggered":"symbol","workingIndicator":"boolean","ordRejReason":"string","leavesQty":"long","cumQty":"long","avgPx":"float","text":"string","transactTime":"timestamp","timestamp":"timestamp"},"filter":{"account":406889},"data":[]}
//{"table":"order","action":"partial","keys":["orderID"],"types":{"orderID":"guid","clOrdID":"string","clOrdLinkID":"string","account":"long","symbol":"symbol","side":"symbol","orderQty":"long","price":"float","displayQty":"long","stopPx":"float","pegOffsetValue":"float","pegPriceType":"symbol","currency":"symbol","settlCurrency":"symbol","ordType":"symbol","timeInForce":"symbol","execInst":"symbol","contingencyType":"symbol","ordStatus":"symbol","triggered":"symbol","workingIndicator":"boolean","ordRejReason":"string","leavesQty":"long","cumQty":"long","avgPx":"float","text":"string","transactTime":"timestamp","timestamp":"timestamp"},"filter":{"account":406889},"data":[]}