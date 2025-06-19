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


## 프로젝트 후기
- 본인은 1인개발을 하면서 타인과 함께 협업을 하는 것을 가정하고 프로젝트에 임했다. '남이 보았을 때 이게 무슨 로직인지 알아볼 수 있을까?', '복잡하게 여기저기서 호출하여 이해하기 힘들면 어떡하지?' 라는 생각으로 설계하고 리팩토링도 진행하였다.
- 보안은 서비스를 개발할 때 제일 중요하다고 생각한다. 그 이유는 개인정보 유출이라는 기사만 떠도 주가가 폭락하는 시대에 살고 있기 때문이다. 본인의 철학은 보안전문가가 아니더라도 데이터베이스와의 상호작용을 하는 백엔드 개발자라면 악성 사용자들의 공격에 대비해야한다고 생각한다. 그래서 이 프로젝트를 진행할 때 최소한 보안을 어떻게 유지하고 공격에 방어하는 설계를 할 수 있을지 깊은 고민을 하였다.
