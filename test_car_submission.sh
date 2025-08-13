#!/bin/bash

# 테스트용 이미지 파일 생성
echo "Creating test image..."
echo "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==" | base64 -d > /tmp/test_image.png

# 사용자 토큰 (실제 로그인한 사용자의 토큰으로 교체 필요)
USER_TOKEN=""

echo "Testing car registration submission API..."

# 새로운 통합 API 테스트
curl -X POST "http://localhost:7950/api/MyPage/car-submission" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -F "carName=테스트차량" \
  -F "rentCarNumber=99테1234" \
  -F "rentPrice=60000" \
  -F "images=@/tmp/test_image.png"

echo -e "\n\nTest completed!"

# 정리
rm -f /tmp/test_image.png
