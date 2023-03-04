package org.example.framework.model;

import org.example.framework.constants.*;
import org.example.framework.model.constants.Algo;
import org.example.framework.model.constants.Symbol;
import org.example.framework.services.tradingAlgorithm.TradingAlgoOne;
import org.example.framework.services.tradingAlgorithm.TradingAlgoThree;
import org.example.framework.services.tradingAlgorithm.TradingAlgoTwo;
import org.example.framework.services.tradingAlgorithm.TradingAlgorithm;

public class Bot implements Runnable{
    private Stock stock;
    private TradingAlgorithm algorithm;
    private String apiKey;
    private String apiSecret;
    private Double step;
    private short level;
    private Double coefficient;
    private Symbol pair = Symbol.XBTUSD;
    private Algo algo = Algo.ALGO_TWO;

    @Override
    public void run() {
        initializeStock();
        initializeAlgo(algo);

        if(stock.authenticate()) {
            algorithm.startAlgo();
        } else {
            System.out.println("Authentication failed. Please check apiKey and apiSecret.");;
        }
    }

    public void stop() {
        algorithm.turnOff();
    }

    private void initializeStock() {
        this.stock = new Stock(apiKey, apiSecret, pair);
    }

    private void initializeAlgo(Algo algo) {
        switch (algo) {
            case ALGO_ONE:
                algorithm = new TradingAlgoOne(step, level, coefficient, stock);
                break;
            case ALGO_TWO:
                algorithm = new TradingAlgoTwo(step, level, coefficient, stock);
                break;
            case ALGO_THREE:
                algorithm = new TradingAlgoThree();
                break;
        }
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public TradingAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(TradingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public Double getStep() {
        return step;
    }

    public void setStep(Double step) {
        this.step = step;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Symbol getPair() {
        return pair;
    }

    public void setPair(Symbol pair) {
        this.pair = pair;
    }

    public static class Builder {
        private Bot newBot;

        public Builder() {
            newBot = new Bot();
        }

        public Builder addStock(Stock stock) {
            newBot.stock = stock;
            return this;
        }

        public Builder addTradingAlgorithm(Algo algo) {
            newBot.algo = algo;
            return this;
        }

        public Builder addApiKey(String apiKey) {
            newBot.apiKey = apiKey;
            return this;
        }

        public Builder addApiSecret(String apiSecret) {
            newBot.apiSecret = apiSecret;
            return this;
        }

        public Builder addStep(Double step) {
            newBot.step = step;
            return this;
        }

        public Builder addLevel(short level) {
            newBot.level = level;
            return this;
        }

        public Builder addCoefficient(Double coefficient) {
            newBot.coefficient = coefficient;
            return this;
        }

        public Builder addCurrencyPair(Symbol pair) {
            newBot.pair = pair;
            return this;
        }

        public Bot build() {
            return newBot;
        }
    }
}
