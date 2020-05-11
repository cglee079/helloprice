# 헬로프라이스 API

<br/>

## Update Note

### v0.0.0 
- 최초 배포
- 사용자 등록 상품 조회
- 사용자 상품 등록
- 사용자 상품 삭제

<br/>

## API document


### 상품 등록

`POST /api/v0/products`

Request Body
- `productCode` : 제품코드 (require)
    
```json
{
  "productCode" : "1231231"
}
```

Response
- `id` : 상품 ID
```json
{
  "id" : "1"
}
```

<br/>

### 사용자 등록 상품 조회

`GET /api/v0/my/products`

Path Variable
- `userId` : 사용자 ID

Response Sample
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
            "saleStatus": "SALE",
            "lastConfirmAt": "2020-05-07T16:34:30",
            "productSales": [
                {
                    "id": 11,
                    "saleType": "NORMAL",
                    "price": 67400,
                    "prevPrice": 67320,
                    "priceChangeRate": 0.0011869436201780415,
                    "additionalInfo": ""
                },
                {
                    "id": 12,
                    "saleType": "CASH",
                    "price": 65300,
                    "prevPrice": 64800,
                    "priceChangeRate": 0.007656967840735069,
                    "additionalInfo": ""
                },
                {
                    "id": 13,
                    "saleType": "CARD",
                    "price": 66730,
                    "prevPrice": 66646,
                    "priceChangeRate": 0.001258804136070733,
                    "additionalInfo": "KB국민"
                }
            ]
        }
    //...
    ]
}
```


<br/>

### 사용자 상품 알림 등록

`POST /api/v0/my/products/notify`

Request Header
- `userId` : 사용자 ID (require)

Request Body
- `productId` : 상품 ID (require)
- `saleType` : 상품 판매 타입
    
```json
{
  "productId" : "1",
  "saleType" : "CASH"
}
```


<br/>

### 사용자 상품 삭제

`DELETE /api/v0/my/products/notify`

Request Header
- `userId` : 사용자 ID (require)

Request Body
- `id` : 알림 ID (require)
    
```json
{
  "id" : "1"
}
```
 




