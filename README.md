# RentCarService 


## 프로젝트 소개

렌트카 업체에서 차량을 빌려 운전 중 문득 '렌트카 업체는 수많은 사용자들이 이용할 때 어떻게 안정적으로 서비스를 제공하는 걸까?' 라는 생각이 들었습니다.</br>
로그인, 차량예약, 예약한 정보 확인하는 로직을 사용자들에게 제공하고 내부적으로는 악성 사용자들의 방어 및 알림 기능을 설계하여 운영자로서 미리 차단할 수 있게끔 하였습니다.

---


## 주요 기능

-   **회원 관리:** 회원가입, 로그인, 회원정보 조회 및 수정
-   **인증/인가:** JWT(Access/Refresh Token) 기반의 상태 비저장(Stateless) 인증 시스템
-   **보안 강화:**
    -   로그인 실패 횟수 기반 계정 잠금 기능 (Brute-force 공격 방어)
    -   Refresh Token 순환(Rotation) 및 탈취 감지 시 자동 무효화, 관리자 이메일 알림
-   **렌터카 예약:** 차량 예약, 기간별 예약 가능 여부 검증, 내 예약 목록 조회 및 취소
-   **랭킹 시스템:** 추천수 기반의 차량 랭킹 페이징 조회

---

## 로컬 환경에서 실행하기 (Quick Start)
복잡한 설정 없이 즉시 실행해 볼 수 있도록 구성했습니다. 아래 절차를 따라주세요.

### 1. 사전 요구사항

* **Java 21** (Corretto 21이상)
* **Gradle 8.10 이상**

### 2. 프로젝트 클론

```
git clone [https://github.com/myuyoung/RentCarService.git](https://github.com/myuyoung/RentCarService.git)
cd RentCarService
```

### 3. 애플리케이션 실행
` ./gradlew bootRun `

## Tech Stacks

### Backend
![Java](https://img.shields.io/badge/Java21-007396?style=for-the-badge&logo=java&logoColor=white) 
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) 
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) 
![JPA](https://img.shields.io/badge/JPA-4A90E2?style=for-the-badge)
![QueryDSL](https://img.shields.io/badge/QueryDSL-59666C?style=for-the-badge)

### Database
![H2](https://img.shields.io/badge/H2-59666C?style=for-the-badge&logo=h2&logoColor=white) 
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### Auth
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

---

## ERD
<img src="https://github.com/user-attachments/assets/2d2936cf-edfd-4efb-98bc-4317df9fbd91" width="50%" >

## API 명세

### 주요 엔드포인트요약

| HTTP Method | Path | Description|
|-----|----|-----|
|Post| /api/register/member| 회원 가입 요청|
|Post| /api/login| 로그인 요청 및 토큰 발급|
|Post| /api/MyPage/reservation| 렌터카 예약|
|GET| /api/MyPage/reservation/list| 내 예약 목록 조회|
|DELETE| /api/MyPage/reservation/list/cancel/{id}| 특정 예약 취소|

<br>

**더 자세한 내용은** [전체API 명세(API.md)](./API.md) **파일을 참고해주세요.**


## 프로젝트 목표 및 개발 철학
이 프로젝트는 단순히 기능을 구현하는 것을 넘어 동료와 함께 개발한다고 가정하고 유지보수성을 높이는 방향으로 개발하였습니다. 또한 사용자의 데이터를 안전하게 보호하는 책임감 있는 백엔드 시스템을 구축하는 것을 목표로 했습니다.

#### 1. 협업과 유지보수를 고려한 설계
1인 개발이었지만,항상'이 코드를 다른 사람이 유지보수한다면?'이라는 질문을 던지며 아래 원칙을 적용했습니다.

- **명확한 역할 분리:** 계층형 아키텍처를 엄격히 준수하여 각 컴포넌트의 책임과 역할을 명확히 했습니다.
- **가독성 높은 코드:** 주석을 통해 로직의 구성을 설명하고자 했습니다.
- **재사용성:** 중복되는 롲기을 최소화하고,비즈니스 로직은 Servcie계층에 집중시켜 응집도를 높였습니다.

#### 2. 보안 중심의 방어적 설계
백엔드 개발자는 데이터베이스의 최전선 수비수라는 철학으로, 발생 가능한 보안위협에 대비하는 방어적인 설계를 적용했습니다.

- **인증/인가:** Stateless한 JWT방식을 채택하고, RefreshToken 순환 및 탈취 감지 로직을 구현하여 세션 보안을 강화했습니다.
- **Brute-Force 공격 방어:** Spring Events를 활용하여 로그인 실패 횟수를 기록하고, 임계값 초과 시 계정을 일시적으로 잠그는 기능을 구현했습니다.
- **민감 정보 정리:** 소스 코드에 민감 정보가 노출되지 않도록 환경변수를 통해 주입받는 방식으로 보안을 강화했습니다.
