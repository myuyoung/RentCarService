#!/bin/bash

# Docker Compose를 사용한 전체 시스템 시작 스크립트
# MySQL + Spring Boot + nginx 모두 Docker 컨테이너로 실행

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
echo "🐳 Docker Compose로 전체 시스템 시작"
echo "   MySQL + Spring Boot + nginx"
echo "========================================="
echo ""

# 프로젝트 디렉토리로 이동
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$PROJECT_DIR"

# 1. 필요한 파일들 확인
log_info "필요한 파일들 확인 중..."

required_files=("Dockerfile" "docker-compose.yml" "nginx-docker.conf")
for file in "${required_files[@]}"; do
    if [ ! -f "$file" ]; then
        log_error "필수 파일을 찾을 수 없습니다: $file"
        exit 1
    fi
done
log_success "필수 파일 확인 완료"

# 2. gradlew 실행 권한 확인
if [ ! -x "./gradlew" ]; then
    log_warning "gradlew 실행 권한이 없습니다. 권한을 추가합니다..."
    chmod +x ./gradlew
fi

# 3. Spring Boot 애플리케이션 빌드
log_info "Spring Boot 애플리케이션 빌드 시작..."
if ./gradlew build -x test --info; then
    log_success "Spring Boot 빌드 완료"
else
    log_error "Spring Boot 빌드 실패"
    exit 1
fi

# 4. JAR 파일 존재 확인
JAR_FILE="build/libs/Wook-1.0-SNAPSHOT.jar"
if [ ! -f "$JAR_FILE" ]; then
    log_error "JAR 파일을 찾을 수 없습니다: $JAR_FILE"
    exit 1
fi
log_success "JAR 파일 확인: $JAR_FILE"

# 5. 로그 디렉토리 생성
mkdir -p logs/nginx
log_info "로그 디렉토리 준비 완료"

# 6. Docker Compose 네트워크 확인 및 생성
log_info "Docker 네트워크 확인 중..."
if ! docker network ls | grep -q "pinpoint-docker_pinpoint"; then
    log_warning "pinpoint-docker_pinpoint 네트워크가 없습니다. 새로운 네트워크를 생성합니다..."
    docker network create pinpoint-docker_pinpoint
    log_success "네트워크 생성 완료: pinpoint-docker_pinpoint"
else
    log_info "네트워크 확인 완료: pinpoint-docker_pinpoint"
fi

# 7. 기존 컨테이너들 정리 (선택사항)
log_info "기존 컨테이너 상태 확인 중..."
if docker compose ps -q | grep -q .; then
    log_warning "실행 중인 컨테이너들을 정리합니다..."
    docker compose down
    log_success "기존 컨테이너 정리 완료"
fi

# 8. Docker Compose로 전체 시스템 시작
log_info "Docker Compose로 전체 시스템 시작 중..."
log_info "이 과정은 몇 분이 소요될 수 있습니다..."

#  사용할 Spring 프로필을 'local2'로 명시적으로 설정
# 만약 다른 프로필로 실행하고 싶다면 이 값을 변경하거나,
# 스크립트 실행 시 'SPRING_PROFILES_ACTIVE=prod ./start_docker_all.sh'와 같이 덮어쓸 수 있음.
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-local2}
log_info "Spring Boot Profile을 '$SPRING_PROFILES_ACTIVE'로 설정하여 실행합니다."

if docker compose up -d --build; then
    log_success "Docker Compose 시작 완료!"
else
    log_error "Docker Compose 시작 실패"
    log_info "로그 확인: docker compose logs"
    exit 1
fi

# 9. 서비스들이 준비될 때까지 대기
log_info "서비스들이 준비될 때까지 대기 중..."

# MySQL 준비 대기
log_info "MySQL 서비스 대기 중..."
for i in {1..30}; do
    if docker compose exec -T rent-car-mysql mysqladmin ping -h localhost -u root -ppassword --silent; then
        log_success "MySQL 서비스 준비 완료"
        break
    fi
    if [ $i -eq 30 ]; then
        log_error "MySQL 서비스 시작 타임아웃"
        docker compose logs rent-car-mysql
        exit 1
    fi
    sleep 2
done

# Spring Boot 준비 대기
log_info "Spring Boot 서비스 대기 중..."
for i in {1..20}; do
    if curl -s -o /dev/null -w "%{http_code}" http://localhost:7950/actuator/health 2>/dev/null | grep -q "200"; then
        log_success "Spring Boot 서비스 준비 완료"
        break
    fi
    if [ $i -eq 20 ]; then
        log_error "Spring Boot 서비스 시작 타임아웃"
        docker compose logs rent-car-service
        exit 1
    fi
    sleep 2
done

# nginx 준비 대기
log_info "nginx 서비스 대기 중..."
for i in {1..30}; do
    if curl -s -o /dev/null -w "%{http_code}" http://localhost:80/nginx-health 2>/dev/null | grep -q "200"; then
        log_success "nginx 서비스 준비 완료"
        break
    fi
    if [ $i -eq 30 ]; then
        log_error "nginx 서비스 시작 타임아웃"
        docker compose logs rent-car-nginx
        exit 1
    fi
    sleep 2
done

# 10. 최종 성공 메시지 및 정보 출력
echo ""
echo "========================================="
echo "🎉 모든 서비스가 성공적으로 시작되었습니다!"
echo "========================================="
echo ""
echo "📊 서비스 정보:"
echo "   🔹 nginx (프록시): http://localhost (포트 80)"
echo "   🔹 nginx (대체 포트): http://localhost:8081"
echo "   🔹 Spring Boot (직접): http://localhost:7950"
echo "   🔹 MySQL: localhost:3307 (root/password, wook/wookpw)"
echo ""
echo "🐳 Docker 명령어:"
echo "   📊 상태 확인: docker compose ps"
echo "   📄 로그 확인: docker compose logs [서비스명]"
echo "   🛑 전체 중지: docker compose down"
echo "   🔄 재시작: docker compose restart [서비스명]"
echo ""
echo "📝 실시간 로그 확인:"
echo "   전체: docker compose logs -f"
echo "   Spring Boot: docker compose logs -f rent-car-service"
echo "   nginx: docker compose logs -f rent-car-nginx"
echo "   MySQL: docker compose logs -f rent-car-mysql"
echo ""
echo "💡 추천 접속 URL: http://localhost (nginx를 통한 접근)"
echo ""

# 컨테이너 상태 확인
log_info "현재 컨테이너 상태:"
docker compose ps
