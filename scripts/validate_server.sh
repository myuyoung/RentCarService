#!/bin/bash
# validate_server.sh

# 30초 동안 1초 간격으로 애플리케이션이 시작되었는지 확인
for i in {1..30}; do
  # 7950 포트가 LISTEN 상태인지 확인
  STATUS=$(netstat -tuln | grep 7950)
  if [ -n "$STATUS" ]; then
    echo "> 애플리케이션이 성공적으로 시작되었습니다."
    exit 0
  fi
  echo "> 애플리케이션 시작 대기 중... ($i/30)"
  sleep 1
done

echo "> 시간 초과: 애플리케이션 시작에 실패했습니다."
# Print last 10 lines of log for debugging
tail -n 10 /home/ec2-user/app/deploy.log
exit 1