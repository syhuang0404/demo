package com.example.demo.service;

import com.example.demo.model.entity.Coin;

import java.time.Instant;
import java.util.List;

public interface DataService {

    void insertCoin(Coin coin);

    void updateCoinById(String code, String name, Instant updatedTime, Integer id);

    void deleteCoinById(Integer id);

    List<Coin> selectCoins();

    Coin selectCoinById(Integer id);

    String selectCoinNameByCode(String code);

}
