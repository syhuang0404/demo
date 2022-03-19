package com.example.demo.model.entity;

public class CoinType {
    private String code;
    private String symbol;
    private String rate;
    private String description;
    private float rate_float;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setRate_float(float rate_float) {
        this.rate_float = rate_float;
    }

    public float getRate_float() {
        return rate_float;
    }
}
