# Wook 프로젝트 nginx 설정 가이드

## 🚀 빠른 시작

### 1. 전체 서비스 시작 (Spring Boot + nginx)
```bash
bash scripts/start_with_nginx.sh
```

### 2. 서비스 접속
- **nginx 프록시 (권장)**: http://localhost
- **Spring Boot 직접**: http://localhost:7950

### 3. 전체 서비스 종료
```bash
bash scripts/stop_all.sh
```

## 📁 파일 구조

```
Wook/
├── nginx.conf                    # nginx 설정 파일
├── logs/                        # 로그 디렉터리
│   └── spring-boot.log         # Spring Boot 로그
├── scripts/                    # 실행 스크립트
│   ├── start_nginx.sh         # nginx만 시작
│   ├── stop_nginx.sh          # nginx만 중지
│   ├── start_with_nginx.sh    # 통합 시작 (권장)
│   └── stop_all.sh            # 전체 중지
└── /Users/myuyong/uploads/images/  # 이미지 업로드 디렉터리
```

## 🔧 개별 서비스 관리

### nginx만 시작/중지
```bash
# nginx 시작
bash scripts/start_nginx.sh

# nginx 중지
bash scripts/stop_nginx.sh

# nginx 상태 확인
ps aux | grep nginx
```

### Spring Boot 직접 실행
```bash
# 빌드 후 실행
./gradlew build && java -jar build/libs/*.jar --spring.profiles.active=local
```

## 🌐 nginx 기능

### 1. 리버스 프록시
- `http://localhost/` → Spring Boot (7950 포트)
- 로드밸런싱 준비 (upstream 블록)

### 2. 정적 파일 서빙
- `/images/` → `/Users/myuyong/uploads/images/` 디렉터리
- 캐싱 적용 (1시간)
- 이미지 파일만 허용 (jpg, png, gif, svg, webp 등)

### 3. 성능 최적화
- Gzip 압축 (텍스트, JS, CSS, JSON)
- 정적 리소스 캐싱
- Keep-alive 연결

### 4. 보안 헤더
- X-Frame-Options: SAMEORIGIN
- X-XSS-Protection: 1; mode=block
- X-Content-Type-Options: nosniff
- Referrer-Policy: no-referrer-when-downgrade

## 🔍 트러블슈팅

### 포트 충돌
```bash
# 포트 사용 프로세스 확인
lsof -i :80    # nginx
lsof -i :7950  # Spring Boot

# 프로세스 강제 종료
sudo kill -9 <PID>
```

### nginx 로그 확인
```bash
# 접근 로그
tail -f /opt/homebrew/var/log/nginx/access.log

# 에러 로그
tail -f /opt/homebrew/var/log/nginx/error.log
```

### Spring Boot 로그 확인
```bash
tail -f logs/spring-boot.log
```

### 설정 파일 테스트
```bash
nginx -t -c /Users/myuyong/Desktop/spring_ex/Wook/nginx.conf
```

## ⚙️ 설정 수정

### 도메인 변경
`nginx.conf` 파일의 `server_name`을 수정:
```nginx
server_name your-domain.com;  # localhost 대신 실제 도메인
```

### 포트 변경
Spring Boot 포트를 변경하는 경우 두 곳 수정:
1. `application.yml`: `server.port`
2. `nginx.conf`: `upstream wook_backend` 블록

### 이미지 디렉터리 변경
`nginx.conf`의 `alias` 경로 수정:
```nginx
location /images/ {
    alias /새로운/경로/;
    # ...
}
```

## 🚀 배포 환경

### SSL 적용 (Let's Encrypt)
```bash
# certbot 설치
brew install certbot

# 인증서 발급 (도메인 필요)
sudo certbot --nginx -d your-domain.com

# 자동 갱신 설정
crontab -e
# 다음 라인 추가: 0 12 * * * /usr/local/bin/certbot renew --quiet
```

### 시스템 서비스 등록 (macOS launchd)
```bash
# Spring Boot 서비스 등록
sudo cp scripts/com.wook.springboot.plist /Library/LaunchDaemons/
sudo launchctl load /Library/LaunchDaemons/com.wook.springboot.plist
```

## 📊 모니터링

### 서비스 상태 확인
```bash
# 전체 서비스 상태
curl -I http://localhost/api/health  # nginx → Spring Boot
curl -I http://localhost:7950/api/health  # Spring Boot 직접

# nginx 상태
nginx -s status
```

### 성능 테스트
```bash
# 부하 테스트 (Apache Bench)
ab -n 1000 -c 10 http://localhost/

# 이미지 서빙 테스트
curl -I http://localhost/images/test.jpg
```

## 🎯 다음 단계

1. **모니터링 도구 추가**: Prometheus, Grafana
2. **로그 분석**: ELK Stack 또는 Loki
3. **캐싱 강화**: Redis 연동
4. **CDN 연동**: CloudFlare, AWS CloudFront
5. **컨테이너화**: Docker, Kubernetes

---

## 📞 지원

문제 발생 시:
1. 로그 파일 확인
2. 포트 충돌 점검
3. 설정 파일 문법 확인 (`nginx -t`)
4. 프로세스 상태 점검 (`ps aux | grep nginx`)

**Happy Coding! 🎉**
