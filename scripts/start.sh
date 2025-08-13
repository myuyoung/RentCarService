#!/bin/bash

echo "🔍 포트 7950 사용 중인 프로세스 정리 중..."
lsof -ti:7950 | xargs -r kill -9 2>/dev/null
sleep 2

echo "🚀 Spring Boot 애플리케이션 시작 중..."
./gradlew bootRun --args='--spring.profiles.active=local'
