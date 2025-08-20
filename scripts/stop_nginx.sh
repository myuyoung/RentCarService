#!/bin/bash

# Wook 프로젝트용 nginx 중지 스크립트

echo "🛑 nginx 중지 중..."

# nginx 프로세스 확인 (마스터 프로세스 기준)
if ps aux | grep "nginx.*master" | grep -v grep > /dev/null; then
    echo "📝 nginx 프로세스를 찾았습니다. 종료 중..."
    
    # 우아한 종료 시도
    sudo nginx -s quit
    
    # 5초 대기 후 강제 종료 확인
    sleep 5
    
    if ps aux | grep "nginx.*master" | grep -v grep > /dev/null; then
        echo "⚠️  우아한 종료가 실패했습니다. 강제 종료 시도 중..."
        sudo nginx -s stop
        sleep 2
        
        if ps aux | grep "nginx.*master" | grep -v grep > /dev/null; then
            echo "❌ nginx 종료에 실패했습니다. 수동으로 프로세스를 확인해주세요."
            echo "실행 중인 nginx 프로세스:"
            ps aux | grep nginx
            exit 1
        fi
    fi
    
    echo "✅ nginx가 성공적으로 종료되었습니다."
else
    echo "ℹ️  실행 중인 nginx 프로세스가 없습니다."
fi
