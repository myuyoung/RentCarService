#!/bin/bash

# Docker Compose 전체 시스템 중지 스크립트

set -e

# 색상 코드 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

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
echo "🛑 Docker Compose 전체 시스템 중지"
echo "========================================="
echo ""

# 프로젝트 디렉토리로 이동
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$PROJECT_DIR"

# 1. 현재 실행 중인 컨테이너 상태 확인
log_info "현재 실행 중인 컨테이너 확인..."
if docker compose ps -q | grep -q .; then
    echo ""
    docker compose ps
    echo ""
else
    log_warning "실행 중인 컨테이너가 없습니다."
    exit 0
fi

# 2. 사용자 확인 (선택사항)
read -p "모든 컨테이너를 중지하시겠습니까? (y/N): " -n 1 -r
echo ""
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    log_info "취소되었습니다."
    exit 0
fi

# 3. Docker Compose 중지
log_info "Docker Compose 컨테이너들을 중지합니다..."
if docker compose down; then
    log_success "모든 컨테이너가 성공적으로 중지되었습니다!"
else
    log_error "컨테이너 중지 중 오류가 발생했습니다."
    exit 1
fi

# 4. 볼륨 및 네트워크 정리 옵션
echo ""
read -p "볼륨도 함께 제거하시겠습니까? (MySQL 데이터가 삭제됩니다) (y/N): " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    log_warning "볼륨을 제거합니다... (데이터 손실 주의!)"
    docker compose down -v
    log_success "볼륨이 제거되었습니다."
fi

# 5. 상태 확인
log_info "현재 Docker 상태:"
echo "실행 중인 컨테이너: $(docker ps -q | wc -l | xargs)"
echo "전체 컨테이너: $(docker ps -a -q | wc -l | xargs)"
echo "사용 중인 네트워크: $(docker network ls -q | wc -l | xargs)"

echo ""
log_success "Docker 시스템 중지 완료!"
echo ""
echo "🔄 다시 시작하려면: bash scripts/start_docker_all.sh"
echo "🧹 완전 정리하려면: docker system prune -a"
echo ""
