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



