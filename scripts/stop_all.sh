#!/bin/bash

# Wook 프로젝트 전체 서비스 중지 스크립트

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "🛑 모든 서비스 종료 중..."

# 1. nginx 중지
echo "🌐 nginx 종료 중..."
bash "$SCRIPT_DIR/stop_nginx.sh"

# 2. Spring Boot 애플리케이션 중지 (포트 7950 기준)
echo "🍃 Spring Boot 애플리케이션 종료 중..."
SPRING_PID=$(lsof -ti:7950)

if [ ! -z "$SPRING_PID" ]; then
    echo "📝 Spring Boot 프로세스를 찾았습니다 (PID: $SPRING_PID). 종료 중..."
    
    # 우아한 종료 시도
    kill -15 $SPRING_PID
    sleep 5
    
    # 여전히 실행 중인지 확인
    if kill -0 $SPRING_PID 2>/dev/null; then
        echo "⚠️  우아한 종료가 실패했습니다. 강제 종료 중..."
        kill -9 $SPRING_PID
        sleep 2
        
        if kill -0 $SPRING_PID 2>/dev/null; then
            echo "❌ Spring Boot 종료에 실패했습니다."
            exit 1
        fi
    fi
    
    echo "✅ Spring Boot가 성공적으로 종료되었습니다."
else
    echo "ℹ️  실행 중인 Spring Boot 프로세스가 없습니다."
fi

# 3. 포트 사용 상태 확인
echo "🔍 포트 사용 상태 확인..."
PORT_80_USED=$(lsof -ti:80)
PORT_7950_USED=$(lsof -ti:7950)
PORT_8081_USED=$(lsof -ti:8081)

if [ ! -z "$PORT_80_USED" ]; then
    echo "⚠️  포트 80이 여전히 사용 중입니다 (PID: $PORT_80_USED)"
fi

if [ ! -z "$PORT_7950_USED" ]; then
    echo "⚠️  포트 7950이 여전히 사용 중입니다 (PID: $PORT_7950_USED)"
fi

if [ ! -z "$PORT_8081_USED" ]; then
    echo "⚠️  포트 8081이 여전히 사용 중입니다 (PID: $PORT_8081_USED)"
fi

if [ -z "$PORT_80_USED" ] && [ -z "$PORT_7950_USED" ] && [ -z "$PORT_8081_USED" ]; then
    echo "✅ 모든 서비스가 정상적으로 종료되었습니다."
else
    echo "🔧 필요시 수동으로 프로세스를 종료해주세요:"
    echo "   sudo kill -9 <PID>"
fi
