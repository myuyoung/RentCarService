# 🔍 Pinpoint APM 설치 및 실행 가이드

## 📋 개요
이 가이드는 WookApp Spring Boot 애플리케이션에 Pinpoint APM을 설정하고 모니터링하는 방법을 설명합니다.

## 🛠️ 설치 및 실행 단계

### 1️⃣ Pinpoint 인프라 시작 (Docker Compose)

```bash
# Pinpoint 인프라 시작 (HBase, Collector, Web UI)
docker-compose -f docker-compose-pinpoint.yml up -d

# 로그 확인
docker-compose -f docker-compose-pinpoint.yml logs -f
```

**주요 서비스:**
- **HBase**: `localhost:2181` (데이터 저장소)
- **Collector**: `localhost:9991-9996` (데이터 수집)
- **Web UI**: `localhost:8080` (모니터링 대시보드)

### 2️⃣ 애플리케이션 빌드 및 실행

**방법 1: Gradle bootRun 사용 (권장)**
```bash
# Pinpoint Agent 자동 다운로드 및 적용
./gradlew bootRun
```

**방법 2: 수동 Agent 설치 후 JAR 실행**
```bash
# Agent 설치
chmod +x setup-pinpoint-agent.sh
./setup-pinpoint-agent.sh

# 애플리케이션 빌드
./gradlew clean build

# Pinpoint와 함께 실행
chmod +x start-with-pinpoint.sh
./start-with-pinpoint.sh
```

## 🎯 모니터링 접속

### Pinpoint Web UI 접속
```
URL: http://localhost:8080
```

### 주요 모니터링 화면
1. **Server Map**: 애플리케이션 간 호출 관계 시각화
2. **Inspector**: 상세 성능 메트릭
3. **Transaction**: 개별 트랜잭션 분석
4. **Alert**: 성능 임계값 알림 설정

## 📊 모니터링 대상

### WookApp 주요 기능 모니터링
- **웹 요청**: HTTP API 응답시간 및 처리량
- **데이터베이스**: JPA/Hibernate 쿼리 성능
- **파일 업로드**: 멀티파트 파일 처리 성능
- **WebSocket**: 실시간 채팅 연결 상태
- **인증/보안**: JWT 토큰 처리 성능

### 성능 메트릭
- **응답 시간** (Response Time)
- **처리량** (TPS - Transaction Per Second)
- **에러율** (Error Rate)
- **GC 성능** (Garbage Collection)
- **메모리 사용량** (Heap/Non-Heap)
- **CPU 사용률**

## 🔧 설정 정보

### 애플리케이션 설정 (application.yml)
```yaml
spring:
  application:
    name: WookApp  # Pinpoint에서 식별하는 앱 이름

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,pinpoint
  endpoint:
    health:
      show-details: always
```

### Agent 설정 (pinpoint.config)
```properties
profiler.application.name=WookApp
profiler.agentid=WookApp-Agent-001
profiler.collector.ip=127.0.0.1
profiler.sampling.rate=1  # 모든 요청 수집 (개발환경)
profiler.jdbc=true        # DB 쿼리 모니터링
profiler.websocket.enable=true  # WebSocket 모니터링
```

## 🚀 성능 테스트 시나리오

### 1. 기본 웹 요청 테스트
```bash
# 홈페이지 접속
curl http://localhost:7950/

# API 요청
curl http://localhost:7950/api/chat/rooms
```

### 2. 채팅 기능 테스트
```bash
# 채팅방 접속하여 메시지 송수신
# Pinpoint에서 WebSocket 연결 및 메시지 처리 성능 확인
```

### 3. 파일 업로드 테스트
```bash
# 파일 업로드 API 호출
curl -X POST -F "file=@test.jpg" -F "roomId=general" -F "sender=testuser" -F "messageType=IMAGE" \
     http://localhost:7950/api/chat/upload-file
```

### 4. 부하 테스트 (Apache Bench)
```bash
# 동시 연결 10개로 100개 요청
ab -n 100 -c 10 http://localhost:7950/

# 결과를 Pinpoint에서 실시간 확인
```

## 📈 대시보드 활용

### Server Map 분석
- 애플리케이션 → 데이터베이스 연결 상태
- 외부 API 호출 (메일 서비스 등)
- 응답시간 분포 확인

### Inspector 상세 분석
- **Heap Memory**: JVM 메모리 사용 패턴
- **CPU Usage**: CPU 사용률 추이
- **TPS**: 초당 트랜잭션 처리량
- **Response Time**: 평균/최대 응답시간

### Transaction 분석
- 느린 쿼리 식별
- 병목 구간 파악
- 에러 발생 트랜잭션 추적

## ⚠️ 문제 해결

### Agent 연결 실패
```bash
# Collector 포트 확인
netstat -an | grep 999[1-6]

# Docker 컨테이너 상태 확인
docker-compose -f docker-compose-pinpoint.yml ps

# 방화벽 설정 확인
```

### 데이터가 보이지 않는 경우
1. Agent ID가 고유한지 확인
2. Collector와 Agent 버전 호환성 확인
3. 샘플링 설정 확인 (`profiler.sampling.rate`)
4. 네트워크 연결 상태 확인

### 성능 영향 최소화
```properties
# 운영 환경 설정 (pinpoint.config)
profiler.sampling.rate=10  # 10개 중 1개만 수집
profiler.jdbc.maxsqlbindvaluesize=512  # SQL 바인드 값 크기 제한
profiler.io.buffering.buffersize=40    # 버퍼 크기 증가
```

## 📚 참고 자료
- [Pinpoint 공식 문서](https://pinpoint-apm.gitbook.io/pinpoint/)
- [성능 최적화 가이드](https://pinpoint-apm.gitbook.io/pinpoint/want-a-quick-tour/quickstart)
- [Agent 설정 옵션](https://pinpoint-apm.gitbook.io/pinpoint/configuration)

---

🎯 **목표**: WookApp의 성능 병목을 식별하고 최적화하여 더 나은 사용자 경험 제공