# 헬로프라이스 API

<br/>

## Update Note

### v0.0.0 
- 최초 배포
- 상품 등록
- 사용자 알림 상품 조회
- 사용자 상품 알림 등록
- 사용자 상품 알림 삭제
<br/>

## API document

### 상품 등록

`POST /api/v0/products`

Request Header
- `Authorization` : 토큰 (require), format : `Bearer tokenblabla`

Request Body
- `productCode` : 제품코드 (require) // 다나와에서 조회 가능
    
```json
{
  "productCode" : "1231231"
}
```

Response
```json
{
  "id": 1,
  "productSales" : [
    { 
      "id": 101,
      "saleType" :  "NORMAL"
    },
    { 
      "id": 102,
      "saleType" :  "CASH"
    }
  ]
}


```


<br/>

### 상품 판매 조회 - 가격 많이 떨어진

`GET /api/v0/product-sales/top-decrease`


Request Header
- `Authorization` : 토큰 (require), format : `Bearer tokenblabla`


Response

``` json

[
    {
        "id": 2240,
        "productName": "원더스리빙 원더스 브리즈킹 WF70",
        "productCode": "7862200",
        "description": "LED백라이트 / 무풍",
        "url": "http://prod.danawa.com/info/?pcode=7862200",
        "imageUrl": "http://img.danawa.com/prod_img/500000/200/862/img/7862200_1.jpg?shrink=500:500&_v=20200325134432",
        "saleType": "CARD",
        "price": 28100,
        "prevPrice": 29900,
        "additionalInfo": "신한",
        "priceChangeRate": -6.02007,
        "lastUpdateAt": "2020-05-12T08:44:40"
        "notifyOn: true // 알림 등록 여부
    }
    //...
]
    
```
<br/>

### 개인화 - 사용자 알림 등록 상품 조회

`GET /api/v0/my/notifies/product`

Request Header
- `Authorization` : 토큰 (require), format : `Bearer tokenblabla`

Response
```
{
    "size": 1,
    "contents": [
        {
            "id": 2237,
            "productCode": "5941995",
            "productName": "삼성전자 DDR4 16G PC4-21300 (정품)",
            "url": "http://prod.danawa.com/info/?pcode=5941995",
            "imageUrl": "http://img.danawa.com/prod_img/500000/995/941/img/5941995_1.jpg?shrink=500:500&_v=20190208132600",
            "description": "DDR4 / PC용 / 패키지: 1개 / 2666MHz (PC4-21300) / 히트싱크: 미포함", 
            "saleStatus": "SALE", // 상품 상태 [SALE, DISCONTINUE, EMPTY_AMOUNT, NOT_SUPPORT, UNKNOWN]
            "lastConfirmAt": "2020-05-07T16:34:30", //최종확인일
            "productSales": [
                {
                    "id": 11,
                    "saleType": "NORMAL", // [NORMAL, CASH, CARD]
                    "price": 67400,
                    "prevPrice": 67320,
                    "priceChangeRate": 0.0011869436201780415,
                    "additionalInfo": "",
                    "notifyOn: true // 알림 등록 여부
                },
                {
                    "id": 12,
                    "saleType": "CASH",
                    "price": 65300,
                    "prevPrice": 64800,
                    "priceChangeRate": 0.007656967840735069,
                    "additionalInfo": "",
                    "notifyOn: true // 알림 등록 여부
                },
                {
                    "id": 13,
                    "saleType": "CARD",
                    "price": 66730,
                    "prevPrice": 66646,
                    "priceChangeRate": 0.001258804136070733,
                    "additionalInfo": "KB국민",
                    "notifyOn: true // 알림 등록 여부
                }
            ]
        }
    //...
    ]
}
```

<br/>



### 개인화 - 사용자 상품 알림 등록

`POST /api/v0/my/notifies`

Request Header
- `Authorization` : 토큰 (require), format : `Bearer tokenblabla`

Request Body
- `productSaleId` : 상품판매 ID (require)
    
```json
{
  "productSaleId" : "1"
}
```


<br/>

### 개인화 - 사용자 알림 상품 삭제

`DELETE /api/v0/my/notifies`

Request Header
- `Authorization` : 토큰 (require), format : `Bearer tokenblabla`

```json
{
  "productSaleId" : "1"
}
```
