#!/bin/bash

# Wook 프로젝트용 nginx 시작 스크립트

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
NGINX_CONFIG="$PROJECT_DIR/nginx.conf"

echo "🚀 Wook 프로젝트용 nginx 시작 중..."

# nginx 설정 파일 테스트
echo "📝 nginx 설정 파일 검증 중..."
sudo nginx -t -c "$NGINX_CONFIG"

if [ $? -ne 0 ]; then
    echo "❌ nginx 설정 파일에 오류가 있습니다. 확인해주세요."
    exit 1
fi

# 기존 nginx 프로세스 확인 및 종료
if pgrep -x "nginx" > /dev/null; then
    echo "⚠️  기존 nginx 프로세스를 종료합니다..."
    sudo nginx -s quit
    sleep 2
fi

# nginx 시작
echo "▶️  nginx 시작..."
sudo nginx -c "$NGINX_CONFIG"

if [ $? -eq 0 ]; then
    echo "✅ nginx가 성공적으로 시작되었습니다!"
    echo "🌐 접속 URL: http://localhost"
    echo "📊 상태 확인: nginx -t -c $NGINX_CONFIG"
else
    echo "❌ nginx 시작에 실패했습니다."
    exit 1
fi
