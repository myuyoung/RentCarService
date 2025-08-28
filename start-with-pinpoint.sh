#!/bin/bash

# Pinpoint Agent와 함께 Spring Boot 애플리케이션 실행 스크립트

# 색상 출력을 위한 변수
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔍 Pinpoint APM과 함께 Spring Boot 애플리케이션 시작...${NC}"

# Pinpoint Agent 경로 확인
PINPOINT_AGENT_PATH="./pinpoint-agent/pinpoint-bootstrap.jar"

if [ ! -f "$PINPOINT_AGENT_PATH" ]; then
    echo -e "${RED}❌ Pinpoint Agent를 찾을 수 없습니다: $PINPOINT_AGENT_PATH${NC}"
    echo -e "${YELLOW}💡 먼저 setup-pinpoint-agent.sh를 실행해주세요.${NC}"
    exit 1
fi

# JAR 파일 경로 찾기
JAR_FILE=$(find target -name "*.jar" -not -name "*sources*" -not -name "*javadoc*" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo -e "${RED}❌ JAR 파일을 찾을 수 없습니다. 먼저 mvn clean package를 실행해주세요.${NC}"
    exit 1
fi

echo -e "${GREEN}📦 JAR 파일: $JAR_FILE${NC}"
echo -e "${GREEN}🔧 Agent 파일: $PINPOINT_AGENT_PATH${NC}"

# 환경변수 설정
export PINPOINT_AGENT_ID="${PINPOINT_AGENT_ID:-WookApp-Agent-001}"
export PINPOINT_APPLICATION_NAME="${PINPOINT_APPLICATION_NAME:-WookApp}"

# JVM 옵션 설정
JVM_OPTS="-javaagent:$PINPOINT_AGENT_PATH"
JVM_OPTS="$JVM_OPTS -Dpinpoint.agentId=$PINPOINT_AGENT_ID"
JVM_OPTS="$JVM_OPTS -Dpinpoint.applicationName=$PINPOINT_APPLICATION_NAME"

# 메모리 및 GC 옵션 (선택사항)
JVM_OPTS="$JVM_OPTS -Xms512m -Xmx1024m"
JVM_OPTS="$JVM_OPTS -XX:+UseG1GC"
JVM_OPTS="$JVM_OPTS -XX:G1HeapRegionSize=16m"
JVM_OPTS="$JVM_OPTS -XX:+PrintGCDetails"
JVM_OPTS="$JVM_OPTS -XX:+PrintGCTimeStamps"

echo -e "${BLUE}🚀 애플리케이션 시작 중...${NC}"
echo -e "${YELLOW}Agent ID: $PINPOINT_AGENT_ID${NC}"
echo -e "${YELLOW}Application Name: $PINPOINT_APPLICATION_NAME${NC}"
echo -e "${YELLOW}JVM Options: $JVM_OPTS${NC}"
echo ""

# Spring Boot 애플리케이션 실행
java $JVM_OPTS -jar "$JAR_FILE" "$@"