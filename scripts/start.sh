#!/bin/bash

# Wook 프로젝트 시작 스크립트

# 스크립트가 오류 발생 시 즉시 중단되도록 설정
set -e

# 스크립트 디렉토리와 프로젝트 디렉토리 설정
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# 색상 코드 정의
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
echo "🚀 Wook 프로젝트 시작"
echo "========================================="
echo ""

# 프로젝트 디렉토리로 이동
cd "$PROJECT_DIR"

# 포트 7950 정리
log_info "포트 7950 사용 중인 프로세스 정리 중..."
if lsof -i :7950 &>/dev/null; then
    EXISTING_PID=$(lsof -ti:7950)
    log_warning "포트 7950을 사용 중인 프로세스를 종료합니다 (PID: $EXISTING_PID)"
    kill -15 "$EXISTING_PID" 2>/dev/null || true
    sleep 3
    
    # 강제 종료가 필요한 경우
    if kill -0 "$EXISTING_PID" 2>/dev/null; then
        log_warning "프로세스를 강제 종료합니다..."
        kill -9 "$EXISTING_PID" 2>/dev/null || true
        sleep 2
    fi
    log_success "기존 프로세스 종료 완료"
else
    log_info "포트 7950이 사용 가능합니다."
fi

# gradlew 실행 권한 확인
if [ ! -x "./gradlew" ]; then
    log_warning "gradlew 실행 권한이 없습니다. 권한을 추가합니다..."
    chmod +x ./gradlew
    log_success "gradlew 실행 권한 추가 완료"
fi

# logs 디렉토리 생성
mkdir -p logs

echo ""
echo "🚀 Spring Boot 애플리케이션 시작 중..."
echo ""

log_info "✅ Local 프로필로 실행"
log_info "   - Spring Boot: http://localhost:7950"

echo ""
echo "Local 프로필로 애플리케이션을 시작합니다... (Ctrl+C로 종료)"
echo ""

# Local 프로필로 실행
./gradlew bootRun --args='--spring.profiles.active=local'