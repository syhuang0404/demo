package com.example.demo.service.impl;

import com.example.demo.model.dao.CoinDao;
import com.example.demo.model.entity.Coin;
import com.example.demo.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private CoinDao coinDao;

    @Override
    public void insertCoin(Coin coin) {
        coinDao.save(coin);
    }

    @Override
    public void updateCoinById(String code, String name, Instant updatedTime, Integer id) {
        coinDao.updateCoinById(code, name, updatedTime, id);
    }

    @Override
    public void deleteCoinById(Integer id) {
        coinDao.deleteById(id);
    }

    @Override
    public List<Coin> selectCoins() {
        return coinDao.findAll();
    }

    @Override
    public Coin selectCoinById(Integer id) {
        return coinDao.selectCoinById(id);
    }

    @Override
    public String selectCoinNameByCode(String code) {
        return coinDao.selectCoinNameByCode(code);
    }
}
