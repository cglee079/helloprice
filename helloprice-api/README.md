# 헬로프라이스 API

<br/>

## Update Note

### v0.0.0 
- 최초 배포
- 상품 등록
- ~~사용자 알림 상품 조회~~
- 사용자 상품 알림 조회
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

~~### 개인화 - 사용자 알림 등록 상품 조회~~

`GET /api/v0/my/notifies/product`

~~Request Header~~
- `Authorization` : 토큰 (require), format : `Bearer tokenblabla`

~~Response~~
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


### 개인화 - 사용자 상품 알림 조회

`GET /api/v0/my/notifies`

Request Header
- `Authorization` : 토큰 (require), format : `Bearer tokenblabla`

Response Sample
```
{
    "size": 2,
    "contents": [
        {
            "id": 2,
            "product": {
                "id": 2259,
                "productCode": "5353095",
                "productName": "플레이어 언노운의 배틀그라운드 PC 스팀 코드",
                "url": "http://prod.danawa.com/info/?pcode=5353095",
                "imageUrl": "http://img.danawa.com/prod_img/500000/095/353/img/5353095_1.jpg?shrink=500:500&_v=20171027181629",
                "description": "PC / 게임타이틀 / 장르: 액션 , FPS , TPS , 슈팅 , 실시간 / 한국어 / 1인용 / 온라인 대응 / 다운로드상품 / 청소년이용불가 / 출시상품 / 스팀 연동",
                "saleStatus": "SALE",
                "lastConfirmAt": null
            },
            "productSale": {
                "id": 64,
                "saleType": "NORMAL",
                "price": 11200,
                "prevPrice": 11200,
                "priceChangeRate": 0.0,
                "additionalInfo": ""
            }
        },
        {
            "id": 39,
            "product": {
                "id": 2253,
                "productCode": "8459973",
                "productName": "AMD 라이젠5-3세대 3600 (마티스) (정품)",
                "url": "http://prod.danawa.com/info/?pcode=8459973",
                "imageUrl": "http://img.danawa.com/prod_img/500000/973/459/img/8459973_1.jpg?shrink=500:500&_v=20200429111019",
                "description": "AMD(소켓AM4) / 3세대 (Zen 2) / 7nm / 헥사(6)코어 / 쓰레드 12개 / 기본 클럭: 3.6GHz / 3MB / 32MB / 설계전력: 65W / PCIe 4.0 / 메모리 규격: DDR4 / 메모리 버스: 3200MHz / Wraith Stealth 쿨러 포함 / 출시가: 199달러(VAT별도)",
                "saleStatus": "SALE",
                "lastConfirmAt": "2020-06-01T14:49:40"
            },
            "productSale": {
                "id": 49,
                "saleType": "NORMAL",
                "price": 223970,
                "prevPrice": 223900,
                "priceChangeRate": 0.03126,
                "additionalInfo": ""
            }
        }
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

`DELETE /api/v0/my/notifies?productSaleId=`

Request Param
-  `productSaleId` : 상품판매 ID (require)

Request Header
- `Authorization` : 토큰 (require), format : `Bearer tokenblabla`
