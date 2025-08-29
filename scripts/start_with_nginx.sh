#!/bin/bash

# Wook 프로젝트 + nginx 통합 시작 스크립트 (개선 버전)

# 스크립트가 오류 발생 시 즉시 중단되도록 설정
set -e

# 스크립트 디렉토리와 프로젝트 디렉토리 설정
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# 색상 코드 정의 (터미널 출력 개선용)
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 로그 함수 정의
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

echo ""
echo "========================================="
echo "🚀 Wook 프로젝트 (Spring Boot + nginx) 시작"
echo "========================================="
echo ""

# 프로젝트 디렉토리로 이동
cd "$PROJECT_DIR"

# 0. 필요한 명령어 존재 여부 확인
log_info "시스템 환경 확인 중..."

# Java 버전 확인
if ! command -v java &> /dev/null; then
    log_error "Java가 설치되어 있지 않습니다. Java 21 이상을 설치해주세요."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    log_warning "Java 21 이상이 권장됩니다. 현재 버전: Java $JAVA_VERSION"
fi

# nginx 설치 확인
if ! command -v nginx &> /dev/null; then
    log_error "nginx가 설치되어 있지 않습니다."
    log_info "Mac에서 설치하려면: brew install nginx"
    exit 1
fi

# curl 설치 확인
if ! command -v curl &> /dev/null; then
    log_error "curl이 설치되어 있지 않습니다."
    exit 1
fi

log_success "시스템 환경 확인 완료"

# 1. gradlew 실행 권한 확인 및 설정
if [ ! -x "./gradlew" ]; then
    log_warning "gradlew 실행 권한이 없습니다. 권한을 추가합니다..."
    chmod +x ./gradlew
    log_success "gradlew 실행 권한 추가 완료"
fi

# 2. Spring Boot 애플리케이션 빌드
log_info "Spring Boot 애플리케이션 빌드 시작..."

# Gradle 데몬이 실행 중이지 않다면 시작
./gradlew --status &> /dev/null || true

# 빌드 실행 (테스트 제외)
if ./gradlew build -x test --info; then
    log_success "Spring Boot 빌드 완료"
else
    log_error "Spring Boot 빌드 실패"
    log_info "상세 로그를 확인하려면: ./gradlew build -x test --debug"
    exit 1
fi

# 3. JAR 파일 존재 확인
JAR_FILE="build/libs/Wook-1.0-SNAPSHOT.jar"
if [ ! -f "$JAR_FILE" ]; then
    log_error "JAR 파일을 찾을 수 없습니다: $JAR_FILE"
    log_info "build/libs 디렉토리 내용:"
    ls -la build/libs/ 2>/dev/null || echo "build/libs 디렉토리가 없습니다."
    exit 1
fi
log_success "JAR 파일 확인: $JAR_FILE"

# 5. 기존 Spring Boot 프로세스 종료
log_info "포트 7950 확인 중..."

# Mac과 Linux 모두 호환되는 방식으로 포트 확인
if lsof -i :7950 &>/dev/null; then
    SPRING_PID=$(lsof -ti:7950)
    log_warning "포트 7950을 사용 중인 프로세스를 종료합니다 (PID: $SPRING_PID)"
    kill -15 $SPRING_PID 2>/dev/null || true
    sleep 3
    
    # 강제 종료가 필요한 경우
    if kill -0 $SPRING_PID 2>/dev/null; then
        log_warning "프로세스를 강제 종료합니다..."
        kill -9 $SPRING_PID 2>/dev/null || true
        sleep 2
    fi
    log_success "기존 프로세스 종료 완료"
else
    log_info "포트 7950이 사용 가능합니다."
fi

# 6. logs 디렉토리 생성
mkdir -p logs
log_info "logs 디렉토리 준비 완료"

# 7. Spring Boot 애플리케이션 시작
log_info "Spring Boot 애플리케이션 시작 중..."

if [ "$USE_PINPOINT" = true ]; then
    AGENT_ID="wook-agent-$(hostname)-$$"
    APPLICATION_NAME="Wook-Application"
    
    log_info "Pinpoint APM 활성화"
    log_info "  - Agent ID: ${AGENT_ID}"
    log_info "  - Application Name: ${APPLICATION_NAME}"
    
    nohup java \
        -javaagent:"${PINPOINT_AGENT_PATH}" \
        -Dpinpoint.agentId="${AGENT_ID}" \
        -Dpinpoint.applicationName="${APPLICATION_NAME}" \
        -jar "$JAR_FILE" \
        --spring.profiles.active=local \
        > logs/spring-boot.log 2>&1 &
else
    log_info "Pinpoint APM 없이 시작"
    
    nohup java \
        -jar "$JAR_FILE" \
        --spring.profiles.active=local \
        > logs/spring-boot.log 2>&1 &
fi

SPRING_PID=$!
log_info "Spring Boot 프로세스 시작됨 (PID: $SPRING_PID)"

# 8. Spring Boot 시작 대기
log_info "Spring Boot 시작 대기 중... (최대 60초)"

for i in {1..60}; do
    # 로그 파일에서 시작 완료 메시지 확인
    if grep -q "Started WookApplication" logs/spring-boot.log 2>/dev/null; then
        log_success "Spring Boot가 성공적으로 시작되었습니다!"
        break
    fi
    
    # curl로 헬스체크 (더 안정적인 방법)
    if curl -s -o /dev/null -w "%{http_code}" http://localhost:7950/actuator/health 2>/dev/null | grep -q "200\|404"; then
        log_success "Spring Boot가 응답하고 있습니다!"
        break
    fi
    
    # 프로세스가 죽었는지 확인
    if ! kill -0 $SPRING_PID 2>/dev/null; then
        log_error "Spring Boot 프로세스가 예기치 않게 종료되었습니다."
        log_info "로그 확인:"
        tail -n 20 logs/spring-boot.log
        exit 1
    fi
    
    if [ $i -eq 60 ]; then
        log_error "Spring Boot 시작 타임아웃 (60초)"
        log_info "로그 확인:"
        tail -n 20 logs/spring-boot.log
        kill -9 $SPRING_PID 2>/dev/null || true
        exit 1
    fi
    
    # 진행 상황 표시
    if [ $((i % 5)) -eq 0 ]; then
        echo -ne "\r⏳ 대기 중... ($i/60)"
    fi
    sleep 1
done
echo "" # 줄바꿈

# 9. nginx 설정 파일 확인
log_info "nginx 설정 파일 확인 중..."
NGINX_CONFIG="$PROJECT_DIR/nginx.conf"

if [ ! -f "$NGINX_CONFIG" ]; then
    log_error "nginx.conf 파일을 찾을 수 없습니다: $NGINX_CONFIG"
    kill -9 $SPRING_PID 2>/dev/null || true
    exit 1
fi

# nginx 설정 파일 유효성 검증
if nginx -t -c "$NGINX_CONFIG" &>/dev/null; then
    log_success "nginx 설정 파일 검증 완료"
else
    log_error "nginx 설정 파일에 오류가 있습니다:"
    nginx -t -c "$NGINX_CONFIG"
    kill -9 $SPRING_PID 2>/dev/null || true
    exit 1
fi

# 10. nginx 시작
log_info "nginx 시작 중..."

# 기존 nginx 프로세스 종료
if pgrep nginx > /dev/null; then
    log_warning "기존 nginx 프로세스를 종료합니다..."
    nginx -s quit 2>/dev/null || sudo nginx -s quit 2>/dev/null || true
    sleep 2
fi

# nginx 시작 (권한 문제 처리)
if nginx -c "$NGINX_CONFIG" 2>/dev/null; then
    log_success "nginx가 성공적으로 시작되었습니다!"
elif sudo nginx -c "$NGINX_CONFIG"; then
    log_success "nginx가 sudo 권한으로 시작되었습니다!"
else
    log_error "nginx 시작에 실패했습니다."
    log_info "포트 8080이 이미 사용 중인지 확인: lsof -i :8080"
    kill -9 $SPRING_PID 2>/dev/null || true
    exit 1
fi

# 11. 최종 성공 메시지
echo ""
echo "========================================="
echo "🎉 모든 서비스가 성공적으로 시작되었습니다!"
echo "========================================="
echo ""
echo "📊 서비스 정보:"
echo "   - Spring Boot PID: $SPRING_PID"
echo "   - Spring Boot URL: http://localhost:7950 (직접 접근)"
echo "   - nginx 프록시: http://localhost:8080 (권장)"
echo "   - 로그 파일: $PROJECT_DIR/logs/spring-boot.log"
echo ""

if [ "$USE_PINPOINT" = true ]; then
    echo "🔍 Pinpoint APM 정보:"
    echo "   - Agent ID: ${AGENT_ID}"
    echo "   - Application Name: ${APPLICATION_NAME}"
    echo "   - Pinpoint Web UI: http://localhost:8079 (별도 설치 필요)"
    echo ""
fi

echo "🛑 종료 방법:"
echo "   bash scripts/stop_all.sh"
echo ""
echo "📝 로그 확인:"
echo "   tail -f logs/spring-boot.log"
echo ""