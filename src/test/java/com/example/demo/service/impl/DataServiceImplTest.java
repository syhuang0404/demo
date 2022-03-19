package com.example.demo.service.impl;

import com.example.demo.model.dao.CoinDao;
import com.example.demo.model.entity.Coin;
import com.example.demo.service.DataService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
class DataServiceImplTest {
    @InjectMocks
    private DataService dataService = new DataServiceImpl();

    @Mock
    private CoinDao coinDao;

    @Test
    void shouldReturnInsertCoinSuccess() {
        Coin coin = new Coin();
        dataService.insertCoin(coin);
        verify(coinDao, times(1)).save(coin);
    }

    @Test
    void shouldReturnUpdateCoinByIdSuccess() {
        Instant updatedTime = Instant.now();
        dataService.updateCoinById("code", "name", updatedTime, 1);
        verify(coinDao, times(1)).updateCoinById("code", "name", updatedTime, 1);
    }

    @Test
    void shouldReturnDeleteCoinByIdSuccess() {
        dataService.deleteCoinById(1);
        verify(coinDao, times(1)).deleteById(1);
    }

    @Test
    void shouldReturnSelectCoinsSuccess() {
        List<Coin> expectedResult = new ArrayList<>();
        Coin twd = new Coin();
        twd.setId(1);
        twd.setCode("TWD");
        twd.setName("新臺幣");
        twd.setUpdatedTime(Instant.now());
        expectedResult.add(twd);

        // Mock
        Mockito.when(coinDao.findAll()).thenReturn(expectedResult);

        List<Coin> result = dataService.selectCoins();
        Assert.assertEquals("shouldReturnSelectCoinsSuccess Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnSelectCoinByIdSuccess() {
        Coin twd = new Coin();
        twd.setId(1);
        twd.setCode("TWD");
        twd.setName("新臺幣");
        twd.setUpdatedTime(Instant.now());

        // Mock
        Mockito.when(coinDao.selectCoinById(1)).thenReturn(twd);

        Coin result = dataService.selectCoinById(1);
        Assert.assertEquals("shouldReturnSelectCoinByIdSuccess Fail!", twd, result);
    }

    @Test
    void shouldReturnSelectCoinNameByCodeSuccess() {
        String expectedResult = "新臺幣";
        // Mock
        Mockito.when(coinDao.selectCoinNameByCode("TWD")).thenReturn("新臺幣");

        String result = dataService.selectCoinNameByCode("TWD");
        Assert.assertEquals("shouldReturnSelectCoinNameByCodeSuccess Fail!", expectedResult, result);
    }
}