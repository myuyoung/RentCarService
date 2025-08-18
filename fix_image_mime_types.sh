#!/bin/bash

# JPG 이미지 표시 문제 해결을 위한 MIME 타입 수정 스크립트
# 사용법: ./fix_image_mime_types.sh

echo "🔧 JPG 이미지 표시 문제 해결 - MIME 타입 수정 시작"
echo "========================================"

# 서버가 실행 중인지 확인
if ! curl -s http://localhost:7950/actuator/health > /dev/null; then
    echo "❌ 오류: 서버가 실행되지 않고 있습니다. 먼저 서버를 시작해주세요."
    echo "   ./gradlew bootRun 또는 java -jar build/libs/*.jar"
    exit 1
fi

echo "✅ 서버 연결 확인됨"

# 관리자 쿠키 파일 확인
COOKIE_FILE=""
if [ -f "final_admin_cookies.txt" ]; then
    COOKIE_FILE="final_admin_cookies.txt"
elif [ -f "admin_cookies.txt" ]; then
    COOKIE_FILE="admin_cookies.txt"
else
    echo "❌ 오류: 관리자 쿠키 파일을 찾을 수 없습니다."
    echo "   관리자로 로그인 후 쿠키를 저장해주세요."
    exit 1
fi

echo "✅ 관리자 쿠키 파일 확인: $COOKIE_FILE"
echo ""

# MIME 타입 수정 API 호출
echo "🔄 이미지 MIME 타입 표준화 실행 중..."
echo ""

RESPONSE=$(curl -s -X POST http://localhost:7950/api/admin/maintenance/fix-image-mime-types \
    -H "Content-Type: application/json" \
    -b "$COOKIE_FILE" \
    -w "HTTP_STATUS:%{http_code}")

# HTTP 상태 코드 추출
HTTP_STATUS=$(echo "$RESPONSE" | grep -o "HTTP_STATUS:[0-9]*" | cut -d: -f2)
BODY=$(echo "$RESPONSE" | sed 's/HTTP_STATUS:[0-9]*$//')

echo "HTTP 상태 코드: $HTTP_STATUS"
echo "응답 내용:"
echo "$BODY" | python3 -m json.tool 2>/dev/null || echo "$BODY"
echo ""

if [ "$HTTP_STATUS" = "200" ]; then
    echo "✅ MIME 타입 표준화가 성공적으로 완료되었습니다!"
    echo ""
    echo "📋 결과:"
    echo "   - 'image/jpg' → 'image/jpeg' 변환 완료"
    echo "   - 파일명 기반 MIME 타입 추론 적용"
    echo "   - JPG 이미지 브라우저 표시 문제 해결"
    echo ""
    echo "🌟 이제 JPG 이미지가 브라우저에서 정상 표시됩니다!"
    echo ""
    echo "📋 통합 이미지 서빙 시스템:"
    echo "   - API 스트리밍: /api/files/view/{id} (인증 필요)"
    echo "   - 정적 리소스: /images/** (빠른 접근, 캐싱)"
    echo "   - 자동 폴백: 정적 → API 재시도"
    echo ""
    echo "🔧 설정 확인:"
    echo "   - application.yml: file.static-serving.enabled=true"  
    echo "   - SecurityConfig: /images/** permitAll 추가됨"
    echo "   - WebConfig: 정적 리소스 핸들러 활성화"
else
    echo "❌ MIME 타입 수정 실패 (HTTP $HTTP_STATUS)"
    echo "   관리자 권한을 확인하고 다시 시도해주세요."
    exit 1
fi

echo ""
echo "========================================"
echo "🎉 통합 이미지 서빙 시스템 구축 완료!"
echo "📖 자세한 사용법: IMAGE_SERVING_GUIDE.md 참고"
