#!/bin/bash

# 렌트카 시스템 부하 테스트 실행 스크립트 (nGrinder 3.5.9-p1)

echo "🚗 렌트카 시스템 부하 테스트 환경 구성"

# 1. 애플리케이션 실행 확인
echo "1. 애플리케이션 상태 확인 중..."
if curl -f -s http://localhost:7950/actuator/health > /dev/null; then
    echo "✅ 애플리케이션이 정상 실행 중입니다."
else
    echo "❌ 애플리케이션이 실행되지 않았습니다."
    echo "📝 다음 명령어로 실행하세요:"
    echo "   ./gradlew bootRun --args='--spring.profiles.active=local2'"
    exit 1
fi

# 2. Docker 데몬 실행 여부 확인
echo "2. Docker 데몬 상태 확인 중..."
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker 데몬이 실행되지 않았습니다."
    echo "📝 Docker Desktop을 먼저 실행해주세요."
    exit 1
fi
echo "✅ Docker 데몬이 정상 실행 중입니다."

# 3. 기존 nGrinder 컨테이너 종료 및 정리
echo "3. 기존 nGrinder 프로세스 정리 중..."
docker compose -f docker-compose-ngrinder.yml down --remove-orphans

# 4. nGrinder 컨테이너 실행 및 대기
echo "🐳 nGrinder Controller와 Agent 실행 중..."
# --wait 옵션이 healthcheck 성공까지 기다려주므로, 이 명령이 끝나면 준비된 것입니다.
docker compose -f docker-compose-ngrinder.yml up -d --wait

# 5. 최종 안내 메시지 출력
echo "🎉 nGrinder 환경이 정상적으로 구성되었습니다!"
echo "✨ 준비 완료! 성공적인 부하 테스트를 진행하세요!"
cat << 'EOF'

🎯 nGrinder 3.5.9-p1 부하 테스트 준비 완료!

📋 다음 단계 가이드:

1️⃣ 웹 UI 접속 & 로그인
   URL: http://localhost
   계정: admin / admin

2️⃣ Agent 연결 확인
   로그인 후 [Agent Management] 메뉴에서 Agent가 정상적으로 연결되었는지 확인하세요.

3️⃣ 테스트 스크립트 작성
   메뉴: Script → Create a Script
   언어: Groovy (권장)
   테스트 대상 URL: http://host.docker.internal:7950

🛑 테스트 종료 후:
   docker compose -f docker-compose-ngrinder.yml down

EOF