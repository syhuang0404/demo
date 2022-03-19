#Demo說明

## Response Sample
各API統一透過以下Json格式進行回應
```
{
    "message": "Read Success",
    "rows": [
        {
            "id": 1,
            "code": "EUR",
            "name": "歐元",
            "updatedTime": "2022/03/19 09:43:21"
        },
        {
            "id": 2,
            "code": "GBP",
            "name": "英鎊",
            "updatedTime": "2022/03/19 09:43:21"
        },
        {
            "id": 3,
            "code": "USD",
            "name": "美元",
            "updatedTime": "2022/03/19 09:43:21"
        },
        {
            "id": 4,
            "code": "TWD2",
            "name": "新台幣2",
            "updatedTime": "2022/03/19 09:44:13"
        }
    ]
}
```
message會回傳request的狀態訊息,
- e.g. Read Success, Create Success

當失敗時則會回傳失敗的狀態訊息
- e.g. Get current price json fail

## 各項功能對應API路徑
| 功能說明 | API路徑 | Http Method | 範例request |
| ------ | ------ | ------ | ------ |
| 測試呼叫查詢幣別對應表資料 API,並顯示其內容。 | localhost:8080/demo/coins | GET | localhost:8080/demo/coins |
| 測試呼叫新增幣別對應表資料 API。 | localhost:8080/demo/coin | POST | localhost:8080/demo/coin?code=TWD&name=新臺幣 |
| 測試呼叫更新幣別對應表資料 API,並顯示其內容。 | localhost:8080/demo/coin/{id} | PUT | localhost:8080/demo/coin/4?code=TWD&name=新台幣 |
| 測試呼叫刪除幣別對應表資料 API。 | localhost:8080/demo/coin/{id} | DELETE | localhost:8080/demo/coin/4 |
| 測試呼叫 coindesk API,並顯示其內容。 | localhost:8080/demo/coin/current-price/raw | GET |  localhost:8080/demo/coin/current-price/raw |
| 測試呼叫資料轉換的 API,並顯示其內容。 | localhost:8080/demo/coin/current-price/transform | GET | localhost:8080/demo/coin/current-price/transform |


## Table Schema
請參考 src/main/resources/data.sql