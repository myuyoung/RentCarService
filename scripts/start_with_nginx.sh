#!/bin/bash

# Wook 프로젝트 + nginx 통합 시작 스크립트

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo "🚀 Wook 프로젝트 (Spring Boot + nginx) 시작 중..."

# 1. Spring Boot 애플리케이션 빌드
echo "📦 Spring Boot 애플리케이션 빌드 중..."
cd "$PROJECT_DIR"
./gradlew build -x test

if [ $? -ne 0 ]; then
    echo "❌ Spring Boot 빌드에 실패했습니다."
    exit 1
fi

# 2. 기존 Spring Boot 프로세스 종료 (포트 7950 사용 중인 경우)
echo "🔍 기존 Spring Boot 프로세스 확인 중..."
SPRING_PID=$(lsof -ti:7950)
if [ ! -z "$SPRING_PID" ]; then
    echo "⚠️  포트 7950을 사용 중인 프로세스를 종료합니다 (PID: $SPRING_PID)"
    kill -15 $SPRING_PID
    sleep 3
    
    # 강제 종료가 필요한 경우
    if kill -0 $SPRING_PID 2>/dev/null; then
        echo "🔥 강제 종료합니다..."
        kill -9 $SPRING_PID
        sleep 2
    fi
fi

# 3. Spring Boot 애플리케이션 백그라운드 시작
echo "▶️  Spring Boot 애플리케이션 시작 중..."
nohup java -jar build/libs/Wook-1.0-SNAPSHOT.jar --spring.profiles.active=local > logs/spring-boot.log 2>&1 &
SPRING_PID=$!

# 4. Spring Boot 시작 대기 (최대 30초)
echo "⏳ Spring Boot 시작 대기 중..."
for i in {1..30}; do
    if curl -s http://localhost:7950 > /dev/null; then
        echo "✅ Spring Boot가 성공적으로 시작되었습니다!"
        break
    fi
    
    if [ $i -eq 30 ]; then
        echo "❌ Spring Boot 시작 타임아웃 (30초)"
        kill -9 $SPRING_PID 2>/dev/null
        exit 1
    fi
    
    echo "⏳ 대기 중... ($i/30)"
    sleep 1
done

# 5. nginx 시작
echo "🌐 nginx 시작 중..."
bash "$SCRIPT_DIR/start_nginx.sh"

if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 모든 서비스가 성공적으로 시작되었습니다!"
    echo ""
    echo "📊 서비스 정보:"
    echo "   - Spring Boot: http://localhost:7950 (직접 접근)"
    echo "   - nginx 프록시: http://localhost:8080 (권장)"
    echo "   - 로그 파일: $PROJECT_DIR/logs/spring-boot.log"
    echo ""
    echo "🛑 종료 방법:"
    echo "   bash scripts/stop_all.sh"
    echo ""
else
    echo "❌ nginx 시작에 실패했습니다. Spring Boot를 종료합니다."
    kill -9 $SPRING_PID 2>/dev/null
    exit 1
fi
