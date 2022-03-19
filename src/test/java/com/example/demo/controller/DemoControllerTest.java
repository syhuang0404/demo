package com.example.demo.controller;

import com.example.demo.model.DemoResponse;
import com.example.demo.model.entity.Coin;
import com.example.demo.model.entity.CoinInfo;
import com.example.demo.service.DataService;
import com.example.demo.util.HttpConnectionTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {
    private final static String PARAM_CODE = "code";
    private final static String PARAM_NAME = "name";
    private final static String VAR_CODE = "TWD";
    private final static String VAR_NAME = "新臺幣";
    private final static String SAMPLE_JSON = "{\"time\":{\"updated\":\"Mar 18, 2022 12:52:00 UTC\",\"updatedISO\":\"2022-03-18T12:52:00+00:00\",\"updateduk\":\"Mar 18, 2022 at 12:52 GMT\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"40,478.7417\",\"description\":\"United States Dollar\",\"rate_float\":40478.7417},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"30,831.7670\",\"description\":\"British Pound Sterling\",\"rate_float\":30831.767},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"36,723.5693\",\"description\":\"Euro\",\"rate_float\":36723.5693}}}";


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataService dataService;

    @MockBean
    private HttpConnectionTool httpConnectionTool;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldReturnSelectEntitysSuccess() {
        String expectedResult = "";
        String result = "";
        Instant updatedTime = Instant.now();

        List<Coin> coinList = new ArrayList<>();
        Coin twd = new Coin();
        twd.setId(1);
        twd.setCode("TWD");
        twd.setName("新臺幣");
        twd.setUpdatedTime(updatedTime);

        Coin usd = new Coin();
        usd.setId(2);
        usd.setCode("USD");
        usd.setName("美元");
        usd.setUpdatedTime(updatedTime);

        coinList.add(twd);
        coinList.add(usd);

        List<Object> rows = new ArrayList<>(coinList);
        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Read Success");
        demoResponse.setRows(rows);
        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.when(dataService.selectCoins()).thenReturn(coinList);

        try {
            result = mockMvc.perform(MockMvcRequestBuilders.get("/demo/coins")
                            .accept(MediaType.APPLICATION_JSON ))
                            .andExpect(status().isOk())
                            .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnSelectEntitysSuccess Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnInsertEntitySuccess() {
        String expectedResult = "";
        String result = "";

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Create Success");

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.doNothing().when(dataService).insertCoin(Mockito.any());

        try {
            result = mockMvc.perform(MockMvcRequestBuilders.post("/demo/coin")
                    .param(PARAM_CODE, VAR_CODE)
                    .param(PARAM_NAME, VAR_NAME)
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnInsertEntitySuccess Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnUpdateEntitySuccess() {
        String expectedResult = "";
        String result = "";
        Instant updatedTime = Instant.now();

        List<Coin> coinList = new ArrayList<>();
        Coin twd = new Coin();
        twd.setId(1);
        twd.setCode("TWD");
        twd.setName("新臺幣");
        twd.setUpdatedTime(updatedTime);

        Coin usd = new Coin();
        usd.setId(2);
        usd.setCode("USD");
        usd.setName("美元");
        usd.setUpdatedTime(updatedTime);

        coinList.add(twd);
        coinList.add(usd);

        List<Object> rows = new ArrayList<>(coinList);
        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Update Success");
        demoResponse.setRows(rows);

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.doNothing().when(dataService).insertCoin(Mockito.any());
        Mockito.when(dataService.selectCoins()).thenReturn(coinList);

        try {
            result = mockMvc.perform(MockMvcRequestBuilders.put("/demo/coin/1")
                    .param(PARAM_CODE, VAR_CODE)
                    .param(PARAM_NAME, VAR_NAME)
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnUpdateEntitySuccess Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnDeleteEntitySuccess() {
        String expectedResult = "";
        String result = "";

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Delete Success");

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.doNothing().when(dataService).insertCoin(Mockito.any());

        try {
            result = mockMvc.perform(MockMvcRequestBuilders.delete("/demo/coin/1")
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnDeleteEntitySuccess Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnGetCoinCurrentPriceTransformSuccess() throws IOException {
        String expectedResult = "";
        String result = "";
        String updatedTime = "2022/03/18 20:52:00";

        List<Object> rows = new LinkedList<>();

        CoinInfo eur = new CoinInfo();
        eur.setCode("EUR");
        eur.setName("歐元");
        eur.setRateFloat(36723.57f);
        eur.setUpdatedTime(updatedTime);

        CoinInfo gbp = new CoinInfo();
        gbp.setCode("GBP");
        gbp.setName("英鎊");
        gbp.setRateFloat(30831.768f);
        gbp.setUpdatedTime(updatedTime);

        CoinInfo usd = new CoinInfo();
        usd.setCode("USD");
        usd.setName("美元");
        usd.setRateFloat(40478.742f);
        usd.setUpdatedTime(updatedTime);

        rows.add(eur);
        rows.add(gbp);
        rows.add(usd);

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Read Success");
        demoResponse.setRows(rows);

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.when(httpConnectionTool.getCurrentPriceJson(Mockito.any())).thenReturn(SAMPLE_JSON);
        Mockito.when(dataService.selectCoinNameByCode("EUR")).thenReturn("歐元");
        Mockito.when(dataService.selectCoinNameByCode("GBP")).thenReturn("英鎊");
        Mockito.when(dataService.selectCoinNameByCode("USD")).thenReturn("美元");

        try {
            result = mockMvc.perform(MockMvcRequestBuilders.get("/demo/coin/current-price/transform")
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnGetCoinCurrentPriceSuccess Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnGetCoinCurrentPriceTransformFailWhenGetJsonFail() throws IOException {
        String expectedResult = "";
        String result = "";

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Get current price json fail");

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.when(httpConnectionTool.getCurrentPriceJson(Mockito.any())).thenThrow(new IOException(""));
        try {
            result = mockMvc.perform(MockMvcRequestBuilders.get("/demo/coin/current-price/transform")
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnGetCoinCurrentPriceTransformFailWhenGetJsonFail Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnGetCoinCurrentPriceTransformFailWhenParseUpdatedTimeFail() throws IOException {
        String expectedResult = "";
        String result = "";

        String errorJson = "{\"time\":{\"updated\":\"Mar 18, 2022 12:52:00 UTC\",\"updatedISO\":\"Mar 18, 2022 12:52:00 UTC\",\"updateduk\":\"Mar 18, 2022 at 12:52 GMT\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"40,478.7417\",\"description\":\"United States Dollar\",\"rate_float\":40478.7417},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"30,831.7670\",\"description\":\"British Pound Sterling\",\"rate_float\":30831.767},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"36,723.5693\",\"description\":\"Euro\",\"rate_float\":36723.5693}}}";

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Parse updated time fail");

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.when(httpConnectionTool.getCurrentPriceJson(Mockito.any())).thenReturn(errorJson);

        try {
            result = mockMvc.perform(MockMvcRequestBuilders.get("/demo/coin/current-price/transform")
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnGetCoinCurrentPriceTransformFailWhenParseUpdatedTimeFail Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnGetCoinCurrentPriceTransformFailWhenParseCoinInfoFail() throws IOException {
        String expectedResult = "";
        String result = "";
        String errorJson = "{\"time\":{\"updated\":\"Mar 18, 2022 12:52:00 UTC\",\"updatedISO\":\"2022-03-18T12:52:00+00:00\",\"updateduk\":\"Mar 18, 2022 at 12:52 GMT\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"ERROR\":{\"error\":\"error\"}}}";

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Parse coin info fail");

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.when(httpConnectionTool.getCurrentPriceJson(Mockito.any())).thenReturn(errorJson);

        try {
            result = mockMvc.perform(MockMvcRequestBuilders.get("/demo/coin/current-price/transform")
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnGetCoinCurrentPriceTransformFailWhenParseCoinInfoFail Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnGetCoinCurrentPriceRawSuccess() throws IOException {
        String expectedResult = "";
        String result = "";
        String testJson = "{\"time\":{\"updated\":\"Mar 18, 2022 12:52:00 UTC\",\"updatedISO\":\"2022-03-18T12:52:00+00:00\",\"updateduk\":\"Mar 18, 2022 at 12:52 GMT\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"40,478.7417\",\"description\":\"United States Dollar\",\"rate_float\":40478.7417},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"30,831.7670\",\"description\":\"British Pound Sterling\",\"rate_float\":30831.767},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"36,723.5693\",\"description\":\"Euro\",\"rate_float\":36723.5693}},\"array\":[],\"obj\":null}";

        List<Object> rows = new LinkedList<>();

        JSONObject obj = new JSONObject(testJson);
        Map<String, Object> jsonMap = convertJSONObjectToMap(obj);
        rows.add(jsonMap);

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Read Success");
        demoResponse.setRows(rows);

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.when(httpConnectionTool.getCurrentPriceJson(Mockito.any())).thenReturn(testJson);

        try {
            result = mockMvc.perform(MockMvcRequestBuilders.get("/demo/coin/current-price/raw")
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnGetCoinCurrentPriceSuccess Fail!", expectedResult, result);
    }

    @Test
    void shouldReturnGetCoinCurrentPriceRawFail() throws IOException {
        String expectedResult = "";
        String result = "";

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Get current price json fail");

        try {
            expectedResult = objectMapper.writeValueAsString(demoResponse);
        } catch (JsonProcessingException e) {
            expectedResult = "Json processing error";
        }

        // Mock
        Mockito.when(httpConnectionTool.getCurrentPriceJson(Mockito.any())).thenThrow(new IOException(""));
        try {
            result = mockMvc.perform(MockMvcRequestBuilders.get("/demo/coin/current-price/raw")
                    .accept(MediaType.APPLICATION_JSON ))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            result = "abnormal";
        }
        Assert.assertEquals("shouldReturnGetCoinCurrentPriceRawFail Fail!", expectedResult, result);
    }


    private Map<String, Object> convertJSONObjectToMap(JSONObject obj){
        Map<String, Object> result = new HashMap<String, Object>();
        Iterator<String> itr = obj.keys();
        while (itr.hasNext())
        {
            String key = itr.next();

            Object temp = obj.get(key);
            if (temp instanceof JSONObject) {
                result.put(key, convertJSONObjectToMap((JSONObject) temp));
            } else if (temp instanceof JSONArray) {
                result.put(key, convertJSONArrayToList(temp));
            } else if (temp == JSONObject.NULL) {
                result.put(key, null);
            } else {
                result.put(key, temp);
            }
        }
        return result;
    }

    private JSONArray convertJSONArrayToList(Object obj) {
        Object json = new JSONTokener(obj.toString()).nextValue();;
        JSONArray jsonArray = null;
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }
}