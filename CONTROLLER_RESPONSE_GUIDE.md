# 컨트롤러 응답 표준화 가이드

## 📋 개요

이 프로젝트의 모든 API 컨트롤러는 일관된 응답 형식을 사용합니다. 이를 통해 클라이언트와의 통신을 표준화하고 코드의 일관성을 보장합니다.

## 🏗️ 응답 구조

### ApiResponseDTO<T>
```json
{
    "success": true,
    "message": "성공 메시지",
    "data": { ... }
}
```

- `success`: 요청 성공 여부 (Boolean)
- `message`: 응답 메시지 (String)
- `data`: 응답 데이터 (Generic Type T)

## 🛠️ ResponseFactory 사용법

### 기본 사용법

모든 컨트롤러에서는 `ResponseFactory`를 주입받아 사용합니다:

```java
@RestController
@RequiredArgsConstructor
public class ExampleController {
    
    private final ResponseFactory responseFactory;
    private final ExampleService exampleService;
    
    @GetMapping("/example")
    public ResponseEntity<ApiResponseDTO<ExampleDTO>> getExample() {
        ExampleDTO data = exampleService.getData();
        return responseFactory.success("조회 성공", data);
    }
}
```

### 성공 응답 메서드

#### 200 OK
```java
// 데이터 포함
responseFactory.success("조회 성공", data);

// 데이터 없음
responseFactory.success("작업 완료");
```

#### 201 Created
```java
// 데이터 포함
responseFactory.created("생성 완료", createdData);

// 데이터 없음
responseFactory.created("생성 완료");
```

#### 202 Accepted
```java
// 데이터 포함
responseFactory.accepted("요청 접수됨", processData);

// 데이터 없음
responseFactory.accepted("요청 접수됨");
```

### 실패 응답 메서드

```java
// 400 Bad Request
responseFactory.badRequest("잘못된 요청입니다");

// 401 Unauthorized
responseFactory.unauthorized("인증이 필요합니다");

// 403 Forbidden
responseFactory.forbidden("권한이 없습니다");

// 404 Not Found
responseFactory.notFound("리소스를 찾을 수 없습니다");

// 409 Conflict
responseFactory.conflict("데이터 충돌이 발생했습니다");

// 500 Internal Server Error
responseFactory.internalServerError("서버 오류가 발생했습니다");
```

## 📝 BaseController 사용법 (선택사항)

BaseController를 상속받으면 더 간편하게 응답을 생성할 수 있습니다:

```java
@RestController
public class ExampleController extends BaseController {
    
    @GetMapping("/example")
    public ResponseEntity<ApiResponseDTO<String>> example() {
        return success("성공", "데이터");
    }
    
    @PostMapping("/example")
    public ResponseEntity<ApiResponseDTO<Void>> create() {
        // 생성 로직...
        return created("생성 완료");
    }
}
```

## ✅ 표준화된 컨트롤러 목록

다음 컨트롤러들이 표준화되었습니다:

- ✅ `LoginController` - 로그인/로그아웃/토큰 갱신
- ✅ `RegisterController` - 회원 가입
- ✅ `MyPageController` - 마이페이지/예약 관리
- ✅ `AdminController` - 관리자 기능
- ✅ `RentCarsController` - 차량 조회
- ✅ `RegisterCarController` - 차량 등록
- ✅ `ProtectedApiController` - JWT 테스트
- ✅ `ImageController` - 이미지 관리 (관리자)
- ✅ `UserImageController` - 이미지 업로드 (사용자)

## 🎯 개발 규칙

### 1. ResponseFactory 필수 사용
```java
// ✅ 권장: ResponseFactory 사용
return responseFactory.success("조회 성공", data);

// ❌ 금지: 직접 ApiResponseDTO 생성
return ResponseEntity.ok(new ApiResponseDTO<>(true, "조회 성공", data));
```

### 2. 일관된 메시지 작성
- 성공: "작업명 + 성공/완료"
- 실패: 구체적이고 사용자가 이해할 수 있는 메시지

### 3. 적절한 HTTP 상태 코드
- 조회: `200 OK` → `success()`
- 생성: `201 Created` → `created()`
- 수정: `200 OK` → `success()`
- 삭제: `200 OK` → `success()`

### 4. 예외 처리
컨트롤러에서는 비즈니스 로직 예외 처리보다는 ResponseFactory를 통한 정상 응답에 집중하고, 예외는 `GlobalExceptionHandler`에서 처리합니다.

## 🔍 응답 형식 예시

### 성공 응답 예시
```json
{
    "success": true,
    "message": "회원 조회 성공",
    "data": {
        "id": "uuid-string",
        "name": "홍길동",
        "email": "hong@example.com"
    }
}
```

### 실패 응답 예시
```json
{
    "success": false,
    "message": "회원을 찾을 수 없습니다",
    "data": null
}
```

## 📚 관련 클래스

- `ApiResponseDTO<T>`: 표준 응답 DTO
- `ResponseFactory`: 응답 생성 팩토리 클래스  
- `BaseController`: 컨트롤러 기본 클래스 (선택사항)
- `GlobalExceptionHandler`: 전역 예외 처리

---

이 가이드를 따라 모든 API가 일관된 응답 형식을 제공하도록 개발해주세요.