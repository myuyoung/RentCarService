---
title: OpenAPI definition v0
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
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

## register

<a id="opIdregister"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/register/member \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/register/member HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "name": "홍길동",
  "email": "test@example.com",
  "password": "Testpassword1!",
  "phone": "010-1234-5678",
  "address": "서울시 강남구"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/register/member',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/register/member',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/register/member', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/register/member', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/register/member");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/register/member", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/register/member`

*일반 회원 가입*

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

<h3 id="register-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[RegisterMemberDTO](#schemaregistermemberdto)|true|none|

> Example responses

> 201 Response

<h3 id="register-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|회원가입 성공|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="openapi-definition--api">관리자 차량 관리 API</h1>

관리자 전용 렌트카 등록, 조회, 수정, 삭제를 담당하는 RESTful API

## getCarById

<a id="opIdgetCarById"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/cars/{carId} \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/cars/{carId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/cars/{carId}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/cars/{carId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/cars/{carId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/cars/{carId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/cars/{carId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/cars/{carId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/cars/{carId}`

*특정 차량 조회*

차량 ID로 특정 차량 정보를 조회합니다.

<h3 id="getcarbyid-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|carId|path|integer(int64)|true|none|

> Example responses

> 200 Response

<h3 id="getcarbyid-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTORentCarsDTO](#schemaapiresponsedtorentcarsdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## updateCar

<a id="opIdupdateCar"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:7950/api/admin/cars/{carId} \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
PUT http://localhost:7950/api/admin/cars/{carId} HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "id": 0,
  "name": "string",
  "rentPrice": 0,
  "recommend": 0,
  "rentCarNumber": "string",
  "reservationStatus": "AVAILABLE",
  "totalDistance": 0
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/cars/{carId}',
{
  method: 'PUT',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.put 'http://localhost:7950/api/admin/cars/{carId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.put('http://localhost:7950/api/admin/cars/{carId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('PUT','http://localhost:7950/api/admin/cars/{carId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/cars/{carId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("PUT");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("PUT", "http://localhost:7950/api/admin/cars/{carId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`PUT /api/admin/cars/{carId}`

*차량 정보 수정*

기존 차량의 정보를 수정합니다.

> Body parameter

```json
{
  "id": 0,
  "name": "string",
  "rentPrice": 0,
  "recommend": 0,
  "rentCarNumber": "string",
  "reservationStatus": "AVAILABLE",
  "totalDistance": 0
}
```

<h3 id="updatecar-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|carId|path|integer(int64)|true|none|
|body|body|[RentCarsDTO](#schemarentcarsdto)|true|none|

> Example responses

> 200 Response

<h3 id="updatecar-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTORentCarsDTO](#schemaapiresponsedtorentcarsdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## deleteCar

<a id="opIddeleteCar"></a>

> Code samples

```shell
# You can also use wget
curl -X DELETE http://localhost:7950/api/admin/cars/{carId} \
  -H 'Accept: */*'

```

```http
DELETE http://localhost:7950/api/admin/cars/{carId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/cars/{carId}',
{
  method: 'DELETE',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.delete 'http://localhost:7950/api/admin/cars/{carId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.delete('http://localhost:7950/api/admin/cars/{carId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('DELETE','http://localhost:7950/api/admin/cars/{carId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/cars/{carId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("DELETE");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("DELETE", "http://localhost:7950/api/admin/cars/{carId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`DELETE /api/admin/cars/{carId}`

*차량 삭제*

등록된 차량을 삭제합니다.

<h3 id="deletecar-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|carId|path|integer(int64)|true|none|

> Example responses

> 200 Response

<h3 id="deletecar-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## getAllCars

<a id="opIdgetAllCars"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/cars?pageable=page,0,size,1,sort,string \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/cars?pageable=page,0,size,1,sort,string HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/cars?pageable=page,0,size,1,sort,string',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/cars',
  params: {
  'pageable' => '[Pageable](#schemapageable)'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/cars', params={
  'pageable': {
  "page": 0,
  "size": 1,
  "sort": [
    "string"
  ]
}
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/cars', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/cars?pageable=page,0,size,1,sort,string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/cars", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/cars`

*차량 목록 조회*

등록된 모든 차량을 페이징하여 조회합니다.

<h3 id="getallcars-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|pageable|query|[Pageable](#schemapageable)|true|none|

> Example responses

> 200 Response

<h3 id="getallcars-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOPageRentCarsDTO](#schemaapiresponsedtopagerentcarsdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## registerCar

<a id="opIdregisterCar"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/admin/cars \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/admin/cars HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "id": 0,
  "name": "string",
  "rentPrice": 0,
  "recommend": 0,
  "rentCarNumber": "string",
  "reservationStatus": "AVAILABLE",
  "totalDistance": 0
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/cars',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/admin/cars',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/admin/cars', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/admin/cars', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/cars");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/admin/cars", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/admin/cars`

*차량 등록*

새로운 렌트카를 등록합니다.

> Body parameter

```json
{
  "id": 0,
  "name": "string",
  "rentPrice": 0,
  "recommend": 0,
  "rentCarNumber": "string",
  "reservationStatus": "AVAILABLE",
  "totalDistance": 0
}
```

<h3 id="registercar-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[RentCarsDTO](#schemarentcarsdto)|true|none|

> Example responses

> 201 Response

<h3 id="registercar-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|차량 등록 성공|[ApiResponseDTORentCarsDTO](#schemaapiresponsedtorentcarsdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

<h1 id="openapi-definition--api">관리자 API</h1>

관리자 전용 기능을 제공하는 API

## updateMemberRole

<a id="opIdupdateMemberRole"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:7950/api/admin/members/{memberId}/role \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
PUT http://localhost:7950/api/admin/members/{memberId}/role HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "property1": "string",
  "property2": "string"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/members/{memberId}/role',
{
  method: 'PUT',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.put 'http://localhost:7950/api/admin/members/{memberId}/role',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.put('http://localhost:7950/api/admin/members/{memberId}/role', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('PUT','http://localhost:7950/api/admin/members/{memberId}/role', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/members/{memberId}/role");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("PUT");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("PUT", "http://localhost:7950/api/admin/members/{memberId}/role", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`PUT /api/admin/members/{memberId}/role`

*회원 권한 변경*

회원의 권한을 변경합니다.

> Body parameter

```json
{
  "property1": "string",
  "property2": "string"
}
```

<h3 id="updatememberrole-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|memberId|path|string(uuid)|true|none|
|body|body|object|true|none|
|» **additionalProperties**|body|string|false|none|

> Example responses

> 200 Response

<h3 id="updatememberrole-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOMemberDTO](#schemaapiresponsedtomemberdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## rejectSubmission

<a id="opIdrejectSubmission"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/admin/car-submissions/{submissionId}/reject \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/admin/car-submissions/{submissionId}/reject HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/car-submissions/{submissionId}/reject',
{
  method: 'POST',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/admin/car-submissions/{submissionId}/reject',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/admin/car-submissions/{submissionId}/reject', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/admin/car-submissions/{submissionId}/reject', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/car-submissions/{submissionId}/reject");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/admin/car-submissions/{submissionId}/reject", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/admin/car-submissions/{submissionId}/reject`

*차량 등록 신청 반려*

신청을 반려합니다.

<h3 id="rejectsubmission-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|submissionId|path|string(uuid)|true|none|

> Example responses

> 200 Response

<h3 id="rejectsubmission-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## approveSubmission

<a id="opIdapproveSubmission"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/admin/car-submissions/{submissionId}/approve \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/admin/car-submissions/{submissionId}/approve HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/car-submissions/{submissionId}/approve',
{
  method: 'POST',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/admin/car-submissions/{submissionId}/approve',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/admin/car-submissions/{submissionId}/approve', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/admin/car-submissions/{submissionId}/approve', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/car-submissions/{submissionId}/approve");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/admin/car-submissions/{submissionId}/approve", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/admin/car-submissions/{submissionId}/approve`

*차량 등록 신청 승인*

신청을 승인하고 차량으로 등록합니다.

<h3 id="approvesubmission-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|submissionId|path|string(uuid)|true|none|

> Example responses

> 200 Response

<h3 id="approvesubmission-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## getStatistics

<a id="opIdgetStatistics"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/statistics \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/statistics HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/statistics',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/statistics',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/statistics', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/statistics', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/statistics");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/statistics", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/statistics`

*시스템 통계*

전체 회원 수, 예약 수, 차량 수 등의 통계를 조회합니다.

> Example responses

> 200 Response

<h3 id="getstatistics-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOMapStringObject](#schemaapiresponsedtomapstringobject)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## getAllRentals

<a id="opIdgetAllRentals"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/rentals?pageable=page,0,size,1,sort,string \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/rentals?pageable=page,0,size,1,sort,string HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/rentals?pageable=page,0,size,1,sort,string',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/rentals',
  params: {
  'pageable' => '[Pageable](#schemapageable)'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/rentals', params={
  'pageable': {
  "page": 0,
  "size": 1,
  "sort": [
    "string"
  ]
}
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/rentals', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/rentals?pageable=page,0,size,1,sort,string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/rentals", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/rentals`

*전체 예약 조회*

모든 예약 정보를 페이징하여 조회합니다.

<h3 id="getallrentals-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|pageable|query|[Pageable](#schemapageable)|true|none|

> Example responses

> 200 Response

<h3 id="getallrentals-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOPageRentDTO](#schemaapiresponsedtopagerentdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## getAllMembers

<a id="opIdgetAllMembers"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/members?pageable=page,0,size,1,sort,string \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/members?pageable=page,0,size,1,sort,string HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/members?pageable=page,0,size,1,sort,string',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/members',
  params: {
  'pageable' => '[Pageable](#schemapageable)'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/members', params={
  'pageable': {
  "page": 0,
  "size": 1,
  "sort": [
    "string"
  ]
}
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/members', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/members?pageable=page,0,size,1,sort,string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/members", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/members`

*전체 회원 조회*

모든 회원 정보를 페이징하여 조회합니다.

<h3 id="getallmembers-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|pageable|query|[Pageable](#schemapageable)|true|none|

> Example responses

> 200 Response

<h3 id="getallmembers-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOPageMemberDTO](#schemaapiresponsedtopagememberdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## getMemberById

<a id="opIdgetMemberById"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/members/{memberId} \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/members/{memberId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/members/{memberId}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/members/{memberId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/members/{memberId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/members/{memberId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/members/{memberId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/members/{memberId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/members/{memberId}`

*특정 회원 조회*

회원 ID로 특정 회원 정보를 조회합니다.

<h3 id="getmemberbyid-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|memberId|path|string(uuid)|true|none|

> Example responses

> 200 Response

<h3 id="getmemberbyid-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOMemberDTO](#schemaapiresponsedtomemberdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## deleteMember

<a id="opIddeleteMember"></a>

> Code samples

```shell
# You can also use wget
curl -X DELETE http://localhost:7950/api/admin/members/{memberId} \
  -H 'Accept: */*'

```

```http
DELETE http://localhost:7950/api/admin/members/{memberId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/members/{memberId}',
{
  method: 'DELETE',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.delete 'http://localhost:7950/api/admin/members/{memberId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.delete('http://localhost:7950/api/admin/members/{memberId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('DELETE','http://localhost:7950/api/admin/members/{memberId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/members/{memberId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("DELETE");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("DELETE", "http://localhost:7950/api/admin/members/{memberId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`DELETE /api/admin/members/{memberId}`

*회원 삭제*

회원을 삭제합니다.

<h3 id="deletemember-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|memberId|path|string(uuid)|true|none|

> Example responses

> 200 Response

<h3 id="deletemember-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## listSubmissions

<a id="opIdlistSubmissions"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/car-submissions?pageable=page,0,size,1,sort,string \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/car-submissions?pageable=page,0,size,1,sort,string HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/car-submissions?pageable=page,0,size,1,sort,string',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/car-submissions',
  params: {
  'pageable' => '[Pageable](#schemapageable)'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/car-submissions', params={
  'pageable': {
  "page": 0,
  "size": 1,
  "sort": [
    "string"
  ]
}
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/car-submissions', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/car-submissions?pageable=page,0,size,1,sort,string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/car-submissions", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/car-submissions`

*차량 등록 신청 목록*

PENDING 상태의 신청 목록을 조회합니다.

<h3 id="listsubmissions-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|pageable|query|[Pageable](#schemapageable)|true|none|

> Example responses

> 200 Response

<h3 id="listsubmissions-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOPageCarRegistrationSubmissionViewDTO](#schemaapiresponsedtopagecarregistrationsubmissionviewdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## getSubmission

<a id="opIdgetSubmission"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/car-submissions/{submissionId} \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/car-submissions/{submissionId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/car-submissions/{submissionId}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/car-submissions/{submissionId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/car-submissions/{submissionId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/car-submissions/{submissionId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/car-submissions/{submissionId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/car-submissions/{submissionId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/car-submissions/{submissionId}`

*차량 등록 신청 상세*

특정 신청의 상세 정보를 조회합니다.

<h3 id="getsubmission-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|submissionId|path|string(uuid)|true|none|

> Example responses

> 200 Response

<h3 id="getsubmission-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOCarRegistrationSubmissionViewDTO](#schemaapiresponsedtocarregistrationsubmissionviewdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

<h1 id="openapi-definition--api">파일 스트리밍 API</h1>

파일 업로드/다운로드/스트리밍을 담당하는 API

## streamImage

<a id="opIdstreamImage"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/files/view/{imageId} \
  -H 'Accept: */*' \
  -H 'Range: string'

```

```http
GET http://localhost:7950/api/files/view/{imageId} HTTP/1.1
Host: localhost:7950
Accept: */*
Range: string

```

```javascript

const headers = {
  'Accept':'*/*',
  'Range':'string'
};

fetch('http://localhost:7950/api/files/view/{imageId}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*',
  'Range' => 'string'
}

result = RestClient.get 'http://localhost:7950/api/files/view/{imageId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*',
  'Range': 'string'
}

r = requests.get('http://localhost:7950/api/files/view/{imageId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
    'Range' => 'string',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/files/view/{imageId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/files/view/{imageId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
        "Range": []string{"string"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/files/view/{imageId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/files/view/{imageId}`

*이미지 파일 스트리밍*

이미지 ID를 통해 파일을 스트리밍합니다. 인증된 사용자만 접근 가능하며, 업로드한 사용자 또는 관리자만 볼 수 있습니다.

<h3 id="streamimage-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|imageId|path|integer(int64)|true|이미지 ID|
|Range|header|string|false|none|

> Example responses

> 200 Response

<h3 id="streamimage-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|이미지 파일 스트리밍 성공|string|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|403|[Forbidden](https://tools.ietf.org/html/rfc7231#section-6.5.3)|접근 권한 없음|string|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|이미지를 찾을 수 없음|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|파일 읽기 오류|string|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

<h1 id="openapi-definition--api">관리자 유지보수 API</h1>

시스템 유지보수 및 데이터 정제 작업

## fixImageMimeTypes

<a id="opIdfixImageMimeTypes"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/admin/maintenance/fix-image-mime-types \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/admin/maintenance/fix-image-mime-types HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/maintenance/fix-image-mime-types',
{
  method: 'POST',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/admin/maintenance/fix-image-mime-types',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/admin/maintenance/fix-image-mime-types', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/admin/maintenance/fix-image-mime-types', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/maintenance/fix-image-mime-types");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/admin/maintenance/fix-image-mime-types", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/admin/maintenance/fix-image-mime-types`

*이미지 MIME 타입 표준화*

모든 이미지의 MIME 타입을 표준 형식으로 수정합니다. JPG 이미지 표시 문제를 해결할 수 있습니다.

> Example responses

> 200 Response

<h3 id="fiximagemimetypes-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOString](#schemaapiresponsedtostring)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

## fixImageMimeType

<a id="opIdfixImageMimeType"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/admin/maintenance/fix-image-mime-type/{imageId} \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/admin/maintenance/fix-image-mime-type/{imageId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/maintenance/fix-image-mime-type/{imageId}',
{
  method: 'POST',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/admin/maintenance/fix-image-mime-type/{imageId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/admin/maintenance/fix-image-mime-type/{imageId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/admin/maintenance/fix-image-mime-type/{imageId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/maintenance/fix-image-mime-type/{imageId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/admin/maintenance/fix-image-mime-type/{imageId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/admin/maintenance/fix-image-mime-type/{imageId}`

*단일 이미지 MIME 타입 수정*

특정 이미지의 MIME 타입을 표준 형식으로 수정합니다.

<h3 id="fiximagemimetype-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|imageId|path|integer(int64)|true|none|

> Example responses

> 200 Response

<h3 id="fiximagemimetype-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOString](#schemaapiresponsedtostring)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
None
</aside>

<h1 id="openapi-definition-login-controller">login-controller</h1>

## refreshToken

<a id="opIdrefreshToken"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/auth/refresh-token \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/auth/refresh-token HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/auth/refresh-token',
{
  method: 'POST',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/auth/refresh-token',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/auth/refresh-token', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/auth/refresh-token', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/auth/refresh-token");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/auth/refresh-token", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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
# You can also use wget
curl -X POST http://localhost:7950/auth/logout \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/auth/logout HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/auth/logout',
{
  method: 'POST',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/auth/logout',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/auth/logout', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/auth/logout', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/auth/logout");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/auth/logout", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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
# You can also use wget
curl -X POST http://localhost:7950/auth/login \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/auth/login HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "email": "string",
  "password": "string"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/auth/login',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/auth/login',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/auth/login', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/auth/login', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/auth/login");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/auth/login", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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

<h1 id="openapi-definition-user-image-controller">user-image-controller</h1>

## uploadImage

<a id="opIduploadImage"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/user/image/upload \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/user/image/upload HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "file": "string"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/user/image/upload',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/user/image/upload',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/user/image/upload', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/user/image/upload', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/user/image/upload");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/user/image/upload", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/user/image/upload`

> Body parameter

```json
{
  "file": "string"
}
```

<h3 id="uploadimage-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|memberId|query|string(uuid)|false|none|
|submissionId|query|string(uuid)|false|none|
|body|body|object|false|none|
|» file|body|string(binary)|true|none|

> Example responses

> 200 Response

<h3 id="uploadimage-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="openapi-definition-image-controller">image-controller</h1>

## uploadImage_1

<a id="opIduploadImage_1"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/admin/images/upload \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/admin/images/upload HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "file": "string"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/images/upload',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/admin/images/upload',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/admin/images/upload', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/admin/images/upload', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/images/upload");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/admin/images/upload", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/admin/images/upload`

> Body parameter

```json
{
  "file": "string"
}
```

<h3 id="uploadimage_1-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|memberId|query|string(uuid)|false|none|
|submissionId|query|string(uuid)|false|none|
|body|body|object|false|none|
|» file|body|string(binary)|true|none|

> Example responses

> 200 Response

<h3 id="uploadimage_1-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## getMyImages

<a id="opIdgetMyImages"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/admin/images/member-images \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/admin/images/member-images HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/images/member-images',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/admin/images/member-images',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/admin/images/member-images', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/admin/images/member-images', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/images/member-images");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/admin/images/member-images", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/admin/images/member-images`

> Example responses

> 200 Response

<h3 id="getmyimages-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOMapStringObject](#schemaapiresponsedtomapstringobject)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## deleteImage

<a id="opIddeleteImage"></a>

> Code samples

```shell
# You can also use wget
curl -X DELETE http://localhost:7950/api/admin/images/{imageId} \
  -H 'Accept: */*'

```

```http
DELETE http://localhost:7950/api/admin/images/{imageId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/admin/images/{imageId}',
{
  method: 'DELETE',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.delete 'http://localhost:7950/api/admin/images/{imageId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.delete('http://localhost:7950/api/admin/images/{imageId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('DELETE','http://localhost:7950/api/admin/images/{imageId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/admin/images/{imageId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("DELETE");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("DELETE", "http://localhost:7950/api/admin/images/{imageId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`DELETE /api/admin/images/{imageId}`

<h3 id="deleteimage-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|imageId|path|integer(int64)|true|none|

> Example responses

> 200 Response

<h3 id="deleteimage-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
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
# You can also use wget
curl -X POST http://localhost:7950/api/MyPage/{memberId}/change \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/MyPage/{memberId}/change HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
  "name": "string",
  "licence": true,
  "email": "string",
  "phone": "string",
  "address": "string"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/MyPage/{memberId}/change',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/MyPage/{memberId}/change',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/MyPage/{memberId}/change', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/MyPage/{memberId}/change', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/MyPage/{memberId}/change");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/MyPage/{memberId}/change", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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
# You can also use wget
curl -X POST http://localhost:7950/api/MyPage/reservation \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/MyPage/reservation HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "rentDTO": {
    "rent_id": "b2296160-8ede-44df-a694-5844f16b3c86",
    "rentTime": "2019-08-24T14:15:22Z",
    "duration": 1,
    "endTime": "2019-08-24T14:15:22Z",
    "rentCars": {
      "id": 0,
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalDistance": 0
    }
  },
  "rentCarsDTO": {
    "id": 0,
    "name": "string",
    "rentPrice": 0,
    "recommend": 0,
    "rentCarNumber": "string",
    "reservationStatus": "AVAILABLE",
    "totalDistance": 0
  }
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/MyPage/reservation',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/MyPage/reservation',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/MyPage/reservation', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/MyPage/reservation', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/MyPage/reservation");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/MyPage/reservation", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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
      "id": 0,
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalDistance": 0
    }
  },
  "rentCarsDTO": {
    "id": 0,
    "name": "string",
    "rentPrice": 0,
    "recommend": 0,
    "rentCarNumber": "string",
    "reservationStatus": "AVAILABLE",
    "totalDistance": 0
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

## createCarSubmission

<a id="opIdcreateCarSubmission"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/MyPage/car-submissions \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/MyPage/car-submissions HTTP/1.1
Host: localhost:7950
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
  "memberId": "92983ab9-49c8-444b-85ae-6e40402cf72e",
  "carName": "string",
  "rentCarNumber": "string",
  "rentPrice": 0,
  "status": "PENDING",
  "imageIds": [
    0
  ]
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/MyPage/car-submissions',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/MyPage/car-submissions',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/MyPage/car-submissions', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/MyPage/car-submissions', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/MyPage/car-submissions");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/MyPage/car-submissions", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/MyPage/car-submissions`

> Body parameter

```json
{
  "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
  "memberId": "92983ab9-49c8-444b-85ae-6e40402cf72e",
  "carName": "string",
  "rentCarNumber": "string",
  "rentPrice": 0,
  "status": "PENDING",
  "imageIds": [
    0
  ]
}
```

<h3 id="createcarsubmission-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[CarRegistrationSubmissionDTO](#schemacarregistrationsubmissiondto)|true|none|

> Example responses

> 200 Response

<h3 id="createcarsubmission-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOMapStringObject](#schemaapiresponsedtomapstringobject)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## submitCarRegistration

<a id="opIdsubmitCarRegistration"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:7950/api/MyPage/car-submission?carName=string&rentCarNumber=string&rentPrice=0&images=string \
  -H 'Accept: */*'

```

```http
POST http://localhost:7950/api/MyPage/car-submission?carName=string&rentCarNumber=string&rentPrice=0&images=string HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/MyPage/car-submission?carName=string&rentCarNumber=string&rentPrice=0&images=string',
{
  method: 'POST',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:7950/api/MyPage/car-submission',
  params: {
  'carName' => 'string',
'rentCarNumber' => 'string',
'rentPrice' => 'integer(int32)',
'images' => 'array[string]'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.post('http://localhost:7950/api/MyPage/car-submission', params={
  'carName': 'string',  'rentCarNumber': 'string',  'rentPrice': '0',  'images': [
  "string"
]
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:7950/api/MyPage/car-submission', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/MyPage/car-submission?carName=string&rentCarNumber=string&rentPrice=0&images=string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:7950/api/MyPage/car-submission", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/MyPage/car-submission`

<h3 id="submitcarregistration-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|carName|query|string|true|none|
|rentCarNumber|query|string|true|none|
|rentPrice|query|integer(int32)|true|none|
|images|query|array[string]|true|none|

> Example responses

> 200 Response

<h3 id="submitcarregistration-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## memberInformation

<a id="opIdmemberInformation"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/MyPage/{memberId} \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/MyPage/{memberId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/MyPage/{memberId}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/MyPage/{memberId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/MyPage/{memberId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/MyPage/{memberId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/MyPage/{memberId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/MyPage/{memberId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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
# You can also use wget
curl -X GET http://localhost:7950/api/MyPage/reservation/list \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/MyPage/reservation/list HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/MyPage/reservation/list',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/MyPage/reservation/list',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/MyPage/reservation/list', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/MyPage/reservation/list', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/MyPage/reservation/list");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/MyPage/reservation/list", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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
# You can also use wget
curl -X GET http://localhost:7950/api/MyPage/reservation/list/{reservationId} \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/MyPage/reservation/list/{reservationId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/MyPage/reservation/list/{reservationId}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/MyPage/reservation/list/{reservationId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/MyPage/reservation/list/{reservationId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/MyPage/reservation/list/{reservationId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/MyPage/reservation/list/{reservationId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/MyPage/reservation/list/{reservationId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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
# You can also use wget
curl -X DELETE http://localhost:7950/api/MyPage/reservation/list/cancel/{reservationId} \
  -H 'Accept: */*'

```

```http
DELETE http://localhost:7950/api/MyPage/reservation/list/cancel/{reservationId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/MyPage/reservation/list/cancel/{reservationId}',
{
  method: 'DELETE',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.delete 'http://localhost:7950/api/MyPage/reservation/list/cancel/{reservationId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.delete('http://localhost:7950/api/MyPage/reservation/list/cancel/{reservationId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('DELETE','http://localhost:7950/api/MyPage/reservation/list/cancel/{reservationId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/MyPage/reservation/list/cancel/{reservationId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("DELETE");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("DELETE", "http://localhost:7950/api/MyPage/reservation/list/cancel/{reservationId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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

<h1 id="openapi-definition-image-test-controller">image-test-controller</h1>

## testStreamingUrl

<a id="opIdtestStreamingUrl"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/test/streaming-url/{imageId} \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/test/streaming-url/{imageId} HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/test/streaming-url/{imageId}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/test/streaming-url/{imageId}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/test/streaming-url/{imageId}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/test/streaming-url/{imageId}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/test/streaming-url/{imageId}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/test/streaming-url/{imageId}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/test/streaming-url/{imageId}`

<h3 id="teststreamingurl-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|imageId|path|integer(int64)|true|none|

> Example responses

> 200 Response

<h3 id="teststreamingurl-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOMapStringObject](#schemaapiresponsedtomapstringobject)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## testStreamingConfig

<a id="opIdtestStreamingConfig"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/test/streaming-config \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/test/streaming-config HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/test/streaming-config',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/test/streaming-config',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/test/streaming-config', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/test/streaming-config', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/test/streaming-config");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/test/streaming-config", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/test/streaming-config`

> Example responses

> 200 Response

<h3 id="teststreamingconfig-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOMapStringObject](#schemaapiresponsedtomapstringobject)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="openapi-definition-rent-cars-controller">rent-cars-controller</h1>

## searchCars

<a id="opIdsearchCars"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/rentcars/search?pageable=page,0,size,1,sort,string \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/rentcars/search?pageable=page,0,size,1,sort,string HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/rentcars/search?pageable=page,0,size,1,sort,string',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/rentcars/search',
  params: {
  'pageable' => '[Pageable](#schemapageable)'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/rentcars/search', params={
  'pageable': {
  "page": 0,
  "size": 1,
  "sort": [
    "string"
  ]
}
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/rentcars/search', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/rentcars/search?pageable=page,0,size,1,sort,string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/rentcars/search", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/rentcars/search`

<h3 id="searchcars-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|segment|query|string|false|none|
|fuelType|query|string|false|none|
|q|query|string|false|none|
|minPrice|query|integer(int32)|false|none|
|maxPrice|query|integer(int32)|false|none|
|pageable|query|[Pageable](#schemapageable)|true|none|

> Example responses

> 200 Response

<h3 id="searchcars-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOPageRentCarsDTO](#schemaapiresponsedtopagerentcarsdto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## getRankedRentCars

<a id="opIdgetRankedRentCars"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/rentcars/rank?pageable=page,0,size,1,sort,string \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/rentcars/rank?pageable=page,0,size,1,sort,string HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/rentcars/rank?pageable=page,0,size,1,sort,string',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/rentcars/rank',
  params: {
  'pageable' => '[Pageable](#schemapageable)'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/rentcars/rank', params={
  'pageable': {
  "page": 0,
  "size": 1,
  "sort": [
    "string"
  ]
}
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/rentcars/rank', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/rentcars/rank?pageable=page,0,size,1,sort,string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/rentcars/rank", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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
# You can also use wget
curl -X GET http://localhost:7950/api/protected \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/protected HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/protected',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/protected',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/protected', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/protected', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/protected");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/protected", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/protected`

> Example responses

> 200 Response

<h3 id="protectedendpoint-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOString](#schemaapiresponsedtostring)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad Request|[ErrorDTO](#schemaerrordto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Not Found|[ApiResponseDTOVoid](#schemaapiresponsedtovoid)|

<aside class="success">
This operation does not require authentication
</aside>

## tokenValidityTest

<a id="opIdtokenValidityTest"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:7950/api/protected/test \
  -H 'Accept: */*'

```

```http
GET http://localhost:7950/api/protected/test HTTP/1.1
Host: localhost:7950
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:7950/api/protected/test',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:7950/api/protected/test',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:7950/api/protected/test', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:7950/api/protected/test', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:7950/api/protected/test");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:7950/api/protected/test", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/protected/test`

> Example responses

> 200 Response

<h3 id="tokenvaliditytest-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseDTOString](#schemaapiresponsedtostring)|
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
  "id": 0,
  "name": "string",
  "rentPrice": 0,
  "recommend": 0,
  "rentCarNumber": "string",
  "reservationStatus": "AVAILABLE",
  "totalDistance": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|name|string|true|none|none|
|rentPrice|integer(int32)|true|none|none|
|recommend|integer(int64)|false|none|none|
|rentCarNumber|string|false|none|none|
|reservationStatus|string|false|none|none|
|totalDistance|integer(int32)|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|reservationStatus|AVAILABLE|
|reservationStatus|RENTED|
|reservationStatus|MAINTENANCE|

<h2 id="tocS_ApiResponseDTORentCarsDTO">ApiResponseDTORentCarsDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtorentcarsdto"></a>
<a id="schema_ApiResponseDTORentCarsDTO"></a>
<a id="tocSapiresponsedtorentcarsdto"></a>
<a id="tocsapiresponsedtorentcarsdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "id": 0,
    "name": "string",
    "rentPrice": 0,
    "recommend": 0,
    "rentCarNumber": "string",
    "reservationStatus": "AVAILABLE",
    "totalDistance": 0
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[RentCarsDTO](#schemarentcarsdto)|false|none|none|

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
    "token": "string",
    "email": "string",
    "name": "string",
    "role": "USER",
    "redirectUrl": "string"
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
  "token": "string",
  "email": "string",
  "name": "string",
  "role": "USER",
  "redirectUrl": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|token|string|false|none|none|
|email|string|false|none|none|
|name|string|false|none|none|
|role|string|false|none|none|
|redirectUrl|string|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|role|USER|
|role|ADMIN|

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
|email|string|true|none|사용자 이메일(이메일 형식 준수)|
|password|string|true|write-only|비밀번호(비밀번호가 영어로만 이루어져야 하고 8자 이상, 특수문자 하나 이상, 숫자가 하나 이상, 16자리 이하로 설정)|
|phone|string|true|none|전화번호|
|address|string|true|none|주소|

<h2 id="tocS_ApiResponseDTOString">ApiResponseDTOString</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtostring"></a>
<a id="schema_ApiResponseDTOString"></a>
<a id="tocSapiresponsedtostring"></a>
<a id="tocsapiresponsedtostring"></a>

```json
{
  "success": true,
  "message": "string",
  "data": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|string|false|none|none|

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
    "id": 0,
    "name": "string",
    "rentPrice": 0,
    "recommend": 0,
    "rentCarNumber": "string",
    "reservationStatus": "AVAILABLE",
    "totalDistance": 0
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
      "id": 0,
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalDistance": 0
    }
  },
  "rentCarsDTO": {
    "id": 0,
    "name": "string",
    "rentPrice": 0,
    "recommend": 0,
    "rentCarNumber": "string",
    "reservationStatus": "AVAILABLE",
    "totalDistance": 0
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
      "id": 0,
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalDistance": 0
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

<h2 id="tocS_CarRegistrationSubmissionDTO">CarRegistrationSubmissionDTO</h2>
<!-- backwards compatibility -->
<a id="schemacarregistrationsubmissiondto"></a>
<a id="schema_CarRegistrationSubmissionDTO"></a>
<a id="tocScarregistrationsubmissiondto"></a>
<a id="tocscarregistrationsubmissiondto"></a>

```json
{
  "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
  "memberId": "92983ab9-49c8-444b-85ae-6e40402cf72e",
  "carName": "string",
  "rentCarNumber": "string",
  "rentPrice": 0,
  "status": "PENDING",
  "imageIds": [
    0
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|string(uuid)|false|none|none|
|memberId|string(uuid)|false|none|none|
|carName|string|false|none|none|
|rentCarNumber|string|false|none|none|
|rentPrice|integer(int32)|false|none|none|
|status|string|false|none|none|
|imageIds|[integer]|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|status|PENDING|
|status|APPROVED|
|status|REJECTED|

<h2 id="tocS_ApiResponseDTOMapStringObject">ApiResponseDTOMapStringObject</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtomapstringobject"></a>
<a id="schema_ApiResponseDTOMapStringObject"></a>
<a id="tocSapiresponsedtomapstringobject"></a>
<a id="tocsapiresponsedtomapstringobject"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "property1": {},
    "property2": {}
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|object|false|none|none|
|» **additionalProperties**|object|false|none|none|

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
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "last": true,
    "size": 0,
    "content": [
      {
        "id": 0,
        "name": "string",
        "rentPrice": 0,
        "recommend": 0,
        "rentCarNumber": "string",
        "reservationStatus": "AVAILABLE",
        "totalDistance": 0
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
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": true,
          "property": "string",
          "ignoreCase": true
        }
      ],
      "unpaged": true,
      "paged": true,
      "pageNumber": 0,
      "pageSize": 0
    },
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
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true,
  "size": 0,
  "content": [
    {
      "id": 0,
      "name": "string",
      "rentPrice": 0,
      "recommend": 0,
      "rentCarNumber": "string",
      "reservationStatus": "AVAILABLE",
      "totalDistance": 0
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
  "numberOfElements": 0,
  "pageable": {
    "offset": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": true,
        "property": "string",
        "ignoreCase": true
      }
    ],
    "unpaged": true,
    "paged": true,
    "pageNumber": 0,
    "pageSize": 0
  },
  "empty": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|totalElements|integer(int64)|false|none|none|
|totalPages|integer(int32)|false|none|none|
|first|boolean|false|none|none|
|last|boolean|false|none|none|
|size|integer(int32)|false|none|none|
|content|[[RentCarsDTO](#schemarentcarsdto)]|false|none|none|
|number|integer(int32)|false|none|none|
|sort|[[SortObject](#schemasortobject)]|false|none|none|
|numberOfElements|integer(int32)|false|none|none|
|pageable|[PageableObject](#schemapageableobject)|false|none|none|
|empty|boolean|false|none|none|

<h2 id="tocS_PageableObject">PageableObject</h2>
<!-- backwards compatibility -->
<a id="schemapageableobject"></a>
<a id="schema_PageableObject"></a>
<a id="tocSpageableobject"></a>
<a id="tocspageableobject"></a>

```json
{
  "offset": 0,
  "sort": [
    {
      "direction": "string",
      "nullHandling": "string",
      "ascending": true,
      "property": "string",
      "ignoreCase": true
    }
  ],
  "unpaged": true,
  "paged": true,
  "pageNumber": 0,
  "pageSize": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|offset|integer(int64)|false|none|none|
|sort|[[SortObject](#schemasortobject)]|false|none|none|
|unpaged|boolean|false|none|none|
|paged|boolean|false|none|none|
|pageNumber|integer(int32)|false|none|none|
|pageSize|integer(int32)|false|none|none|

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

<h2 id="tocS_ApiResponseDTOPageRentDTO">ApiResponseDTOPageRentDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtopagerentdto"></a>
<a id="schema_ApiResponseDTOPageRentDTO"></a>
<a id="tocSapiresponsedtopagerentdto"></a>
<a id="tocsapiresponsedtopagerentdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "last": true,
    "size": 0,
    "content": [
      {
        "rent_id": "b2296160-8ede-44df-a694-5844f16b3c86",
        "rentTime": "2019-08-24T14:15:22Z",
        "duration": 1,
        "endTime": "2019-08-24T14:15:22Z",
        "rentCars": {
          "id": 0,
          "name": "string",
          "rentPrice": 0,
          "recommend": 0,
          "rentCarNumber": "string",
          "reservationStatus": "AVAILABLE",
          "totalDistance": 0
        }
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
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": true,
          "property": "string",
          "ignoreCase": true
        }
      ],
      "unpaged": true,
      "paged": true,
      "pageNumber": 0,
      "pageSize": 0
    },
    "empty": true
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[PageRentDTO](#schemapagerentdto)|false|none|none|

<h2 id="tocS_PageRentDTO">PageRentDTO</h2>
<!-- backwards compatibility -->
<a id="schemapagerentdto"></a>
<a id="schema_PageRentDTO"></a>
<a id="tocSpagerentdto"></a>
<a id="tocspagerentdto"></a>

```json
{
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true,
  "size": 0,
  "content": [
    {
      "rent_id": "b2296160-8ede-44df-a694-5844f16b3c86",
      "rentTime": "2019-08-24T14:15:22Z",
      "duration": 1,
      "endTime": "2019-08-24T14:15:22Z",
      "rentCars": {
        "id": 0,
        "name": "string",
        "rentPrice": 0,
        "recommend": 0,
        "rentCarNumber": "string",
        "reservationStatus": "AVAILABLE",
        "totalDistance": 0
      }
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
  "numberOfElements": 0,
  "pageable": {
    "offset": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": true,
        "property": "string",
        "ignoreCase": true
      }
    ],
    "unpaged": true,
    "paged": true,
    "pageNumber": 0,
    "pageSize": 0
  },
  "empty": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|totalElements|integer(int64)|false|none|none|
|totalPages|integer(int32)|false|none|none|
|first|boolean|false|none|none|
|last|boolean|false|none|none|
|size|integer(int32)|false|none|none|
|content|[[RentDTO](#schemarentdto)]|false|none|none|
|number|integer(int32)|false|none|none|
|sort|[[SortObject](#schemasortobject)]|false|none|none|
|numberOfElements|integer(int32)|false|none|none|
|pageable|[PageableObject](#schemapageableobject)|false|none|none|
|empty|boolean|false|none|none|

<h2 id="tocS_ApiResponseDTOPageMemberDTO">ApiResponseDTOPageMemberDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtopagememberdto"></a>
<a id="schema_ApiResponseDTOPageMemberDTO"></a>
<a id="tocSapiresponsedtopagememberdto"></a>
<a id="tocsapiresponsedtopagememberdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "last": true,
    "size": 0,
    "content": [
      {
        "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
        "name": "string",
        "licence": true,
        "email": "string",
        "phone": "string",
        "address": "string"
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
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": true,
          "property": "string",
          "ignoreCase": true
        }
      ],
      "unpaged": true,
      "paged": true,
      "pageNumber": 0,
      "pageSize": 0
    },
    "empty": true
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[PageMemberDTO](#schemapagememberdto)|false|none|none|

<h2 id="tocS_PageMemberDTO">PageMemberDTO</h2>
<!-- backwards compatibility -->
<a id="schemapagememberdto"></a>
<a id="schema_PageMemberDTO"></a>
<a id="tocSpagememberdto"></a>
<a id="tocspagememberdto"></a>

```json
{
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true,
  "size": 0,
  "content": [
    {
      "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
      "name": "string",
      "licence": true,
      "email": "string",
      "phone": "string",
      "address": "string"
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
  "numberOfElements": 0,
  "pageable": {
    "offset": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": true,
        "property": "string",
        "ignoreCase": true
      }
    ],
    "unpaged": true,
    "paged": true,
    "pageNumber": 0,
    "pageSize": 0
  },
  "empty": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|totalElements|integer(int64)|false|none|none|
|totalPages|integer(int32)|false|none|none|
|first|boolean|false|none|none|
|last|boolean|false|none|none|
|size|integer(int32)|false|none|none|
|content|[[MemberDTO](#schemamemberdto)]|false|none|none|
|number|integer(int32)|false|none|none|
|sort|[[SortObject](#schemasortobject)]|false|none|none|
|numberOfElements|integer(int32)|false|none|none|
|pageable|[PageableObject](#schemapageableobject)|false|none|none|
|empty|boolean|false|none|none|

<h2 id="tocS_ApiResponseDTOPageCarRegistrationSubmissionViewDTO">ApiResponseDTOPageCarRegistrationSubmissionViewDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtopagecarregistrationsubmissionviewdto"></a>
<a id="schema_ApiResponseDTOPageCarRegistrationSubmissionViewDTO"></a>
<a id="tocSapiresponsedtopagecarregistrationsubmissionviewdto"></a>
<a id="tocsapiresponsedtopagecarregistrationsubmissionviewdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "last": true,
    "size": 0,
    "content": [
      {
        "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
        "memberId": "92983ab9-49c8-444b-85ae-6e40402cf72e",
        "memberName": "string",
        "carName": "string",
        "rentCarNumber": "string",
        "rentPrice": 0,
        "status": "PENDING",
        "imageUrls": [
          "string"
        ]
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
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": true,
          "property": "string",
          "ignoreCase": true
        }
      ],
      "unpaged": true,
      "paged": true,
      "pageNumber": 0,
      "pageSize": 0
    },
    "empty": true
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[PageCarRegistrationSubmissionViewDTO](#schemapagecarregistrationsubmissionviewdto)|false|none|none|

<h2 id="tocS_CarRegistrationSubmissionViewDTO">CarRegistrationSubmissionViewDTO</h2>
<!-- backwards compatibility -->
<a id="schemacarregistrationsubmissionviewdto"></a>
<a id="schema_CarRegistrationSubmissionViewDTO"></a>
<a id="tocScarregistrationsubmissionviewdto"></a>
<a id="tocscarregistrationsubmissionviewdto"></a>

```json
{
  "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
  "memberId": "92983ab9-49c8-444b-85ae-6e40402cf72e",
  "memberName": "string",
  "carName": "string",
  "rentCarNumber": "string",
  "rentPrice": 0,
  "status": "PENDING",
  "imageUrls": [
    "string"
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|string(uuid)|false|none|none|
|memberId|string(uuid)|false|none|none|
|memberName|string|false|none|none|
|carName|string|false|none|none|
|rentCarNumber|string|false|none|none|
|rentPrice|integer(int32)|false|none|none|
|status|string|false|none|none|
|imageUrls|[string]|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|status|PENDING|
|status|APPROVED|
|status|REJECTED|

<h2 id="tocS_PageCarRegistrationSubmissionViewDTO">PageCarRegistrationSubmissionViewDTO</h2>
<!-- backwards compatibility -->
<a id="schemapagecarregistrationsubmissionviewdto"></a>
<a id="schema_PageCarRegistrationSubmissionViewDTO"></a>
<a id="tocSpagecarregistrationsubmissionviewdto"></a>
<a id="tocspagecarregistrationsubmissionviewdto"></a>

```json
{
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true,
  "size": 0,
  "content": [
    {
      "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
      "memberId": "92983ab9-49c8-444b-85ae-6e40402cf72e",
      "memberName": "string",
      "carName": "string",
      "rentCarNumber": "string",
      "rentPrice": 0,
      "status": "PENDING",
      "imageUrls": [
        "string"
      ]
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
  "numberOfElements": 0,
  "pageable": {
    "offset": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": true,
        "property": "string",
        "ignoreCase": true
      }
    ],
    "unpaged": true,
    "paged": true,
    "pageNumber": 0,
    "pageSize": 0
  },
  "empty": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|totalElements|integer(int64)|false|none|none|
|totalPages|integer(int32)|false|none|none|
|first|boolean|false|none|none|
|last|boolean|false|none|none|
|size|integer(int32)|false|none|none|
|content|[[CarRegistrationSubmissionViewDTO](#schemacarregistrationsubmissionviewdto)]|false|none|none|
|number|integer(int32)|false|none|none|
|sort|[[SortObject](#schemasortobject)]|false|none|none|
|numberOfElements|integer(int32)|false|none|none|
|pageable|[PageableObject](#schemapageableobject)|false|none|none|
|empty|boolean|false|none|none|

<h2 id="tocS_ApiResponseDTOCarRegistrationSubmissionViewDTO">ApiResponseDTOCarRegistrationSubmissionViewDTO</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponsedtocarregistrationsubmissionviewdto"></a>
<a id="schema_ApiResponseDTOCarRegistrationSubmissionViewDTO"></a>
<a id="tocSapiresponsedtocarregistrationsubmissionviewdto"></a>
<a id="tocsapiresponsedtocarregistrationsubmissionviewdto"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
    "memberId": "92983ab9-49c8-444b-85ae-6e40402cf72e",
    "memberName": "string",
    "carName": "string",
    "rentCarNumber": "string",
    "rentPrice": 0,
    "status": "PENDING",
    "imageUrls": [
      "string"
    ]
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|success|boolean|false|none|none|
|message|string|false|none|none|
|data|[CarRegistrationSubmissionViewDTO](#schemacarregistrationsubmissionviewdto)|false|none|none|

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
        "id": 0,
        "name": "string",
        "rentPrice": 0,
        "recommend": 0,
        "rentCarNumber": "string",
        "reservationStatus": "AVAILABLE",
        "totalDistance": 0
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

