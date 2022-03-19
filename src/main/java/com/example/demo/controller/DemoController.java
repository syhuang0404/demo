package com.example.demo.controller;

import com.example.demo.model.DemoResponse;
import com.example.demo.model.entity.Coin;
import com.example.demo.model.entity.CoinInfo;
import com.example.demo.model.entity.CoinType;
import com.example.demo.service.DataService;
import com.example.demo.util.HttpConnectionTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value="/demo")
public class DemoController {

    @Autowired
    private DataService dataService;
    @Autowired
    private HttpConnectionTool httpConnectionTool;

    @GetMapping(value="/coins", produces = APPLICATION_JSON_UTF8_VALUE)
    public DemoResponse selectEntitys() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        List<Object> rows = new ArrayList<>(dataService.selectCoins());
        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Read Success");
        demoResponse.setRows(rows);

        return demoResponse;
    }

    @PostMapping(value="/coin", produces = APPLICATION_JSON_UTF8_VALUE)
    public DemoResponse insertEntity(
            @RequestParam(value="code", required=true) String code,
            @RequestParam(value="name", required=true) String name) {
        Coin coin = new Coin();
        coin.setCode(code);
        coin.setName(name);
        coin.setUpdatedTime(Instant.now());
        dataService.insertCoin(coin);

        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Create Success");

        return demoResponse;
    }

    @PutMapping(value="/coin/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public DemoResponse updateEntity(
            @PathVariable(value="id", required = true) Integer id,
            @RequestParam(value="code", required=true) String code,
            @RequestParam(value="name", required=true) String name) {

        dataService.updateCoinById(code, name, Instant.now(), id);

        List<Object> rows = new ArrayList<>(dataService.selectCoins());
        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Update Success");
        demoResponse.setRows(rows);

        return demoResponse;
    }

    @DeleteMapping(value="/coin/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public DemoResponse deleteEntity(
            @PathVariable(value="id", required = true) Integer id) {
        dataService.deleteCoinById(id);
        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Delete Success");
        return demoResponse;
    }

    @GetMapping(value="/coin/current-price/raw", produces = APPLICATION_JSON_UTF8_VALUE)
    public DemoResponse getCoinCurrentPriceRaw() {
        DemoResponse demoResponse = new DemoResponse();
        try {
            URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
            String json = httpConnectionTool.getCurrentPriceJson(url);

            JSONObject obj = new JSONObject(json);
            Map<String, Object> jsonMap = convertJSONObjectToMap(obj);
            List<Object> result = new ArrayList<>();
            result.add(jsonMap);

            demoResponse.setMessage("Read Success");
            demoResponse.setRows(result);
        } catch (IOException e) {
            demoResponse.setMessage("Get current price json fail");
        }
        return demoResponse;
    }

    @GetMapping(value="/coin/current-price/transform", produces = APPLICATION_JSON_UTF8_VALUE)
    public DemoResponse getCoinCurrentPrice() {
        String json = "";
        String updatedTime = "";
        try {
            URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
            json = httpConnectionTool.getCurrentPriceJson(url);
        } catch (IOException e) {
            DemoResponse demoResponse = new DemoResponse();
            demoResponse.setMessage("Get current price json fail");
            return demoResponse;
        }
        JSONObject obj = new JSONObject(json);
        JSONObject timeObj = new JSONObject(obj.get("time").toString());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date date = sdf.parse(timeObj.get("updatedISO").toString());
            SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            updatedTime = outputSdf.format(date);
        } catch (ParseException e) {
            DemoResponse demoResponse = new DemoResponse();
            demoResponse.setMessage("Parse updated time fail");
            return demoResponse;
        }

        JSONObject codeObj = new JSONObject(obj.get("bpi").toString());
        List<Object> result = new ArrayList<>();
        for (String key : codeObj.keySet()){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                CoinType coinType = objectMapper.readValue(codeObj.get(key).toString(), CoinType.class);
                String name = dataService.selectCoinNameByCode(coinType.getCode());
                CoinInfo coinInfo = new CoinInfo();
                coinInfo.setCode(coinType.getCode());
                coinInfo.setName(name);
                coinInfo.setRateFloat(coinType.getRate_float());
                coinInfo.setUpdatedTime(updatedTime);
                result.add(coinInfo);
            } catch (JsonProcessingException e) {
                DemoResponse demoResponse = new DemoResponse();
                demoResponse.setMessage("Parse coin info fail");
                return demoResponse;
            }
        }
        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMessage("Read Success");
        demoResponse.setRows(result);
        return demoResponse;
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
