# RentCarService 


## 프로젝트 소개

"Dream Drive"프로젝트는 차량을 빌리려고 하는데 차량이 있는 장소로 가야하거나 비용을 더 지불하여 차량을 호출하는 방식으로 렌트카를 이용하는 불폄함으로 인해 진행된 프로젝트입니다.  
이 프로젝트의 아이디어는 "소유자" 와 "대여자"라는 역할로 부터 시작됩니다. 소유자는 유후한 차량으로 인한 추가 소득을 원하는 사용자로 구분되고 대여자는 소유자가 등록한 차량을 이용하는 사용자로 구분합니다. 추후에 국토교통부 자동차종합정보 API를 도입하여 차량의 번호를 입력시 차량의 제원,점검이력 등을 조회하도록 할 계획입니다. 

---


## 주요 기능

-   **회원 관리:** 회원가입, 로그인, 회원정보 조회 및 수정
-   **인증/인가:** JWT(Access/Refresh Token) 기반의 상태 비저장(Stateless) 인증 시스템
-   **보안 강화:**
    -   로그인 실패 횟수 기반 계정 잠금 기능 (Brute-force 공격 방어)
    -   Refresh Token 순환(Rotation) 및 탈취 감지 시 자동 무효화, 관리자 이메일 알림
-   **렌터카 예약:** 차량 예약, 기간별 예약 가능 여부 검증, 내 예약 목록 조회 및 취소
-   **랭킹 시스템:** 추천수 기반의 차량 랭킹 페이징 조회
-   **차량 등록 신청 및 관리:** 
    - 사용자는 자신의 차량사진을 업로드하여 등록신청가능
    - 관리자는 신청 목록을 조회하고, 신청 내용을 검토한 후 승인 또는 반려 처리를 할 수 있습니다. 승인 시 해당 차량은 즉시 예약 가능한 차량으로 등록
-   **관리자 기능:**
    - 회원, 차량, 예약, 차량 등록 신청 할 수 있는 관리자 페이지를 제공
    - 전체 회원 수, 총 예약 수, 등록된 차량 수 등 시스템의 주요 현황을 한눈에 파악할 수 있는 대시보드 제공.


---


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
