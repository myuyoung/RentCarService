#!/bin/bash

echo "🛑 Spring Boot 애플리케이션 종료 중..."

# Gradle bootRun 프로세스 종료
pkill -f "gradle.*bootRun" 2>/dev/null

# 포트 7950 사용 프로세스 강제 종료
PORT_PIDS=$(lsof -ti:7950)
if [ ! -z "$PORT_PIDS" ]; then
    echo "📍 포트 7950 사용 프로세스들 종료 중: $PORT_PIDS"
    echo $PORT_PIDS | xargs kill -9 2>/dev/null
    sleep 2
fi

# 확인
REMAINING=$(lsof -ti:7950)
if [ -z "$REMAINING" ]; then
    echo "✅ 포트 7950이 성공적으로 정리되었습니다."
else
    echo "⚠️  일부 프로세스가 아직 남아있습니다: $REMAINING"
fi
