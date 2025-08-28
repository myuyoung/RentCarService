#!/bin/bash

# Pinpoint Agent 설치 스크립트
echo "🔍 Pinpoint Agent 설치 시작..."

# 작업 디렉터리 생성
mkdir -p pinpoint-agent
cd pinpoint-agent

# Pinpoint Agent 다운로드 (최신 버전)
PINPOINT_VERSION="2.5.3"
AGENT_URL="https://github.com/pinpoint-apm/pinpoint/releases/download/v${PINPOINT_VERSION}/pinpoint-agent-${PINPOINT_VERSION}.tar.gz"

echo "📥 Pinpoint Agent v${PINPOINT_VERSION} 다운로드 중..."
curl -L -o pinpoint-agent.tar.gz "$AGENT_URL"

# 압축 해제
echo "📁 Agent 압축 해제 중..."
tar -xzf pinpoint-agent.tar.gz
rm pinpoint-agent.tar.gz

echo "✅ Pinpoint Agent 다운로드 완료!"
echo "📂 Agent 경로: $(pwd)"

# pinpoint.config 설정 파일 수정
echo "⚙️ pinpoint.config 설정 중..."

# 기본 설정 파일 백업
cp pinpoint.config pinpoint.config.backup

# 설정 값 수정
cat > pinpoint.config << EOF
# Pinpoint Agent Configuration

# Profiler 활성화
profiler.enable=true

# 애플리케이션 이름 (수정 필요)
profiler.applicationservertype=SPRING_BOOT
profiler.application.name=WookApp

# Agent ID (각 인스턴스마다 고유해야 함)
profiler.agentid=WookApp-Agent-001

# Collector 설정 (Docker 컨테이너와 통신)
profiler.collector.ip=127.0.0.1
profiler.collector.tcp.port=9994
profiler.collector.stat.port=9995
profiler.collector.span.port=9996

# 샘플링 설정 (1이면 모든 요청, 10이면 10개 중 1개)
profiler.sampling.rate=1

# IO 샘플링
profiler.io.buffering.enable=true
profiler.io.buffering.buffersize=20

# SQL 쿼리 수집
profiler.jdbc=true
profiler.jdbc.sqlcachesize=1024
profiler.jdbc.maxsqlbindvaluesize=1024

# Spring 관련 설정
profiler.spring.beans=true
profiler.spring.beans.name.pattern=
profiler.spring.beans.class.pattern=
profiler.spring.beans.annotation=org.springframework.stereotype.Controller,org.springframework.stereotype.Service,org.springframework.stereotype.Repository

# HTTP 관련 설정
profiler.tomcat.enable=true
profiler.tomcat.bootstrap.main=org.apache.catalina.startup.Bootstrap
profiler.tomcat.conditional.transform=true

# JVM 정보 수집
profiler.jvm.collect.interval=1000
profiler.jvm.collect.detailed.metrics=true

# 로그 레벨
profiler.log.level=INFO

# 트랜잭션 추적 설정
profiler.entrypoint=true
profiler.callstack.max.depth=64

# WebSocket 모니터링 (채팅 기능을 위해)
profiler.websocket.enable=true
EOF

echo "✅ pinpoint.config 설정 완료!"

# JVM 옵션 안내
echo ""
echo "🚀 Spring Boot 애플리케이션 실행 시 다음 JVM 옵션을 추가하세요:"
echo ""
echo "java -javaagent:$(pwd)/pinpoint-bootstrap.jar \\"
echo "     -Dpinpoint.agentId=WookApp-Agent-001 \\"
echo "     -Dpinpoint.applicationName=WookApp \\"
echo "     -jar your-application.jar"
echo ""
echo "또는 application.properties에 추가:"
echo "spring.application.name=WookApp"
echo ""
echo "📍 Pinpoint Web UI: http://localhost:8080"
echo "🔧 Agent 설정 파일: $(pwd)/pinpoint.config"