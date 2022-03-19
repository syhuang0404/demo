package com.example.demo.model.entity;

public class CoinInfo {
    private String code;
    private String name;
    private float rateFloat;
    private String updatedTime;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRateFloat(float rateFloat) {
        this.rateFloat = rateFloat;
    }

    public float getRateFloat() {
        return rateFloat;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }
}
