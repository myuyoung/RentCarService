---
title: OpenAPI definition v0
language_tabs:
  - shell: curl
language_clients:
  - shell: ""
toc_footers: []
includes: []
search: true
highlight_theme: darkula
headingLevel: 2

---

<!-- Generator: Widdershins v4.0.1 -->

<h1 id="openapi-definition">OpenAPI definition v0</h1>

> Scroll down for code samples, example requests and responses. Select a language for code samples from the tabs above or the mobile navigation menu.

Base URLs:

* <a href="http://localhost:7950">http://localhost:7950</a>

<h1 id="openapi-definition-1-api">1.회원 가입 API</h1>

사용자 회원 가입을 처리하는 API

## 일반 회원 가입

<a id="opIdregister"></a>

> Code samples

```shell
curl --request POST \
  --url http://localhost:7950/api/register/member \
  --header 'Accept: */*' \
  --header 'Content-Type: application/json' \
  --data '{"name":"홍길동","email":"test@example.com","password":"Testpassword1!","phone":"010-1234-5678","address":"서울시 강남구"}'
```

`POST /api/register/member`

이메일, 비밀번호, 이름 등으로 회원 가입을 요청합니다.

> Body parameter

```json
{
  "name": "홍길동",
  "email": "test@example.com",
  "password": "Testpassword1!",
  "phone": "010-1234-5678",
  "address": "서울시 강남구"
}
```

<h3 id="일반-회원-가입-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[RegisterMemberDTO](#schemaregistermemberdto)|true|none|

> Example responses

> 201 Response

<h3 id="일반-회원-가입-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|회원가입 성공|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="openapi-definition-login-controller">login-controller</h1>

## refreshToken

<a id="opIdrefreshToken"></a>

> Code samples

```shell
curl --request POST \
  --url http://localhost:7950/auth/refresh-token \
  --header 'Accept: */*'
```

`POST /auth/refresh-token`

> Example responses

> 200 Response

<h3 id="refreshtoken-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOAuthResponseDTO](#schemaapiresponsedtoauthresponsedto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## logout

<a id="opIdlogout"></a>

> Code samples

```shell
curl --request POST \
  --url http://localhost:7950/auth/logout \
  --header 'Accept: */*'
```

`POST /auth/logout`

> Example responses

> 200 Response

<h3 id="logout-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## login

<a id="opIdlogin"></a>

> Code samples

```shell
curl --request POST \
  --url http://localhost:7950/auth/login \
  --header 'Accept: */*' \
  --header 'Content-Type: application/json' \
  --data '{"email":"string","password":"string"}'
```

`POST /auth/login`

> Body parameter

```json
{
  "email": "string",
  "password": "string"
}
```

<h3 id="login-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[LoginRequestDTO](#schemaloginrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="login-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOAuthResponseDTO](#schemaapiresponsedtoauthresponsedto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="openapi-definition-my-page-controller">my-page-controller</h1>

## changeMemberInformation

<a id="opIdchangeMemberInformation"></a>

> Code samples

```shell
curl --request POST \
  --url http://localhost:7950/api/MyPage/497f6eca-6276-4993-bfeb-53cbbbba6f08/change \
  --header 'Accept: */*' \
  --header 'Content-Type: application/json' \
  --data '{"id":"497f6eca-6276-4993-bfeb-53cbbbba6f08","name":"string","licence":true,"email":"string","phone":"string","address":"string"}'
```

`POST /api/MyPage/{memberId}/change`

> Body parameter

```json
{
  "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
  "name": "string",
  "licence": true,
  "email": "string",
  "phone": "string",
  "address": "string"
}
```

<h3 id="changememberinformation-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|memberId|path|string(uuid)|true|none|
|body|body|[MemberDTO](#schemamemberdto)|true|none|

> Example responses

> 200 Response

<h3 id="changememberinformation-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## reserveCar

<a id="opIdreserveCar"></a>

> Code samples

```shell
curl --request POST \
  --url http://localhost:7950/api/MyPage/reservation \
  --header 'Accept: */*' \
  --header 'Content-Type: application/json' \
  --data '{"rentDTO":{"rent_id":"b2296160-8ede-44df-a694-5844f16b3c86","rentTime":"2019-08-24T14:15:22Z","duration":1,"endTime":"2019-08-24T14:15:22Z","rentCars":{"name":"string","rentPrice":0,"recommend":0,"rentCarNumber":"string","reservationStatus":"AVAILABLE","totalPrice":0}},"rentCarsDTO":{"name":"string","rentPrice":0,"recommend":0,"rentCarNumber":"string","reservationStatus":"AVAILABLE","totalPrice":0}}'
```

`POST /api/MyPage/reservation`

> Body parameter

```json
{
  "rentDTO": {
    "rent_id": "b2296160-8ede-44df-a694-5844f16b3c86",
    "rentTime": "2019-08-24T14:15:22Z",
    "duration": 1,
    "endTime": "2019-08-24T14:15:22Z",
    "rentCars": {
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalPrice": 0
    }
  },
  "rentCarsDTO": {
    "name": "string",
    "rentPrice": 0,
    "recommend": 0,
    "rentCarNumber": "string",
    "reservationStatus": "AVAILABLE",
    "totalPrice": 0
  }
}
```

<h3 id="reservecar-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[ReservationDTO](#schemareservationdto)|true|none|

> Example responses

> 200 Response

<h3 id="reservecar-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTORentDTO](#schemaapiresponsedtorentdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## memberInformation

<a id="opIdmemberInformation"></a>

> Code samples

```shell
curl --request GET \
  --url http://localhost:7950/api/MyPage/497f6eca-6276-4993-bfeb-53cbbbba6f08 \
  --header 'Accept: */*'
```

`GET /api/MyPage/{memberId}`

<h3 id="memberinformation-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|memberId|path|string(uuid)|true|none|

> Example responses

> 200 Response

<h3 id="memberinformation-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOMemberDTO](#schemaapiresponsedtomemberdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## getReservationList

<a id="opIdgetReservationList"></a>

> Code samples

```shell
curl --request GET \
  --url http://localhost:7950/api/MyPage/reservation/list \
  --header 'Accept: */*'
```

`GET /api/MyPage/reservation/list`

> Example responses

> 200 Response

<h3 id="getreservationlist-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOListRentDTO](#schemaapiresponsedtolistrentdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## getReservation

<a id="opIdgetReservation"></a>

> Code samples

```shell
curl --request GET \
  --url http://localhost:7950/api/MyPage/reservation/list/497f6eca-6276-4993-bfeb-53cbbbba6f08 \
  --header 'Accept: */*'
```

`GET /api/MyPage/reservation/list/{reservationId}`

<h3 id="getreservation-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|reservationId|path|string(uuid)|true|none|

> Example responses

> 200 Response

<h3 id="getreservation-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTORentDTO](#schemaapiresponsedtorentdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## cancelReservation

<a id="opIdcancelReservation"></a>

> Code samples

```shell
curl --request DELETE \
  --url http://localhost:7950/api/MyPage/reservation/list/cancel/497f6eca-6276-4993-bfeb-53cbbbba6f08 \
  --header 'Accept: */*'
```

`DELETE /api/MyPage/reservation/list/cancel/{reservationId}`

<h3 id="cancelreservation-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|reservationId|path|string(uuid)|true|none|

> Example responses

> 200 Response

<h3 id="cancelreservation-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="openapi-definition-rent-cars-controller">rent-cars-controller</h1>

## getRankedRentCars

<a id="opIdgetRankedRentCars"></a>

> Code samples

```shell
curl --request GET \
  --url 'http://localhost:7950/api/rentcars/rank?pageable=page%2C0%2Csize%2C1%2Csort%2Cstring' \
  --header 'Accept: */*'
```

`GET /api/rentcars/rank`

<h3 id="getrankedrentcars-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|pageable|query|[Pageable](#schemapageable)|true|none|

> Example responses

> 200 Response

<h3 id="getrankedrentcars-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOPageRentCarsDTO](#schemaapiresponsedtopagerentcarsdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="openapi-definition-protected-api-controller">protected-api-controller</h1>

## protectedEndpoint

<a id="opIdprotectedEndpoint"></a>

> Code samples

```shell
curl --request GET \
  --url http://localhost:7950/api/protected \
  --header 'Accept: */*'
```

`GET /api/protected`

> Example responses

> 200 Response

<h3 id="protectedendpoint-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|string|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

# Schemas

<h2 id="tocS_ErrorDTO">ErrorDTO</h2>
<!-- backwards compatibility -->
<a id="schemaerrordto"></a>
<a id="schema_ErrorDTO"></a>
<a id="tocSerrordto"></a>
<a id="tocserrordto"></a>

```json
{
  "message": "string",
  "code": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|message|string|false|none|none|
|code|string|false|none|none|

<h2 id="tocS_ApiResponseDTOVoid">ApiResponseDTOVoid</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtovoid"></a>
<a id="schema_ApiResponseDTOVoid"></a>
<a id="tocSapiresponsedtovoid"></a>
<a id="tocsapiresponsedtovoid"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {}
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|object|false|none|none|

<h2 id="tocS_ApiResponseDTOAuthResponseDTO">ApiResponseDTOAuthResponseDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtoauthresponsedto"></a>
<a id="schema_ApiResponseDTOAuthResponseDTO"></a>
<a id="tocSapiresponsedtoauthresponsedto"></a>
<a id="tocsapiresponsedtoauthresponsedto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "token": "string"
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[AuthResponseDTO](#schemaauthresponsedto)|false|none|none|

<h2 id="tocS_AuthResponseDTO">AuthResponseDTO</h2>
<!-- backwards compatibility -->
<a id="schemaauthresponsedto"></a>
<a id="schema_AuthResponseDTO"></a>
<a id="tocSauthresponsedto"></a>
<a id="tocsauthresponsedto"></a>

```json
{
  "token": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|token|string|false|none|none|

<h2 id="tocS_LoginRequestDTO">LoginRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemaloginrequestdto"></a>
<a id="schema_LoginRequestDTO"></a>
<a id="tocSloginrequestdto"></a>
<a id="tocsloginrequestdto"></a>

```json
{
  "email": "string",
  "password": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|email|string|false|none|none|
|password|string|false|none|none|

<h2 id="tocS_RegisterMemberDTO">RegisterMemberDTO</h2>
<!-- backwards compatibility -->
<a id="schemaregistermemberdto"></a>
<a id="schema_RegisterMemberDTO"></a>
<a id="tocSregistermemberdto"></a>
<a id="tocsregistermemberdto"></a>

```json
{
  "name": "홍길동",
  "email": "test@example.com",
  "password": "Testpassword1!",
  "phone": "010-1234-5678",
  "address": "서울시 강남구"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|name|string|true|none|사용자 이름|
|email|string|true|none|사용자 이메일|
|password|string|true|write-only|비밀번호(비밀번호가 영어로만 이루어져야 하고 8자 이상, 특수문자 하나 이상, 숫자가 하나 이상, 16자리 이하로 설정)|
|phone|string|true|none|전화번호|
|address|string|true|none|주소|

<h2 id="tocS_MemberDTO">MemberDTO</h2>
<!-- backwards compatibility -->
<a id="schemamemberdto"></a>
<a id="schema_MemberDTO"></a>
<a id="tocSmemberdto"></a>
<a id="tocsmemberdto"></a>

```json
{
  "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
  "name": "string",
  "licence": true,
  "email": "string",
  "phone": "string",
  "address": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|string(uuid)|false|none|none|
|name|string|false|none|none|
|licence|boolean|false|none|none|
|email|string|false|none|none|
|phone|string|false|none|none|
|address|string|false|none|none|

<h2 id="tocS_RentCarsDTO">RentCarsDTO</h2>
<!-- backwards compatibility -->
<a id="schemarentcarsdto"></a>
<a id="schema_RentCarsDTO"></a>
<a id="tocSrentcarsdto"></a>
<a id="tocsrentcarsdto"></a>

```json
{
  "name": "string",
  "rentPrice": 0,
  "recommend": 0,
  "rentCarNumber": "string",
  "reservationStatus": "AVAILABLE",
  "totalPrice": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|name|string|true|none|none|
|rentPrice|integer(int32)|true|none|none|
|recommend|integer(int64)|false|none|none|
|rentCarNumber|string|false|none|none|
|reservationStatus|string|false|none|none|
|totalPrice|integer(int32)|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|reservationStatus|AVAILABLE|
|reservationStatus|RENTED|
|reservationStatus|MAINTENANCE|

<h2 id="tocS_RentDTO">RentDTO</h2>
<!-- backwards compatibility -->
<a id="schemarentdto"></a>
<a id="schema_RentDTO"></a>
<a id="tocSrentdto"></a>
<a id="tocsrentdto"></a>

```json
{
  "rent_id": "b2296160-8ede-44df-a694-5844f16b3c86",
  "rentTime": "2019-08-24T14:15:22Z",
  "duration": 1,
  "endTime": "2019-08-24T14:15:22Z",
  "rentCars": {
    "name": "string",
    "rentPrice": 0,
    "recommend": 0,
    "rentCarNumber": "string",
    "reservationStatus": "AVAILABLE",
    "totalPrice": 0
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|rent_id|string(uuid)|false|none|none|
|rentTime|string(date-time)|false|none|none|
|duration|integer(int32)|false|none|none|
|endTime|string(date-time)|false|none|none|
|rentCars|[RentCarsDTO](#schemarentcarsdto)|true|none|none|

<h2 id="tocS_ReservationDTO">ReservationDTO</h2>
<!-- backwards compatibility -->
<a id="schemareservationdto"></a>
<a id="schema_ReservationDTO"></a>
<a id="tocSreservationdto"></a>
<a id="tocsreservationdto"></a>

```json
{
  "rentDTO": {
    "rent_id": "b2296160-8ede-44df-a694-5844f16b3c86",
    "rentTime": "2019-08-24T14:15:22Z",
    "duration": 1,
    "endTime": "2019-08-24T14:15:22Z",
    "rentCars": {
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalPrice": 0
    }
  },
  "rentCarsDTO": {
    "name": "string",
    "rentPrice": 0,
    "recommend": 0,
    "rentCarNumber": "string",
    "reservationStatus": "AVAILABLE",
    "totalPrice": 0
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|rentDTO|[RentDTO](#schemarentdto)|true|none|none|
|rentCarsDTO|[RentCarsDTO](#schemarentcarsdto)|true|none|none|

<h2 id="tocS_ApiResponseDTORentDTO">ApiResponseDTORentDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtorentdto"></a>
<a id="schema_ApiResponseDTORentDTO"></a>
<a id="tocSapiresponsedtorentdto"></a>
<a id="tocsapiresponsedtorentdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "rent_id": "b2296160-8ede-44df-a694-5844f16b3c86",
    "rentTime": "2019-08-24T14:15:22Z",
    "duration": 1,
    "endTime": "2019-08-24T14:15:22Z",
    "rentCars": {
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalPrice": 0
    }
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[RentDTO](#schemarentdto)|false|none|none|

<h2 id="tocS_Pageable">Pageable</h2>
<!-- backwards compatibility -->
<a id="schemapageable"></a>
<a id="schema_Pageable"></a>
<a id="tocSpageable"></a>
<a id="tocspageable"></a>

```json
{
  "page": 0,
  "size": 1,
  "sort": [
    "string"
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|page|integer(int32)|false|none|none|
|size|integer(int32)|false|none|none|
|sort|[string]|false|none|none|

<h2 id="tocS_ApiResponseDTOPageRentCarsDTO">ApiResponseDTOPageRentCarsDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtopagerentcarsdto"></a>
<a id="schema_ApiResponseDTOPageRentCarsDTO"></a>
<a id="tocSapiresponsedtopagerentcarsdto"></a>
<a id="tocsapiresponsedtopagerentcarsdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "totalPages": 0,
    "totalElements": 0,
    "numberOfElements": 0,
    "first": true,
    "last": true,
    "pageable": {
      "pageNumber": 0,
      "pageSize": 0,
      "paged": true,
      "unpaged": true,
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": true,
          "property": "string",
          "ignoreCase": true
        }
      ]
    },
    "size": 0,
    "content": [
      {
        "name": "string",
        "rentPrice": 0,
        "recommend": 0,
        "rentCarNumber": "string",
        "reservationStatus": "AVAILABLE",
        "totalPrice": 0
      }
    ],
    "number": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": true,
        "property": "string",
        "ignoreCase": true
      }
    ],
    "empty": true
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[PageRentCarsDTO](#schemapagerentcarsdto)|false|none|none|

<h2 id="tocS_PageRentCarsDTO">PageRentCarsDTO</h2>
<!-- backwards compatibility -->
<a id="schemapagerentcarsdto"></a>
<a id="schema_PageRentCarsDTO"></a>
<a id="tocSpagerentcarsdto"></a>
<a id="tocspagerentcarsdto"></a>

```json
{
  "totalPages": 0,
  "totalElements": 0,
  "numberOfElements": 0,
  "first": true,
  "last": true,
  "pageable": {
    "pageNumber": 0,
    "pageSize": 0,
    "paged": true,
    "unpaged": true,
    "offset": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": true,
        "property": "string",
        "ignoreCase": true
      }
    ]
  },
  "size": 0,
  "content": [
    {
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalPrice": 0
    }
  ],
  "number": 0,
  "sort": [
    {
      "direction": "string",
      "nullHandling": "string",
      "ascending": true,
      "property": "string",
      "ignoreCase": true
    }
  ],
  "empty": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|totalPages|integer(int32)|false|none|none|
|totalElements|integer(int64)|false|none|none|
|numberOfElements|integer(int32)|false|none|none|
|first|boolean|false|none|none|
|last|boolean|false|none|none|
|pageable|[PageableObject](#schemapageableobject)|false|none|none|
|size|integer(int32)|false|none|none|
|content|[[RentCarsDTO](#schemarentcarsdto)]|false|none|none|
|number|integer(int32)|false|none|none|
|sort|[[SortObject](#schemasortobject)]|false|none|none|
|empty|boolean|false|none|none|

<h2 id="tocS_PageableObject">PageableObject</h2>
<!-- backwards compatibility -->
<a id="schemapageableobject"></a>
<a id="schema_PageableObject"></a>
<a id="tocSpageableobject"></a>
<a id="tocspageableobject"></a>

```json
{
  "pageNumber": 0,
  "pageSize": 0,
  "paged": true,
  "unpaged": true,
  "offset": 0,
  "sort": [
    {
      "direction": "string",
      "nullHandling": "string",
      "ascending": true,
      "property": "string",
      "ignoreCase": true
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|pageNumber|integer(int32)|false|none|none|
|pageSize|integer(int32)|false|none|none|
|paged|boolean|false|none|none|
|unpaged|boolean|false|none|none|
|offset|integer(int64)|false|none|none|
|sort|[[SortObject](#schemasortobject)]|false|none|none|

<h2 id="tocS_SortObject">SortObject</h2>
<!-- backwards compatibility -->
<a id="schemasortobject"></a>
<a id="schema_SortObject"></a>
<a id="tocSsortobject"></a>
<a id="tocssortobject"></a>

```json
{
  "direction": "string",
  "nullHandling": "string",
  "ascending": true,
  "property": "string",
  "ignoreCase": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|direction|string|false|none|none|
|nullHandling|string|false|none|none|
|ascending|boolean|false|none|none|
|property|string|false|none|none|
|ignoreCase|boolean|false|none|none|

<h2 id="tocS_ApiResponseDTOMemberDTO">ApiResponseDTOMemberDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtomemberdto"></a>
<a id="schema_ApiResponseDTOMemberDTO"></a>
<a id="tocSapiresponsedtomemberdto"></a>
<a id="tocsapiresponsedtomemberdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
    "name": "string",
    "licence": true,
    "email": "string",
    "phone": "string",
    "address": "string"
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[MemberDTO](#schemamemberdto)|false|none|none|

<h2 id="tocS_ApiResponseDTOListRentDTO">ApiResponseDTOListRentDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtolistrentdto"></a>
<a id="schema_ApiResponseDTOListRentDTO"></a>
<a id="tocSapiresponsedtolistrentdto"></a>
<a id="tocsapiresponsedtolistrentdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": [
    {
      "rent_id": "b2296160-8ede-44df-a694-5844f16b3c86",
      "rentTime": "2019-08-24T14:15:22Z",
      "duration": 1,
      "endTime": "2019-08-24T14:15:22Z",
      "rentCars": {
        "name": "string",
        "rentPrice": 0,
        "recommend": 0,
        "rentCarNumber": "string",
        "reservationStatus": "AVAILABLE",
        "totalPrice": 0
      }
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[[RentDTO](#schemarentdto)]|false|none|none|

