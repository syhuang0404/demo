package com.example.demo.model.dao;

import com.example.demo.model.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public interface CoinDao extends JpaRepository<Coin, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE T_COIN_INFO SET CODE=:code, NAME=:name, UPDATED_TIME=:updatedTime where ID = :id", nativeQuery = true)
    void updateCoinById(String code, String name, Instant updatedTime, Integer id);

    @Query(value = "SELECT * FROM T_COIN_INFO where ID = :id", nativeQuery = true)
    Coin selectCoinById(Integer id);

    @Query(value = "SELECT NAME FROM T_COIN_INFO where CODE = :code", nativeQuery = true)
    String selectCoinNameByCode(String code);



}
