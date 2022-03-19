package com.example.demo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class HttpConnectionToolTest {
    private final static String SAMPLE_JSON = "{\"time\":{\"updated\":\"Mar 18, 2022 12:52:00 UTC\",\"updatedISO\":\"2022-03-18T12:52:00+00:00\",\"updateduk\":\"Mar 18, 2022 at 12:52 GMT\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"40,478.7417\",\"description\":\"United States Dollar\",\"rate_float\":40478.7417},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"30,831.7670\",\"description\":\"British Pound Sterling\",\"rate_float\":30831.767},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"36,723.5693\",\"description\":\"Euro\",\"rate_float\":36723.5693}}}";

    @Spy
    private HttpConnectionTool httpConnectionTool;

    @Mock
    HttpURLConnection connection;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldReturnGetCurrentPriceJsonSuccess() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(SAMPLE_JSON.getBytes());
        doReturn(connection).when(httpConnectionTool).create(Mockito.any());
        doReturn(200).when(connection).getResponseCode();
        doReturn(inputStream).when(connection).getInputStream();

        URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
        String result = httpConnectionTool.getCurrentPriceJson(url);

        Assertions.assertEquals(SAMPLE_JSON, result);
    }

    @Test
    void shouldReturnGetCurrentPriceJsonFailWhenResponseCodeError() throws IOException {
        doReturn(connection).when(httpConnectionTool).create(Mockito.any());
        doReturn(404).when(connection).getResponseCode();

        URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
        String result = httpConnectionTool.getCurrentPriceJson(url);

        Assertions.assertNull(result);
    }

}